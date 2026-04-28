package cn.tgxy.tgbase.service;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * @author Chris Deng
 * @Date 2024/03/12 17:52:13
 */
public interface RedisService {

    /**
     * 写入缓存
     *
     * @param key
     * @param value
     * @return boolean
     */
    boolean set(final String key, Object value);

    /**
     * 写入缓存设置时效时间
     *
     * @param key
     * @param value
     * @return
     */
    boolean set(final String key, Object value, Long expireTime);

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    void remove(final String... keys);

    /**
     * 批量删除key
     *
     * @param pattern
     */
    void removePattern(final String pattern);

    /**
     * 删除对应的value
     *
     * @param key
     */
    void remove(final String key);

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    boolean exists(final String key);

    /**
     * 读取缓存
     *
     * @param key
     * @return
     */
    Object get(final String key);

    /**
     * 哈希 添加
     *
     * @param key
     * @param hashKey
     * @param value
     */
    void hmSet(String key, Object hashKey, Object value);

    /**
     * 哈希获取数据
     *
     * @param key
     * @param hashKey
     * @return
     */
    Object hmGet(String key, Object hashKey);

    /**
     * 获取所有哈希数据
     *
     * @param key
     * @return
     */
    Map<Object, Object> hmEntries(String key);

    /**
     * 删除哈希数据
     *
     * @param key
     * @param hashKey
     */
    void hmDelete(String key, Object hashKey);

    /**
     * 列表添加
     *
     * @param k
     * @param v
     */
    void lPush(String k, Object v);

    /**
     * 列表获取
     *
     * @param k
     * @param l
     * @param l1
     * @return
     */
    List<Object> lRange(String k, long l, long l1);

    /**
     * 集合添加
     *
     * @param key
     * @param value
     */
    void add(String key, Object value);

    /**
     * set集合获取
     *
     * @param key
     * @return
     */
    Set<Object> setMembers(String key);

    /**
     * 删除set集合元素
     *
     * @param key
     * @param values
     * @return
     */
    Long setRemove(String key, Object... values);

    /**
     * 有序集合添加
     *
     * @param key
     * @param value
     * @param scoure
     */
    void zAdd(String key, Object value, double scoure);

    /**
     * 删除有序集合元素
     *
     * @param key
     * @param values
     * @return
     */
    Long zRemove(String key, Object... values);

    /**
     * 有序集合获取
     *
     * @param key
     * @param scoreMin
     * @param scoreMax
     * @return
     */
    Set<Object> rangeByScore(String key, double scoreMin, double scoreMax);

    /**
     * 有序集合计数
     *
     * @param key
     * @param scoreMin
     * @param scoreMax
     * @return
     */
    Long countByScore(String key, double scoreMin, double scoreMax);

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
    Set<Object> rangeByScore(String key, double scoreMin, double scoreMax, int offset, int count);

    /**
     * 有序集合获取
     *
     * @param key
     * @return
     */
    Set<Object> zsetMembers(String key);

    /**
     * 设置过期
     *
     * @param key
     * @param expireTime 秒
     * @return
     */
    boolean expire(final String key, Long expireTime);
    
    /**
     * 简单自增
     * @param key
     * @return
     */
    long singleIncrement(String key);
}
