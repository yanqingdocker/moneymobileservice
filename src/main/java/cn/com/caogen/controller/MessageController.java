package cn.com.caogen.controller;

import cn.com.caogen.entity.Message;
import cn.com.caogen.entity.User;
import cn.com.caogen.service.IMessageService;
import cn.com.caogen.service.IUserService;
import cn.com.caogen.util.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:huyanqing
 * Date:2018/4/25
 */
@RestController
@RequestMapping("/message")
public class MessageController {

    private static Logger logger = LoggerFactory.getLogger(MessageController.class);
    @Autowired
    private IMessageService messageService;

    @Autowired
    private IUserService userServiceImpl;



    /**
     * 发送信息
     *
     * @param title
     * @param content
     * @param request
     * @return
     */
    @RequestMapping(path = "/send", method = RequestMethod.POST)
    public String sendMessage(@RequestParam("receivecount") String receivecount,@RequestParam("title") String title, @RequestParam("content") String content, HttpServletRequest request) {
            logger.info("sendMessage start: receivecount="+receivecount+",titlt="+title+",content="+content);
            User currentUser=JedisUtil.getUser(request);
            if (StringUtil.checkStrs(title, content)) {
                User user=getUser(receivecount,null);
                if(user==null||user.getIsauthentication()==0){
                    return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.NOTUSER_OR_NOTAUTENTENTION)).toString();
                }
                if(user.getUserid()==currentUser.getUserid()){
                    return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.NOTALLOWSELF)).toString();
                }
                Map<String, String> parmMap = new HashMap<String, String>();
                parmMap.put("title", title);
                parmMap.put("content", content);
                parmMap.put("sendname",currentUser.getUsername());
                parmMap.put("receivename",user.getUsername());
                parmMap.put("receiveuserid",String.valueOf(user.getUserid()));
                parmMap.put("senduserid",String.valueOf(currentUser.getUserid()));
                messageService.add(parmMap);
                return JSONObject.fromObject(new ResponseMessage(ConstantUtil.SUCCESS)).toString();
            }else {
                return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.ERROR_ARGS)).toString();
            }


    }

    /**
     * 获取发送信息
     * @param request
     * @return
     */
    @RequestMapping(path = "/querysend", method = RequestMethod.GET)
    public String querysend(HttpServletRequest request) {
        logger.info("querysend start: request="+request);
        User currentUser=JedisUtil.getUser(request);
        Map<String,Object> parmMap=new HashMap<String,Object>();
        parmMap.put("userid",currentUser.getUserid());
        parmMap.put("messagetype",1);
        List<Message> message=messageService.queryByCondition(parmMap);
        return JSONArray.fromObject(message).toString();
    }

    /**
     * 获取接收信息
     *
     * @param request
     * @return
     */
    @RequestMapping(path = "/queryreceive", method = RequestMethod.GET)
    public String reciveMessage(HttpServletRequest request) {
        logger.info("reciveMessage start");
        User currentUser=JedisUtil.getUser(request);
        if(currentUser==null){
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.NOT_LOGIN)).toString();
        }
        Map<String,Object> parmMap=new HashMap<String,Object>();
        parmMap.put("userid",currentUser.getUserid());
        parmMap.put("messagetype",0);
        List<Message> message=messageService.queryByCondition(parmMap);
        return JSONArray.fromObject(message).toString();
    }

    /**
     * 标记发送信息已阅读
     * @param id
     * @return
     */
    @RequestMapping(path = "/marksend", method = RequestMethod.POST)
    public String markSend(@RequestParam("id") String id) {
        logger.info("markSend start: id="+id);
        if (StringUtil.checkStrs(id)) {
            Map<String,Object> parmMap=new HashMap<String,Object>();
            parmMap.put("id",Integer.parseInt(id));
            parmMap.put("visiable",1);
            messageService.update(parmMap);
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.SUCCESS)).toString();
        }else {
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.ERROR_ARGS)).toString();
        }
    }

    /**
     * 查询信息详情
     * @param id
     * @return
     */
    @RequestMapping(path = "/queryMessage", method = RequestMethod.GET)
    public String queryMessage(@RequestParam("id") String id) {
        logger.info("queryMessage start: id="+id);
        if (StringUtil.checkStrs(id)) {
            Map<String,Object> parmMap=new HashMap<String,Object>();
            parmMap.put("id",id);

            List<Message> messageList=messageService.queryByCondition(parmMap);
            if(messageList.isEmpty()){
                return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL)).toString();
            }

            return JSONObject.fromObject(messageList.get(0)).toString();
        }else {
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.ERROR_ARGS)).toString();
        }
    }

    /**
     * 标记接收信息已阅读
     * @param id
     * @return
     */
    @RequestMapping(path = "/markreceive", method = RequestMethod.POST)
    public String markrecive(@RequestParam("id") String id) {
        logger.info("markrecive start: id="+id);
        if (StringUtil.checkStrs(id)) {
            Map<String,Object> parmMap=new HashMap<String,Object>();
            parmMap.put("id",Integer.parseInt(id));
            parmMap.put("visiable",1);
            messageService.update(parmMap);
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.SUCCESS)).toString();
        }else {
            return net.sf.json.JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.ERROR_ARGS)).toString();
        }

    }

    /**
     * 删除信息
     * @param id
     * @return
     */
    @RequestMapping(path = "/delete", method = RequestMethod.GET)
    public String delete(@RequestParam("id") String id) {
        logger.info("delete start: id="+id);
        if (StringUtil.checkStrs(id)) {
            messageService.delete(Integer.parseInt(id));
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.SUCCESS)).toString();
        }else {
            return net.sf.json.JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.ERROR_ARGS)).toString();
        }
    }

    /**
     * 删除信息
     * @param ids
     * @return
     */
    @RequestMapping(path = "/batchdelete", method = RequestMethod.GET)
    public String batchdelete(@RequestParam("ids") String ids) {
        logger.info("batchdelete start: ids="+ids);
        if (StringUtil.checkStrs(ids)) {
            messageService.batchdelete(ids);
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.SUCCESS)).toString();
        }else {
            return net.sf.json.JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.ERROR_ARGS)).toString();
        }
    }


    public User getUser(String telphone,String userid){
        logger.info("getUser start: telphone="+telphone+",userid="+userid);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("phone", telphone);
        User temp = null;
        List<User> userList=userServiceImpl.queryAll(map);
        if(userList.isEmpty()){
            return null;
        }else {
            return userList.get(0);
        }
    }
}
