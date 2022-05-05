package com.huarui;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SbMoreSubmitApplicationTests {

	@Autowired
	private RedisTemplate redisTemplate;

	@Test
	public void contextLoads() {
		/*
		if(no exists key){
			return false;
		}else {
		 	del key;
		 	return true;
		 }
		 */

		String keys = "token_111";

		String luaScript =
				"if redis.call('exists', KEYS[1]) == 0 then " +
						"return 0; " +
						"else " +
						"redis.call('del', KEYS[1]); " +
						"return 1; " +
						"end;";

		Boolean existKey = findToken(luaScript, keys);
		System.out.println(existKey);
	}


	public Boolean findToken(String luaScript,String keys)  {
		Boolean existKey = (Boolean) redisTemplate.execute(
				(RedisConnection connection) -> connection.eval(
						luaScript.getBytes(),//lua脚本字符串
						ReturnType.BOOLEAN,//设置返回值 byte[]
						1, //设置key数量
						keys.getBytes()
				)
		);
		return existKey;
	}

}
