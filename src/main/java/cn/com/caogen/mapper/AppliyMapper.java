package cn.com.caogen.mapper;

import cn.com.caogen.entity.Appliy;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * author:huyanqing
 * Date:2018/5/8
 */
@Repository
public interface AppliyMapper {
    void add(Appliy appliy);
    void delete(int id);
    List<Appliy> queryAll(Map<String,Object> parmMap);
}
