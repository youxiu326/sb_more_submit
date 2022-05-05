package com.huarui.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * redis工具类
 */
@Component
public class RedisTokenUtils {

    private long timeout = 2;//过期时间

    @Autowired
    private RedisTemplate redisTemplate;

    private static final String LUASCRIPT =
            "if redis.call('exists', KEYS[1]) == 0 then " +
                    "return 0; " +
                    "else " +
                    "redis.call('del', KEYS[1]); " +
                    "return 1; " +
                    "end;";

    /**
     * 获取Token 并将Token保存至redis
     * @return
     */
    public String getToken() {
        String token = "token_"+ UUID.randomUUID();
        redisTemplate.opsForValue().set(token,token,timeout, TimeUnit.MINUTES);
        return token;
    }

    /**
     * 判断Token是否存在 并且删除Token
     * @param tokenKey
     * @return
     */
    @Deprecated
    public boolean findTokenOld(String tokenKey){
        String token = (String) redisTemplate.opsForValue().get(tokenKey);
        if (StringUtils.isEmpty(token)) {
            return false;
        }
        // token 获取成功后 删除对应tokenMapstoken
        redisTemplate.delete(tokenKey);
        return true;
    }

    /**
     * 判断Token是否存在 并且删除Token
     * @param tokenKey
     * @return
     */
    public boolean findToken(String tokenKey){
        Boolean existKey = (Boolean) redisTemplate.execute(
                (RedisConnection connection) -> connection.eval(
                        LUASCRIPT.getBytes(), //lua脚本
                        ReturnType.BOOLEAN,   //设置返回 布尔值
                        1, //设置key数量
                        tokenKey.getBytes()
                )
        );
        return existKey;
    }

}
