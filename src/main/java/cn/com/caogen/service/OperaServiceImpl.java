package cn.com.caogen.service;

import cn.com.caogen.entity.Operation;
import cn.com.caogen.mapper.OperaMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * author:huyanqing
 * Date:2018/5/10
 */
@Service
public class OperaServiceImpl implements IOperaService {
    @Autowired
    private OperaMapper operaMapper;
    @Override
    public void add(Operation operation) {
            operaMapper.add(operation);
    }


    @Override
    public List<Operation> queryAll(Map<String, Object> parmMap) {

        return operaMapper.queryCondition(parmMap);
    }

    @Override
    public List<Operation> queryByDate(Map<String, Object> parmMap) {
        return operaMapper.queryByDate(parmMap);
    }

    @Override
    public List<Operation> queryById(String snumber) {
        return operaMapper.queryById(snumber);
    }
}
