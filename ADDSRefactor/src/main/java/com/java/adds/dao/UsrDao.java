package com.java.adds.dao;

import com.java.adds.entity.UserEntity;
import com.java.adds.entity.Usr;
import com.java.adds.mapper.UserMapper;
import com.java.adds.mapper.UsrMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Security: Permission management Usr Dao
 * @author QXL
 */
@Repository
public class UsrDao {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UsrMapper usrMapper;

    public Usr getUsrByUsername(String username) {
        UserEntity userEntity = userMapper.login(username);
        if (userEntity != null) {
            Long userId = userEntity.getId();
            ArrayList<String> permissions = usrMapper.getPermissionByUserId(userId);
            Usr usr = new Usr();
            usr.setId(userEntity.getId());
            usr.setUsername(userEntity.getLogin_name());
            usr.setPassword(userEntity.getPassword());

            List<SimpleGrantedAuthority> authorities = new ArrayList<>();
            permissions.forEach(per -> authorities.add(new SimpleGrantedAuthority(per)));

//            permissions.forEach(System.out::println);

            usr.setAuthorities(authorities);
            return usr;
        } else {
            return null;
        }
    }
}
