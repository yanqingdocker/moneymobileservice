package cn.com.caogen.service;

import cn.com.caogen.entity.Borrow;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * author:huyanqing
 * Date:2018/6/7
 */
public interface IBorrowService {
    String add(Borrow borrow, String ip);
    List<Borrow> queryAll(Map<String, Object> parmMap);
    String update(int id, String ip,HttpServletRequest request);
}
