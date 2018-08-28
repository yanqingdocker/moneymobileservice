package cn.com.caogen.controller;

import cn.com.caogen.entity.User;
import cn.com.caogen.util.ConstantUtil;
import cn.com.caogen.util.IpUtil;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * author:huyanqing
 * Date:2018/4/19
 */
@RestController
@RequestMapping("/rate")
public class RateController {

    private static Logger logger = LoggerFactory.getLogger(RateController.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 获取汇率
     *
     * @return
     */
    @RequestMapping(path = "/queryAll", method = RequestMethod.GET)
    public String getRates() {
        logger.info("getRates start");
        List<String> list = new ArrayList<String>(7);
        list.add(stringRedisTemplate.opsForValue().get(ConstantUtil.ONE));
        list.add(stringRedisTemplate.opsForValue().get(ConstantUtil.TWO));
        list.add(stringRedisTemplate.opsForValue().get(ConstantUtil.THREE));
        list.add(stringRedisTemplate.opsForValue().get(ConstantUtil.FOURE));
        list.add(stringRedisTemplate.opsForValue().get(ConstantUtil.FIVE));
        list.add(stringRedisTemplate.opsForValue().get(ConstantUtil.SIX));
        list.add(stringRedisTemplate.opsForValue().get(ConstantUtil.SENVEN));
        return JSONArray.fromObject(list).toString();
    }

    /**
     * 获取IP
     *
     * @param request
     * @return
     */
    @RequestMapping(path = "/getIp", method = RequestMethod.GET)
    public String getIp(HttpServletRequest request) {
        return IpUtil.getIpAddr(request);
    }

    @RequestMapping(path = "/getSingleRate", method = RequestMethod.GET)
    public String getSingleRate(@RequestParam("type") String type){
        String rs=stringRedisTemplate.opsForValue().get(ConstantUtil.SENVEN);
        StringBuffer sb=new StringBuffer();
        if(rs!=null){
            JSONObject jsonObject=JSONObject.fromObject(rs);
            String buyPid=jsonObject.getJSONObject(type).getString("buyPic");
            String sellPic=jsonObject.getJSONObject(type).getString("sellPic");

            sb.append("{'buyPic':'").append(buyPid).append("','sellPic':'").append(sellPic).append("'}");
        }


        return   JSONObject.fromObject(sb.toString()).toString();
    }




}
