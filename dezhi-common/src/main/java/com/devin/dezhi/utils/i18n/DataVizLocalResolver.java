package com.devin.dezhi.utils.i18n;

import jakarta.servlet.http.HttpServletRequest;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * 2025/12/4 18:41.
 *
 * <p>
 * 国际化
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Slf4j
public class DataVizLocalResolver extends AcceptHeaderLocaleResolver {

    private final List<Locale> locales = Arrays.asList(
            new Locale("en_US"),
            new Locale("zh_CN"));

    /**
     * 根据Accept-Language，解决国际化.
     *
     * @param request HttpServletRequest
     * @return Locale
     */
    @NonNull
    @Override
    public Locale resolveLocale(final HttpServletRequest request) {
        String headerLang = request.getHeader("Accept-Language");

        if (headerLang == null || headerLang.isEmpty()) {
            return Locale.CHINA;
        }
        Locale locale = new Locale(headerLang);
        if (locales.contains(locale)) {
            return locale;
        }
        return Locale.CHINA;
    }
}
