package cn.medicine.controller;

import cn.medicine.pojo.*;
import cn.medicine.service.*;
import cn.medicine.service.impl.PGService;
import cn.medicine.utils.MyException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
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
 * @Date: 2016/8/31
 * @History: //历史修改记录
 * <author>  // 修改人
 * <time> //修改时间
 * <version> //版本
 * <desc> // 描述修改内容
 */
@Controller
@RequestMapping("/record")
public class RecordController {
    @Autowired
    private IUserService userService;
    @Autowired
    private IPatientService patientService;
    @Autowired
    private IRecordService recordService;
    @Autowired
    private IDoctorService doctorService;
    @Autowired
    private IExpertService expertService;
    @Autowired
    private PatientGraphService patientGraphService;
    @RequestMapping(value = "/basicBody")
    public String basicBody(HttpServletRequest request,Model model) {
        int curpage=1;//当前页数
        int pagesize=1;//页行数
        int count;//总行数
        int pagenumber;//总页数
        List<BasicBodyRecord> display;//显示集合
        int start,end;
        String curpagestr=request.getParameter("curpage");
        if(curpagestr!=null){
            curpage=Integer.parseInt(curpagestr);
        }  
        
        if(request.getSession()!=null){
            User user= (User)request.getSession().getAttribute("user");
            if(user!=null){
                List<BasicBodyRecord> basicBodyInfoList=recordService.getBasicBodyRecordList(user.getIdentityID());
                
                count=basicBodyInfoList.size();
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
                display=basicBodyInfoList.subList(start, end);
                
                //不知在其他地方有木有用到
              //  request.getSession(true).setAttribute("basicBodyInfoList", basicBodyInfoList);

//                model.addAttribute("basicBodyInfoList", basicBodyInfoList);
                
                model.addAttribute("basicBodyInfoList", display);
                model.addAttribute("curpage", curpage);
                model.addAttribute("totalpage", pagenumber);
            }
        }else{
            System.out.println(">>>>>>>>null");
        }
        return "patientBasicBodyInfo";
    }
    @RequestMapping(value = "/uploadClinic",method = RequestMethod.POST)
    public String uploadClinic(HttpServletRequest request,Model model)throws Exception {
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");
        InputStream in=file.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String lineTxt = null;String[] strings;
        Diagnose diagnose=new Diagnose();
        List<Symptoms> symptoms=new ArrayList<>();
        List<LabTest> labTest=new ArrayList<>();
        List<PreviousMH> previousMH=new ArrayList<>();
        List<Treatment> treatment=new ArrayList<>();
        Graph graph=new Graph() ;
        //第1行
        lineTxt=reader.readLine();
        strings=lineTxt.split(" ");
        graph.setG_time(strings[0]);
        graph.setPgID(strings[1]);
        System.out.println("Time:" + strings[0]+" "+strings[1]);
        //第2行 病
        lineTxt=reader.readLine();
        strings=lineTxt.split(" ");
        diagnose.setDiagnose(strings[0]);
        //第3行 症状
        lineTxt=reader.readLine();
        strings=lineTxt.split(",");
        for(int i=0;i<strings.length;i++){
            String[] str=strings[i].split(":");
            symptoms.add(new Symptoms(str[0],str[1]));
        }

        //第4行 检查
        lineTxt=reader.readLine();

        strings=lineTxt.split(",");
        for(int i=0;i<strings.length;i++){
            String[] str=strings[i].split(":");
            labTest.add(new LabTest(str[0],str[1]));
            System.out.println("labTest:" + strings[0] + " " + strings[1]);
        }
        //第5行 病史
        lineTxt=reader.readLine();
        strings=lineTxt.split(",");
        for(int i=0;i<strings.length;i++){
            String[] str=strings[i].split(":");
            previousMH.add(new PreviousMH(str[0],str[1]));
        }
        //第6行 治疗
        lineTxt=reader.readLine();
        strings=lineTxt.split(",");
        for(int i=0;i<strings.length;i++){
            String[] str=strings[i].split(":");
            treatment.add(new Treatment(str[0],str[1]));
        }
        patientGraphService.addClinic(diagnose,symptoms,labTest,previousMH,treatment,graph);
        return "patientGraphDemo";
    }
    @RequestMapping(value = "/uploadPatientList",method = RequestMethod.POST)
    public String uploadPatientList(HttpServletRequest request,Model model)throws Exception {
        System.out.println("file:");
        MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)request;
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");

        InputStream in=file.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String lineTxt = null;
        User user=new User();
        Patient patient=new Patient();
        while((lineTxt=reader.readLine()) !=null && lineTxt !=""){
            String[] strings=lineTxt.split(" ");
            user.setLogin_name(strings[0]);
            user.setUsername(strings[1]);
            user.setPassword("123");
            user.setAge(Integer.parseInt(strings[2]));
            user.setGender(strings[3]);
            user.setPhone(strings[4]);
            user.setIdentityID(strings[5]);
            user.setType(2);
            user.setState(true);
            patient.setIdentityID(strings[5]);
            patient.setCategory("市保健");
            patient.setBirthday(strings[6]);
            patient.setMarriage(strings[7]);
            patient.setNation("汉族");
            patient.setBirth_place(strings[8]);
            patient.setWork_place(strings[9]);
            user.setBirthday(strings[6]);
            user.setMarriage(strings[7]);
            user.setNation("汉族");
            user.setBirth_place(strings[8]);
            user.setWork_place(strings[9]);
            userService.add(user);
            patientService.add(patient);
        }
        return "patientClinicRecord";
    }
    @RequestMapping(value = "/uploadDoctorList",method = RequestMethod.POST)
    public String uploadDoctorList(HttpServletRequest request,Model model)throws Exception {
        System.out.println("file:");
        MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)request;
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");

        InputStream in=file.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String lineTxt = null;
        User user=new User();
        Expert doctor=new Expert();
        while((lineTxt=reader.readLine()) !=null && lineTxt !=""){
            String[] strings=lineTxt.split(" ");
            user.setLogin_name(strings[0]);
            user.setUsername(strings[1]);
            user.setPassword("123");
            user.setAge(Integer.parseInt(strings[2]));
            user.setGender(strings[3]);
            user.setPhone(strings[4]);
            user.setIdentityID(strings[5]);
            user.setType(1);
            user.setState(true);
            doctor.setIdentityID(strings[5]);
            doctor.setBirthday(strings[6]);
            doctor.setMarriage(strings[7]);
            doctor.setNation("汉族");
            doctor.setBirth_place(strings[8]);
            doctor.setWork_place(strings[9]);
            user.setBirthday(strings[6]);
            user.setMarriage(strings[7]);
            user.setNation("汉族");
            user.setBirth_place(strings[8]);
            user.setWork_place(strings[9]);
            user.setEmail(strings[10]);



            userService.add(user);
            expertService.add(doctor);
        }
        return "patientClinicRecord";
    }

    @RequestMapping(value = "/clinicRecord")
    public String clinicRecord(HttpServletRequest request,Model model) {
        int curpage=1;//当前页数
        int pagesize=1;//页行数
        int count;//总行数
        int pagenumber = 0;//总页数
        List<ClinicRecord> display = null;//显示集合
        int start,end;
        String curpagestr=request.getParameter("curpage");
        if(curpagestr!=null){
            curpage=Integer.parseInt(curpagestr);
        }  
        
        if(request.getSession()!=null){
            User user= (User)request.getSession().getAttribute("user");
            if(user!=null){
                //int result =patientService.update(patient);
                List<ClinicRecord> clinicRecordList=new ArrayList<>();
                clinicRecordList=recordService.getClinicRecordList(user.getIdentityID());
//                System.out.println(clinicRecordList.toString());
//                System.out.println(clinicRecordList.get(0));
//                Map<String,List<ClinicRecord>> allClinicRecord=new HashMap<>();
//                allClinicRecord.put(user.getIdentityID(), clinicRecordList);
                 count=clinicRecordList.size();
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
                 display=clinicRecordList.subList(start, end);
                 model.addAttribute("clinicRecordList", display);
                 model.addAttribute("curpage", curpage);
                 model.addAttribute("totalpage", pagenumber);
                 model.addAttribute("usertype", user.getType());   
                 
//                 request.getSession(true).setAttribute("clinicRecordList", clinicRecordList);
//                request.getSession(true).setAttribute("count", clinicRecordList.size());
//                request.getSession(true).setAttribute("current", 0);
//                if(clinicRecordList.size()>0){
//                    model.addAttribute("clinicRecord", clinicRecordList.get(0));
//                    model.addAttribute("count", clinicRecordList.size());
//                    model.addAttribute("current", 0);
//                }
            }
        }else{
            System.out.println(">>>>>>>>null");
        }
        return "patientClinicRecord";
    }
    
    @RequestMapping(value = "/getClinicRecordByDoc")
    public String getClinicRecordByDoc(HttpServletRequest request,Model model) {
        int curpage=1;//当前页数
        int pagesize=1;//页行数
        int count;//总行数
        int pagenumber = 0;//总页数
        List<ClinicRecord> display = null;//显示集合
        int start,end;
        String curpagestr=request.getParameter("curpage");
        if(curpagestr!=null){
            curpage=Integer.parseInt(curpagestr);
        }  
        
        if(request.getSession()!=null){
            User user= (User)request.getSession().getAttribute("user");
            if(user!=null){
                List<ClinicRecord> clinicRecordList=new ArrayList<>();
                 try {
                    clinicRecordList=recordService.getClinicRecordByDoctor(user.getIdentityID());
                    
                    count=clinicRecordList.size();
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
                    display=clinicRecordList.subList(start, end);
                    
                } catch (MyException e) {
                    e.printStackTrace();
                }
                model.addAttribute("clinicRecordList", display);
                model.addAttribute("curpage", curpage);
                model.addAttribute("totalpage", pagenumber);
                model.addAttribute("usertype", user.getType());                
            }
        }else{
            System.out.println(">>>>>>>>null");
        }
        return "patientClinicRecord";
    }
    
    @RequestMapping(value = "/hospitalRecordIn")
    public String getHospitalRecordList(HttpServletRequest request,Model model) {
        int curpage=1;//当前页数
        int pagesize=1;//页行数
        int count;//总行数
        int pagenumber = 0;//总页数
        List<HospitalIn> display = null;//显示集合
        int start,end;
        String curpagestr=request.getParameter("curpage");
        if(curpagestr!=null){
            curpage=Integer.parseInt(curpagestr);
        }  
        
        if(request.getSession()!=null){
            User user= (User)request.getSession().getAttribute("user");
            if(user!=null){
                List<HospitalIn> hospitalInList=recordService.getHospitalInList(user.getIdentityID());
                count=hospitalInList.size();
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
                display=hospitalInList.subList(start, end);
                
                model.addAttribute("hospitalInList", display);
                model.addAttribute("curpage", curpage);
                model.addAttribute("totalpage", pagenumber);
                model.addAttribute("usertype", user.getType());  
            }
        }else{
            System.out.println(">>>>>>>>null");
        }
        return "patientHospitalIn";
    }
    @RequestMapping(value = "/getHospitalRecordInByDoc")
    public String getHospitalRecordInByDoc(HttpServletRequest request,Model model) {
        int curpage=1;//当前页数
        int pagesize=1;//页行数
        int count;//总行数
        int pagenumber = 0;//总页数
        List<HospitalIn> display = null;//显示集合
        int start,end;
        String curpagestr=request.getParameter("curpage");
        if(curpagestr!=null){
            curpage=Integer.parseInt(curpagestr);
        }  
        
        if(request.getSession()!=null){
            User user= (User)request.getSession().getAttribute("user");
            if(user!=null){
                List<HospitalIn> hospitalInList;
                try {
                    hospitalInList = recordService.getHospitalInByDoctor(user.getIdentityID());
                    count=hospitalInList.size();
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
                    display=hospitalInList.subList(start, end);
                    
                    model.addAttribute("hospitalInList", display);
                    model.addAttribute("curpage", curpage);
                    model.addAttribute("totalpage", pagenumber);
                    model.addAttribute("usertype", user.getType()); 
                } catch (MyException e) {
                    e.printStackTrace();
                }               
            }
        }else{
            System.out.println(">>>>>>>>null");
        }
        return "patientHospitalIn";
    }
    
    @RequestMapping(value = "/getHospitalCheckRecordByDoc")
    public String getHospitalCheckRecordByDoc(HttpServletRequest request,Model model) {
        int curpage=1;//当前页数
        int pagesize=1;//页行数
        int count;//总行数
        int pagenumber = 0;//总页数
        List<HospitalCheck> display = null;//显示集合
        int start,end;
        String curpagestr=request.getParameter("curpage");
        if(curpagestr!=null){
            curpage=Integer.parseInt(curpagestr);
        }  
        
        if(request.getSession()!=null){
            User user= (User)request.getSession().getAttribute("user");
            if(user!=null){
                List<HospitalCheck> hospitalCheckList;
                try {
                    hospitalCheckList = recordService.getHospitalCheckByDoctor(user.getIdentityID());
                    count=hospitalCheckList.size();
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
                    display=hospitalCheckList.subList(start, end);
                    
                    model.addAttribute("hospitalCheckList", display);
                    model.addAttribute("curpage", curpage);
                    model.addAttribute("totalpage", pagenumber);
                    model.addAttribute("usertype", user.getType()); 
                } catch (MyException e) {
                    e.printStackTrace();
                }               
            }
        }else{
            System.out.println(">>>>>>>>null");
        }
        return "patientHospitalCheck";
    }
    @RequestMapping(value = "/getHospitalOutRecordByDoc")
    public String getHospitalOutRecordByDoc(HttpServletRequest request,Model model) {
        int curpage=1;//当前页数
        int pagesize=1;//页行数
        int count;//总行数
        int pagenumber = 0;//总页数
        List<HospitalOut> display = null;//显示集合
        int start,end;
        String curpagestr=request.getParameter("curpage");
        if(curpagestr!=null){
            curpage=Integer.parseInt(curpagestr);
        }  
        
        if(request.getSession()!=null){
            User user= (User)request.getSession().getAttribute("user");
            if(user!=null){
                List<HospitalOut> hospitalOutList;
                try {
                    hospitalOutList = recordService.getHospitalOutByDoctor(user.getIdentityID());
                    count=hospitalOutList.size();
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
                    display=hospitalOutList.subList(start, end);
                    
                    model.addAttribute("hospitalOutList", display);
                    model.addAttribute("curpage", curpage);
                    model.addAttribute("totalpage", pagenumber);
                    model.addAttribute("usertype", user.getType()); 
                } catch (MyException e) {
                    e.printStackTrace();
                }               
            }
        }else{
            System.out.println(">>>>>>>>null");
        }
        return "patientHospitalOut";
    }
    
    @RequestMapping(value="/toAddBasicBody")
    public String toAddBasicBody(HttpServletRequest request,Model model){
        return "patientAddBasicBodyInfo";
    }
    @RequestMapping(value="/hospitalRecord4Doctor")
    public String hospitalRecord4Doctor(HttpServletRequest request,Model model){
        return "doctorHospitalRecord";
    }
    @RequestMapping(value="/clinicRecord4Doctor")
    public String clinicRecord4Doctor(HttpServletRequest request,Model model){
        return "doctorClinicRecord";
    }
    @RequestMapping(value="/toAddClinicRecord")
    public String toAddClinicRecord(HttpServletRequest request,Model model){
        return "addClinicRecord";
    }
    @RequestMapping(value="/toAddHospitalIn")
    public String toAddHospitalIn(HttpServletRequest request,Model model){
        return "addHospitalIn";
    }
    @RequestMapping(value="/toAddHospitalCheck")
    public String toAddHospitalCheck(HttpServletRequest request,Model model){
        return "addHospitalCheck";
    }
    @RequestMapping(value="/toAddHospitalOut")
    public String toAddHospitalOut(HttpServletRequest request,Model model){
        return "addHospitalOut";
    }
    @RequestMapping(value = "/addBasicBodyInfo")
    public String addBasicBodyInfo(HttpServletRequest request,Model model) {
        if(request.getSession()!=null){
            User user=(User)request.getSession().getAttribute("user");
            BasicBodyRecord bodyRecord=new BasicBodyRecord();
            bodyRecord.setIdentityID(user.getIdentityID());
            if(!request.getParameter("time").matches("")){
                bodyRecord.setTime(request.getParameter("time"));
            }
            if(!request.getParameter("pulse").matches("")){
                bodyRecord.setPulse(Integer.parseInt(request.getParameter("pulse")));
            }
            if(!request.getParameter("oral").matches("")){
                bodyRecord.setOral(Float.parseFloat(request.getParameter("oral")));
            }
            if(!request.getParameter("rectal").matches("")){
                bodyRecord.setRectal(Float.parseFloat(request.getParameter("rectal")));
            }
            if(!request.getParameter("axillary").matches("")){
                bodyRecord.setAxillary(Float.parseFloat(request.getParameter("axillary")));
            }
           if(!request.getParameter("heart_rate").matches("")){
                bodyRecord.setHeart_rate(Float.parseFloat(request.getParameter("heart_rate")));
            }if(!request.getParameter("weight").matches("")){
                bodyRecord.setWeight(Float.parseFloat(request.getParameter("weight")));
            }if(!request.getParameter("height").matches("")){
                bodyRecord.setHeight(Float.parseFloat(request.getParameter("height")));
            }
            if(!request.getParameter("blood_pressure_low").matches("")){
                bodyRecord.setBlood_pressure_low(Float.parseFloat(request.getParameter("blood_pressure_low")));
            }if(!request.getParameter("blood_pressure_high").matches("")){
                bodyRecord.setBlood_pressure_high(Float.parseFloat(request.getParameter("blood_pressure_high")));
            }if(!request.getParameter("blood_glucose").matches("")){
                bodyRecord.setBlood_glucose(Float.parseFloat(request.getParameter("blood_glucose")));
            }
            try {
                int result=0;
               result=recordService.addBasicBodyRecord(bodyRecord);
                if(result>0){
                    model.addAttribute("user", user);
                    return "doctorHospitalRecord";
                }else{
                    return "fail";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            System.out.println(">>>>>>>>null");
        }
        return "doctorHospitalRecord";
    }
    @RequestMapping(value = "/addClinicRecord")
    public String addClinicRecord(HttpServletRequest request,Model model) {
        if(request.getSession()!=null) {
            User user = (User) request.getSession().getAttribute("user");
            ClinicRecord clinicRecord =new ClinicRecord();
            clinicRecord.setDoctor_ID(user.getIdentityID());
            clinicRecord.setDoctor_name(user.getUsername());

            if (!request.getParameter("time").matches("")) {
                clinicRecord.setTime(request.getParameter("time"));
            }if (!request.getParameter("patient_ID").matches("")) {
                clinicRecord.setPatient_ID(request.getParameter("patient_ID"));
            }if (!request.getParameter("patient_name").matches("")) {
                clinicRecord.setPatient_name(request.getParameter("patient_name"));
            }if (!request.getParameter("content").matches("")) {
                clinicRecord.setContent(request.getParameter("content"));
            }if (!request.getParameter("present_illness").matches("")) {
                clinicRecord.setPresent_illness(request.getParameter("present_illness"));
            }if (!request.getParameter("medical_history").matches("")) {
                clinicRecord.setMedical_history(request.getParameter("medical_history"));
            }if (!request.getParameter("examination").matches("")) {
                clinicRecord.setExamination(request.getParameter("examination"));
            }if (!request.getParameter("inspection").matches("")) {
                clinicRecord.setInspection(request.getParameter("inspection"));
            }if (!request.getParameter("prescription").matches("")) {
                clinicRecord.setPrescription(request.getParameter("prescription"));
            }if (!request.getParameter("type").matches("")) {
                if(request.getParameter("type").matches("0")){
                    clinicRecord.setType(0);
                }else{
                    clinicRecord.setType(1);
                }
            }
            try {
                int result=0;
                result=recordService.addClinicRecord(clinicRecord);
                if(result>0){
                    model.addAttribute("user", user);
                    return "doctorClinicRecord";
                }else{
                    return "fail";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else{
            System.out.println(">>>>>>>>null");
        }
        return "doctorClinicRecord";
    }
    @RequestMapping(value = "/addHospitalIn")
    public String addHospitalIn(HttpServletRequest request,Model model) {
        if(request.getSession()!=null) {
            User user = (User) request.getSession().getAttribute("user");
            HospitalIn hospitalIn =new HospitalIn();
            hospitalIn.setDoctor_ID(user.getIdentityID());
            hospitalIn.setDoctor_name(user.getUsername());
            if (!request.getParameter("time").matches("")) {
                hospitalIn.setTime(request.getParameter("time"));
            }if (!request.getParameter("patient_ID").matches("")) {
                hospitalIn.setPatient_ID(request.getParameter("patient_ID"));
            }if (!request.getParameter("patient_name").matches("")) {
                hospitalIn.setPatient_name(request.getParameter("patient_name"));
            }if (!request.getParameter("causes").matches("")) {
                hospitalIn.setCauses(request.getParameter("causes"));
            }if (!request.getParameter("course").matches("")) {
                hospitalIn.setCourse(request.getParameter("course"));
            }if (!request.getParameter("symptoms").matches("")) {
                hospitalIn.setSymptoms(request.getParameter("symptoms"));
            }if (!request.getParameter("physical_examination").matches("")) {
                hospitalIn.setPhysical_examination(request.getParameter("physical_examination"));
            }if (!request.getParameter("medical_history").matches("")) {
                hospitalIn.setMedical_history(request.getParameter("medical_history"));
            }if (!request.getParameter("assistant_examination").matches("")) {
                hospitalIn.setAssistant_examination(request.getParameter("assistant_examination"));
            }if (!request.getParameter("preliminary_diagnosis").matches("")) {
                hospitalIn.setPreliminary_diagnosis(request.getParameter("preliminary_diagnosis"));
            }if (!request.getParameter("diagnostic_basis").matches("")) {
                hospitalIn.setDiagnostic_basis(request.getParameter("diagnostic_basis"));
            }if (!request.getParameter("differential_diagnosis").matches("")) {
                hospitalIn.setDifferential_diagnosis(request.getParameter("differential_diagnosis"));
            }if (!request.getParameter("assessment_plan").matches("")) {
                hospitalIn.setAssessment_plan(request.getParameter("assessment_plan"));
            }

        try {
            int result=0;
            result=recordService.addHospitalIn(hospitalIn);
            if(result>0){
                model.addAttribute("user", user);
                return "doctorHospitalRecord";
            }else{
                return "fail";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }else{
        System.out.println(">>>>>>>>null");
    }
    return "doctorHospitalRecord";
    }
    @RequestMapping(value = "/addHospitalCheck")
    public String addHospitalCheck(HttpServletRequest request,Model model) {
        if(request.getSession()!=null) {
            User user = (User) request.getSession().getAttribute("user");
            HospitalCheck hospitalCheck =new HospitalCheck();
            hospitalCheck.setRecord_doctor_ID(user.getIdentityID());
            hospitalCheck.setRecord_doctor_name(user.getUsername());
             if (!request.getParameter("time").matches("")) {
                hospitalCheck.setTime(request.getParameter("time"));
            }if (!request.getParameter("patient_ID").matches("")) {
                hospitalCheck.setPatient_ID(request.getParameter("patient_ID"));
            }if (!request.getParameter("patient_name").matches("")) {
                hospitalCheck.setPatient_name(request.getParameter("patient_name"));
            }if (!request.getParameter("doctor_ID").matches("")) {
                hospitalCheck.setDoctor_ID(request.getParameter("doctor_ID"));
            }if (!request.getParameter("doctor_name").matches("")) {
                hospitalCheck.setDoctor_name(request.getParameter("doctor_name"));
            }if (!request.getParameter("symptoms").matches("")) {
                hospitalCheck.setSymptoms(request.getParameter("symptoms"));
            }if (!request.getParameter("physical_examination").matches("")) {
                hospitalCheck.setPhysical_examination(request.getParameter("physical_examination"));
            }if (!request.getParameter("assistant_examination").matches("")) {
                hospitalCheck.setAssistant_examination(request.getParameter("assistant_examination"));
            }if (!request.getParameter("diagnosis").matches("")) {
                hospitalCheck.setDiagnosis(request.getParameter("diagnosis"));
            }

            try {
                int result=0;
                result=recordService.addHospitalCheck(hospitalCheck);
                if(result>0){
                    model.addAttribute("user", user);
                    return "doctorHospitalRecord";
                }else{
                    return "fail";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }else{
            System.out.println(">>>>>>>>null");
        }
        return "doctorHospitalRecord";
    }
    @RequestMapping(value = "/addHospitalOut")
    public String addHospitalOut(HttpServletRequest request,Model model) {
        if(request.getSession()!=null) {
            User user = (User) request.getSession().getAttribute("user");
            HospitalOut hospitalOut =new HospitalOut();
            hospitalOut.setRecord_doctor_ID(user.getIdentityID());
            hospitalOut.setRecord_doctor_name(user.getUsername());

            if (!request.getParameter("time_in").matches("")) {
                hospitalOut.setTime_in(request.getParameter("time_in"));
            }if (!request.getParameter("time_out").matches("")) {
                hospitalOut.setTime_out(request.getParameter("time_out"));
            }if (!request.getParameter("patient_ID").matches("")) {
                hospitalOut.setPatient_ID(request.getParameter("patient_ID"));
            }if (!request.getParameter("patient_name").matches("")) {
                hospitalOut.setPatient_name(request.getParameter("patient_name"));
            }if (!request.getParameter("doctor_ID").matches("")) {
                hospitalOut.setDoctor_ID(request.getParameter("doctor_ID"));
            }if (!request.getParameter("doctor_name").matches("")) {
                hospitalOut.setDoctor_name(request.getParameter("doctor_name"));
            }if (!request.getParameter("treatment_procedure").matches("")) {
                hospitalOut.setTreatment_procedure(request.getParameter("treatment_procedure"));
            }if (!request.getParameter("admission_diagnosis").matches("")) {
                hospitalOut.setAdmission_diagnosis(request.getParameter("admission_diagnosis"));
            }if (!request.getParameter("discharge_diagnosis").matches("")) {
                hospitalOut.setDischarge_diagnosis(request.getParameter("discharge_diagnosis"));
            }if (!request.getParameter("medical_advice").matches("")) {
                hospitalOut.setMedical_advice(request.getParameter("medical_advice"));
            }

            try {
                int result=0;
                result=recordService.addHospitalOut(hospitalOut);
                if(result>0){
                    model.addAttribute("user", user);
                    return "doctorHospitalRecord";
                }else{
                    return "fail";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        }else{
            System.out.println(">>>>>>>>null");
        }
        return "doctorHospitalRecord";
    }
    @RequestMapping(value = "/nextPage")
    public String nextPage(HttpServletRequest request,Model model) {

        if(request.getSession()!=null){
            User user= (User)request.getSession().getAttribute("user");
            String recordType=(String)request.getSession().getAttribute("recordType");
            if(recordType.matches("clinicRecord")){
                int current=(int)request.getSession().getAttribute("recordCurrent");
                int count=(int)request.getSession().getAttribute("recordCount");
                if(current<count-1){
                    current++;
                }
                request.getSession(true).setAttribute("recordCurrent", current);
                model.addAttribute("clinicRecord", recordService.getClinicRecordList(user.getIdentityID()).get(current));
                model.addAttribute("recordCurrent", current);
                model.addAttribute("recordCount", count);
                return "patientClinicRecord";
            }else if(recordType.matches("basicBody")){
                int current=(int)request.getSession().getAttribute("recordCurrent");
                int count=(int)request.getSession().getAttribute("recordCount");
                if(current<count-1){
                    current++;
                }
                request.getSession(true).setAttribute("recordCurrent", current);
                model.addAttribute("basicBodyInfo", recordService.getBasicBodyRecordList(user.getIdentityID()).get(current));
                model.addAttribute("recordCurrent", current);
                model.addAttribute("recordCount", count);
                return "patientBasicBodyInfo";
            }

        }else{
            System.out.println(">>>>>>>>null");
        }
        return "index";
    }
    @RequestMapping(value = "/frontPage")
    public String frontPage(HttpServletRequest request,Model model) {
        if(request.getSession()!=null){
            User user= (User)request.getSession().getAttribute("user");
            String recordType=(String)request.getSession().getAttribute("recordType");
            if(recordType.matches("clinicRecord")){
                int current=(int)request.getSession().getAttribute("recordCurrent");
                if(current>0){
                    current--;
                }
                request.getSession(true).setAttribute("recordCurrent", current);
                model.addAttribute("clinicRecord", recordService.getClinicRecordList(user.getIdentityID()).get(current));
                model.addAttribute("recordCurrent", current);
                return "patientClinicRecord";
            }else if(recordType.matches("basicBody")){
                int current=(int)request.getSession().getAttribute("recordCurrent");
                if(current>0){
                    current--;
                }
                request.getSession(true).setAttribute("recordCurrent", current);
                model.addAttribute("basicBodyInfo", recordService.getBasicBodyRecordList(user.getIdentityID()).get(current));
                model.addAttribute("recordCurrent", current);
                return "patientBasicBodyInfo";
            }
        }else{
            System.out.println(">>>>>>>>null");
        }
        return "patientClinicRecord";
    }


}
