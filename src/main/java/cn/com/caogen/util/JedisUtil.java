package cn.com.caogen.util;

import cn.com.caogen.entity.User;
import redis.clients.jedis.Jedis;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Set;

/**
 * author:huyanqing
 * Date:2018/5/22
 */
public class JedisUtil {
    public static Jedis jedis;
    public static Jedis getJedis(){
        if(jedis!=null){
            return jedis;
        }
        //r-j6ce364c2198fca4.redis.rds.aliyuncs.com
        try {
            jedis = new Jedis("r-j6ce364c2198fca4.redis.rds.aliyuncs.com",6379);
            jedis.auth("Admin123");
            return jedis;
        }catch (Exception e){

        }finally {
            return null;
        }


    }
    public static User getUser(HttpServletRequest request){
        Jedis jedis=getJedis();
        if(jedis==null){
            return null;
        }
        Map<String,Object> map=(Map)SerializeUtil.unserialize(jedis.get(ConstantUtil.SESSIONCOLLCTION.getBytes()));
        if(map==null||map.get(request.getSession().getId())==null){
            return null;
        }
        User currentUser=(User)SerializeUtil.unserialize((byte[])map.get(request.getSession().getId()));
        return currentUser;
    }

    public static User getUserbysessionid(String sessionid){
        Jedis jedis=getJedis();
        if(jedis==null){
            return null;
        }
        Map<String,Object> map=(Map)SerializeUtil.unserialize(jedis.get(ConstantUtil.SESSIONCOLLCTION.getBytes()));
        if(map==null||map.get(sessionid)==null){
            return null;
        }
        User currentUser=(User)SerializeUtil.unserialize((byte[])map.get(sessionid));
        return currentUser;
    }
    public static Map<String,Object> getSessionMap(){
        Jedis jedis=getJedis();
        if(jedis==null){
            return null;
        }
        Map<String,Object> sessionMap=(Map)SerializeUtil.unserialize(jedis.get(ConstantUtil.SESSIONCOLLCTION.getBytes()));
        if(sessionMap==null){
            return null;
        }
        return sessionMap;
    }
}
