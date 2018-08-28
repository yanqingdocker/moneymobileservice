package cn.com.caogen.service;

import cn.com.caogen.entity.Operation;

import java.util.List;
import java.util.Map;

/**
 * author:huyanqing
 * Date:2018/5/8
 */
public interface IOperaService {
    public void add(Operation operation);
    public List<Operation> queryAll();
    public List<Operation> queryAll(Map<String, Object> parmMap);
    List<Operation> queryByDate(Map<String,Object> parmMap);
    List<Operation> queryById(String snumber);
}
