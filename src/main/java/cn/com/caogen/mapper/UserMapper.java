package cn.com.caogen.mapper;


import cn.com.caogen.entity.User;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Component
public interface UserMapper {
    void add(User user);
    void delete(int userid);
    List<User> queryAll(Map<String,Object> map);
    void update(User user);
    User querybyId(int id);
}
