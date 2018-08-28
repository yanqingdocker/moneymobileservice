package cn.com.caogen.mapper;

import cn.com.caogen.entity.Task;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * author:huyanqing
 * Date:2018/5/18
 */
@Repository
public interface TaskMapper {
        void addTask(Task task);
        void updateTask(Map<String, Object> parmMap);
        List<Task> queryByState(String state);
        List<Task> queryAll();
}
