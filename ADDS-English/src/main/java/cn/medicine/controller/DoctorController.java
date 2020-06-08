package cn.medicine.controller;

import cn.medicine.ocr.Demo;
import cn.medicine.ocr.Demo1;
import cn.medicine.pojo.Doctor;
import cn.medicine.Contant.Contant;
import cn.medicine.pojo.HospitalDepartment;
import cn.medicine.pojo.User;
import cn.medicine.service.IDoctorService;
import cn.medicine.service.IMyDataService;
import cn.medicine.service.IUserService;
import cn.medicine.service.impl.HospitalDepartmentService;
import cn.medicine.utils.JsonUtil;
import cn.medicine.utils.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName:
 * @Description: TODO
 * @Function List: TODO
 * @author: ytchen
 * @version:
 * @Date: 2016/9/3
 * @History: //历史修改记录
 * <author>  // 修改人
 * <time> //修改时间
 * <version> //版本
 * <desc> // 描述修改内容
 */
@Controller
@RequestMapping("/doctor")
public class DoctorController {
    @Autowired
    private HospitalDepartmentService hdService;
    @Autowired
    private IDoctorService doctorService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IMyDataService myDataService;
    
    @RequestMapping(value = "/QA")
    public String doctorQA(HttpServletRequest request,Model model) {
        try {
            List<HospitalDepartment> hdlist=hdService.getAll();
            model.addAttribute("hospitalDepartmentList", hdlist);
        } catch (MyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "doctorQA";
    }
    @RequestMapping(value = "/account")
    public String account(HttpServletRequest request,Model model) {
        if(request.getSession()!=null){
            model.addAttribute("user", request.getSession().getAttribute("user"));
            model.addAttribute("doctor", request.getSession().getAttribute("doctor"));
        }else{
            System.out.println(">>>>>>>>null");
        }
        return "doctorInformation";
    }
    @RequestMapping(value = "/allPatients4doctor")
    public String allPatients4doctor(HttpServletRequest request,Model model) {
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
            if(request.getSession()!=null) {
                User user = (User) request.getSession().getAttribute("user");
                result = doctorService.getPatients(user.getIdentityID());
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
        } catch (Exception e) {
            e.printStackTrace();
        }

        model.addAttribute("userList", display);
        model.addAttribute("curpage", curpage);
        model.addAttribute("totalpage", pagenumber);
        return "allPatients";
    }
    @RequestMapping(value = "/documentList")
    public String getList(HttpServletRequest request,Model model) {
        return "documentList";
    }

    @RequestMapping(value = "/uploadDocumentDoctor")
    public String uploadDocDoctor(HttpServletRequest request,Model model) {
        return "uploadDocumentDoctor";
    }

    @RequestMapping(value = "/uploadDocument")
    public String uploadDoc(HttpServletRequest request,Model model) {
        return "uploadDocumentPatient";
    }
    @RequestMapping(value = "/toUpdate")
    public String toUpdate(HttpServletRequest request,Model model) {
        return "doctorModifyInformation";
    }
    @RequestMapping(value = "/update",method= RequestMethod.POST)
    public String update(HttpServletRequest request,Model model) {
        if(request.getSession()!=null) {
            User user = (User) request.getSession().getAttribute("user");

            Doctor doctor=doctorService.getByIdentityID(user.getIdentityID());
            if(doctor==null){
                doctor=new Doctor();
                doctor.setIdentityID(user.getIdentityID());
                doctorService.add(doctor);
            }
            if(!request.getParameter("username").matches("")){
                user.setUsername(request.getParameter("username"));
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
            if(!request.getParameter("birthday").matches("")){
                doctor.setBirthday(request.getParameter("birthday"));
                System.out.println("birthday"+doctor.getBirthday());

            }
            if(!request.getParameter("marriage").matches("")){
                doctor.setMarriage(request.getParameter("marriage"));
            }
            if(!request.getParameter("nation").matches("")){
                doctor.setNation(request.getParameter("nation"));
            }if(!request.getParameter("birth_place").matches("")){
                doctor.setBirth_place(request.getParameter("birth_place"));
            }
            if(!request.getParameter("work_place").matches("")){
                doctor.setWork_place(request.getParameter("work_place"));
            }if(!request.getParameter("entry_time").matches("")){
                doctor.setEntry_time(request.getParameter("entry_time"));
            }if(!request.getParameter("department").matches("")){
                doctor.setDepartment(request.getParameter("department"));
            }if(!request.getParameter("duty").matches("")){
                doctor.setDuty(request.getParameter("duty"));
            }if(!request.getParameter("title").matches("")){
                doctor.setTitle(request.getParameter("title"));
            }if(!request.getParameter("skill").matches("")){
                doctor.setSkill(request.getParameter("skill"));
            }if(!request.getParameter("outpatient_time").matches("")){
                doctor.setOutpatient_time(request.getParameter("outpatient_time"));
            }if(!request.getParameter("introduction").matches("")){
                doctor.setIntroduction(request.getParameter("introduction"));
            }
        try {
            userService.update(user);
            long result =doctorService.update(doctor);
            if(result>0){
                model.addAttribute("user", user);
                model.addAttribute("doctor", doctor);
                request.getSession().setAttribute("user", user);//更新session中的数据
                request.getSession().setAttribute("doctor", doctor);
                return "doctorInformation";
            }else{
                return "fail";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }else{
        System.out.println(">>>>>>>>null");
    }
    return "doctorInformation";
   }

    
    @RequestMapping(value = "/talk")
    public ModelAndView talk(HttpServletRequest request,Model model) {
             
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
    
       model.addAttribute("onlineusers", users);
       model.addAttribute("usertype", user.getType());
               
       return new ModelAndView("talk",model.asMap());
    }


    @ResponseBody
    @RequestMapping(value = "/uploadZip",method= RequestMethod.POST)
    public Boolean uploadZip(HttpServletRequest request,Model model,@RequestParam("file") MultipartFile multipartFile) {
        //String path = "../../resources/";
        String name=multipartFile.getOriginalFilename();//直接返回文件的名字
        String subffix = name.substring(name.lastIndexOf(".") + 1, name.length());
        String fileName=new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        //String filepath=request.getServletContext().getRealPath("/")+"files\\";//获取项目路径到webapp
        //String filepath=request.getServletContext().getRealPath("")+"/zip/";//获取项目路径到webapp
        String filepath="/root/temp/case/upload/";//获取项目路径到webapp
        File file=new File(filepath);
        if(!file.exists()){//目录不存在就创建
            file.mkdirs();
        }
        String realPath = file.getPath();
        try {
            multipartFile.transferTo(new File(realPath + "/" + fileName+"."+subffix));//保存文件
            //multipartFile.transferTo(new File("../../resources/"+fileName+"."+subffix));//保存文件
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(realPath);
        Demo demo = new Demo();
        demo.ocr(realPath);
        return true;
    }

}



