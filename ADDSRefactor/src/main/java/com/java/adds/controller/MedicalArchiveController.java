package com.java.adds.controller;

import com.java.adds.config.UploadFileConfig;
import com.java.adds.entity.MedicalArchiveEntity;
import com.java.adds.service.MedicalArchiveService;
import com.java.adds.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
     * Get Medical Archive By Doctor's Id
     */
    @RequestMapping(value = "/doctor/{doctorId}", method = RequestMethod.GET)
    public ArrayList<MedicalArchiveEntity> getArchiveList(@PathVariable("doctorId") Long doctorId) {
        return medicalArchiveService.getMedicalArchiveByUserId(doctorId);
    }
}
