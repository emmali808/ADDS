package cn.medicine.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@Controller
@RequestMapping("/mvc")
public class MVCController {
    
    //pass the parameters to front-end using ajax
    @RequestMapping("/getPerson")
    public void getPerson(String name,PrintWriter pw){
        pw.write("hello,"+name);
    }
    @RequestMapping("/name")
    public String sayHello(){
        return "name";
    }
    
    @RequestMapping(value="/upload",method=RequestMethod.POST)
    public String upload(HttpServletRequest req) throws Exception{
        MultipartHttpServletRequest mreq = (MultipartHttpServletRequest)req;
        MultipartFile file = mreq.getFile("file");
        String fileName = file.getOriginalFilename();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); 
        String realpath=req.getSession().getServletContext().getRealPath("/");
        String picpath=realpath+"upload/";
        File fileDir=new File(picpath);
        if(!fileDir.exists()){
            fileDir.mkdir();
        }
        
        FileOutputStream fos = new FileOutputStream(picpath+fileName.substring(0, fileName.lastIndexOf('.'))+sdf.format(new Date())+fileName.substring(fileName.lastIndexOf('.')));
        fos.write(file.getBytes());
        fos.flush();
        fos.close();
         
        return "hello";
    }

}
