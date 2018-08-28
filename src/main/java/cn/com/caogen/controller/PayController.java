package cn.com.caogen.controller;

import cn.com.caogen.cash.serevice.PayforService;
import cn.com.caogen.entity.BankCard;
import cn.com.caogen.entity.Count;
import cn.com.caogen.entity.Operation;
import cn.com.caogen.externIsystem.service.PayService;
import cn.com.caogen.service.BankCardServiceImpl;
import cn.com.caogen.service.CountServiceImpl;
import cn.com.caogen.service.OperaServiceImpl;
import cn.com.caogen.util.*;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * author:huyanqing
 * Date:2018/4/23
 */
@RestController
@RequestMapping("/pay")
public class PayController {

    private static Logger logger = LoggerFactory.getLogger(PayController.class);

    @Autowired
    private OperaServiceImpl operaService;
    @Autowired
    private CountServiceImpl countServiceImpl;
    @Autowired
    private BankCardServiceImpl bankCardService;
    @Autowired
    private DataSourceTransactionManager transactionManager;
    private int userid;

    @RequestMapping("/recharge")
    public void pay( @RequestParam("tradeMoney") String tradeMoney, @RequestParam("cardid") String cardid,HttpServletResponse response,HttpServletRequest request) {
        //@RequestParam("datas") String datas,

        Lock lock = new ReentrantLock();
        lock.lock();//加锁
        String msg="";
        try {
            String blance=String.valueOf(Integer.parseInt(tradeMoney)*100);
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("blance", blance);
            map.put("cardid",cardid );
            map.put("userid",JedisUtil.getUser(request).getUserid());
            map.put("sessionid",request.getSession().getId());
            userid=JedisUtil.getUser(request).getUserid();
            msg=PayService.orderPay(map);
            response.setContentType("text/html");
            response.getWriter().print(msg);
        } catch (Exception e) {

        } finally {
            lock.unlock();//释放锁
        }
    }

    /**
     * 提现接口
     */
    @RequestMapping("/getcash")
    @Transactional
    public String cash(@RequestParam("datas") String datas,HttpServletRequest request){
        DefaultTransactionDefinition def=new DefaultTransactionDefinition();
        def.setName("countswitch");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status=transactionManager.getTransaction(def);
        String currencyName = "CNY";
        String id = "" + System.currentTimeMillis();
        try {
            JSONObject jsonObject = JSONObject.fromObject(datas);

            String countid=jsonObject.getString("countid");
            String tradeMoney = jsonObject.getString("tradeMoney");//转账金额
            Count count=countServiceImpl.queryById(countid);
            if(count==null){
                return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL)).toString();
            }
            if(count.getBlance()<Double.parseDouble(tradeMoney)){
                return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.NOTBLANCE)).toString();
            }
            String bankid = jsonObject.get("bankid").toString();
            Map<String,Object> parmMap=new HashMap<String,Object>();
            parmMap.put("id",bankid);
            BankCard bankCard=bankCardService.queryCondition(parmMap).get(0);
            String cardHolder = bankCard.getUsername();//持卡人姓名
            String cardNo = bankCard.getBankCard();//卡号

            String re  = PayforService.singlePenTransfer(id, cardHolder, cardNo, String.valueOf(tradeMoney), "",null,currencyName,"","","");
            JSONObject jsonObject1=JSONObject.fromObject(re);
            if(!"0000".equalsIgnoreCase(jsonObject1.getString("respCode"))){
                logger.info("cash fail");
                return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL)).toString();
            }
            countServiceImpl.updateCount(String.valueOf(count.getId()), count.getBlance() - Double.parseDouble(tradeMoney), null,null);
            Operation operation=new Operation();
            operation.setOperaType(ConstantUtil.SERVICETYPE_DEPOSIT);
            operation.setCountid(count.getCardId());
            operation.setOi(ConstantUtil.MONEY_OUT);
            operation.setCountType(count.getCountType());
            operation.setSnumber(SerialnumberUtil.Getnum());
            operation.setNum(-Double.parseDouble(tradeMoney));
            operation.setOperaTime(DateUtil.getTime());
            operation.setOperaIp(IpUtil.getIpAddr(request));
            operation.setOperaUser(JedisUtil.getUser(request).getUsername());
            operaService.add(operation);

        } catch (Exception e) {
            logger.info("cash fail");
            transactionManager.rollback(status);
        } finally {

        }
        logger.info("cash success");
        return JSONObject.fromObject(new ResponseMessage(ConstantUtil.SUCCESS)).toString();
    }


}
