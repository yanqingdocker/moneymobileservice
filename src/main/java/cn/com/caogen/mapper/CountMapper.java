package cn.com.caogen.mapper;

import cn.com.caogen.entity.Count;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * author:huyanqing
 * Date:2018/4/20
 */
@Component
public interface CountMapper {
    void add(Count count);
    void update(Count count);
    void delete(int id);
    List<Count> queryAll();
    List<Count> queryByUserId(int userid);
    Count queryById(int id);
}
