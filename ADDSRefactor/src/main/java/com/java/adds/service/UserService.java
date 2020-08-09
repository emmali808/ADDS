package com.java.adds.service;


import com.java.adds.dao.QADao;
import com.java.adds.dao.UserDao;
import com.java.adds.entity.QuestionEntity;
import com.java.adds.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService {

    @Autowired
    UserDao userDao;

    @Autowired
    QADao qaDao;

    /**
     * QXL
     * User log in
     * @param login_name User's login name
     * @param password User's login password
     * @return UserEntity User's info without password
     */
    public UserEntity login(String login_name, String password) {
        UserEntity userEntity = userDao.login(login_name);
        if (userEntity != null && password.equals(userEntity.getPassword())) {
            userEntity.setPassword("");
            return userEntity;
        } else {
            return null;
        }
    }

    /**ljy
     * 用户注册
     * @return
     */
    public boolean userRegister(UserEntity userEntity)
    {
        return userDao.userRegister(userEntity);
    }


    /**ljy
     *管理员获取所有用户信息
     * @rturn
     */
    public ArrayList<UserEntity> getAllUsers()
    {
        return userDao.getAllUsers();
    }

    /**ljy
     *QA检索
     * @return
     */
    public ArrayList<QuestionEntity> searchSimpleQuestion(String question)
    {
        return qaDao.searchSimpleQuestion(question);
    }
}
