//package com.truongvu.blogrestapi.config;
//
//import org.redisson.Redisson;
//import org.redisson.api.RedissonClient;
//import org.redisson.config.Config;
//import org.springframework.context.annotation.Bean;
//
//public class RedissonConfig {
//    @Bean(destroyMethod = "shutdown")
//    public RedissonClient redisson() {
//        Config config = new Config();
//        config.useSingleServer()
//                .setAddress("redis://localhost:6379")
//                .setConnectionMinimumIdleSize(10)
//                .setConnectionPoolSize(64)
//                .setIdleConnectionTimeout(10000)
//                .setConnectTimeout(10000)
//                .setTimeout(3000)
//                .setRetryAttempts(3)
//                .setRetryInterval(1500)
//                .setSubscriptionsPerConnection(5)
//                .setSubscriptionConnectionMinimumIdleSize(1)
//                .setSubscriptionConnectionPoolSize(50);
//
//        return Redisson.create(config);
//    }
//}
