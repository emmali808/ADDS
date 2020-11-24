package com.java.adds.mapper;

import com.java.adds.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface UserMapper {
    /**
     * 用户登录
     * @param
     * @return
     */
    UserEntity login(@Param("loginName") String login_name);


    /**ljy
     * 用户注册
     * @return
     */
    public void userRegister(UserEntity userEntity);

    /**ljy
     *管理员获取所有用户信息
     * @rturn
     */
    public ArrayList<UserEntity> getAllUsers();
}
