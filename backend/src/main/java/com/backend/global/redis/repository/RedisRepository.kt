package com.backend.global.redis.repository;

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class RedisRepository (private val redisTemplate: RedisTemplate<String, Any>){

     fun save(key: String, value: Any, duration: Long, timeUnit: TimeUnit) =
         redisTemplate.opsForValue().set(key, value, duration, timeUnit)

    /**
     * Redis Key 값의 Value 반환
     * @return [Any]
     */
    fun get(key: String): Any? = redisTemplate.opsForValue().get(key)

    fun remove(key: String) = redisTemplate.delete(key)

    /**
     * Redis 키가 존재하는지 확인하는 메소드
     * @return [Boolean] 있다면 true, 없다면 false 반환
     */
    fun hasKey(key: String): Boolean = redisTemplate.hasKey(key)

    /**
     * Redis에 Key, Value 저장하는 메소드
     */
    fun save(key: String, value: Any) = redisTemplate.opsForValue().set(key, value)

}
