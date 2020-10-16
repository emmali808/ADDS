package com.java.adds.controller;

import com.java.adds.entity.MedicalArchiveEntity;
import com.java.adds.service.AutoDiagnosisService;
import com.java.adds.service.MedicalArchiveService;
import com.java.adds.service.OCRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Auto Diagnosis Controller
 * @author XYX
 */
@RestController
@RequestMapping(value = "/diagnosis")
public class AutoDiagnosisController {
    @Autowired
    private MedicalArchiveService medicalArchiveService;

    @Autowired
    private OCRService ocrService;

    @Autowired
    private AutoDiagnosisService autoDiagnosisService;

    /**
     * Author: XYX
     * Auto Diagnosis Medical Archive
     */
    @RequestMapping(value = "/{archiveId}", method = RequestMethod.POST)
    public void autoDiagnosisMedicalArchive(@PathVariable Long archiveId) {
        MedicalArchiveEntity medicalArchiveEntity = medicalArchiveService.getMedicalArchiveById(archiveId);
//        ocrService.ocrMedicalArchive(medicalArchiveEntity);
        autoDiagnosisService.createGraph(medicalArchiveEntity);
    }
}

