package com.mmall.common;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class TokenCache {

    public static final String TOKEN_PREFIX = "token_";

    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS).build(new CacheLoader<String, String>() {
        @Override
        public String load(String s) throws Exception {
            return null;
        }
    });

    public static void setValue(String key, String value) {
        localCache.put(key,value);
    }

    public static String getValue(String key) {
        try {
            return localCache.get(key);
        } catch (Exception e) {
            logger.error("localCache get error", e);
        }
        return null;
    }
}
