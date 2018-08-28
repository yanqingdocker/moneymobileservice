package cn.com.caogen.controller;

import cn.com.caogen.entity.Borrow;
import cn.com.caogen.entity.User;
import cn.com.caogen.service.BorrowServiceImpl;
import cn.com.caogen.service.UserServiceImpl;
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
 * Date:2018/6/7
 */
@RestController
@RequestMapping("/borrow")
public class BorrowController {

    private static Logger logger = LoggerFactory.getLogger(BorrowController.class);
    @Autowired
    private BorrowServiceImpl borrowService;
    @Autowired
    private UserServiceImpl userService;

    /**
     * 新增借贷记录
     * @param request
     * @return
     */
    @RequestMapping(path = "add",method = RequestMethod.POST)
    public String add(@RequestParam("datas") String datas, HttpServletRequest request){
        return "";
    }

    /**
     * 查询个人所有借贷记录
     * @param request
     * @return
     */
    @RequestMapping(path = "queryAll",method = RequestMethod.GET)
    public String queryAll(HttpServletRequest request){
        logger.info("queryAllborrower start:");
        User user=JedisUtil.getUser(request);
        Map<String,Object> parmMap=new HashMap<String,Object>();
        parmMap.put("borrowerphone",user.getPhone());
        List<Borrow> borrowList=borrowService.queryAll(parmMap);
        return JSONArray.fromObject(borrowList).toString();
    }

    /**
     * 确认还款
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(path = "confirmdo",method = RequestMethod.POST)
    public String confirmdo(@RequestParam("id") int id, HttpServletRequest request){
        if(id==0){
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,ConstantUtil.ERROR_ARGS)).toString();
        }
        logger.info("confirmdo start:");
        String rs=borrowService.update(id,IpUtil.getIpAddr(request),request);
        if(rs==null){
            return JSONObject.fromObject(new ResponseMessage(ConstantUtil.SUCCESS)).toString();
        }
        return JSONObject.fromObject(new ResponseMessage(ConstantUtil.FAIL,rs)).toString();
    }
}
