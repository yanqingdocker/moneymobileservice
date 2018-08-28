package cn.com.caogen.service;

import cn.com.caogen.entity.Appliy;
import cn.com.caogen.mapper.AppliyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * author:huyanqing
 * Date:2018/5/8
 */
@Service
public class AppliyServiceImpl implements IAppliyService {

    @Autowired
    private AppliyMapper appliyMapper;
    @Override
    public void bind(Appliy appliy){
        appliyMapper.add(appliy);

    }
    @Override
    public void unbind(int id){
        appliyMapper.delete(id);
    }
    @Override
    public List<Appliy> query(Map<String,Object> parmMap){
        return appliyMapper.queryAll(parmMap);
    }
}
