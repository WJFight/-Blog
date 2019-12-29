package com.myblog.wj.config;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.Duration;

@EnableCaching
@Configuration
@EnableScheduling
public class RedisConfig extends CachingConfigurerSupport {
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory) {
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                //设置key中value的序列化类型一般使用GenericJackson2JsonRedisSerializer
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
                .entryTtl(Duration.ofDays(2))//设置有效时间
                .disableCachingNullValues();//设置不缓存null
        RedisCacheManager redisCacheManager = RedisCacheManager.builder(factory).cacheDefaults(cacheConfiguration)
                .build();
        return redisCacheManager;
    }
//    @Bean("cacheManager")//可以配置多个缓存管理
//    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//        //创建序列化实例
////        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
////        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
////        //解决查询缓存转换异常的问题
////        ObjectMapper objectMapper = new ObjectMapper();
////        //设置对象映射器的可见性，所有属性都可以访问，所有json自动检测
////        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
////        //设置序列化实例的对象映射器
////        jackson2JsonRedisSerializer.setObjectMapper(objectMapper);
//        //配置序列化
////        Duration duration=Duration.ofDays(1);//设置缓存有效时间
////        Boolean cacheNullValues=false;//是否存null值
////        Boolean usePrefix=false;//是否使用缓存key前缀
////        CacheKeyPrefix keyPrefix;//key前缀
////        ConversionService conversionService//类型转换器
////        redisSerializer//key的序列化类型
//// jackson2JsonRedisSerializer//value序列化类型
//          GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer=new GenericJackson2JsonRedisSerializer();
//        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
//                .entryTtl(Duration.ofDays(1))//设置缓存管理器的有效时间
////        RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer)
//                .serializeKeysWith((RedisSerializationContext.SerializationPair<String>) genericJackson2JsonRedisSerializer)
//                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(genericJackson2JsonRedisSerializer))
//
//                .disableCachingNullValues();
//                RedisCacheManager cacheManager = RedisCacheManager.builder(connectionFactory)
//                .cacheDefaults(configuration).build();
//        return cacheManager;
//    }

//    @Bean("redisTemplate2")
//    public RedisTemplate<String, Object> redisTemplate2(RedisConnectionFactory redisConnectionFactory) {
//        // 设置序列化
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
//                Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//        // 配置redisTemplate
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        RedisSerializer<String> stringSerializer = new StringRedisSerializer();
//        redisTemplate.setKeySerializer(stringSerializer);// key序列化
//        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);// value序列化
//        redisTemplate.setHashKeySerializer(stringSerializer);// Hash key序列化
//        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);// Hash value序列化
//        redisTemplate.setDefaultSerializer(jackson2JsonRedisSerializer);
//        redisTemplate.afterPropertiesSet();
//
//        return redisTemplate;
//    }

//    @Bean
//    public RedisTemplate<Object, BlogUser> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        // 设置序列化
//        Jackson2JsonRedisSerializer<BlogUser> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<BlogUser>(
//    BlogUser.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//        // 配置redisTemplate
//        RedisTemplate<Object, BlogUser> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        redisTemplate.setDefaultSerializer(jackson2JsonRedisSerializer);
//        return redisTemplate;
//    }
//    @Bean
//    public RedisTemplate<Object, BlogRole> redisTemplate3(RedisConnectionFactory redisConnectionFactory) {
//        // 设置序列化
//        Jackson2JsonRedisSerializer<BlogRole> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<BlogRole>(
//                BlogRole.class);
//        // 配置redisTemplate
//        RedisTemplate<Object, BlogRole> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        redisTemplate.setDefaultSerializer(jackson2JsonRedisSerializer);
//        return redisTemplate;
//    }
//    @Bean
//    public RedisTemplate<Object, BlogPost> redisTemplate4(RedisConnectionFactory redisConnectionFactory) {
//        // 设置序列化
//        Jackson2JsonRedisSerializer< BlogPost> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<BlogPost>(
//                BlogPost.class);
//        // 配置redisTemplate
//        RedisTemplate<Object, BlogPost> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//        redisTemplate.setDefaultSerializer(jackson2JsonRedisSerializer);
//        return redisTemplate;
//    }
//    @Bean
//    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory factory) {
//        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
//        stringRedisTemplate.setConnectionFactory(factory);
//        return stringRedisTemplate;
//    }

}
