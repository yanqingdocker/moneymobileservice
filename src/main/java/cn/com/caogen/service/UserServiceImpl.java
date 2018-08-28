package cn.com.caogen.service;


import cn.com.caogen.entity.User;
import cn.com.caogen.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserServiceImpl implements IUserService{
    @Autowired
    private UserMapper userMapper;
    @Override
    public void addUser(User user){
        userMapper.add(user);
    }
    @Override
    public void deleteUser(int userid){
        userMapper.delete(userid);
    }
    @Override
    public List<User> queryAll(Map<String,Object> map){
        return userMapper.queryAll(map);
    }
    @Override
    public void update(User user){
        userMapper.update(user);
    }

    @Override
    public User querybyId(int id) {
        return userMapper.querybyId(id);
    }
}
