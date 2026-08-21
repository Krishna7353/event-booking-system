package com.eventbooking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class SeatLockService {

    private final RedisTemplate<String, String> redisTemplate;

    @Value("${app.seat-lock.ttl-seconds}")
    private long ttlSeconds;

    private String buildKey(Long eventId, Long seatId) {
        return "seat:lock:" + eventId + ":" + seatId;
    }

    public boolean tryLockSeat(Long eventId, Long seatId, String userEmail) {
        String key = buildKey(eventId, seatId);
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, userEmail, Duration.ofSeconds(ttlSeconds));
        return Boolean.TRUE.equals(success);
    }

    public void releaseLock(Long eventId, Long seatId, String userEmail) {
        String key = buildKey(eventId, seatId);
        String currentHolder = redisTemplate.opsForValue().get(key);

        if (userEmail.equals(currentHolder)) {
            redisTemplate.delete(key);
        }
    }

    public String getLockHolder(Long eventId, Long seatId) {
        return redisTemplate.opsForValue().get(buildKey(eventId, seatId));
    }
}