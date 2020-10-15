package com.java.adds.controller;

import com.java.adds.entity.MedicalArchiveEntity;
import com.java.adds.service.MedicalArchiveService;
import com.java.adds.service.OCRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Preprocess Controller
 * @author XYX
 */
@RestController
@RequestMapping(value = "/preprocess")
public class PreprocessController {
    @Autowired
    private MedicalArchiveService medicalArchiveService;

    @Autowired
    private OCRService ocrService;

    /**
     * Author: XYX
     * Preprocess Medical Archive
     */
    @RequestMapping(value = "/{archiveId}", method = RequestMethod.POST)
    public void preprocessMedicalArchive(@PathVariable Long archiveId) {
        MedicalArchiveEntity medicalArchiveEntity = medicalArchiveService.getMedicalArchiveById(archiveId);
        ocrService.ocrMedicalArchive(medicalArchiveEntity);
    }
}
