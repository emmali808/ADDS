package com.java.adds.controller;

import com.java.adds.config.UploadFileConfig;
import com.java.adds.entity.HistoryAdmssionEntity;
import com.java.adds.entity.MedicalArchiveEntity;
import com.java.adds.entity.MedicalCaseEntity;
import com.java.adds.entity.MedicalCaseEntity;
import com.java.adds.service.HistoryAdmissionService;
import com.java.adds.service.MedicalCaseService;
import com.java.adds.service.MedicalCaseService;
import com.java.adds.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Medical Case Controller
 * @author ZY
 */
@RestController
@RequestMapping(value = "/case")
public class MedicalCaseController {
    @Autowired
    private MedicalCaseService medicalCaseService;

    @Autowired
    private HistoryAdmissionService historyAdmissionService;


    @Autowired
    private UploadFileConfig fileConfig;

    @Autowired
    private FileUtil fileUtil;

    /**
     * Author: XYX
     * Get Medical Case List By User Id
     */
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public ArrayList<MedicalCaseEntity> getCaseList(@PathVariable("userId") Long userId) {
        //todo: implement vo for front end
        return medicalCaseService.getMedicalCaseByUserId(userId);
    }

    /**
     * Author: ZY
     * Upload Medical Case
     */
    @RequestMapping(value = "/history_admissions/{userId}", method = RequestMethod.POST)
    public Map<String, Object> uploadHistoryAdmissions(HttpServletResponse response, @RequestParam("file") MultipartFile file, @RequestParam("title") String title, @RequestParam("category") String category, @RequestParam("desc") String desc, @PathVariable Long userId) {
        Map<String, Object> res = new HashMap<>();
        String fileName = fileUtil.getFileNameWithTimeStamp(userId.toString(), file.getOriginalFilename());
        String filePath = fileConfig.getHistoryAdmissionsCsvPath() + fileName;
        File dest = new File(filePath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            response.setStatus(501);
            res.put("error", "文件保存失败");
            return res;
        }

        //read from csv and write to mysql
        HistoryAdmssionEntity historyAdmssionEntity = new HistoryAdmssionEntity();

        try {

            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            reader.readLine();
            String line = null;
            while((line=reader.readLine())!=null){
                String item[] = line.split(",");
                historyAdmssionEntity.setSUBJECT_ID(item[0]);
                historyAdmssionEntity.setHADM_ID(item[1]);
                historyAdmssionEntity.setICD9_DIAG(item[2]);
                historyAdmssionEntity.setICD9_PROCE(item[3]);
                historyAdmssionEntity.setNDC(item[4]);
                historyAdmssionEntity.setDisease(item[5]);
                historyAdmssionEntity.setATC(item[6]);
                historyAdmssionEntity.setTEXT(item[7]);
                historyAdmissionService.insertHistoryAdmissions(historyAdmssionEntity);
            }
            res.put("success","successfully save to database");

        } catch (Exception e) {
            e.printStackTrace();
            res.put("error", "数据库写入失败");
        }

        return res;
    }


    /**
     * Author: ZY
     * Upload Medical Case
     */
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.POST)
    public Map<String, Object> uploadMedicalCase(HttpServletResponse response, @RequestParam("file") MultipartFile file, @RequestParam("title") String title, @RequestParam("category") String category, @RequestParam("desc") String desc, @PathVariable Long userId) {
        Map<String, Object> res = new HashMap<>();

        String fileName = fileUtil.getFileNameWithTimeStamp(userId.toString(), file.getOriginalFilename());
        String filePath = fileConfig.getMedicalCaseFilePath() + fileName;
        File dest = new File(filePath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }

        try {
            file.transferTo(dest);
        } catch (IOException e) {
            response.setStatus(501);
            res.put("error", "文件保存失败");
            return res;
        }

        MedicalCaseEntity medicalCase = new MedicalCaseEntity();
        medicalCase.setUserId(userId);
        medicalCase.setTitle(title);
        medicalCase.setCategory(category);
        medicalCase.setDescription(desc);
        medicalCase.setZipFilePath(filePath);
        medicalCase.setStatus(false);

        Long medicalCaseId = medicalCaseService.uploadMedicalCase(medicalCase);
        if (medicalCaseId >= 0) {
            response.setStatus(200);
            res.put("medicalCaseId", medicalCaseId);
        } else {
            response.setStatus(502);
            res.put("error", "数据库写入失败");
        }
        return res;
    }


    /**
     * Author: XYX
     * Download Medical Archive By Archive's Id
     */
    @RequestMapping(value = "/download/SampleCsvFile", method = RequestMethod.GET)
    public Map<String, Object> downloadSampleCsvFile(HttpServletResponse response) {
        Map<String, Object> res = new HashMap<>();
//
//        MedicalArchiveEntity medicalArchiveEntity = medicalArchiveService.getMedicalArchiveById(archiveId);
//        if (medicalArchiveEntity == null) {
//            response.setStatus(404);
//            res.put("error", "找不到文件");
//            return res;
//        }

//        String filePath = medicalArchiveEntity.getTxtFilePath();

        String filePath = fileConfig.getMedicalCaseSearchSampleCsvPath();

        String[] filePathParts = filePath.split("/");
        String fileName = filePathParts[filePathParts.length - 1];

        response.addHeader("Content-Disposition","attachment;filename=" + fileName);
        try {
            FileCopyUtils.copy(new FileInputStream(filePath), response.getOutputStream());
            response.setStatus(200);
            res.put("success", "successfully download!");
        } catch (IOException e) {
            response.setStatus(504);
            res.put("error", "文件下载失败");
        }
        return res;
    }

    /**
     * Author: XYX
     * Download Medical Archive By Archive's Id
     */
    @RequestMapping(value = "/download/SampleImageFile", method = RequestMethod.GET)
    public Map<String, Object> downloadSampleImageFile(HttpServletResponse response) {
        Map<String, Object> res = new HashMap<>();
//
//        MedicalArchiveEntity medicalArchiveEntity = medicalArchiveService.getMedicalArchiveById(archiveId);
//        if (medicalArchiveEntity == null) {
//            response.setStatus(404);
//            res.put("error", "找不到文件");
//            return res;
//        }

//        String filePath = medicalArchiveEntity.getTxtFilePath();

        String filePath = fileConfig.getMedicalCaseSearchSampleImagePath();

        String[] filePathParts = filePath.split("/");
        String fileName = filePathParts[filePathParts.length - 1];

        response.addHeader("Content-Disposition","attachment;filename=" + fileName);
        try {
            FileCopyUtils.copy(new FileInputStream(filePath), response.getOutputStream());
            response.setStatus(200);
            res.put("success", "successfully download!");
        } catch (IOException e) {
            response.setStatus(504);
            res.put("error", "文件下载失败");
        }
        return res;
    }

}
