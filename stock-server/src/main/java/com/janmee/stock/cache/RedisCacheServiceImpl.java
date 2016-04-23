package com.janmee.stock.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.janmee.stock.base.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.BoundListOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
public class RedisCacheServiceImpl implements RedisCacheService {

    private static Logger logger = LoggerFactory.getLogger(RedisCacheServiceImpl.class);

    @Value("${redis.default.timetolive}")
    private long DEFAULT_TIMEOUT = 1800;

    @Autowired
    RedisTemplate<Serializable, Serializable> redisTemplate;

    @Value("${redis.flag}")
    private boolean flag = true;

    /**
     * hash put
     *
     * @param tableKey
     * @param field
     * @param val
     * @return
     */
    @Override
    public boolean hput(Serializable tableKey, Serializable field, Serializable val) {
        if (!flag) {
            return true;
        }
        redisTemplate.boundHashOps(tableKey).put(field, val);
        redisTemplate.boundValueOps(tableKey).expire(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public boolean hput(Serializable tableKey, Serializable field, Serializable val, long expiredTime) {
        if (!flag) {
            return true;
        }
        redisTemplate.boundHashOps(tableKey).put(field, val);
        redisTemplate.boundValueOps(tableKey).expire(expiredTime, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public boolean hput(Serializable tableKey, Map<String, Object> obj) {
        if (!flag) {
            return true;
        }
        redisTemplate.boundHashOps(tableKey).putAll(obj);
        redisTemplate.boundValueOps(tableKey).expire(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public <T> boolean hput(Serializable tableKey, T obj) {
        if (!flag) {
            return true;
        }
        JSONObject tmp = JSON.parseObject(JSON.toJSONStringWithDateFormat(obj, "yyyy-MM-dd HH:mm:ss.SSS"));
        redisTemplate.boundHashOps(tableKey).putAll(tmp);
        redisTemplate.boundValueOps(tableKey).expire(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public Object hget(Serializable tableKey, Serializable field) {
        if (!flag) {
            return null;
        }
        Object o = null;
        try {
            o = redisTemplate.boundHashOps(tableKey).get(field);
        } catch (Exception e) {
            deleteKey(tableKey);
            logger.error("get redis error,reflush key : {}", tableKey);
        }
        return o;
    }

    /**
     * 测试哈希表中field是否存在
     */
    public boolean hexists(Serializable tableKey, Serializable field) {
        if (!flag) {
            return true;
        }
        return redisTemplate.boundHashOps(tableKey).hasKey(field);
    }

    @Override
    public void hdel(Serializable key) {
        if (!flag) {
            return;
        }
        redisTemplate.boundHashOps(key).expire(0, TimeUnit.SECONDS);
    }

    @Override
    public <T> T hget(Serializable key, Class<? extends T> clazz) {
        if (!flag) {
            return null;
        }
        Map<Object, Object> obj = null;
        try {
            obj = redisTemplate.boundHashOps(key).entries();
        } catch (Exception e) {
            deleteKey(key);
            logger.error("get redis error,reflush key : {}", key);
        }
        return JSON.parseObject(JSON.toJSONString(obj), clazz);
    }

    @Override
    public boolean vput(final String key, final Long val) {
        if (!flag) {
            return true;
        }
        redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                StringRedisSerializer serializer = new StringRedisSerializer();
                connection.setEx(serializer.serialize(key), DEFAULT_TIMEOUT, serializer.serialize(val.toString()));
                return null;
            }
        });
        return true;
    }

    @Override
    public Long vget(final String key) {
        if (!flag) {
            return null;
        }
        Object obj = null;
        try {
            obj = redisTemplate.execute(new RedisCallback<Object>() {
                @Override
                public Object doInRedis(RedisConnection connection) throws DataAccessException {
                    StringRedisSerializer serializer = new StringRedisSerializer();
                    byte[] data = connection.get(serializer.serialize(key));
                    if (data == null) {
                        return null;
                    }
                    return serializer.deserialize(data);
                }
            });
        } catch (Exception e) {
            deleteKey(key);
            logger.error("get redis error,reflush key : {}", key);
        }
        return obj != null ? Long.valueOf(obj.toString()) : null;
    }

    @Override
    public <T extends Serializable> void put(Serializable key, T val) {
        if (!flag) {
            return;
        }
        redisTemplate.opsForValue().set(key, val);
        redisTemplate.boundValueOps(key).expire(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
    }

    @Override
    public <T> T get(Serializable key, Class<? extends T> clazz) {
        if (!flag) {
            return null;
        }
        T obj = null;
        try {
            obj = (T) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            deleteKey(key);
            logger.error("get redis error,reflush key : {}", key);
        }
        return obj;
    }

    public long vincrement(final String key, final long i) {
        if (!flag) {
            return -1;
        }
        Object obj = redisTemplate.execute(new RedisCallback<Object>() {
            @Override
            public Object doInRedis(RedisConnection connection) throws DataAccessException {
                StringRedisSerializer serializer = new StringRedisSerializer();
                return connection.incrBy(serializer.serialize(key), i);
            }
        });
        return (Long) obj;
    }

    /**
     * 当key存在时增加i
     */
    public long vincrementExistKey(String key, final long i) {
        if (!flag) {
            return -1;
        }
        if (hasKey(key)) return vincrement(key, i);
        else return -1;
    }

    @Override
    public boolean spush(Serializable key, Serializable val) {
        if (!flag) {
            return true;
        }
        Long index = redisTemplate.boundSetOps(key).add(val);
        redisTemplate.boundSetOps(key).expire(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        return index >= 0 ? true : false;
    }

    @Override
    public boolean spushAll(Serializable key, List<Serializable> val) {
        if (!flag) {
            return true;
        }
        for (Serializable v : val) {
            redisTemplate.boundSetOps(key).add(v);
        }
        redisTemplate.boundSetOps(key).expire(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        return true;
    }

    @Override
    public boolean sDelAndPushAll(Serializable key, List<Serializable> val) {
        if (!flag) {
            return true;
        }
        deleteKey(key);
        return spushAll(key, val);
    }

    /**
     * 获得所有zset的值
     */
    @Override
    public List<Serializable> zgetAll(Serializable key) {
        if (!flag) {
            return null;
        }
        if (!redisTemplate.hasKey(key)) {
            return null;
        }
        List<Serializable> objects = null;
        try {
            ZSetOperations operations = redisTemplate.opsForZSet();
            Set sets = operations.range(key, 0, operations.size(key) - 1);    //(key, new ScanOptions.ScanOptionsBuilder().count(100).match("*").build());
            objects = new ArrayList<>(sets);
        } catch (Exception e) {
            deleteKey(key);
            logger.error("get redis error,reflush key : {}", key);
        }
        return objects != null && objects.size() > 0 ? objects : null;
    }

    /**
     * 获得范围内所有zset的值
     */
    @Override
    public List<Serializable> zget(Serializable key, int start, int end) {
        if (!flag) {
            return null;
        }
        if (!redisTemplate.hasKey(key)) {
            return null;
        }
        List<Serializable> objects = null;
        try {
            ZSetOperations operations = redisTemplate.opsForZSet();
            Set sets = operations.range(key, start, end);
            objects = new ArrayList<>(sets);
        } catch (Exception e) {
            deleteKey(key);
            logger.error("get redis error,reflush key : {}", key);
        }
        return objects != null && objects.size() > 0 ? objects : null;
    }

    @Override
    public boolean zpush(Serializable key, Serializable val, double score) {
        if (!flag) {
            return true;
        }
        boolean isSuccess = redisTemplate.boundZSetOps(key).add(val, score);
        redisTemplate.boundZSetOps(key).expire(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        return isSuccess;
    }

    /**
     * 获得zset的大小
     */
    @Override
    public long zsize(Serializable key) {
        if (!flag) {
            return 0;
        }
        return redisTemplate.boundZSetOps(key).size();
    }


    /**
     * 获得所有set的值
     *
     * @param key
     * @return
     */
    @Override
    public List<Serializable> sgetAll(Serializable key) {
        if (!flag) {
            return null;
        }
        if (!redisTemplate.hasKey(key)) {
            return null;
        }
        List<Serializable> objects = null;
        try {
            Set<Serializable> sets = redisTemplate.opsForSet().members(key);    //(key, new ScanOptions.ScanOptionsBuilder().count(100).match("*").build());
            objects = new ArrayList<Serializable>(sets);
        } catch (Exception e) {
            deleteKey(key);
            logger.error("get redis error,reflush key : {}", key);
        }
        return objects != null && objects.size() > 0 ? objects : null;
    }

    public void sremove(Serializable key, Serializable obj) {
        if (!flag) {
            return;
        }
        redisTemplate.boundSetOps(key).remove(obj);
    }

    /**
     * list尾添加一个值为value的元素
     */
    @Override
    public boolean lpush(Serializable key, Serializable val) {
        if (!flag) {
            return true;
        }
        Long index = redisTemplate.opsForList().rightPush(key, val);
        redisTemplate.boundListOps(key).expire(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        return index > 0 ? true : false;
    }

    /**
     * list首添加一个值为value的元素
     */
    @Override
    public boolean lpushToFirst(Serializable key, Serializable val) {
        if (!flag) {
            return true;
        }
        Long index = redisTemplate.opsForList().leftPush(key, val);
        redisTemplate.boundListOps(key).expire(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        return index > 0 ? true : false;
    }

    /**
     * 添加列表
     */
    @Override
    public boolean lpushAll(Serializable key, List<Serializable> val) {
        if (!flag) {
            return true;
        }

        for (Serializable v : val) {
            redisTemplate.boundListOps(key).rightPush(v);
        }
        redisTemplate.boundSetOps(key).expire(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        return true;
    }

    /**
     * 清空并添加列表
     */
    @Override
    public boolean lDelAndPushAll(Serializable key, List<Serializable> val) {
        if (!flag) {
            return true;
        }
        deleteKey(key);
        return lpushAll(key, val);
    }

    /**
     * 获得key的列表
     */
    @Override
    public List<Serializable> lgetAll(Serializable key) {
        if (!flag) {
            return null;
        }
        if (!redisTemplate.hasKey(key)) {
            return null;
        }
        List<Serializable> objects = null;
        try {
            BoundListOperations operation = redisTemplate.boundListOps(key);
            objects = operation.range(0, operation.size() - 1);
        } catch (Exception e) {
            deleteKey(key);
            logger.error("get redis error,reflush key : {}", key);
        }
        return objects;
    }

    /**
     * 获取key的列表大小
     *
     * @param key
     * @return
     */
    @Override
    public long lsize(Serializable key) {
        if (!flag) {
            return 0;
        }
        if (!redisTemplate.hasKey(key)) {
            return 0;
        }
        return redisTemplate.boundListOps(key).size();
    }


    @Override
    public void reflushAll() {
        if (!flag) {
            return;
        }
        String pattern = Constants.REDIS_PREFIX + "*";
        deleteKeyWithPattern(pattern);
    }


    public void deleteKey(Serializable key) {
        if (!flag) {
            return;
        }
        redisTemplate.delete(key);
    }

    public void deleteKey(List<Serializable> key) {
        if (!flag) {
            return;
        }
        redisTemplate.delete(key);
    }

    /**
     * 清除匹配pattern指定模式的缓存
     * （1）?：用于匹配单个字符。例如，h?llo可以匹配hello、hallo和hxllo等；
     * （2）*：用于匹配零个或者多个字符。例如，h*llo可以匹配hllo和heeeello等；
     * （3）[]：可以用来指定模式的选择区间。例如h[ae]llo可以匹配hello和hallo，但是不能匹配hillo。
     * 同时，可以使用“/”符号来转义特殊的字符
     */
    public void deleteKeyWithPattern(Serializable pattern) {
        if (!flag) {
            return;
        }
        Set<Serializable> keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

    public boolean hasKey(Serializable key) {
        if (!flag) {
            return false;
        }
        return redisTemplate.hasKey(key);
    }

}
