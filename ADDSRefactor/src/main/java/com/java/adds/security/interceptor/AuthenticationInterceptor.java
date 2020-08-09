package com.java.adds.security.interceptor;

import com.java.adds.security.annotation.PassToken;
import com.java.adds.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * Describe: JWT(JSON Web Token) Authentication Interceptor
 * Create Date: Mar. 15th 2020
 * @author QXL
 */
public class AuthenticationInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) {
        response.setHeader("Access-Control-Allow-Origin","*");
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Methods", "*");
//        response.setHeader("Access-Control-Allow-Headers", "Token");
//        response.setHeader("Access-Control-Expose-Headers", "*");
//
//        if (request.getMethod().equals("OPTIONS")) {
//            response.setStatus(200);
//            return true;
//        }
//
//        if (!(object instanceof HandlerMethod)) {
//            // 如果不是映射到方法则直接通过
//            return true;
//        }
//
//        HandlerMethod handlerMethod = (HandlerMethod) object;
//        Method method = handlerMethod.getMethod();
//
//        if (method.isAnnotationPresent(PassToken.class)) {
//            // 检查是否有 PassToken 注解，有则跳过认证
//            PassToken passToken = method.getAnnotation(PassToken.class);
//            if (passToken.required()) {
//                return true;
//            }
//        } else {
//            // Or, verify TOKEN
//            String token = request.getHeader("Token");
//            if (!StringUtils.isEmpty(token)) {
//                // If there is a TOKEN in request header
//                String name = jwtUtil.getAccount(token);
//                if (name != null) {
//                    if (jwtUtil.validateToken(token)) {
//                        // If this TOKEN is valid
//                        UsernamePasswordAuthenticationToken authentication = jwtUtil.getAuthentication(name, token);
//                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//                        // 向 Security 上下文中注入已认证的账户
//                        // 之后可以直接在控制器 Controller 的入参获得 Principal 或 Authentication
//
////                            System.out.println("[QXL's LOG: AuthenticationInterceptor -> preHandle] " +
////                                    SecurityContextHolder.getContext().getAuthentication().getAuthorities());
//
//                        SecurityContextHolder.getContext().setAuthentication(authentication);
//                        return true;
//                    } else {
//                        System.out.println("[QXL's LOG: AuthenticationInterceptor -> preHandle] Invalid Token. ");
//                    }
//                }
//            } else {
//                System.out.println("[QXL's LOG: AuthenticationInterceptor -> preHandle] No Token. ");
//            }
//            response.setStatus(403);
//            return false;
//        }
        return true;
    }
}
