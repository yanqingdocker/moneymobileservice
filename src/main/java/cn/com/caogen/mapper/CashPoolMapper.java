package cn.com.caogen.mapper;

import cn.com.caogen.entity.CashPool;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * author:huyanqing
 * Date:2018/5/17
 */
@Repository
public interface CashPoolMapper {
    List<CashPool> queryAll();
    void update(CashPool cashPool);
    List<CashPool> queryByType(Map<String, String> parmMap);
    void add(CashPool cashPool);

}
