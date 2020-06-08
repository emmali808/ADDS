package cn.medicine.controller;

import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import cn.medicine.pojo.User;


@Controller
public class PersonController {
    
    @RequestMapping("/showperson")
    public String showPerson(HttpServletRequest request,Model model){
        String name=request.getParameter("name");
        String password=request.getParameter("password");
        User user=new User(name,password);
       
        System.out.println(name+" "+password);
        model.addAttribute("user", user);
        return "user";
    }
    
    @RequestMapping("/showperson1")
    public String showPerson1(@RequestParam(value="name")String name,@RequestParam(value="password")String password,ModelMap modelMap){
        User user=new User(name,password);       
        System.out.println(name+" "+password);
        modelMap.addAttribute("user", user);
        return "user";
    }        
}
