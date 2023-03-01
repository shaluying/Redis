package com.shaluy.redis;

import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Set;

public class JedisTest {

    /**
     * 测试连接Redis服务
     * 能连接上的前提：
     *      1.需要关闭虚拟机Linux系统的防火墙
     *      2.需要修改Redis的配置文件
     *          2.1 将bind 127.0.0.1注释掉
     *          2.2 将保护模式关闭，即protect-mode的值设置为no
     */
    @Test
    public void testConnection(){
        //创建Jedis对象
        Jedis jedis = new Jedis("192.168.6.100",6379);

        //测试
        String ping = jedis.ping();
        System.out.println("ping = " + ping);

        //关闭连接
        jedis.close();

    }

    @Test
    public void testRedis(){
        Jedis jedis = new Jedis("192.168.6.100", 6379);

        //获取所有的key
        Set<String> keys = jedis.keys("*");
        System.out.println("keys = " + keys);
        //判读某个key是否存在
        Boolean isExistK2 = jedis.exists("k2");
        System.out.println("isExistK2 = " + isExistK2);
        //获取key中值的类型
        String typeK7 = jedis.type("k7");
        System.out.println("typeK7 = " + typeK7);

        //获取k2(String)的值
        String k2 = jedis.get("k2");
        System.out.println("k2 = " + k2);
        //获取k3(List)的值
        List<String> k3 = jedis.lrange("k3", 0, -1);
        System.out.println("k3 = " + k3);
        //获取k4(Set)的值
        Set<String> k4 = jedis.smembers("k4");
        System.out.println("k4 = " + k4);
        //获取k6(Hash)的值
        String k6Address = jedis.hget("k6", "address");
        System.out.println("k6Address = " + k6Address);
        //获取k7(Zset)的值
        Set<String> k7 = jedis.zrange("k7", 0, -1);
        System.out.println("k7 = " + k7);
    }

}
