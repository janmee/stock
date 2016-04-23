package com.janmee.stock.cache;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface RedisCacheService {
    <T> boolean hput(Serializable key, T obj);

    boolean hput(Serializable key, Serializable field, Serializable val);

    boolean hput(Serializable key, Map<String, Object> obj);

    boolean hput(Serializable tableKey, Serializable field, Serializable val, long expiredTime);

    Object hget(Serializable key, Serializable field);

    void hdel(Serializable key);

    <T> T hget(Serializable key, Class<? extends T> clazz);

    /**
     * 测试field是否存在
     */
    boolean hexists(Serializable tableKey, Serializable field);

    /**
     * 缓存对象
     */
    <T extends Serializable> void put(Serializable key, T val);

    /**
     * 获取缓存对象
     */
    <T> T get(Serializable key, Class<? extends T> clazz);

    /**
     * 缓存数值
     */
    boolean vput(final String key, final Long val);

    /**
     * 获取缓存数值
     */
    Long vget(final String key);

    /**
     * 增加i
     */
    long vincrement(String key, final long i);

    /**
     * 当key存在时增加i
     */
    long vincrementExistKey(String key, final long i);

    boolean spush(Serializable key, Serializable val);

    boolean spushAll(Serializable key, List<Serializable> val);

    boolean sDelAndPushAll(Serializable key, List<Serializable> val);

    List<Serializable> sgetAll(Serializable key);

    boolean zpush(Serializable key, Serializable val, double score);

    List<Serializable> zgetAll(Serializable key);

    /**
     * 获得范围内所有zset的值
     */
    List<Serializable> zget(Serializable key, int start, int end);

    /**
     * 获得zset的大小
     */
    long zsize(Serializable key);

    /**
     * list尾添加一个值为value的元素
     */
    boolean lpush(Serializable key, Serializable val);

    /**
     * list首添加一个值为value的元素
     */
    boolean lpushToFirst(Serializable key, Serializable val);

    /**
     * 添加列表
     */
    boolean lpushAll(Serializable key, List<Serializable> val);

    /**
     * 清空并添加列表
     */
    boolean lDelAndPushAll(Serializable key, List<Serializable> val);

    /**
     * 获得key的列表
     */
    List<Serializable> lgetAll(Serializable key);

    /**
     * 获得key的列表大小
     **/
    long lsize(Serializable key);

    void reflushAll();

    void sremove(Serializable key, Serializable obj);

    /**
     * 清除key的缓存
     */
    void deleteKey(Serializable key);

    void deleteKey(List<Serializable> key);

    /**
     * 清除匹配pattern指定模式的缓存
     *（1）?：用于匹配单个字符。例如，h?llo可以匹配hello、hallo和hxllo等；
     *（2）*：用于匹配零个或者多个字符。例如，h*llo可以匹配hllo和heeeello等；
     *（3）[]：可以用来指定模式的选择区间。例如h[ae]llo可以匹配hello和hallo，但是不能匹配hillo。
     * 同时，可以使用“/”符号来转义特殊的字符
     */
    void deleteKeyWithPattern(Serializable pattern);

    boolean hasKey(Serializable key);
}
