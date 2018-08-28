package cn.com.caogen.controller;

import cn.com.caogen.externIsystem.service.RateService;
import cn.com.caogen.util.ConstantUtil;
import cn.com.caogen.util.DataMonitor;
import cn.com.caogen.util.JedisUtil;
import cn.com.caogen.util.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * author:huyanqing
 * Date:2018/5/23
 */

@Component
@Order(value = 1)
public class Starup implements CommandLineRunner {

    private static Logger logger = LoggerFactory.getLogger(Starup.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 系统启动时存入sessionMap
     * @param args
     * @throws Exception
     */
    @Override
    public void run(String... args) {
             Jedis jedis=JedisUtil.getJedis();
             if(jedis==null){
                 return;
             }
            if(JedisUtil.getSessionMap()==null){
                Map<String,Object> sessionMap=new HashMap<String, Object>();
                jedis.set(ConstantUtil.SESSIONCOLLCTION.getBytes(),SerializeUtil.serialize(sessionMap));
            }
    }
}
