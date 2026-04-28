package cn.tgxy.tgbase.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;

import cn.tgxy.tgbase.service.RedisService;

@Service("redisService")
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean set(final String key, Object value) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value);
        return true;
    }

    /**
     * 写入缓存设置时效时间
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public boolean set(final String key, Object value, Long expireTime) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        operations.set(key, value);
        redisTemplate.expire(key, expireTime, TimeUnit.SECONDS);
        return true;
    }

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    @Override
    public void remove(final String... keys) {
        for (String key : keys) {
            remove(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    @Override
    public void removePattern(final String pattern) {
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    @Override
    public void remove(final String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    @Override
    public boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    @Override
    public Object get(final String key) {
        ValueOperations<String, Object> operations = redisTemplate.opsForValue();
        return operations.get(key);
    }

    /**
     * 哈希 添加
     *
     * @param key
     * @param hashKey
     * @param value
     */
    @Override
    public void hmSet(String key, Object hashKey, Object value) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.put(key, hashKey, value);
    }

    /**
     * 哈希获取数据
     *
     * @param key
     * @param hashKey
     * @return
     */
    @Override
    public Object hmGet(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.get(key, hashKey);
    }

    /**
     * 获取所有哈希数据
     *
     * @param key
     * @return
     */
    @Override
    public Map<Object, Object> hmEntries(String key) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        return hash.entries(key);
    }

    /**
     * 删除哈希数据
     *
     * @param key
     * @param hashKey
     */
    @Override
    public void hmDelete(String key, Object hashKey) {
        HashOperations<String, Object, Object> hash = redisTemplate.opsForHash();
        hash.delete(key, hashKey);
    }

    /**
     * 列表添加
     *
     * @param k
     * @param v
     */
    @Override
    public void lPush(String k, Object v) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        list.rightPush(k, v);
    }

    /**
     * 列表获取
     *
     * @param k
     * @param l
     * @param l1
     * @return
     */
    @Override
    public List<Object> lRange(String k, long l, long l1) {
        ListOperations<String, Object> list = redisTemplate.opsForList();
        return list.range(k, l, l1);
    }

    /**
     * 集合添加
     *
     * @param key
     * @param value
     */
    @Override
    public void add(String key, Object value) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add(key, value);
    }

    /**
     * set集合获取
     *
     * @param key
     * @return
     */
    @Override
    public Set<Object> setMembers(String key) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.members(key);
    }

    /**
     * 删除set集合元素
     *
     * @param key
     * @param values
     * @return
     */
    @Override
    public Long setRemove(String key, Object... values) {
        SetOperations<String, Object> set = redisTemplate.opsForSet();
        return set.remove(key, values);
    }

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param scoure
     */
    @Override
    public void zAdd(String key, Object value, double scoure) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        zset.add(key, value, scoure);
    }

    /**
     * 删除有序集合元素
     *
     * @param key
     * @param values
     * @return
     */
    @Override
    public Long zRemove(String key, Object... values) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.remove(key, values);
    }

    /**
     * 有序集合获取
     *
     * @param key
     * @param scoreMin
     * @param scoreMax
     * @return
     */
    @Override
    public Set<Object> rangeByScore(String key, double scoreMin, double scoreMax) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, scoreMin, scoreMax);
    }

    /**
     * 有序集合计数
     *
     * @param key
     * @param scoreMin
     * @param scoreMax
     * @return
     */
    @Override
    public Long countByScore(String key, double scoreMin, double scoreMax) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.count(key, scoreMin, scoreMax);
    }

    /**
     * 有序集合获取
     *
     * @param key
     * @param scoreMin
     * @param scoreMax
     * @param offset
     * @param count
     * @return
     */
    @Override
    public Set<Object> rangeByScore(String key, double scoreMin, double scoreMax, int offset, int count) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        return zset.rangeByScore(key, scoreMin, scoreMax, offset, count);
    }

    /**
     * 有序集合获取
     *
     * @param key
     * @return
     */
    @Override
    public Set<Object> zsetMembers(String key) {
        ZSetOperations<String, Object> zset = redisTemplate.opsForZSet();
        Long size = zset.size(key);
        if (size != null) {
            return zset.range(key, 0, size);
        }
        return null;
    }

    /**
     * 设置过期
     *
     * @param key
     * @param expireTime 秒
     * @return
     */
    @Override
    public boolean expire(final String key, Long expireTime) {
        boolean result = false;
        if (Boolean.TRUE.equals(redisTemplate.expire(key, expireTime, TimeUnit.SECONDS))) {
            result = true;
        }
        return result;
    }

	@Override
	public long singleIncrement(String key) {
		RedisAtomicLong atomicLong = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
		return atomicLong.incrementAndGet();
	}
}
