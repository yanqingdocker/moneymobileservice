package cn.com.caogen.controller;

import cn.com.caogen.entity.Count;
import cn.com.caogen.entity.User;
import cn.com.caogen.externIsystem.service.MobileService;
import cn.com.caogen.externIsystem.util.Md5Util;
import cn.com.caogen.service.CountServiceImpl;
import cn.com.caogen.service.ICountService;
import cn.com.caogen.service.IOperaService;
import cn.com.caogen.service.OperaServiceImpl;
import cn.com.caogen.util.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
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
import java.util.HashMap;
import java.util.Map;

/**
 * author:huyanqing
 * Date:2018/4/24
 */
@RestController
@RequestMapping("/mobile")
public class MobileController {

    private static Logger logger = LoggerFactory.getLogger(MobileController.class);
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private CountServiceImpl countServiceImpl;

    /**
     * 话费充值
     * @param countId
     * @param cardNum
     * @param phone
     * @return
     */
    @RequestMapping(path = "/payMent", method = RequestMethod.GET)
    public String payMent(HttpServletRequest request, @RequestParam("countId") String countId, @RequestParam("cardNum") String cardNum, @RequestParam("phone") String phone,@RequestParam("payPwd") String payPwd) {
        logger.info("payMent start: countId="+countId+",cardNum="+cardNum+",phone="+phone);
        User currentUser=JedisUtil.getUser(request);
        if(currentUser==null){
            logger.info("user=null");
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.NOT_LOGIN)).toString();
        }
        if (!StringUtil.checkStrs(countId,cardNum,phone,payPwd)) {
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.ERROR_ARGS)).toString();
        }
        String oderid = String.valueOf(System.currentTimeMillis());
        double num=Double.parseDouble(cardNum);
        Count count=countServiceImpl.queryById(countId);
        if(count==null){
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.NOTSRCORDEST)).toString();
        }
        if(!count.getPayPwd().equals(Md5Util.strToMD5(payPwd))){
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.PAYPWDERROR)).toString();
        }
        if(num>count.getBlance()){
            //金额不足
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.NOTBLANCE)).toString();
        }
        String snumber="";
        try {
            String result = MobileService.onlineOrder(phone, (int)num, oderid);
            if (StringUtils.isNotEmpty(result)) {
                JSONObject res = JSONObject.fromObject(result);
                if (ConstantUtil.MOBILE_SUCCESS_CODE.equals(res.get("reason"))) {
                    //充值提交成功,更新账户余额
                    if(!count.getCountType().equals("CNY")){
                        String rate=getSingleRate("CNY:USD");
                        num=switchNmu(num,rate);
                    }
                    count.setBlance(count.getBlance()-num);
                    countServiceImpl.updateCount(String.valueOf(count.getId()),count.getBlance(),null,null);
                     snumber=countServiceImpl.saveOperaLog(count.getCardId(),count.getCountType(),-num,ConstantUtil.SERVICETYPE_PHONERECHARGE,"会员-"+currentUser.getUsername(),ConstantUtil.MONEY_OUT,IpUtil.getIpAddr(request));
                } else {
                    return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL)).toString();
                }
            } else {
                return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.FAILSYSTEM)).toString();
            }
        } catch (Exception e) {

        }
        return JSONObject.fromObject(new ResponseMessage(ConstantUtil.SUCCESS,snumber)).toString();
    }

    private String getSingleRate(String type){
        logger.info("getSingleRate start type:"+type);
        if (!StringUtil.checkStrs(type)) {
            return   JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.ERROR_ARGS)).toString();
        }
        String[] strs=type.split(":");
        String from=strs[0];
        String to=strs[1];
        String type1=from+to;
        String type2=to+from;
        String rs=stringRedisTemplate.opsForValue().get(ConstantUtil.SENVEN);

        StringBuffer sb=new StringBuffer();
        if(rs!=null){
            JSONObject jsonObject=JSONObject.fromObject(rs);
            String buyPid="";
            String sellPic="";
            if(jsonObject.has(type1)){
                buyPid=jsonObject.getJSONObject(type1).getString("buyPic");

                return   JSONObject.fromObject(new ResponseMessage(ConstantUtil.SUCCESS,buyPid)).toString();
            }else{
                sellPic=jsonObject.getJSONObject(type2).getString("sellPic");
                return   JSONObject.fromObject(new ResponseMessage(ConstantUtil.SUCCESS,sellPic)).toString();
            }

        }
        return   JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL)).toString();
    }
    private Double switchNmu(Double num,String rate){
        double temp=num/Double.parseDouble(rate);
        String value=String.format("%.4f",temp);
        return Double.parseDouble(value);

    }

}
