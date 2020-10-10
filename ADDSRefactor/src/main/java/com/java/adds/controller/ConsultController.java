package com.java.adds.controller;

import com.java.adds.service.ConsultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RestController
@RequestMapping(value="", produces = "application/json;charset=UTF-8")
public class ConsultController {
    @Autowired
    private ConsultService consultService;

    @PostMapping("/consult/online")
    public Object consultOnline(HttpServletRequest request, @RequestParam String msg){
        if (msg.length() > 0) {
            String ans=consultService.consultReuslt(msg);
            return ans;
        }
        return null;
    }
}
