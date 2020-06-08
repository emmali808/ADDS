package cn.medicine.controller;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import cn.medicine.Contant.Contant;
import cn.medicine.dao.user.IDoctorDao;
import cn.medicine.pojo.Doctor;
import cn.medicine.pojo.HospitalDepartment;
import cn.medicine.pojo.Patient;
import cn.medicine.service.IDoctorService;
import cn.medicine.service.IExpertService;
import cn.medicine.service.IMyDataService;
import cn.medicine.service.IPatientService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.medicine.pojo.User;
import cn.medicine.service.IUserService;
import cn.medicine.service.impl.HospitalDepartmentService;
import cn.medicine.utils.JsonUtil;
import cn.medicine.utils.MyException;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    private static final Logger logger = LogManager.getLogger(UserController.class);
    
    @Autowired
    private IUserService userService;

    @Autowired
    private IPatientService patientService;

    @Autowired
    private IDoctorService doctorService;
    @Autowired
    private IExpertService expertService;
    @Autowired
    private HospitalDepartmentService hdService;
    @Autowired
    private IMyDataService myDataService;

    @RequestMapping(value="/login",method=RequestMethod.POST)
    public String login(HttpServletRequest request,Model model){
        String login_name=request.getParameter("login_name");
        String password=request.getParameter("password");
        
        User user=userService.login(login_name, password);

        if(user !=null){
            model.addAttribute("user", user);
            request.getSession(true).setAttribute("user", user);
            
            if(user.getType()==0){//管理 员
                return "admin";
            }else if(user.getType()==1){//医生
                request.getSession(true).setAttribute("doctor", doctorService.getByIdentityID(user.getIdentityID()));
                return "doctor";
            }else if(user.getType()==2){//病人
                request.getSession(true).setAttribute("patient", patientService.getPatientByIdentityID(user.getIdentityID()));
                return "patient";
            }else if(user.getType()==3){//专家
                request.getSession(true).setAttribute("expert", expertService.getByIdentityID(user.getIdentityID()));
                return "expert";
            }
            return "index1";
        }else{
            return "loginFail";
        }
    }
    @RequestMapping(value="/userLogin")
    public String toLogin(HttpServletRequest request,Model model){
        return "login";
    }

    @RequestMapping(value="/toAddUser")
    public String toAddUser(HttpServletRequest request,Model model){
        return "register";
    }

    //author TYJ
    @RequestMapping(value="/goToHome")
    public String goToHome(HttpServletRequest request,Model model){
        return "index1";
    }
    //author TYJ
    @RequestMapping(value="/succeedAddUser")
    public String succeedAddUser(HttpServletRequest request,Model model){
        return "registerSucceed";
    }
    //author TYJ
    @RequestMapping(value="/register",method=RequestMethod.POST)
    public String register(HttpServletRequest request,Model model){
        String login_name=request.getParameter("login_name");
        String password=request.getParameter("password");
        String identityID=request.getParameter("identityID");
        String isDoctor=request.getParameter("isDoctor");
        String gender=request.getParameter("gender");
        System.out.println("here");
        System.out.println(isDoctor);
        int result=-1;
        if(isDoctor.equals("true")){   //如果是医生
            User newUser=new User(login_name,password);
            newUser.setIdentityID(identityID);
            newUser.setLogin_name(login_name);
            newUser.setGender(gender);
            newUser.setType(1);
            newUser.setState(false);
            try {
                result=userService.add(newUser);
                System.out.println("result="+result);
            } catch (MyException e) {
                e.printStackTrace();
            }
        }
        else{     //如果是病人，直接注册成功
            User newUser=new User(login_name,password);
            newUser.setIdentityID(identityID);
            newUser.setLogin_name(login_name);
            newUser.setGender(gender);
            try {
                result=userService.add(newUser);
            } catch (MyException e) {
                e.printStackTrace();
            }
        }
        if(result==1&&!isDoctor.equals("true")){
            return "registerSucceed";
        }
        else if(result==1&&isDoctor.equals("true")){
            return "doctorRegisterSucceed";
        }
        else {
            return "registerFailed";
        }
    }

    //author ljy
    @ResponseBody
    @RequestMapping(value="/check",method=RequestMethod.GET)
    public int check(String login_name){
        String loginName=login_name;
        try {
            return userService.checkDoctor(loginName);
        } catch (MyException e) {
            e.printStackTrace();
        }
        return 0;
    }


    @RequestMapping(value = "/account")
    public String account(HttpServletRequest request,Model model) {
        if(request.getSession()!=null){
            User currentUser=(User)request.getSession().getAttribute("user");
            model.addAttribute("user", currentUser);
            model.addAttribute("usertype", currentUser.getType());
        }else{
            System.out.println("用户没有登录");
        }
        return "userInformation";
    }
    @RequestMapping(value = "/toUpdate")
    public String toUpdate(HttpServletRequest request,Model model) {
        if(request.getSession()!=null){
            User currentUser=(User)request.getSession().getAttribute("user");
            model.addAttribute("user", currentUser);
            model.addAttribute("usertype", currentUser.getType());
        }else{
            System.out.println("用户没有登录");
        }
        return "userModifyInformation";
    }
    @RequestMapping(value = "/update",method= RequestMethod.POST)
    public String update(HttpServletRequest request,Model model) {
        if(request.getSession()!=null) {
            User user = (User) request.getSession().getAttribute("user");
            
            if(!request.getParameter("username").matches("")){
                user.setUsername(request.getParameter("username"));
            }
            if(!request.getParameter("identityID").matches("")){
                user.setIdentityID(request.getParameter("identityID"));
            }
            if(!request.getParameter("age").matches("")){
                user.setAge(Integer.parseInt(request.getParameter("age")));
            }
            if(!request.getParameter("gender").matches("")){
                user.setGender(request.getParameter("gender"));
            }
            if(!request.getParameter("phone").matches("")){
                user.setPhone(request.getParameter("phone"));
            }
            if(!request.getParameter("email").matches("")){
                user.setEmail(request.getParameter("email"));
            }           
        try {
            userService.update(user);
            request.getSession().setAttribute("user", user);//更新session中的数据
//            model.addAttribute("user", user);
//            return "userInformation";         
        } catch (Exception e) {
            e.printStackTrace();
        }
    }else{
        System.out.println(">>>>>>>>null");
    }
    return "redirect:/user/account";
   }

    @RequestMapping(value="/addUser",method=RequestMethod.POST)
    public String add(HttpServletRequest request,Model model){
        String username=request.getParameter("username");
        String         login_name =request.getParameter("login_name");
        String password=request.getParameter("password");
        String gender=request.getParameter("gender");
        String phone=request.getParameter("phone");
        String identityID=request.getParameter("identityID");
        int type=Integer.parseInt(request.getParameter("type"));
        User newUser=new User(username,password,gender,phone,type);
        newUser.setLogin_name(login_name);
        newUser.setIdentityID(identityID);
        int result = 0;
        try {
            logger.info(newUser.toString());
            result = userService.add(newUser);
        } catch (MyException e) {
            e.printStackTrace();
        }
        if(result==1){
        	 model.addAttribute("user", newUser);
            return "index";
        }else{
            return "fail";
        }

    }
    @RequestMapping(value="/allUser")
    public String getAllUser(HttpServletRequest request,Model model){
        int curpage=1;//当前页数
        int pagesize=8;//页行数
        int count;//总行数
        int pagenumber;//总页数
        List<User> display;//显示集合
        int start,end;
        String curpagestr=request.getParameter("curpage");
        if(curpagestr!=null){
            curpage=Integer.parseInt(curpagestr);
        }  
        
        List<User> result = userService.getAllUser();
        if(result!=null){
            System.out.println("result!=null:"+result.size());
        }        
                              
        count=result.size();
        if(count%pagesize==0){
            pagenumber=count/pagesize;
        }else{
            pagenumber=count/pagesize+1;
        }
        if(curpage>pagenumber)
            curpage=pagenumber;
        if(curpage<1)
            curpage=1;
       
        
        start=(curpage-1)*pagesize;
        end=curpage*pagesize;
        if(end>count)
            end=count;
        
        display=result.subList(start,end);
        
        model.addAttribute("userList", display);
        model.addAttribute("curpage", curpage);
        model.addAttribute("totalpage", pagenumber);
        return "allUsers";
    }

    @ResponseBody
    @RequestMapping(value="/allDoctorNotPass")
    public List<User> getAllDoctorNotPass(HttpServletRequest request,Model model){
        List<User> result = null;
        try {
            result = userService.getAllDoctorNotPass();
        } catch (MyException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @RequestMapping(value="/allpatients")
    public String getAllPatients(HttpServletRequest request,Model model){
        int curpage=1;//当前页数
        int pagesize=8;//页行数
        int count;//总行数
        int pagenumber = 0;//总页数
        List<User> display = null;//显示集合
        int start,end;
        String curpagestr=request.getParameter("curpage");
        if(curpagestr!=null){
            curpage=Integer.parseInt(curpagestr);
        }  
        List<User> result = null;
        try {
            result = userService.getPatients();
            count=result.size();
            if(count%pagesize==0){
                pagenumber=count/pagesize;
            }else{
                pagenumber=count/pagesize+1;
            }
            if(curpage>pagenumber)
                curpage=pagenumber;
            if(curpage<1)
                curpage=1;
           
            
            start=(curpage-1)*pagesize;
            end=curpage*pagesize;
            if(end>count)
                end=count;
            display=result.subList(start,end);
        } catch (MyException e) {
            e.printStackTrace();
        }
       
        model.addAttribute("userList", display);
        model.addAttribute("curpage", curpage);
        model.addAttribute("totalpage", pagenumber);
        return "allPatients";
    }
    
    @RequestMapping(value="/alldoctors")
    public String getAllDoctors(HttpServletRequest request,Model model){
        int curpage=1;//当前页数
        int pagesize=8;//页行数
        int count;//总行数
        int pagenumber = 0;//总页数
        List<User> display = null;//显示集合
        int start,end;
        String curpagestr=request.getParameter("curpage");
        if(curpagestr!=null){
            curpage=Integer.parseInt(curpagestr);
        }  
        List<User> result = null;
        try {
            result = userService.getDoctors();
            count=result.size();
            if(count%pagesize==0){
                pagenumber=count/pagesize;
            }else{
                pagenumber=count/pagesize+1;
            }
            if(curpage>pagenumber)
                curpage=pagenumber;
            if(curpage<1)
                curpage=1;
           
            
            start=(curpage-1)*pagesize;
            end=curpage*pagesize;
            if(end>count)
                end=count;
            display=result.subList(start,end);
        } catch (MyException e) {
            e.printStackTrace();
        }
      
        model.addAttribute("userList", display);
        model.addAttribute("curpage", curpage);
        model.addAttribute("totalpage", pagenumber);
        return "allDoctors";
    }
    @RequestMapping(value="/allexperts")
    public String getAllExperts(HttpServletRequest request,Model model){
        int curpage=1;//当前页数
        int pagesize=8;//页行数
        int count;//总行数
        int pagenumber = 0;//总页数
        List<User> display = null;//显示集合
        int start,end;
        String curpagestr=request.getParameter("curpage");
        if(curpagestr!=null){
            curpage=Integer.parseInt(curpagestr);
        }  
        List<User> result = null;
        try {
            result = userService.getExperts();
            count=result.size();
            if(count%pagesize==0){
                pagenumber=count/pagesize;
            }else{
                pagenumber=count/pagesize+1;
            }
            if(curpage>pagenumber)
                curpage=pagenumber;
            if(curpage<1)
                curpage=1;
          
            
            start=(curpage-1)*pagesize;
            end=curpage*pagesize;
            if(end>count)
                end=count;
            display=result.subList(start,end);
        } catch (MyException e) {
            e.printStackTrace();
        }
//        if(result!=null){
//            System.out.println("result!=null:"+result.size());
//        }
        model.addAttribute("userList", display);
        model.addAttribute("curpage", curpage);
        model.addAttribute("totalpage", pagenumber);
        return "allExperts";
    }

    @RequestMapping(value = "/loginOut")
    public String loginOut(HttpServletRequest request,Model model) {

    	request.getSession().invalidate();
        return "index1";
    }
    @RequestMapping(value = "/index")
      public String index(HttpServletRequest request,Model model) {
        if(request.getSession()!=null){
            User user=(User)request.getSession().getAttribute("user");
            if(user==null){
            	return "fail";
            }else  if(user.getType()==0){//管理 员
                return "admin";
            }else if(user.getType()==1){//医生
                return "doctor";
            }else if(user.getType()==2){//病人
                return "patient";
            }else if(user.getType()==3){//专家
                return "expert";
            }
            return "index1";
        }else{
            System.out.println(">>>>>>>>null");
        }

        return "index1";
    }
    
    @RequestMapping(value = "/hospitalOverview")
    public String hospitalOverview(HttpServletRequest request,Model model) {
        if(request.getSession()!=null){
            User user=(User)request.getSession().getAttribute("user");
            model.addAttribute("usertype", user.getType());
            return "hospitalOverview";
        }else{
            System.out.println(">>>>>>>>null");
        }

        return "index1";
    }
    @RequestMapping(value = "/updateOnlineUsers")
    @ResponseBody
    public JSONObject updateOnlineUsers(HttpServletRequest request,Model model) {             
       String content=myDataService.get(Contant.websocketuser);
       JSONArray array=new JSONArray();
       JSONObject object=new JSONObject();
       List<User> users=new ArrayList<User>();
       User onlineuser;
       int type;
       String name;      
       if(!content.trim().equals("")){
           array=JsonUtil.getJSONArrayFromJsonString(content);
           for(int i=0;i<array.size();i++){
               object=array.getJSONObject(i);                              
               onlineuser=new User();
               
               onlineuser.setId(object.getLongValue("id"));
               type=object.getIntValue("type");
               name=object.getString("name");
               if(type==1){
                   onlineuser.setUsername(name+"("+"医生"+")"); 
               }else if(type==2){
                   onlineuser.setUsername(name+"("+"病人"+")"); 
               }else if(type==3){
                   onlineuser.setUsername(name+"("+"专家"+")"); 
               }else if(type==4){
                   onlineuser.setUsername(name+"("+"普通用户"+")"); 
               }else if(type==0){
                   onlineuser.setUsername(name+"("+"管理员"+")"); 
               }else{
                   onlineuser.setUsername(name);
               }
               users.add(onlineuser);
           }
       }
       User user=(User)request.getSession().getAttribute("user");
       if(user!=null){
           int position=-1;
           for(int i=0;i<users.size();i++){
               if(users.get(i).getId()==user.getId()){
                   position=i;
               }
           }
           if(position!=-1){
               users.remove(position);
           }
       }
       object.clear();
       object.put("onlineusers", users);
       object.put("msg", 1);               
       return object;
    }

}
