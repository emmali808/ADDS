package cn.medicine.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.medicine.pojo.BasicBodyRecord;
import cn.medicine.pojo.HospitalDepartment;
import cn.medicine.pojo.Question;
import cn.medicine.pojo.QuestionResult;
import cn.medicine.pojo.User;
import cn.medicine.service.IQuestionResultService;
import cn.medicine.service.IQuestionService;
import cn.medicine.service.impl.HospitalDepartmentService;
import cn.medicine.utils.JsonUtil;
import cn.medicine.utils.MyException;

@Controller
@RequestMapping("/question")
public class QuestionController {
    private static final Logger logger = LogManager.getLogger(QuestionController.class);
    
    @Autowired
    private IQuestionService questionService;
    
    @Autowired
    private IQuestionResultService questionResultService;
    
    @Autowired
    private HospitalDepartmentService hdService;

    @RequestMapping(value="/add",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject add(String questioncontent,Long hospitalDepartmentid,HttpServletRequest request,Model model){
        JSONObject result=new JSONObject();
        String question=questioncontent;
        long hospitalDepartmentID=hospitalDepartmentid;
        System.out.println(hospitalDepartmentID);
        //取得用户信息
        User currentUser=(User)request.getSession().getAttribute("user");
        long userid=currentUser.getId();

        Question q=new Question(question,hospitalDepartmentID,1,userid,"");
        try {
            questionService.add(q);
            result.put("msg",1);
//            List<HospitalDepartment> hdlist=hdService.getAll();
//            model.addAttribute("hospitalDepartmentList", hdlist);
        } catch (MyException e) {
            result.put("msg",-1);
            e.printStackTrace();
        }
        return result;
    }
    
    @RequestMapping(value="/add1",method=RequestMethod.POST)
    public String add1(HttpServletRequest request,Model model){
        String question=request.getParameter("question");
        long hospitalDepartmentID=Long.parseLong(request.getParameter("hospitalDepartment"));
        System.out.println(hospitalDepartmentID);
        //取得用户信息
        User currentUser=(User)request.getSession().getAttribute("user");
        long userid=currentUser.getId();

        Question q=new Question(question,hospitalDepartmentID,1,userid,"");
        try {
            questionService.add(q);
            
            List<HospitalDepartment> hdlist=hdService.getAll();
            model.addAttribute("hospitalDepartmentList", hdlist);
        } catch (MyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
        return "doctorQA";
    }
    
    @RequestMapping(value="/searchAnswer",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject searchAnswer(Long questionid,HttpServletRequest request,Model model){
        JSONObject result=new JSONObject();
        List<QuestionResult> qrlist;
        
        try {

           /* Question question= questionService.get(questionid);
            model.addAttribute("question", question);*/
            qrlist=questionResultService.getByQuestionid(questionid);                       
//            model.addAttribute("questionresultList", qrlist);
            int allnumber=qrlist.size();
            model.addAttribute("allnumber", allnumber);
            Iterator<QuestionResult> it=qrlist.iterator();
            int yescount=0;
            while(it.hasNext()){
                if(it.next().getResultType()==1){
                    yescount++;
                }
            }
            model.addAttribute("yesnumber", yescount);
            model.addAttribute("nonumber", allnumber-yescount);

            result.put("allnumber", allnumber);
            result.put("yesnumber",yescount);
            result.put("nonumber",allnumber-yescount);
            result.put("msg",1);
        } catch (Exception e) {
            e.printStackTrace();
            result.put("msg", -1);
            return result;
        } 
        return result;
    }
    @RequestMapping(value="/searchSelfQuestion",method=RequestMethod.GET)
    public String searchSelfQuestion(HttpServletRequest request,Model model){
        int curpage=1;//当前页数
        int pagesize=5;//页行数
        int count;//总行数
        int pagenumber;//总页数
        List<Question> display;//显示集合
        int start,end;
        String curpagestr=request.getParameter("curpage");
        if(curpagestr!=null){
            curpage=Integer.parseInt(curpagestr);
        }  
        //取得用户信息
        User currentUser=(User)request.getSession().getAttribute("user");
        long userid=currentUser.getId();
        try {
            List<Question> questionList=questionService.getByUserid(userid);
            
            count=questionList.size();
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
            display=questionList.subList(start, end);
            
            model.addAttribute("questionList",display);
            model.addAttribute("count", display.size());
            model.addAttribute("usertype", currentUser.getType());
            model.addAttribute("curpage", curpage);
            model.addAttribute("totalpage", pagenumber);
//            System.out.print("type"+currentUser.getType());
        } catch (MyException e) {
            e.printStackTrace();
        }
        return "selfQuestions";
    }
    
    @RequestMapping(value="/search",method=RequestMethod.GET)
    public String search(HttpServletRequest request,Model model){
        int curpage=1;//当前页数
        int pagesize=5;//页行数
        int count;//总行数
        int pagenumber;//总页数
        List<Question> display;//显示集合
        int start,end;
        String curpagestr=request.getParameter("curpage");
        if(curpagestr!=null){
            curpage=Integer.parseInt(curpagestr);
        }  
        
        List<Question> questionlist,questionlisttwo = new ArrayList<Question>();
        Question q;
        try {
            
            long hdid=Long.parseLong(request.getParameter("hospitalDepartment"));
            if(hdid==0){
                questionlist=questionService.getAllSingleChoiceQuestions(); 
            }else{
                questionlist=questionService.getQuestionsByHospitalDepartmentId(hdid);
            }
            
            //从中删除该用户已经回答过的问题
            User currentUser=(User)request.getSession().getAttribute("user");
            long userid=currentUser.getId();
            List<QuestionResult> qrlist=questionResultService.getByUserid(userid);
            
            List<Long> qridlist=new ArrayList<Long>();
            Iterator<QuestionResult> it=qrlist.iterator();
            while(it.hasNext()){
                qridlist.add(it.next().getQuestionid());
            }
            //复制一份            
            Iterator<Question> qit=questionlist.iterator();
            while(qit.hasNext()){
                questionlisttwo.add(qit.next());
            }
            
            qit=questionlist.iterator();
            while(qit.hasNext()){
                q=qit.next();
                if(qridlist.contains(q.getQid())){
                    questionlisttwo.remove(q);
                }
            }
            
            for(int i=0;i<questionlisttwo.size();i++){
                q=questionlisttwo.get(i);
                q.setRemark(String.valueOf(i+1));
            }
            
            count=questionlisttwo.size();
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
//            System.out.println(start+"--"+end+"curpage:"+curpage+",pagenumber:"+pagenumber+",count:"+count);
            display=questionlisttwo.subList(start, end);
            
            model.addAttribute("questionList", display);
            model.addAttribute("curpage", curpage);
            model.addAttribute("totalpage", pagenumber);
            model.addAttribute("usertype",currentUser.getType());
            model.addAttribute("hospitalDepartment", hdid);
                  
        } catch (Exception e) {
            e.printStackTrace();
            return "index";
        } 
        return "questionList";     
    }
    @RequestMapping(value="/allQuestion",method=RequestMethod.GET)
    public String allQuestion(HttpServletRequest request,Model model){
        int curpage=1;//当前页数
        int pagesize=5;//页行数
        int count;//总行数
        int pagenumber;//总页数
        List<Question> display;//显示集合
        int start,end;
        String curpagestr=request.getParameter("curpage");
        if(curpagestr!=null){
            curpage=Integer.parseInt(curpagestr);
        }

        List<Question> questionlist,questionlisttwo = new ArrayList<Question>();
        Question q;
        try {

            questionlist=questionService.getAllSingleChoiceQuestions();

            //从中删除该用户已经回答过的问题
            User currentUser=(User)request.getSession().getAttribute("user");
            long userid=currentUser.getId();
            List<QuestionResult> qrlist=questionResultService.getByUserid(userid);

            List<Long> qridlist=new ArrayList<Long>();
            Iterator<QuestionResult> it=qrlist.iterator();
            while(it.hasNext()){
                qridlist.add(it.next().getQuestionid());
            }
            //复制一份
            Iterator<Question> qit=questionlist.iterator();
            while(qit.hasNext()){
                questionlisttwo.add(qit.next());
            }

            qit=questionlist.iterator();
            while(qit.hasNext()){
                q=qit.next();
                if(qridlist.contains(q.getQid())){
                    questionlisttwo.remove(q);
                }
            }

            for(int i=0;i<questionlisttwo.size();i++){
                q=questionlisttwo.get(i);
                q.setRemark(String.valueOf(i+1));
            }

            count=questionlisttwo.size();
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
//            System.out.println(start+"--"+end+"curpage:"+curpage+",pagenumber:"+pagenumber+",count:"+count);
            display=questionlisttwo.subList(start, end);

            model.addAttribute("questionList", display);
            model.addAttribute("curpage", curpage);
            model.addAttribute("totalpage", pagenumber);
            model.addAttribute("usertype",currentUser.getType());

        } catch (Exception e) {
            e.printStackTrace();
            return "index";
        }
        return "questionList";
    }

    @RequestMapping(value="/search1",method=RequestMethod.POST)
    @ResponseBody
    public JSONArray search1(HttpServletRequest request,Model model){
        JSONArray array=new JSONArray();
        JSONObject object;
        List<Question> questionlist;
        try {
            String data=URLDecoder.decode(request.getParameter("orderJson"),"UTF-8");
            object=JsonUtil.toJsonObject(data);
            long hdid= Long.parseLong((String)object.get("hdid"));
            if(hdid==0){
                questionlist=questionService.getAllSingleChoiceQuestions(); 
            }else{
                questionlist=questionService.getQuestionsByHospitalDepartmentId(hdid);
            }
            for(int i=0;i<questionlist.size();i++){
                object=JsonUtil.makeObjectToJSONObject(questionlist.get(i));
                array.add(object);
            }
            System.out.println(array.toJSONString());
            return array;            
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }       
        return array;
    }
    
    @RequestMapping(value="/addQuestionResult",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject addQuestionResult(Long questionid,Integer questionanswer,HttpServletRequest request,Model model){
        JSONObject jsonobject=new JSONObject();
        try {
            User currentUser=(User)request.getSession().getAttribute("user");
            long userid=currentUser.getId();
            
//            System.out.println(questionanswer+","+questionid+","+userid);
            
            QuestionResult qr=new QuestionResult(questionid,userid,questionanswer);
            questionResultService.add(qr);
            jsonobject.put("msg", 1);
        } catch (Exception e ) {
            e.printStackTrace();
            jsonobject.put("msg", -1);
            return jsonobject;
        }              
        return jsonobject;
    }
    
    @RequestMapping(value="/addQuestionResult1",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject addQuestionResult1(HttpServletRequest request,Model model){
        JSONObject jsonobject=new JSONObject();
        String data;
        try {
            data = URLDecoder.decode(request.getParameter("orderJson"),"UTF-8");
            jsonobject=JsonUtil.toJsonObject(data);
            int answerid=Integer.parseInt(jsonobject.getString("answerid"));
            long questionid=Long.parseLong(jsonobject.getString("questionid"));
            
            User currentUser=(User)request.getSession().getAttribute("user");
            long userid=currentUser.getId();
            
            System.out.println(answerid+","+questionid+","+userid);
            
            QuestionResult qr=new QuestionResult(questionid,userid,answerid);
            questionResultService.add(qr);
            jsonobject.clear();
            jsonobject.put("mag", "success");
        } catch (Exception e ) {
            e.printStackTrace();
            jsonobject.clear();
            jsonobject.put("mag", "failure");
            return jsonobject;
        }              
        return jsonobject;
    }
    

}
