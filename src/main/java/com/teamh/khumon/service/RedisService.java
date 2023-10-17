package com.teamh.khumon.service;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;


    public String getData(String key){
        return (String) redisTemplate.opsForValue().get(key);
    }

    protected void setData(String key, String value, Long time){
        Duration expireDuration = Duration.ofSeconds(time);
        redisTemplate.opsForValue().set(key, value, expireDuration);
    }

    public void setDataWithExpiration(String key, String value, Long time){
        if(this.getData(key) != null){
            this.redisTemplate.delete(key);
        }
        setData(key, value, time);
    }

    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }





}
