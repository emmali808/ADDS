package com.java.adds.dao;

import com.java.adds.entity.UserEntity;
import com.java.adds.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class UserDao {
    @Autowired
    UserMapper userMapper;

    /**
     * QXL
     * User log in
     * @param login_name User's login name
     * @return UserEntity User's info
     */
    public UserEntity login(String login_name) {
        return userMapper.login(login_name);
    }

    /**ljy
     * 用户注册
     * @return
     */
    public boolean userRegister(UserEntity userEntity)
    {
        userMapper.userRegister(userEntity);

        return true;
    }

    /**ljy
     *管理员获取所有用户信息
     * @rturn
     */
    public ArrayList<UserEntity> getAllUsers()
    {
        return userMapper.getAllUsers();
    }
}
