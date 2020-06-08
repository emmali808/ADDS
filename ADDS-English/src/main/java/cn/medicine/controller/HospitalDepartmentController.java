package cn.medicine.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import cn.medicine.pojo.Patient;
import cn.medicine.pojo.User;
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

import cn.medicine.pojo.HospitalDepartment;
import cn.medicine.service.impl.HospitalDepartmentService;
import cn.medicine.utils.JsonUtil;
import cn.medicine.utils.MyException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping("/hospitalDepartment")
public class HospitalDepartmentController {
    
    private static final Logger logger = LogManager.getLogger(HospitalDepartmentController.class);
    
    @Autowired
    private HospitalDepartmentService hdService;
    
    @RequestMapping(value="/add",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject add(HttpServletRequest request){
        JSONObject object=new JSONObject();
        JSONObject hdobject=new JSONObject();
        
        int hdid=0;
        try{
            String data=URLDecoder.decode(request.getParameter("orderJson"),"UTF-8");
            hdobject=JsonUtil.toJsonObject(data);
            HospitalDepartment hd=new HospitalDepartment();
            hd.setName(hdobject.getString("name"));
            hd.setDescription(hdobject.getString("description"));
            hd.setRemark(hdobject.getString("remark"));
            hdid=hdService.add(hd);            
            logger.info("新增科室");
            object.put("msg", hdid);
        }catch(MyException | UnsupportedEncodingException e){
            logger.error("新增科室时出错"+e.getMessage());
            object.put("msg", hdid);
            return object;
        }
        return object;
    }
    @RequestMapping(value = "/add2",method = RequestMethod.POST)
    public String add2(HttpServletRequest request,Model model)throws Exception {
        System.out.println("file:");
        MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)request;
        MultipartFile file = ((MultipartHttpServletRequest) request).getFile("file");

        InputStream in=file.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String lineTxt = null;
        User user=new User();
        Patient patient=new Patient();
        while((lineTxt=reader.readLine()) !=null && lineTxt !=""){
            HospitalDepartment hd=new HospitalDepartment();
            hd.setName(lineTxt);
            hdService.add(hd);
        }
        return "patientClinicRecord";
    }
    @RequestMapping(value="/add1",method=RequestMethod.POST)
    @ResponseBody
    public String add1(String name,String description,String remark,HttpServletRequest request){
        System.out.println("add方法  科室");
//        JSONObject object=new JSONObject();
        int hdid=0;
        try{
            HospitalDepartment hd=new HospitalDepartment();
            hd.setName(name);
            hd.setDescription(description);
            hd.setRemark(remark);
            hdid=hdService.add(hd);            
            logger.info("新增科室");
            return "";
        }catch(MyException e){
            logger.error("新增科室时出错"+e.getMessage());
//            e.printStackTrace();
            return "";
        }
    }
    
    @RequestMapping(value="/listall")
    public String listAll(HttpServletRequest req,Model model){
        try{
            List<HospitalDepartment> departlist=hdService.getAll();
            if(departlist!=null&&departlist.size()>0){
                System.out.println("listsize"+departlist.size());
                model.addAttribute("hdlist", departlist);
            }
            return "allHospitalDepartment";
        }catch(MyException e){
            logger.error("listAll查询所有科室时出错"+e.getMessage());
            return "allHospitalDepartment";
        }
    }
    
    @RequestMapping(value="/listAll2")
    @ResponseBody
    public Map<String,Object> listAll(HttpServletRequest req){
        try{
            Map<String,Object> resultMap=new HashMap<String,Object>();
            List<HospitalDepartment> departlist=hdService.getAll();
            if(departlist!=null&&departlist.size()>0){
                System.out.println("listsize"+departlist.size());
            }
            resultMap.put("departlist", departlist);
            return resultMap;
        }catch(MyException e){
            logger.error("listAll查询所有科室时出错"+e.getMessage());
            return new HashMap<String,Object>();
        }
    }
    
    @RequestMapping(value="/listAll3")
    @ResponseBody
    public JSONArray listAll3(HttpServletRequest req){
        JSONArray array=new JSONArray();
        JSONObject object;
        try{
//            Map<String,Object> resultMap=new HashMap<String,Object>();
            List<HospitalDepartment> departlist=hdService.getAll();
            if(departlist!=null&&departlist.size()>0){
                System.out.println("listsize"+departlist.size());
            }
            for(int i=0;i<departlist.size();i++){
                object=JsonUtil.makeObjectToJSONObject(departlist.get(i));
                array.add(object);
            }
//            resultMap.put("departlist", departlist);
            return array;
        }catch(MyException e){
            logger.error("listAll查询所有科室时出错"+e.getMessage());
            return array;
        }
    }
    
    @RequestMapping(value="/manage")
    public String manage(HttpServletRequest req){
        return "hospitalDepartmentManage";
    }
    
    

}
