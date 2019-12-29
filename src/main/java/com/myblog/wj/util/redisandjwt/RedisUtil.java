package com.myblog.wj.util.redisandjwt;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil {
    @Resource
    public StringRedisTemplate stringRedisTemplate;
    @Resource
    RedisTemplate<Object, Object> redisTemplate;

    public void setlockKey(){
        stringRedisTemplate.opsForValue().set("k128","128");
    }
    public String getLockKey(){
        return stringRedisTemplate.opsForValue().get("k128");
    }
    //redis实现排行榜

    /**
     * redisTemplate
     */
    /**
     * @param key-集合key
     * @param post_id-需要做排行榜的文章id
     * @param value-文章的访问数
     */
    public void add(String key, Integer post_id, Integer value) {
        redisTemplate.opsForZSet().add(key, post_id, value);
    }

    /**
     * 获取排行榜中start~end直接的排名id
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<ZSetOperations.TypedTuple<Object>> getTop(String key, Integer start, Integer end) {
        return redisTemplate.opsForZSet().reverseRangeWithScores(key, start, end);
    }

    /**
     * 在集合key中post_id增加阅读数value
     *
     * @param key
     * @param post_id
     * @param value
     * @return
     */
    public Double increateScore(String key, Integer post_id, Integer value) {
        return redisTemplate.opsForZSet().incrementScore(key, post_id, value);
    }

    public Integer getScore(Integer post_id) {
        Double score = redisTemplate.opsForZSet().score("top", post_id);
        if (!(score == null)) {
            double sc = score;
            int s = (int) sc;
            return s;
        } else {
            return null;
        }

    }

    /**
     * @param key-集合key
     * @param post_id-需要从排行榜移除的文章id
     * @return
     */
    public Long remove(String key, Object post_id) {
        return redisTemplate.opsForZSet().remove(key, post_id);
    }


    /**
     * @param key--文章id
     * @param value--文章点赞数 存进redis数据库，减少mysql的读写
     */
    public void setKey1(Integer key, Integer value) {
        redisTemplate.opsForValue().set(key, value);
    }

    //根据key获取值
    public Object getKey1(Integer key) {
        return redisTemplate.opsForValue().get(key);
    }

    //删除key
    public Boolean remove(Integer post_id) {
        return redisTemplate.delete(post_id);
    }

    /**
     * 存在线人数和浏览人数；
     */
    public void setNumber(String key, Integer value) {
        redisTemplate.opsForValue().set(key, value);
    }

    //根据key获取值
    public Object getNumber(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * stringRedisTemplate
     *
     * @param key
     * @param value
     */
    //设置key
    public void setKey(String key, Object value) {
        stringRedisTemplate.opsForValue().set(key, value.toString());
    }

    //根据key获取值
    public String getKey(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }


    //根据key获取该key缓存的有效时间
    public Long getExpire(String key) {
        return stringRedisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    //根据key设置有效时间 对应 key 时间长度  时间类型
    public void setExpire(String key, long timeOut, TimeUnit timeType) {
        stringRedisTemplate.expire(key, timeOut, timeType);
    }

    //根据key删除缓存
    public Boolean deleteKey(String key) {
        return stringRedisTemplate.delete(key);
    }

    //判断该key是否还存在
    public Boolean exists(String key) {
        return stringRedisTemplate.hasKey(key);
    }

}
