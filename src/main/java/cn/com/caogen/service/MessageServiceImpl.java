package cn.com.caogen.service;

import cn.com.caogen.entity.Message;
import cn.com.caogen.mapper.MessageMapper;
import cn.com.caogen.util.DateUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * author:huyanqing
 * Date:2018/4/25
 */
@Service
public class MessageServiceImpl implements IMessageService {
    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private DataSourceTransactionManager transactionManager;

    @Override
    @Transactional
    public void add(Map<String, String> parmMap) {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setName("addmessag");
        def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        TransactionStatus status = transactionManager.getTransaction(def);
        Message message = new Message();
        message.setTitle(parmMap.get("title"));
        message.setContent(parmMap.get("content"));
        try {
            //添加到收件箱
            message.setCreateTime(DateUtil.getTime());
            message.setUsername(parmMap.get("sendname"));
            message.setUserid(Integer.parseInt(parmMap.get("receiveuserid")));
            message.setMessagetype(0);
            messageMapper.addMessage(message);
            //添加到发件箱
            message.setMessagetype(1);
            message.setUserid(Integer.parseInt(parmMap.get("senduserid")));
            message.setUsername(parmMap.get("receivename"));
            message.setCreateTime(DateUtil.getTime());
            messageMapper.addMessage(message);
        } catch (Exception e) {
            //有一个不成功能则回滚事务
            transactionManager.rollback(status);
        }


    }

    @Override
    public List<Message> queryByCondition(Map<String, Object> parmMap) {
        return messageMapper.queryAll(parmMap);


    }

    @Override
    public void update(Map<String, Object> parmMap) {
        messageMapper.updateMessage(parmMap);

    }

    @Override
    public void delete(int id) {
        messageMapper.delete(id);
    }

    @Override
    public void batchdelete(String ids) {

        String[] strs = ids.split("，");
        strs = strs[0].split(",");
        int[] arr = new int[strs.length];
        for (int i = 0; i < strs.length; i++) {

            arr[i] = Integer.parseInt(strs[i]);
        }
        messageMapper.batchdelete(arr);
    }

}
