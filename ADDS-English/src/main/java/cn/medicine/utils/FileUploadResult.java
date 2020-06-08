package cn.medicine.utils;

public class FileUploadResult extends JsonResult{
    
    private String fileName;

    public FileUploadResult(String result, String message,String fileName) {
        super(result, message);
        this.fileName=fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }        
}
