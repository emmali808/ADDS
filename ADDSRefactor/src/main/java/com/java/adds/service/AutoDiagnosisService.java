package com.java.adds.service;

import com.java.adds.entity.*;
import com.java.adds.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

/**
 * Auto Diagnosis Service
 * @author XYX
 */
@Service
public class AutoDiagnosisService {

    @Autowired
    FileUtil fileUtil;

    @Autowired
    KGService kgService;

    /**
     * Create Knowledge Graph From Medical Archive Text
     * @author XYX
     */
    public void createGraph(MedicalArchiveEntity medicalArchiveEntity)
    {
        String filePath = medicalArchiveEntity.getTxtFilePath();
        filePath = "H:/Upload/admissions/admission78.txt";
        ArrayList<String> fileContentLines = fileUtil.readFileIntoList(filePath);
        String fileContent = String.join(" ", fileContentLines);
        Map <String, ArrayList<String>> entityMap = this.extractEntity(fileContent);
        entityMap = this.validateEntity(entityMap);
        ArrayList<String> diseaseEntities = entityMap.get("diseaseEntities");
        ArrayList<String> drugEntities = entityMap.get("drugEntities");
        for (String disease : diseaseEntities) {
            System.out.println(disease);
        }
        System.out.println("------------------");
        for (String disease : drugEntities) {
            System.out.println(disease);
        }
    }

    /**
     * Extract Drug And Disease Entity From Given File Content
     * @author XYX
     */
    public Map extractEntity(String fileContent)
    {
        String staticPath = this.getClass().getResource("/entityDict/").getPath();
        String diseaseDictPath = staticPath.substring(1) + "DiseaseDict.txt";
        ArrayList<String> diseaseDict = fileUtil.readFileIntoList(diseaseDictPath);
        ArrayList<String> diseaseEntities = new ArrayList<>();
        for (String disease : diseaseDict) {
            if (fileContent.contains(disease)) {
                diseaseEntities.add(disease);
            }
        }
        String drugDictPath = staticPath.substring(1) + "DrugDict.txt";
        ArrayList<String> drugDict = fileUtil.readFileIntoList(drugDictPath);
        ArrayList<String> drugEntities = new ArrayList<>();
        for (String drug : drugDict) {
            if (fileContent.contains(drug)) {
                drugEntities.add(drug);
            }
        }
        Map <String, ArrayList<String>> resultMap = new HashMap<>();
        resultMap.put("diseaseEntities", diseaseEntities);
        resultMap.put("drugEntities", drugEntities);
        return resultMap;
    }

    /**
     * Validate If An Entity Exists In The Knowledge Map
     * @author XYX
     */
    public Map <String, ArrayList<String>> validateEntity(Map <String, ArrayList<String>> entityMap)
    {
        ArrayList<String> diseaseEntities = entityMap.get("diseaseEntities");
        ListIterator iter = diseaseEntities.listIterator();
        while(iter.hasNext()) {
            if (!kgService.hasDiseaseWithAlias((String)iter.next())) {
                iter.remove();
            }
        }
        ArrayList<String> drugEntities = entityMap.get("drugEntities");
        iter = drugEntities.listIterator();
        while(iter.hasNext()) {
            if (!kgService.hasDrugWithAlias((String)iter.next())) {
                iter.remove();
            }
        }
        Map <String, ArrayList<String>> validatedMap = new HashMap<>();
        validatedMap.put("diseaseEntities", diseaseEntities);
        validatedMap.put("drugEntities", drugEntities);
        return validatedMap;
    }

    /**
     * Find Relations Between Diseases And Drugs
     * @author XYX
     */
    public void findRelation(Map <String, ArrayList<String>> entityMap)
    {

    }

    /**
     * Diagnose By Comparing Graphs
     * @author XYX
     */
    public void diagnose(MedicalArchiveEntity medicalArchiveEntity)
    {

    }
}
