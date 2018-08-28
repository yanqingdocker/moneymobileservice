package cn.com.caogen.filter;

import cn.com.caogen.controller.AppliyController;
import cn.com.caogen.entity.User;
import cn.com.caogen.util.ConstantUtil;
import cn.com.caogen.util.DateUtil;
import cn.com.caogen.util.JedisUtil;
import cn.com.caogen.util.SerializeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.Map;


/**
 * author:huyanqing
 * Date:2018/5/22
 */
public class SystemListener implements HttpSessionListener {
    private static Logger logger = LoggerFactory.getLogger(SystemListener.class);

    @Override
    public void sessionCreated(HttpSessionEvent se) {


    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        Map<String,Object> sessionMap=JedisUtil.getSessionMap();
        sessionMap.remove(se.getSession().getId());
        JedisUtil.getJedis().set(ConstantUtil.SESSIONCOLLCTION.getBytes(),SerializeUtil.serialize(sessionMap));
        logger.info(se.getSession().getMaxInactiveInterval()+"====="+se.getSession().getId()+"destorysession=============================="+DateUtil.getTime());
    }
}
