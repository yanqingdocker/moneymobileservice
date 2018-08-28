package cn.com.caogen.mapper;

import cn.com.caogen.entity.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * author:huyanqing
 * Date:2018/4/25
 */
@Component
public interface MessageMapper {

    public void addMessage(Message message);

    List<Message> queryAll(Map<String, Object> map);

    public void updateMessage(Map<String, Object> map);

    void delete(int id);
    void batchdelete(int[] arr);
}
