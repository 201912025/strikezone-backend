package com.strikezone.strikezone_backend.domain.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

@TestConfiguration
@Profile("test")
public class TestRedisCacheConfig {

    @Bean(name = "postCacheManager")
    public CacheManager postCacheManager() {
        // ConcurrentMapCacheManager를 사용하면 메모리 기반의 간단한 캐시 기능을 제공할 수 있습니다.
        return new ConcurrentMapCacheManager();
    }
}
