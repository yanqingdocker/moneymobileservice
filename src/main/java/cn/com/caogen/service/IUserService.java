package cn.com.caogen.service;

import cn.com.caogen.entity.User;

import java.util.List;
import java.util.Map;

public interface IUserService {
    void addUser(User user);
    void deleteUser(int userid);
    List<User> queryAll(Map<String,Object> map);
    void update(User user);
    User querybyId(int id);
}
