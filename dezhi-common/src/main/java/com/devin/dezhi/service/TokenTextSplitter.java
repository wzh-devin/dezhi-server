package com.devin.dezhi.service;

import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.DocumentSplitter;
import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.internal.ValidationUtils;
import dev.langchain4j.model.Tokenizer;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import java.util.ArrayList;
import java.util.List;

/**
 * 2026/1/15 20:31.
 *
 * <p>
 * 文本分词工具类
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
@Builder
public record TokenTextSplitter(
        @NotNull int maxTokensPerSegment,
        @NotNull int minTokensPerSegment,
        @NotNull int maxSegments,
        @NotNull Tokenizer tokenizer
) implements DocumentSplitter {

    @Override
    public List<TextSegment> split(final Document document) {
        ValidationUtils.ensureNotNull(document, "document");
        String text = document.text();

        // 1. 快速检查：如果全局未超过限制，直接返回
        if (tokenizer.estimateTokenCountInText(text) <= maxTokensPerSegment) {
            return List.of(TextSegment.from(text, document.metadata().copy()));
        }

        // 2. 按照段落（主要是标题和换行）进行初步切分
        // 匹配逻辑：双换行（标准段落）或单换行前缀带标题符（如 Markdown 的 #）
        String[] paragraphs = text.split("(?m)(?=\\n\\n)|(?=\\n#)");

        List<String> candidateChunks = new ArrayList<>();
        StringBuilder currentBuffer = new StringBuilder();

        for (String paragraph : paragraphs) {
            String trimmedPara = paragraph.trim();
            if (trimmedPara.isEmpty()) {
                continue;
            }

            // 如果单段已经超过了 maxTokensPerSegment，需要对段落内部进行二级切分
            if (tokenizer.estimateTokenCountInText(trimmedPara) > maxTokensPerSegment) {
                // 先把 buffer 里的存了
                flushBuffer(candidateChunks, currentBuffer);
                // 对超长段落进行“硬切分”
                candidateChunks.addAll(splitLongParagraph(trimmedPara));
            } else {
                // 尝试合并到当前 buffer
                String testContent = currentBuffer.isEmpty() ? trimmedPara : currentBuffer + "\n\n" + trimmedPara;
                if (tokenizer.estimateTokenCountInText(testContent) <= maxTokensPerSegment) {
                    currentBuffer.setLength(0);
                    currentBuffer.append(testContent);
                } else {
                    flushBuffer(candidateChunks, currentBuffer);
                    currentBuffer.append(trimmedPara);
                }
            }
        }
        flushBuffer(candidateChunks, currentBuffer);

        // 3. 基于最小 Token 限制进行“优雅合并”
        List<String> mergedChunks = mergeSmallChunks(candidateChunks);

        // 4. 封装为 TextSegment 并应用 maxSegments 限制
        return mergedChunks.stream()
                .limit(maxSegments)
                .map(content -> TextSegment.from(content, document.metadata().copy()))
                .toList();
    }

    private void flushBuffer(final List<String> chunks, final StringBuilder buffer) {
        if (!buffer.isEmpty()) {
            chunks.add(buffer.toString());
            buffer.setLength(0);
        }
    }

    /**
     * 二级切分：当某一段落（如超长正文）依然超过 Token 限制时，按字符进行硬切分.
     *
     * @param longPara 待切分的段落
     * @return 切分的子段落
     */
    private List<String> splitLongParagraph(final String longPara) {
        List<String> subChunks = new ArrayList<>();
        int start = 0;
        while (start < longPara.length()) {
            int end = Math.min(start + (maxTokensPerSegment * 2), longPara.length());
            String sub = longPara.substring(start, end);
            while (tokenizer.estimateTokenCountInText(sub) > maxTokensPerSegment && sub.length() > 1) {
                sub = sub.substring(0, sub.length() - 1);
            }
            subChunks.add(sub);
            start += sub.length();
        }
        return subChunks;
    }

    /**
     * 如果某个分片太小（低于 minTokensPerSegment），尝试将其合并到前一个分片.
     *
     * @param chunks 待处理的分片
     * @return 合并后的分片
     */
    private List<String> mergeSmallChunks(final List<String> chunks) {
        if (chunks.size() <= 1) {
            return chunks;
        }

        List<String> result = new ArrayList<>();
        for (String chunk : chunks) {
            if (result.isEmpty()) {
                result.add(chunk);
                continue;
            }

            String lastChunk = result.get(result.size() - 1);
            // 如果上一个分片或当前分片太小，且合并后不超过 maxTokens
            if (tokenizer.estimateTokenCountInText(chunk) < minTokensPerSegment
                    || tokenizer.estimateTokenCountInText(lastChunk) < minTokensPerSegment) {
                String merged = lastChunk + "\n\n" + chunk;
                if (tokenizer.estimateTokenCountInText(merged) <= maxTokensPerSegment) {
                    result.set(result.size() - 1, merged);
                    continue;
                }
            }
            result.add(chunk);
        }
        return result;
    }
}
