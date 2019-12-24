package com.imooc.bigdata.redis;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisAppCopy {

    private String host = "192.168.47.66";
    private int port = 6379;


    private Jedis jedis;




    @Before
    public void setUp(){
        jedis = new Jedis(host, port);

    }

    @Test
    public void test01(){

        jedis.set("info","this is chris test");

        Assert.assertEquals("this is chris test", jedis.get("info"));
    }

    @Test
    public void test02(){
        GenericObjectPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(10);
        config.setMaxTotal(100);
        config.setMaxWaitMillis(1000);
        config.setTestOnBorrow(true);

        JedisPool pool = new JedisPool(config, host, port);
        Jedis jedis = pool.getResource();
        Assert.assertEquals("this is chris test", jedis.get("info"));

    }

    @After
    public void tearDown(){
        jedis.close();

    }



}
