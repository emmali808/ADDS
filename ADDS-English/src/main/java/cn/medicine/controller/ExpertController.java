package cn.medicine.controller;

import cn.medicine.Contant.Contant;
import cn.medicine.dao.user.IDoctorDao;
import cn.medicine.dao.user.IExpertDao;
import cn.medicine.pojo.BasicBodyRecord;
import cn.medicine.pojo.Doctor;
import cn.medicine.pojo.Expert;
import cn.medicine.pojo.HospitalDepartment;
import cn.medicine.pojo.Question;
import cn.medicine.pojo.QuestionAndResult;
import cn.medicine.pojo.QuestionResult;
import cn.medicine.pojo.User;
import cn.medicine.service.*;
import cn.medicine.service.impl.HospitalDepartmentService;
import cn.medicine.utils.JsonUtil;
import cn.medicine.utils.MyException;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
@RequestMapping("/expert")
public class ExpertController {
    @Autowired
    private HospitalDepartmentService hdService;
    @Autowired
    private IUserService userService;
    @Autowired
    private IPatientService patientService;
    @Autowired
    private IExpertService expertService;
    
    @Autowired
    private IMyDataService myDataService;
    @Autowired
    private IQuestionResultService qrService;
    @Autowired
    private IQuestionService questionService;
    
    @RequestMapping(value = "/answerQuestion")
    public String answerQuestion(HttpServletRequest request,Model model) {
        try {
            List<HospitalDepartment> hdlist=hdService.getAll();
            model.addAttribute("hospitalDepartmentList", hdlist);
        } catch (MyException e) {
            e.printStackTrace();
        }
        return "expertAnswerQuestion";
    }
    
    @RequestMapping(value = "/historyAnswerQuestion")
    public String historyAnswerQuestion(HttpServletRequest request,Model model) {
        int curpage=1;//当前页数
        int pagesize=5;//页行数
        int count;//总行数
        int pagenumber;//总页数
        List<QuestionAndResult> display;//显示集合
        int start,end;
        String curpagestr=request.getParameter("curpage");
        if(curpagestr!=null){
            curpage=Integer.parseInt(curpagestr);
        }  
        if(request.getSession()!=null){
            User user=(User)request.getSession().getAttribute("user");
            try {
                
                List<QuestionAndResult> qarlist=qrService.getQuestionAndResultByUserid(user.getId());
                count=qarlist.size();
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
                display=qarlist.subList(start, end);
                
                model.addAttribute("qarList", display);
                model.addAttribute("count", qarlist.size());
                model.addAttribute("usertype", user.getType());
                model.addAttribute("curpage", curpage);
                model.addAttribute("totalpage", pagenumber);
//                List<QuestionResult> qrlist=qrService.getByUserid(user.getId());
//                QuestionResult qr;
//                Question q;
//                List<Long> questionidList=new ArrayList<Long>();
//                Iterator<QuestionResult> it=qrlist.iterator();
//                while(it.hasNext()){
//                    questionidList.add(it.next().getQuestionid());
//                }
//                Map<Long,Question> questionMap=questionService.getByQuestionidlist(questionidList);
//                
//                List<QuestionAndResult> result=new ArrayList<QuestionAndResult>();
//                it=qrlist.iterator();
//                while(it.hasNext()){
//                    QuestionAndResult qar=new QuestionAndResult();
//                    qr=it.next();
//                    q=questionMap.get(qr.getQuestionid());
//                    qar.setQrid(qr.getQrid());
//                }
                
            } catch (MyException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }        
        return "historyQuestionResult";
    }
    
    @RequestMapping(value = "/account")
    public String account(HttpServletRequest request,Model model) {
        if(request.getSession()!=null){
            User user=(User)request.getSession().getAttribute("user");
            model.addAttribute("user", user);
            model.addAttribute("expert", request.getSession().getAttribute("expert"));
        }else{
            System.out.println(">>>>>>>>null");
        }
        return "expertInformation";
    }
    @RequestMapping(value = "/toUpdate")
    public String toUpdate(HttpServletRequest request,Model model) {      
        return "expertModifyInformation";
    }
    @RequestMapping(value = "/update",method= RequestMethod.POST)
    public String update(HttpServletRequest request,Model model) {
        if(request.getSession()!=null) {
            User user = (User) request.getSession().getAttribute("user");
            Expert expert=expertService.getByIdentityID(user.getIdentityID());
            if(expert==null){
                expert=new Expert();
                expert.setIdentityID(user.getIdentityID());
                expertService.add(expert);
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
                expert.setBirthday(request.getParameter("birthday"));
            }
            if(!request.getParameter("marriage").matches("")){
                expert.setMarriage(request.getParameter("marriage"));
            }
            if(!request.getParameter("nation").matches("")){
                expert.setNation(request.getParameter("nation"));
            }if(!request.getParameter("birth_place").matches("")){
                expert.setBirth_place(request.getParameter("birth_place"));
            }
            if(!request.getParameter("work_place").matches("")){
                expert.setWork_place(request.getParameter("work_place"));
            }if(!request.getParameter("title").matches("")){
                expert.setTitle(request.getParameter("title"));
            }if(!request.getParameter("skill").matches("")){
                expert.setSkill(request.getParameter("skill"));
            }if(!request.getParameter("education").matches("")){
                expert.setEducation(request.getParameter("education"));
            }if(!request.getParameter("introduction").matches("")){
                expert.setIntroduction(request.getParameter("introduction"));
            }
        try {
            userService.update(user);
            long result =expertService.update(expert);
            if(result>0){
                model.addAttribute("user", user);
                model.addAttribute("expert", expert);
                request.getSession().setAttribute("user", user);//更新session中的数据
                request.getSession().setAttribute("expert", expert);
                return "expertInformation";
            }else{
                return "fail";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }else{
        System.out.println(">>>>>>>>null");
    }
    return "expertInformation";
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
}



