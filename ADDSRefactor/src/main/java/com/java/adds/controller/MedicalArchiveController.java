package com.java.adds.controller;

import com.java.adds.config.UploadFileConfig;
import com.java.adds.entity.MedicalArchiveEntity;
import com.java.adds.service.MedicalArchiveService;
import com.java.adds.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Medical Archive Controller
 * @author XYX
 */
@RestController
@RequestMapping(value = "/archive")
public class MedicalArchiveController {
    @Autowired
    private MedicalArchiveService medicalArchiveService;

    @Autowired
    private UploadFileConfig fileConfig;

    @Autowired
    private FileUtil fileUtil;

    /**
     * Author: XYX
     * Get Medical Archive List By Doctor's Id
     */
    @RequestMapping(value = "/doctor/{doctorId}", method = RequestMethod.GET)
    public ArrayList<MedicalArchiveEntity> getArchiveList(@PathVariable("doctorId") Long doctorId) {
        //todo: implement vo for front end
        return medicalArchiveService.getMedicalArchiveByUserId(doctorId);
    }

    /**
     * Author: XYX
     * Upload Medical Archive
     */
    @RequestMapping(value = "/doctor/{doctorId}", method = RequestMethod.POST)
    public Map<String, Object> uploadMedicalArchive(HttpServletResponse response, @RequestParam("file") MultipartFile file, @RequestParam("title") String title, @RequestParam("category") String category, @RequestParam("desc") String desc, @PathVariable Long doctorId) {
        Map<String, Object> res = new HashMap<>();

        String fileName = fileUtil.getFileNameWithTimeStamp(doctorId.toString(), file.getOriginalFilename());
        String filePath = fileConfig.getMedicalArchiveFilePath() + fileName;
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

        MedicalArchiveEntity medicalArchive = new MedicalArchiveEntity();
        medicalArchive.setUserId(doctorId);
        medicalArchive.setTitle(title);
        medicalArchive.setCategory(category);
        medicalArchive.setDescription(desc);
        medicalArchive.setZipFilePath(filePath);
        medicalArchive.setStatus(false);

        Long medicalArchiveId = medicalArchiveService.uploadMedicalArchive(medicalArchive);
        response.setStatus(200);
        res.put("success", medicalArchiveId);
        return res;
    }

    /**
     * Author: XYX
     * Download Medical Archive By Archive's Id
     */
    @RequestMapping(value = "/{archiveId}", method = RequestMethod.GET)
    public Map<String, Object> downloadMedicalArchive(@PathVariable("archiveId") Long archiveId, HttpServletResponse response) {
        Map<String, Object> res = new HashMap<>();

        MedicalArchiveEntity medicalArchiveEntity = medicalArchiveService.getMedicalArchiveById(archiveId);
        if (medicalArchiveEntity == null) {
            response.setStatus(404);
            res.put("error", "找不到文件");
            return res;
        }

        String filePath = medicalArchiveEntity.getTxtFilePath();
        String[] filePathParts = filePath.split("/");
        String fileName = filePathParts[filePathParts.length - 1];

        response.addHeader("Content-Disposition","attachment;filename=" + fileName);
        try {
            FileCopyUtils.copy(new FileInputStream(filePath), response.getOutputStream());
            response.setStatus(200);
            res.put("success", medicalArchiveEntity.getId());
        } catch (IOException e) {
            response.setStatus(504);
            res.put("error", "文件下载失败");
        }
        return res;
    }

}
