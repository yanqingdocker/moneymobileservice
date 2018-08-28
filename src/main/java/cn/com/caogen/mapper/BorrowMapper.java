package cn.com.caogen.mapper;

import cn.com.caogen.entity.Borrow;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * author:huyanqing
 * Date:2018/6/7
 */
@Repository
public interface BorrowMapper {
    void add(Borrow borrow);
    List<Borrow> queryAll(Map<String, Object> parmMap);

    void update(int id);
}
