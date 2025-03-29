package org.example.meetlearning.common;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Component
public class ZoomUseRedisSetCommon {

    @Autowired
    private RedisTemplate redisTemplate;

    public Long dailyIncrement(String key) {
        String dailyKey = "counter:" + LocalDateTime.now().toLocalDate().toString();

        // 原子性增加并获取新值
        Long newValue = redisTemplate.opsForValue().increment(dailyKey);

        // 如果是第一次设置这个key，设置过期时间为当天24点
        if (newValue != null && newValue == 1) {
            LocalDateTime midnight = LocalDateTime.now()
                    .plusDays(1)
                    .truncatedTo(ChronoUnit.DAYS);
            Date expireDate = Date.from(midnight.atZone(ZoneId.systemDefault()).toInstant());
            redisTemplate.expireAt(dailyKey, expireDate);
        }
        return newValue;
    }
}
