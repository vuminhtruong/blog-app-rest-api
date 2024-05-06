//package com.truongvu.blogrestapi.config;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
//
//import java.time.Duration;
//
//@Configuration
//public class RedisConfig {
//    @Value("6379")
//    private String redisPort;
//    @Value("localhost")
//    private String redisHost;
//
////    @Bean
////    JedisConnectionFactory jedisConnectionFactory() {
////        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
////        redisStandaloneConfiguration.setHostName(redisHost);
////        redisStandaloneConfiguration.setPort(Integer.parseInt(redisPort));
////
////        return new JedisConnectionFactory(redisStandaloneConfiguration);
////    }
////
////    @Bean
////    RedisTemplate<String, Object> redisTemplate() {
////        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
////
////        redisTemplate.setConnectionFactory(jedisConnectionFactory());
////        redisTemplate.setKeySerializer(new StringRedisSerializer());
////        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
////        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
////        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
////
////        return redisTemplate;
////    }
//
//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory() {
//        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, Integer.parseInt(redisPort));
//
//        return new LettuceConnectionFactory(configuration);
//    }
//
////  @Bean
////  public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
////    return RedisCacheManager.create(connectionFactory);
////  }
//
//    @Bean
//    public RedisCacheManager cacheManager() {
//        RedisCacheConfiguration cacheConfig = myDefaultCacheConfig(Duration.ofMinutes(10)).disableCachingNullValues();
//
//        return RedisCacheManager.builder(redisConnectionFactory())
//                .cacheDefaults(cacheConfig)
//                .withCacheConfiguration("posts", myDefaultCacheConfig(Duration.ofMinutes(1)))
//                .withCacheConfiguration("post", myDefaultCacheConfig(Duration.ofMinutes(1)))
//                .withCacheConfiguration("images", myDefaultCacheConfig(Duration.ofMinutes(5)))
//                .withCacheConfiguration("image", myDefaultCacheConfig(Duration.ofMinutes(1)))
//                .build();
//    }
//
//    private RedisCacheConfiguration myDefaultCacheConfig(Duration duration) {
//        return RedisCacheConfiguration
//                .defaultCacheConfig()
//                .entryTtl(duration)
//                .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
//    }
//}
