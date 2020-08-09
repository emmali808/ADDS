package com.java.adds.controller;


import com.java.adds.controller.vo.LoginVO;
import com.java.adds.entity.QuestionEntity;
import com.java.adds.entity.UserEntity;
import com.java.adds.entity.Usr;
import com.java.adds.security.annotation.PassToken;
import com.java.adds.service.UserService;
import com.java.adds.service.UsrService;
import com.java.adds.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("user")
public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    UsrService usrService;

    @Autowired
    JwtUtil jwtUtil;

    /**
     * QXL
     * User log in
     * @param loginVO login form
     * @return User info, permission and frontend dynamic route
     */
    @PassToken
    @PostMapping("login")
    public Map<String, Object> login(@RequestBody LoginVO loginVO, HttpServletResponse response) {
        Map<String, Object> res;
        UserEntity userEntity = userService.login(loginVO.getLogin_name(), loginVO.getPassword());
        if (userEntity == null) {
            res = null;
            response.setStatus(401);  // 用户名或密码错误
        } else {
            Usr usr = (Usr) usrService.loadUserByUsername(loginVO.getLogin_name());
            String token = jwtUtil.sign(usr.getUsername(), usr.getAuthorities());

            res = new HashMap<>();
            res.put("info", userEntity);
            res.put("permission", usr);
            res.put("route", null);
            res.put("token", token);

            response.setStatus(200);
            response.addHeader("Token", token);
        }
        return res;
    }

    /**ljy
     * 用户注册
     * @return
     */
    @PassToken
    @PutMapping("register")
    public boolean userRegister(@RequestBody UserEntity userEntity)
    {
        return userService.userRegister(userEntity);
    }

    /**ljy
     *管理员获取所有用户信息
     * @rturn
     */
    @GetMapping("")
    public ArrayList<UserEntity> getAllUsers()
    {
        return userService.getAllUsers();
    }


    /**ljy
     *QA检索（有待完善，等qa数据处理好还需要修改）
     * @return
     */
    @PostMapping("similarityQuestion")
    public ArrayList<QuestionEntity> searchSimpleQuestion(@RequestBody String question)
    {
        return userService.searchSimpleQuestion(question);
    }
}
