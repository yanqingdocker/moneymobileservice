package cn.com.caogen.controller;

import cn.com.caogen.entity.Count;
import cn.com.caogen.entity.Operation;
import cn.com.caogen.entity.Task;
import cn.com.caogen.entity.User;
import cn.com.caogen.externIsystem.service.MessageService;
import cn.com.caogen.service.*;
import cn.com.caogen.util.*;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.OpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:huyanqing
 * Date:2018/4/19
 */
@RestController
@RequestMapping("/count")
public class CountController {

    private static Logger logger = LoggerFactory.getLogger(CountController.class);

    @Autowired
    private CountServiceImpl countServiceImpl;

    @Autowired
    private IUserService userServiceImpl;
    @Autowired
    private TaskServiceImpl taskService;
    @Autowired
    private OperaServiceImpl operaService;

    private static String check_Num = "";

    private static String phone = "";


    /**
     * 创建账户
     *
     * @param countType
     * @return
     */
    @RequestMapping(path = "/createCount", method = RequestMethod.GET)
    public String createCount(@RequestParam("countType") String countType, @RequestParam("payPwd") String payPwd, HttpServletRequest request) {

        logger.info("createCount start :countType=" + countType);
        User user=JedisUtil.getUser(request);
        if (StringUtil.checkStrs(countType)) {
            if (checkUser(request.getSession().getAttribute("phone").toString())) {
                payPwd = MD5Util.string2MD5(payPwd);
                return countServiceImpl.createCount(countType, payPwd, String.valueOf(user.getUserid()));

            } else {
                return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL, ConstantUtil.NOT_AUTHENTION)).toString();
            }
        } else {
            logger.error("startOrstopcount id or state is null");
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL, ConstantUtil.ERROR_ARGS)).toString();
        }

    }


    /**
     * 修改账户支付密码
     *
     * @param id
     * @param payPwd
     * @return
     */
    @RequestMapping(path = "/updateCountpwd", method = RequestMethod.POST)
    public String updateCountpwd(@RequestParam("telphone") String telphone,@RequestParam("checknum") String num,@RequestParam("id") String id, @RequestParam("payPwd") String payPwd) {
        logger.info("startOrstopcount start: id="+id+" payPwd="+payPwd);

        payPwd = MD5Util.string2MD5(payPwd);

        if (StringUtil.checkStrs(telphone,num,id, payPwd)) {
            if(phone.equals(telphone)&&check_Num.equals(num)){
                return countServiceImpl.updateCount(id, 0, null,payPwd);
            }else{
                return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL, ConstantUtil.CHECKERROR_NUM)).toString();
            }

        } else {
            logger.error("startOrstopcount id or state is null");
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL, ConstantUtil.ERROR_ARGS)).toString();
        }
    }

    /**
     *
     *
     * @param id
     * @param blance
     * @return
     */
    @RequestMapping(path = "/updateBlance", method = RequestMethod.POST)
    public String updateblance(@RequestParam("id") String id, @RequestParam("blance") String blance) {
        logger.info("updateblance start: id="+id+" blance="+blance);
        if (StringUtil.checkStrs(id, blance)) {
            return countServiceImpl.updateCount(id, Double.parseDouble(blance), null,null);
        } else {
            logger.error("updateBlance id or blance is null");
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL, ConstantUtil.ERROR_ARGS)).toString();
        }
    }

    /**
     * 注销账户
     *
     * @return
     */
    @RequestMapping(path = "/logoutCount", method = RequestMethod.GET)
    public String logoutCount(@RequestParam("id") String id) {
        logger.info("logoutCount start: id="+id);
        if (StringUtil.checkStrs(id)) {
            return countServiceImpl.logoutCount(id);
        } else {
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL, ConstantUtil.ERROR_ARGS)).toString();
        }
    }

    /**
     * 查询当前用户下的所有账户
     *
     * @param request
     * @return
     */
    @RequestMapping(path = "/queryCountByUserid", method = RequestMethod.GET)
    public String queryCountByUserid(HttpServletRequest request) {

        logger.info("queryCountByUserid start ");
        User user=JedisUtil.getUser(request);
        String userId = null;
        if(request.getSession().getAttribute("userid")!=null){
            userId=String.valueOf(user.getUserid());
        }
        return countServiceImpl.queryByUserId(userId);
    }

    /**
     * 查询单个账户
     *
     * @param id
     * @return
     */
    @RequestMapping(path = "/queryCount", method = RequestMethod.GET)
    public String queryCount(@RequestParam("id") String id) {

        logger.info("queryCount start ");
        if (!StringUtil.checkStrs(id)) {
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL, ConstantUtil.ERROR_ARGS)).toString();
        }
        Count count=countServiceImpl.queryById(id);
        if(count==null){
            return "";
        }

        return JSONObject.fromObject(countServiceImpl.queryById(id)).toString();
    }

    /**
     * 转账
     *
     * @param id
     * @param moneynum
     * @param receivecount
     * @param payPwd
     * @return
     */
    @RequestMapping(path = "/switch", method = RequestMethod.GET)
    public String countSwitch(HttpServletRequest request,@RequestParam("countid") String id, @RequestParam("moneynum") Double moneynum, @RequestParam("receivecount") String receivecount, @RequestParam("payPwd") String payPwd) {
        logger.info("countSwitch start: countid="+id+",moneynum="+moneynum+",receivecount="+receivecount+",payPaw="+payPwd);
        User currentuser=JedisUtil.getUser(request);
        if (!StringUtil.checkStrs(id, String.valueOf(moneynum), receivecount)) {
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL, ConstantUtil.ERROR_ARGS)).toString();
        }
        Count srccount = countServiceImpl.queryById(id);
        if(srccount==null){
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL, ConstantUtil.NOTFOUND_COUNT)).toString();

        }
        //校验支付密码
        payPwd = MD5Util.string2MD5(payPwd);
        if (!payPwd.equals(srccount.getPayPwd())) {
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL, ConstantUtil.PAYPWDERROR)).toString();
        }
        //校验账户余额
        if (srccount.getBlance() < moneynum) {
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL, ConstantUtil.NOTBLANCE)).toString();
        }
        User user = getUser(receivecount, null);
        if (user == null) {
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL, ConstantUtil.PAYPWDERROR)).toString();
        }
        //校验对方账户
        List<Count> countList = countServiceImpl.queryByUserId(user.getUserid());
        Count destCount = null;
        for (Count tempcount : countList) {
            if (tempcount.getCountType().equals(srccount.getCountType())) {
                destCount = tempcount;
                break;
            }
        }
        if (destCount == null) {
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL, ConstantUtil.NOTTYPECOUNT)).toString();

        }
        String operuser="会员-"+currentuser.getUsername();
        String snumber=countServiceImpl.countswitch(srccount, destCount, moneynum,IpUtil.getIpAddr(request),operuser);
        return JSONObject.fromObject(new ResponseMessage(ConstantUtil.SUCCESS,snumber)).toString();

    }

    /**
     * 兑换
     *
     * @param datas
     * @return
     */
    @RequestMapping(path = "/exchange", method = RequestMethod.GET)
    public String exchange(@RequestParam("datas") String datas,HttpServletRequest request) {
        logger.info("exchange start: datas="+datas);
        User user=JedisUtil.getUser(request);
        if (!StringUtil.checkStrs(datas)) {
            return net.sf.json.JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL, ConstantUtil.ERROR_ARGS)).toString();
        }
        try {
            JSONObject jsonObject = JSONObject.fromObject(datas);
            String srccountid = jsonObject.getString("srcountid");
            String destcountid = jsonObject.getString("destcountid");
            Double srcmoney = jsonObject.getDouble("srcmoney");
            Double destmoney = jsonObject.getDouble("destmoney");
            String payPwd = jsonObject.getString("paypwd");
            payPwd = MD5Util.string2MD5(payPwd);
            if (!StringUtil.checkStrs(srccountid, destcountid, String.valueOf(srcmoney), String.valueOf(destmoney), payPwd)) {
                return net.sf.json.JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL, ConstantUtil.ERROR_ARGS)).toString();
            }
            String operuser="会员-"+user.getUsername();
            return countServiceImpl.exchange(srccountid, destcountid, srcmoney, destmoney, payPwd,IpUtil.getIpAddr(request),operuser);
        }catch (JSONException e){
            return net.sf.json.JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL, ConstantUtil.ERROR_ARGS)).toString();
        }


    }


    @RequestMapping(path="queryMoneyType",method = RequestMethod.GET)
    public String getMoneyType(){
        return JSONObject.fromObject(ConstantUtil.MONEY_TYPES).toString();
    }

    public boolean checkUser(String telphone) {
        logger.info("checkUser start: telphone="+telphone);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("phone", telphone);
        User temp = null;
        List<User> userList = userServiceImpl.queryAll(map);
        if (userList.isEmpty()) {
            return false;
        } else {
            temp = userList.get(0);
        }
        if (temp.getIsauthentication() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public User getUser(String telphone, String userid) {
        logger.info("getUser start: telphone="+telphone+",userid="+userid);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("phone", telphone);
        User temp = null;
        List<User> userList = userServiceImpl.queryAll(map);
        if (userList.isEmpty()) {
            return null;
        } else {
            return userList.get(0);
        }
    }

    /**
     * 发送手机验证码
     * @param telphone
     */
    @RequestMapping(path = "/checkPhone", method = RequestMethod.POST)
    public String checkPhone(@RequestParam("telphone") String telphone) {
        logger.info("checkPhone start: telphone="+telphone);
        if (!StringUtil.checkStrs(telphone)) {
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.ERROR_ARGS)).toString();
        }
        int num = MessageService.checkPhone(telphone);
        if (num != 0) {
            phone=telphone;
            check_Num = String.valueOf(num);
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.SUCCESS)).toString();
        }else{
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL)).toString();
        }
    }
    @RequestMapping(path = "/getOutCash",method = RequestMethod.POST)
    public String getOutCash(HttpServletRequest request,@RequestParam("paypwd") String paypwd,@RequestParam("countid") String countid,@RequestParam("cardnum") String cardnum,@RequestParam("banktype") String banktype,@RequestParam("num") Double num,@RequestParam("username") String username){
        if(!StringUtil.checkStrs(paypwd,countid,cardnum,banktype,String.valueOf(num),username)){
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.ERROR_ARGS)).toString();
        }
        Count count=countServiceImpl.queryById(countid);
        if(!count.getPayPwd().equals( MD5Util.string2MD5(paypwd))){
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.PAYPWDERROR)).toString();
        }
        if(count.getBlance()<num){
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.NOTBLANCE)).toString();
        }
        count.setBlance(count.getBlance()-num);
        countServiceImpl.updateCount(countid, count.getBlance()-num, null,null);
        Task task=new Task();
        StringBuffer title=new StringBuffer();
        title.append("系统账号为").append(JedisUtil.getUser(request).getPhone()).append("发起提现操作");
        title.append("*******");
        title.append("币种类型:").append(count.getCountType());
        title.append(",姓名:").append(username);
        title.append(",银行类别:").append(banktype);
        title.append(",银行卡号:").append(cardnum);
        title.append(",提现金额:").append(String.valueOf(num));
        title.append("*******");
        task.setTaskname(ConstantUtil.SERVICETYPE_DEPOSIT);
        task.setCreatetime(DateUtil.getTime());
        task.setState(ConstantUtil.TASK_UNDO);
        task.setTaskcontent(title.toString());
        task.setOperauser(JedisUtil.getUser(request).getUsername());
        taskService.addTask(task);
        Operation operation=new Operation();
        operation.setOperaUser("会员-"+JedisUtil.getUser(request).getUsername());
        operation.setCountid(count.getCardId());
        operation.setCountType(count.getCountType());
        operation.setOperaType(ConstantUtil.SERVICETYPE_DEPOSIT);
        operation.setOperaTime(task.getCreatetime());
        operation.setSnumber(SerialnumberUtil.Getnum());
        operation.setOperaIp(IpUtil.getIpAddr(request));
        operation.setOi(ConstantUtil.MONEY_OUT);
        operation.setNum(-num);
        operation.setServicebranch(ConstantUtil.SYSTEM);

        operaService.add(operation);
        return JSONObject.fromObject(new ResponseMessage(ConstantUtil.SUCCESS)).toString();
    }


    @RequestMapping(path="/scanPay",method = RequestMethod.POST)
    public String scanPay(@RequestParam("telphone") String telphone,@RequestParam("type") String type,@RequestParam("num") Double num,@RequestParam("payPwd") String payPwd, HttpServletRequest request){
        logger.info("scanPay start:");
        if (!StringUtil.checkStrs(telphone,type,String.valueOf(num),payPwd)) {
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.ERROR_ARGS)).toString();
        }
        User srcUser=JedisUtil.getUser(request);
        User destUser=getUser(telphone,null);

        String rs=countServiceImpl.scanPay(srcUser,destUser,type,num,payPwd,IpUtil.getIpAddr(request));
        return rs;

    }

    @RequestMapping(path="/settypeAndnum",method = RequestMethod.GET)
    public String scanPay(@RequestParam("type") String type,@RequestParam("num") Double num,HttpServletRequest request){
        logger.info("scanPay start:");
        if (!StringUtil.checkStrs(type,String.valueOf(num))) {
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.ERROR_ARGS)).toString();
        }
        User user=JedisUtil.getUser(request);
        user.setType(type);
        user.setNum(num);

        return JSONObject.fromObject(user).toString();

    }

}
