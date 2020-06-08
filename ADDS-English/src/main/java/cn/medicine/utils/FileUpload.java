package cn.medicine.utils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUpload {
    
    private String basePath;
    private MultipartFile file;
    private HttpServletRequest request;
    
    public FileUpload(){
        super();
    }
    
    public FileUpload(String basePath,MultipartFile file,HttpServletRequest request){
        super();
        this.basePath=basePath;
        this.file=file;
        this.request=request;
    }
    
    public FileUploadResult uploadImage(){
        if (file.isEmpty())
        {
            return new FileUploadResult("error", "上传失败，文件不能空", "");
        }
        else
        {
            List fileTypes = new ArrayList();
            fileTypes.add("png");
            fileTypes.add("jpg");
            fileTypes.add("jpeg");
            fileTypes.add("bmp");
            fileTypes.add("gif");
            try {
                String extension = FilenameUtils.getExtension(file.getOriginalFilename());
                String ext = extension.toLowerCase();
                if(!fileTypes.contains(ext)) {
                    return new FileUploadResult("error", "，上传失败，图片格式不对", "");
                }
                String fileName = UUID.randomUUID().toString();
                if (!StringUtils.isEmpty(extension)) {
                    fileName += "." + extension;
                }
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd/");
                String dateString = simpleDateFormat.format(new Date());
                String path = request.getSession().getServletContext().getRealPath(basePath + dateString);
                File targetFile = new File(path, fileName);
                if (!targetFile.exists()) {
                    targetFile.mkdirs();
                }
                file.transferTo(targetFile);
                return new FileUploadResult("success", "上传成功", basePath + dateString + targetFile.getName());
            } catch (IOException e) {
                return new FileUploadResult("error", "上传失败，未知错误", "");
            }
        }
    }
    
    public FileUploadResult upload(String base,String imagePath,HttpServletRequest httpServletRequest){
        try{
            String extension = FilenameUtils.getExtension(imagePath);
            String fileName = UUID.randomUUID().toString();
            if (!StringUtils.isEmpty(extension)) {
                fileName += "." + extension;
            }
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd/");
            String dateString = simpleDateFormat.format(new Date());
            String path = httpServletRequest.getSession().getServletContext().getRealPath(base + dateString);
            File targetFile = new File(path, fileName);
            if(!targetFile.exists()){
                targetFile.mkdir();
            }
            File file = new File(imagePath);
            if (file.exists()) {
                FileUtils.copyFile(file,targetFile);
                return new FileUploadResult("success", "上传成功", base + dateString + targetFile.getName());
            }
            return new FileUploadResult("error", "上传失败，未知错误", "");
        }
        catch (IOException e){
            return new FileUploadResult("error", "上传失败，未知错误", "");
        }
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public HttpServletRequest getRequest() {
        return request;
    }

    public void setRequest(HttpServletRequest request) {
        this.request = request;
    }                
}
