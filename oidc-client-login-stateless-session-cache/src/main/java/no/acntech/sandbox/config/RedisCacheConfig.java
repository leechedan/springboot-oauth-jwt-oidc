package no.acntech.sandbox.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContext;

@Configuration(proxyBeanMethods = false)
public class RedisCacheConfig {

    @Bean
    public RedisTemplate<String, SecurityContext> redisTemplate(final RedisConnectionFactory connectionFactory) {
        RedisTemplate redisTemplate = new RedisTemplate<String, SecurityContext>();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }
}
