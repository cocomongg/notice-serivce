package com.rsupport.notice.support;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
public class RedisCleanUp {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void execute() {
        Set<String> keys = redisTemplate.keys("*");

        if (!CollectionUtils.isEmpty(keys)) {
            redisTemplate.delete(keys);
        }
    }
}
