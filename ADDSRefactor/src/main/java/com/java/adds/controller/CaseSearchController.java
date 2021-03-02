package com.java.adds.controller;

import com.java.adds.entity.MedicalCaseEntity;
import com.java.adds.entity.SimilarCasesEntity;
import com.java.adds.service.AutoDiagnosisService;
import com.java.adds.service.MedicalCaseService;
import com.java.adds.service.CaseSearchService;
import com.java.adds.service.OCRService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

/**
 * CaseSearch Controller
 * @author ZY
 */
@RestController
@RequestMapping(value = "/caseSearch")
public class CaseSearchController {
    @Autowired
    private MedicalCaseService medicalCaseService;

    @Autowired
    private OCRService ocrService;

    @Autowired
    private CaseSearchService caseSearchService;

    /**
     * Author: ZY
     * Prepare For Case Search
     */
    @RequestMapping(value = "/{caseId}", method = RequestMethod.POST)
    public void caseSearchPreparation(@PathVariable Long caseId) {
        MedicalCaseEntity medicalCaseEntity = medicalCaseService.getMedicalCaseById(caseId);
        medicalCaseEntity = ocrService.ocrMedicalCase(medicalCaseEntity);
        Long kdId = caseSearchService.createGraph(medicalCaseEntity);
    }

    /**
     * Author: ZY
     * Get Similar Graphs From The Given KG
     */
    @RequestMapping(value = "/{kdId}", method = RequestMethod.GET)
    public SimilarCasesEntity getSimilarGraphs(@PathVariable Long kdId) {
        return caseSearchService.searchForSimilarGraphs(kdId);

    }

    /**
     * Author: ZY
     * Get NoteEvents By hadmId
     */
    @RequestMapping(value = "/history_admissions/{hadmId}", method = RequestMethod.GET)
    public ArrayList<String>  getNoteEvents(@PathVariable String hadmId) {
        return caseSearchService.getNoteEventsByHadmId(hadmId);
    }


}

