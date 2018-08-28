package cn.com.caogen.service;

import cn.com.caogen.entity.Task;
import cn.com.caogen.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * author:huyanqing
 * Date:2018/5/18
 */
@Service
public class TaskServiceImpl implements  ITaskService {
    @Autowired
    private TaskMapper taskMapper;
    @Override
    public void addTask(Task task) {
        taskMapper.addTask(task);
    }

    @Override
    public void updateTask(Map<String, Object> parmMap) {
        taskMapper.updateTask(parmMap);
    }

    @Override
    public List<Task> queryByState(String state) {
        return taskMapper.queryByState(state);
    }

    @Override
    public List<Task> queryAll() {
        return taskMapper.queryAll();
    }
}
