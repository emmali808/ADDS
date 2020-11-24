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
    @Value("YourDataSetFolder")
    String dataSetsPath;

    @Value("YourDataSetFolder")
    String dataSetsPathInServer;

    @Value("YourKgFileFolder")
    String kgFilePath;

    @Value("${file.upload.path}")
    String uploadFilePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(dataSetsPath+"**").addResourceLocations("file:"+dataSetsPathInServer);
    }

}
