package com.java.adds.config;


import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
/**
 * 上传文件相关配置
 * @author ljy
 */
@Data
@Configuration
public class UploadFileConfig extends WebMvcConfigurerAdapter {
    @Value("/ADDS/dataSets/**")
    String dataSetsPath;   //虚拟路径
//    @Value("E://医疗项目//大创//ADDS重构//ADDS//src//main//resources//dataSets//")
//    String dataSetsPathInServer;  //在服务器上存储的位置

    @Value("C://Users//302//Desktop//addsFileUploadTesting//")
    String dataSetsPathInServer;  //在服务器上存储的位置

    @Value("H:/Upload/")
    String kgFilePath;

    //todo
    @Value("H:/Upload/")
    String medicalArchiveFilePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(dataSetsPath+"**").addResourceLocations("file:"+dataSetsPathInServer);
    }

}
