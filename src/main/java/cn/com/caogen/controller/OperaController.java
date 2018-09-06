package cn.com.caogen.controller;

import cn.com.caogen.entity.Count;
import cn.com.caogen.entity.Operation;
import cn.com.caogen.entity.User;
import cn.com.caogen.service.CountServiceImpl;
import cn.com.caogen.service.ICountService;
import cn.com.caogen.service.IOperaService;
import cn.com.caogen.service.UserServiceImpl;
import cn.com.caogen.util.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:huyanqing
 * Date:2018/5/10
 */
@RestController
@RequestMapping("/opera")
public class OperaController {

    private static Logger logger = LoggerFactory.getLogger(OperaController.class);
    @Autowired
    private IOperaService operaServiceimpl;

    @Autowired
    private CountServiceImpl countServiceimpl;



    /**
     * 查询当前用户下的账户的所有操作纪记录
     * @param request
     * @return
     */
    @RequestMapping(path = "/queryByUserid",method = RequestMethod.GET)
    public String queryByUserid(HttpServletRequest request){
        User currentUser=JedisUtil.getUser(request);
        if(currentUser==null){
            logger.info("user=null");
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.NOT_LOGIN)).toString();
        }
       List<Count> countList=countServiceimpl.queryByUserId(currentUser.getUserid());
       List<Operation> operationList=query(currentUser,null,null);
        return JSONArray.fromObject(operationList).toString();
    }

    /**
     * 查询当天账户的所有记录
     * @return
     */
    @RequestMapping(path = "/queryByDay",method = RequestMethod.GET)
    public String queryByDay(){


        Map<String,Object> parmMap=new HashMap<String,Object>();
        parmMap.put(ConstantUtil.QUERYDATE,ConstantUtil.DATE_TODAY);
        return JSONArray.fromObject(operaServiceimpl.queryByDate(parmMap)).toString();
    }


    /**
     * 查询本周账户的所有记录
     * @return
     */
    @RequestMapping(path = "/queryByWeek",method = RequestMethod.GET)
    public String queryByWeek(){
        Map<String,Object> parmMap=new HashMap<String,Object>();
        parmMap.put(ConstantUtil.QUERYDATE,ConstantUtil.DATE_WEEK);
        return JSONArray.fromObject(operaServiceimpl.queryByDate(parmMap)).toString();
    }

    /**
     * 查询本月账户的所有记录
     * @return
     */
    @RequestMapping(path = "/queryByMonth",method = RequestMethod.GET)
    public String queryByMonth(){
        Map<String,Object> parmMap=new HashMap<String,Object>();
        parmMap.put(ConstantUtil.QUERYDATE,ConstantUtil.DATE_MONTH);
        return JSONArray.fromObject(operaServiceimpl.queryByDate(parmMap)).toString();
    }

    /**
     * 查询本季度账户的所有记录
     * @return
     */
    @RequestMapping(path = "/queryByquarter",method = RequestMethod.GET)
    public String queryByquarter(){
        Map<String,Object> parmMap=new HashMap<String,Object>();
        parmMap.put(ConstantUtil.QUERYDATE,ConstantUtil.DATE_QUARTER);
        return JSONArray.fromObject(operaServiceimpl.queryByDate(parmMap)).toString();
    }

    /**
     * 查询本年账户的所有记录
     * @return
     */
    @RequestMapping(path = "/queryByYear",method = RequestMethod.GET)
    public String queryByYear(){
        Map<String,Object> parmMap=new HashMap<String,Object>();
        parmMap.put(ConstantUtil.QUERYDATE,ConstantUtil.DATE_YEAR);
        return JSONArray.fromObject(operaServiceimpl.queryByDate(parmMap)).toString();
    }

    @RequestMapping(path = "/queryById",method = RequestMethod.GET)
    public String queryById(@RequestParam("id") String snumber){
        logger.info("queryById start: snumber="+snumber);
        if(!StringUtil.checkStrs(snumber)){
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.ERROR_ARGS)).toString();

        }
        List<Operation> operation=operaServiceimpl.queryById(snumber);
        if(operation==null){
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL)).toString();
        }
        return JSONArray.fromObject(operation).toString();

    }

    @RequestMapping(path = "/queryByDate",method = RequestMethod.GET)
    public String queryByDate(@RequestParam("date") String date,HttpServletRequest request){
        //0
        logger.info("queryById start: date="+date);
        if(!StringUtil.checkStrs(date)){
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.ERROR_ARGS)).toString();
        }
        User currentUser=JedisUtil.getUser(request);
        if(currentUser==null){
            logger.info("user=null");
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.NOT_LOGIN)).toString();
        }
        List<Operation> operationList=query(currentUser,date,null);

        return JSONArray.fromObject(operationList).toString();

    }

    @RequestMapping(path = "/queryByType",method = RequestMethod.GET)
    public String queryByType(@RequestParam("type") String type,HttpServletRequest request){
        //0
        logger.info("queryById start: date="+type);
        if(!StringUtil.checkStrs(type)){
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.ERROR_ARGS)).toString();
        }
        User currentUser=JedisUtil.getUser(request);
        if(currentUser==null){
            logger.info("user=null");
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.NOT_LOGIN)).toString();
        }
        List<Operation> operationList=null;
        if("全部".equals(type)){
            operationList=query(currentUser,null,null);
        }else{
            operationList=query(currentUser,null,type);
        }
        return JSONArray.fromObject(operationList).toString();

    }


    private List<Operation> query(User currentUser,String date,String type){
        List<Count> countList=countServiceimpl.queryByUserId(currentUser.getUserid());
        List<Operation> operationList=new ArrayList<Operation>();
        for(Count count:countList){
            Map<String,Object> parmMap=new HashMap<String,Object>();
            parmMap.put("countid",count.getCardId());
            if(StringUtil.checkStrs(date)){
                parmMap.put("date",date);
            }
            if(StringUtil.checkStrs(type)){
                parmMap.put("type",type);
            }
            List<Operation> list=operaServiceimpl.queryAll(parmMap);
            operationList.addAll(list);
        }
        return operationList;
    }

    @Test
    public void test(){
        String str="2018-08-08";
        System.out.println(str.substring(0,str.length()-3));
    }

}
