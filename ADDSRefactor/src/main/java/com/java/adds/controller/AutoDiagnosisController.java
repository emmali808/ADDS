package com.java.adds.controller;

import com.java.adds.service.AutoDiagnosisService;
import com.java.adds.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
@RequestMapping("diagnosis")
public class AutoDiagnosisController {

    @Autowired
    AutoDiagnosisService autoDiagnosisService;

    @Value("H:\\upload\\")
    String dataSetsPathInServer;

    @PostMapping("upload")
    public void uploadRecords(HttpServletResponse httpServletResponse, @RequestParam MultipartFile file)
    {
        String fileName;
        try {
            fileName=file.getOriginalFilename();  //获取原始文件名
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            Date date = new Date();
            String nowDate = format.format(date);
            fileName = nowDate+"-"+fileName;//为了避免文件重名

            String filePath = dataSetsPathInServer + fileName;

            File dest=new File(filePath);
            if(!dest.getParentFile().exists()){
                dest.getParentFile().mkdir();
            }
            file.transferTo(dest);  //将文件保存到服务器
//          make records in database

            autoDiagnosisService.diagnose(dataSetsPathInServer);

        } catch (IOException e) {
            httpServletResponse.setStatus(302,"文件上传失败");
        }
        httpServletResponse.setStatus(200,"文件上传成功");
    }

}
