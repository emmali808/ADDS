package com.java.adds.controller;

import com.java.adds.entity.MedicalArchiveEntity;
import com.java.adds.service.AutoDiagnosisService;
import com.java.adds.service.MedicalArchiveService;
import com.java.adds.service.OCRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
    public void diagnosisPreparation(@PathVariable Long archiveId) {
        MedicalArchiveEntity medicalArchiveEntity = medicalArchiveService.getMedicalArchiveById(archiveId);
        medicalArchiveEntity = ocrService.ocrMedicalArchive(medicalArchiveEntity);
        Long kdId = autoDiagnosisService.createGraph(medicalArchiveEntity);
        //1.1 similarity search and insert into database
    }

    //1.2 given kgId query for similar graphs
    /**
     * Author: XYX
     * Get Similar Graphs From The Given KG
     */
    @RequestMapping(value = "/{kdId}", method = RequestMethod.GET)
    public ArrayList<Long> getSimilarGraphs(@PathVariable Long kdId) {
        return autoDiagnosisService.searchForSimilarGraphs(kdId);
    }


    /**
     * Author: XYX
     * Get Doctor's Diagnosis
     */
    @RequestMapping(value = "/admission/{admissionId}", method = RequestMethod.GET)
    public String getDoctorDiagnosis(@PathVariable Long admissionId) {
        return autoDiagnosisService.getDoctorDiagnosis(admissionId);
    }
}

