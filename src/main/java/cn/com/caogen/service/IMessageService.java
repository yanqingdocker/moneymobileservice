package cn.com.caogen.service;

import cn.com.caogen.entity.Message;

import java.util.List;
import java.util.Map;

/**
 * author:huyanqing
 * Date:2018/4/25
 */
public interface IMessageService {
    public void add(Map<String, String> parmMap);

    public List<Message> queryByCondition(Map<String, Object> parmMap);

    public void update(Map<String, Object> parmMap);

    void delete(int i);

    void batchdelete(String ids);
}
