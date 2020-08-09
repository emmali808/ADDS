package com.java.adds.mapper;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * Security: Permission management Usr Mapper
 * @author QXL
 */
@Mapper
@Repository
public interface UsrMapper {

//    /**
//     * 根据用户 id 获取角色 id
//     * @param userId user id
//     * @return Long user role id
//     */
//    @Select("SELECT role_id FROM user_role WHERE user_id=#{userId}")
//    @Results(id = "usrMap", value = {
//            @Result(property = "id", column = "id"),
//            @Result(property = "username", column = "username"),
//            @Result(property = "password", column = "password")
//    })
//    Long getRoleByUserId(@Param("userId") Long userId);

    @Select("SELECT p.url " +
            "FROM permission p, role_permission rp, role r " +
            "WHERE r.id=rp.role_id AND rp.permission_id=p.id AND r.id " +
            "IN " +
            "(SELECT role_id " +
            "FROM user_role " +
            "WHERE user_id=#{userId})")
    ArrayList<String> getPermissionByUserId(@Param("userId") Long userId);
}
