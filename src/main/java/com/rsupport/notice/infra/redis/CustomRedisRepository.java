package com.rsupport.notice.infra.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rsupport.notice.common.error.CommonErrorCode;
import com.rsupport.notice.common.error.CoreException;
import java.time.Duration;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class CustomRedisRepository {
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    public void setValue(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public <T> T getValue(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            return null;
        }

        try {
            return objectMapper.convertValue(value, clazz);
        } catch (Exception e) {
            log.error("Failed to deserialize JSON, value[{}], class[{}]", value, clazz.getName(), e);
            throw new CoreException(CommonErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public Long incrementValue(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    public void setExpire(String key, Duration duration) {
        redisTemplate.expire(key, duration);
    }

    public void addToSet(String key, Object... values) {
        redisTemplate.opsForSet().add(key, values);
    }

    public boolean isMemberOfSet(String key, Object value) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(key, value));
    }

    public Set<Object> members(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public void removeFromSet(String key, Object member) {
        redisTemplate.opsForSet().remove(key, member);
    }
}
