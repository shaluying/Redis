package com.atguigu.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.io.IOException;
import java.util.List;

/**
 *
 */
public class SecKill_redis {

	public static void main(String[] args) {
		Jedis jedis =new Jedis("192.168.6.100",6379);
		System.out.println(jedis.ping());
		jedis.close();
	}

	//秒杀过程
	public static boolean doSecKill(String uid,String prodid) throws IOException {
		//拼接向redis中保存的商品和秒杀成功的key
		String qtKey = "sk:"+prodid+":qt";
		String userKey = "sk:"+prodid+":user";

		//创建jedis对象
//		Jedis jedis = new Jedis("192.168.6.100", 6379);
		//同过JedisPoolUtil工具类获取JedisPool对象
		JedisPool jedisPoolInstance = JedisPoolUtil.getJedisPoolInstance();
		//获取jedis对象
		Jedis jedis = jedisPoolInstance.getResource();

		//添加乐观锁
		jedis.watch(qtKey);

		//获取秒杀商品剩余库存
		String count = jedis.get(qtKey);

		//判断库存数量
		if ("0".equals(count)){//商品被抢完
			System.err.println("商品被抢完");
			//关闭jedis
//			jedis.close();
			JedisPoolUtil.release(jedisPoolInstance,jedis);
			return false;
		}

		//有库存，做如下操作

		//添加事务
		Transaction multi = jedis.multi();

		//秒杀商品库存减一
//		jedis.decr(qtKey);
		multi.decr(qtKey);
		//抢购成功人员库存加一
//		jedis.sadd(userKey,uid);
		multi.sadd(userKey,uid);
		//执行
		List<Object> exec = multi.exec();
		if (exec == null || exec.size() == 0){
			System.err.println("秒杀失败");
		}

		System.out.println("秒杀成功");

		//关闭jedis
//		jedis.close();
		JedisPoolUtil.release(jedisPoolInstance,jedis);

		return true;
	}
}
















