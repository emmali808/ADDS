package cn.medicine.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.medicine.Contant.Contant;
import cn.medicine.pojo.*;
import cn.medicine.service.IMyDataService;
import cn.medicine.service.IPatientService;
import cn.medicine.service.IRecordService;
import cn.medicine.service.IUserService;
import cn.medicine.service.PatientGraphService;
import cn.medicine.service.impl.HospitalDepartmentService;
import cn.medicine.utils.JsonUtil;
import cn.medicine.utils.MyException;
import com.alibaba.fastjson.JSON;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;

/**
 * @ClassName:
 * @Description: TODO
 * @Function List: TODO
 * @author: ytchen
 * @version:
 * @Date: 2016/8/11
 * @History: //历史修改记录
 * <author>  // 修改人
 * <time> //修改时间
 * <version> //版本
 * <desc> // 描述修改内容
 */
@Controller
@RequestMapping("/patient")
public class PatientController {
    private static final Logger logger = LogManager.getLogger(PatientController.class);

    @Autowired
    private IUserService userService;
    @Autowired
    private IPatientService patientService;
    @Autowired
    private IRecordService recordService;
    @Autowired
    private PatientGraphService PGService;
    @Autowired
    private HospitalDepartmentService hdService;
    
    @Autowired
    private IMyDataService myDataService;

    @RequestMapping(value = "/account")
    public String account(HttpServletRequest request,Model model) {
        if(request.getSession()!=null){
            model.addAttribute("user", request.getSession().getAttribute("user"));
            model.addAttribute("patient", request.getSession().getAttribute("patient"));

        }else{
            System.out.println(">>>>>>>>null");
        }
        return "patientInformation";
    }

    @RequestMapping(value = "/toUpdate")
    public String toUpdate(HttpServletRequest request,Model model) {
        return "patientModifyInformation";
    }

//    @RequestMapping(value = "/personalizedGraphs")
//    public String personalizedGraphs(HttpServletRequest request,Model model) throws MyException {
//        if(request.getSession()!=null){
//            User user= (User)request.getSession().getAttribute("user");
//            if(user!=null){
//                ArraryList.initall();
//                List<Graph> pgRecord = PGService.getpatientRecord(user.getIdentityID(), null);
//                List d,s,p,t,l,strall,str,str1;
//                d=s=p=t=l=strall=str=str1=null;
//                if(pgRecord.size()!=0) {
//                    strall = PGService.getall();
//                    str = ArraryList.getStrs();
//                    str1 = ArraryList.getStrs1();
//
//                }
//
//                //JSONArray jsonObject= JSON.parseObject(strall);
//                model.addAttribute("strall",strall);
//                model.addAttribute("str",str);
//                model.addAttribute("str1",str1);
//            }
//        }
//        return "patientGraph";
//    }

    @RequestMapping(value = "/personalizedGraphs")
    public String personalizedGraphs1(HttpServletRequest request,Model model) throws MyException {
        if(request.getSession()!=null){
            User user= (User)request.getSession().getAttribute("user");
            if(user!=null){
                List<PatientGraph> list=PGService.getPatientGraph(user.getIdentityID());
                for(PatientGraph patientGraph:list){
                    System.out.println(patientGraph.getTime());
                    System.out.println(patientGraph.getLinks().toJSONString());
                    System.out.println(patientGraph.getNodes().toJSONString());
                }
                System.out.println(JSON.toJSONString(list));
                model.addAttribute("graph",JSON.toJSONString(list));
            }
        }
        return "patientGraphTestText";
    }

    @RequestMapping(value = "/nextPage")
    public String nextPage(HttpServletRequest request,Model model) {
        if(request.getSession()!=null){
            List<ClinicRecord>  clinicRecordList= (List<ClinicRecord> )request.getSession().getAttribute("clinicRecordList");
            if(clinicRecordList!=null){
                int current=(int)request.getSession().getAttribute("current");
                if(current<clinicRecordList.size()-1){
                    current++;
                }
                request.getSession(true).setAttribute("current", current);
                model.addAttribute("clinicRecord", clinicRecordList.get(current));
                model.addAttribute("count", clinicRecordList.size());
                model.addAttribute("current", current);
            }
        }else{
            System.out.println(">>>>>>>>null");
        }
        return "patientClinicRecord";
    }
    @RequestMapping(value = "/frontPage")
    public String frontPage(HttpServletRequest request,Model model) {
        if(request.getSession()!=null){
            List<ClinicRecord>  clinicRecordList= (List<ClinicRecord> )request.getSession().getAttribute("clinicRecordList");
            if(clinicRecordList!=null){
                int current=(int)request.getSession().getAttribute("current");
                if(current>0){
                    current--;
                }
                request.getSession(true).setAttribute("current", current);
                model.addAttribute("clinicRecord", clinicRecordList.get(current));
                model.addAttribute("count", clinicRecordList.size());
                model.addAttribute("current", current);
            }
        }else{
            System.out.println(">>>>>>>>null");
        }
        return "patientClinicRecord";
    }
    @RequestMapping(value = "/medicalRecord")
    public String medicalRecord(HttpServletRequest request,Model model) {
        if(request.getSession()!=null){

            User user= (User)request.getSession().getAttribute("user");
            if(user!=null){
                //int result =patientService.update(patient);
                List<ClinicRecord> clinicRecordList=recordService.getClinicRecordList(user.getIdentityID());
                System.out.println(clinicRecordList.toString());
                System.out.println(clinicRecordList.get(0));
                model.addAttribute("clinicRecord", clinicRecordList.get(0));
                model.addAttribute("count", clinicRecordList.size());
                // model.addAttribute("clinicRecordList", clinicRecordList);
            }
        }else{
            System.out.println(">>>>>>>>null");
        }
        return "patientMedicalRecord";
    }
    @RequestMapping(value = "/ask")
    public String ask(HttpServletRequest request,Model model) {
        try {
            List<HospitalDepartment> hdlist=hdService.getAll();
            model.addAttribute("hospitalDepartmentList", hdlist);
            System.out.println("size="+hdlist.size());
        } catch (MyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "patientQA";
    }
    @RequestMapping(value = "/update",method= RequestMethod.POST)
    public String update(HttpServletRequest request,Model model) {
        if(request.getSession()!=null){
            User user=(User)request.getSession().getAttribute("user");
            Patient patient=(Patient)request.getSession().getAttribute("patient");
            String username=request.getParameter("username");
            String age=request.getParameter("age");
            String gender=request.getParameter("gender");
            String phone=request.getParameter("phone");
            String email=request.getParameter("email");
            String birthday=request.getParameter("birthday");
            String marriage=request.getParameter("marriage");
            String nation=request.getParameter("nation");
            String birth_place=request.getParameter("birth_place");
            String work_place=request.getParameter("work_place");
            String contact_person=request.getParameter("contact_person");
            String contact_relationship=request.getParameter("contact_relationship");
            String contact_address=request.getParameter("contact_address");
            String contact_phone=request.getParameter("contact_phone");
            String category=request.getParameter("category");
            String medicare_card_id=request.getParameter("medicare_card_id");
            if(!username.matches("")){
                user.setUsername(username);
            }
          
            if(!age.matches("")){
                user.setAge(Integer.parseInt(age));
            }
            if(!gender.matches("")){
                user.setGender(gender);
            }
            if(!phone.matches("")){
                user.setPhone(phone);
            }
            if(!email.matches("")){
                user.setEmail(email);
            }
            if(!birthday.matches("")){
                patient.setBirthday(birthday);
            }
            if(marriage.matches("0")){
                patient.setMarriage("未婚");
            }else if(marriage.matches("1")){
                patient.setMarriage("已婚");
            }else if(marriage.matches("2")){
                patient.setMarriage("离异");
            }else if(marriage.matches("3")){
                patient.setMarriage("丧偶");
            }
            if(!nation.matches("")){
                patient.setNation(nation);
            }

            if(!birth_place.matches("")){
                patient.setBirth_place(birth_place);
            }
            if(!work_place.matches("")){
                patient.setWork_place(work_place);
            }
            if(!contact_person.matches("")){
                patient.setContact_person(contact_person);
            }
            if(!contact_relationship.matches("")){
                patient.setContact_relationship(contact_relationship);
            }
            if(!contact_address.matches("")){
                patient.setContact_address(contact_address);
            }
            if(!contact_phone.matches("")){
                patient.setContact_phone(contact_phone);
            }
            if(!category.matches("")){
                patient.setCategory(category);
            }
            if(!medicare_card_id.matches("")){
                patient.setMedicare_card_id(medicare_card_id);
            }

            long result = 0;
            try {
                patientService.update(patient);
                result = userService.update(user);
                if(result>0){
                    model.addAttribute("user", user);
                    model.addAttribute("patient", patient);
                    return "patientInformation";
                }else{
                    return "fail";
                }
            } catch (MyException e) {
                e.printStackTrace();
            }


        }else{
            System.out.println(">>>>>>>>null");
        }
        return "patientInformation";

    }
    
    @RequestMapping(value = "/talk")
    public ModelAndView talk(HttpServletRequest request,Model model) {
        String content=myDataService.get(Contant.websocketuser);
        JSONArray array=new JSONArray();
        JSONObject object=new JSONObject();
//        Map<Long,String> allOnlineUser=new HashMap<Long,String>();
        List<Long> ids=new ArrayList<Long>();
        List<User> users=new ArrayList<User>();
        User onlineuser;
        int type;
        String name;
        
        if(!content.trim().equals("")){
            array=JsonUtil.getJSONArrayFromJsonString(content);
            for(int i=0;i<array.size();i++){
                object=array.getJSONObject(i);
//                allOnlineUser.put(object.getLong("id"), object.getString("name"));
                
                ids.add(object.getLong("id"));
                
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
//            for(Long key:allOnlineUser.keySet()){
//                System.out.println(key+"  "+allOnlineUser.get(key));
//            }
            
//            if(allOnlineUser.containsKey(user.getId())){
//                allOnlineUser.remove(user.getId());
//            }   
           /* for(User touser:users){
                if(touser.getId()==user.getId()){
                    users.remove(touser);
                }
            }   */
            
            /*if(ids.contains(user.getId())){
                ids.remove(user.getId());
            }*/
        }
        
//        model.addAttribute("onlineUser", allOnlineUser);
        
//        model.addAttribute("ids", ids);
        
        model.addAttribute("onlineusers", users);
        model.addAttribute("usertype", user.getType());
                
        return new ModelAndView("talk",model.asMap());

    }

    @RequestMapping(value = "/uploadCase")
    public String uploadCase(HttpServletRequest request,Model model) throws MyException {
        return "uploadDocumentPatient";
    }
    @RequestMapping(value = "/uploadQuestion")
    public String uploadQuestion(HttpServletRequest request,Model model) throws MyException {
        return "uploadQuestion";
    }

    @RequestMapping(value = "/uploadGraphPatient")
    public String uploadGraphPatient(HttpServletRequest request,Model model) throws MyException {
        if(request.getSession()!=null){
            User user= (User)request.getSession().getAttribute("user");
            if(user!=null){
                List<PatientGraph> list=PGService.getPatientGraph(user.getIdentityID());
                for(PatientGraph patientGraph:list){
                    System.out.println(patientGraph.getTime());
                    System.out.println(patientGraph.getLinks().toJSONString());
                    System.out.println(patientGraph.getNodes().toJSONString());
                }
                System.out.println(JSON.toJSONString(list));
                model.addAttribute("graph",JSON.toJSONString(list));
            }
        }
        return "uploadGraphPatient";
    }

    @RequestMapping(value = "/uploadCaseHistory",method = RequestMethod.POST)
    @ResponseBody
    public String addPicture(MultipartFile file)
    {
        String caseExcel="/ADDS/case/";
        System.out.println("BEGIN");
        try {
            String path=System.currentTimeMillis()%10000+file.getOriginalFilename();
            File dest=new File(caseExcel+path);
            if(!dest.getParentFile().exists()){
                dest.getParentFile().mkdir();
            }
            file.transferTo(dest);
        } catch (IOException e) {
            e.printStackTrace();
            return "false";
        }
        return "true";
    }
}
