package com.devin.dezhi.satoken;

import cn.dev33.satoken.dao.SaTokenDao;
import cn.dev33.satoken.dao.auto.SaTokenDaoByStringFollowObject;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONReader;
import com.alibaba.fastjson2.JSONWriter;
import com.alibaba.fastjson2.filter.Filter;
import com.devin.dezhi.constant.RedisKey;
import com.devin.dezhi.utils.RedisUtils;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 2025/12/4 21:45.
 *
 * <p>
 * 自定义 Sa-Token 实现类，基于 Redis 存储
 * </p>
 *
 * @author <a href="https://github.com/wzh-devin">devin</a>
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
public class CustomSaTokenDao implements SaTokenDaoByStringFollowObject {

    /**
     * Sa-Token AutoType 白名单过滤器.
     */
    private static final Filter AUTO_TYPE_FILTER = JSONReader.autoTypeFilter("cn.dev33.satoken.");

    // ------------------------ String 读写操作

    /**
     * 获取 Value，如无返空.
     */
    @Override
    public String get(final String key) {
        return RedisUtils.get(RedisKey.generateRedisKey(key));
    }

    /**
     * 写入 Value，并设定存活时间 (单位: 秒).
     */
    @Override
    public void set(final String key, final String value, final long timeout) {
        if (timeout == 0 || timeout <= SaTokenDao.NOT_VALUE_EXPIRE) {
            return;
        }

        String fullKey = RedisKey.generateRedisKey(key);

        // 判断是否为永不过期
        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            RedisUtils.set(fullKey, value);
        } else {
            RedisUtils.setEx(fullKey, value, timeout, TimeUnit.SECONDS);
        }
    }

    /**
     * 修改 Value.
     */
    @Override
    public void update(final String key, final String value) {
        long expire = getTimeout(key);
        if (expire == SaTokenDao.NOT_VALUE_EXPIRE) {
            return;
        }
        this.set(key, value, expire);
    }

    /**
     * 删除 Value.
     */
    @Override
    public void delete(final String key) {
        String fullKey = RedisKey.generateRedisKey(key);
        RedisUtils.delete(fullKey);
    }

    /**
     * 获取 Value 的剩余存活时间 (单位: 秒).
     */
    @Override
    public long getTimeout(final String key) {
        String fullKey = RedisKey.generateRedisKey(key);
        return RedisUtils.getExpire(fullKey, TimeUnit.SECONDS);
    }

    /**
     * 修改 Value 的剩余存活时间 (单位: 秒).
     */
    @Override
    public void updateTimeout(final String key, final long timeout) {
        String fullKey = RedisKey.generateRedisKey(key);
        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            long expire = getTimeout(key);
            if (expire == SaTokenDao.NEVER_EXPIRE) {
                return;
            }
            this.set(key, this.get(key), timeout);
        } else {
            RedisUtils.expire(fullKey, timeout, TimeUnit.SECONDS);
        }
    }

    // ------------------------ Object 读写操作

    /**
     * 获取 Object，如无返空.
     */
    @Override
    public Object getObject(final String key) {
        String fullKey = RedisKey.generateRedisKey(key);
        String value = RedisUtils.get(fullKey);
        if (value == null) {
            return null;
        }
        // 使用 AutoType 过滤器来反序列化 Sa-Token 相关类
        return JSON.parseObject(value, Object.class, AUTO_TYPE_FILTER);
    }

    @Override
    public <T> T getObject(final String key, final Class<T> classType) {
        String fullKey = RedisKey.generateRedisKey(key);
        String value = RedisUtils.get(fullKey);
        if (value == null) {
            return null;
        }
        return JSON.parseObject(value, classType, AUTO_TYPE_FILTER);
    }

    /**
     * 写入 Object，并设定存活时间 (单位: 秒).
     */
    @Override
    public void setObject(final String key, final Object object, final long timeout) {
        if (timeout == 0 || timeout <= SaTokenDao.NOT_VALUE_EXPIRE) {
            return;
        }

        String value = JSON.toJSONString(object, JSONWriter.Feature.WriteClassName);
        String fullKey = RedisKey.generateRedisKey(key);

        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            RedisUtils.set(fullKey, value);
        } else {
            RedisUtils.setEx(fullKey, value, timeout, TimeUnit.SECONDS);
        }
    }

    /**
     * 更新 Object.
     */
    @Override
    public void updateObject(final String key, final Object object) {
        long expire = getObjectTimeout(key);
        if (expire == SaTokenDao.NOT_VALUE_EXPIRE) {
            return;
        }
        this.setObject(key, object, expire);
    }

    /**
     * 删除 Object.
     */
    @Override
    public void deleteObject(final String key) {
        String fullKey = RedisKey.generateRedisKey(key);
        RedisUtils.delete(fullKey);
    }

    /**
     * 获取 Object 的剩余存活时间 (单位: 秒).
     */
    @Override
    public long getObjectTimeout(final String key) {
        String fullKey = RedisKey.generateRedisKey(key);
        return RedisUtils.getExpire(fullKey, TimeUnit.SECONDS);
    }

    /**
     * 修改 Object 的剩余存活时间 (单位: 秒).
     */
    @Override
    public void updateObjectTimeout(final String key, final long timeout) {
        String fullKey = RedisKey.generateRedisKey(key);
        if (timeout == SaTokenDao.NEVER_EXPIRE) {
            long expire = getObjectTimeout(key);
            if (expire == SaTokenDao.NEVER_EXPIRE) {
                return;
            }
            this.setObject(key, this.getObject(key), timeout);
        } else {
            RedisUtils.expire(fullKey, timeout, TimeUnit.SECONDS);
        }
    }

    /**
     * 搜索数据.
     */
    @Override
    public List<String> searchData(final String prefix, final String keyword, final int start, final int size, final boolean sortType) {
        String fullKey = RedisKey.generateRedisKey(prefix);
        Set<String> keys = RedisUtils.keys(fullKey + "*" + keyword + "*");
        List<String> list = new ArrayList<>(keys);

        // 限制范围
        int fromIndex = Math.min(start, list.size());
        int toIndex = Math.min(start + size, list.size());

        return list.subList(fromIndex, toIndex);
    }
}