package com.java.adds.service;

import com.java.adds.entity.*;
import com.java.adds.utils.FileUtil;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

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
    public Long createGraph(MedicalArchiveEntity medicalArchive)
    {
        String filePath = medicalArchive.getTxtFilePath();
        filePath = "H:/Upload/admissions/admission78.txt";//todo
        ArrayList<String> fileContentLines = fileUtil.readFileIntoList(filePath);
        String fileContent = String.join(" ", fileContentLines);
        Map <String, ArrayList<String>> entityMap = this.extractEntity(fileContent);
        entityMap = this.validateEntity(entityMap);
        ArrayList<Pair<Integer, Integer>> relationList = this.findRelation(entityMap);
        return kgService.createGraph(entityMap, relationList, medicalArchive);
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
    public ArrayList<Pair<Integer, Integer>> findRelation(Map <String, ArrayList<String>> entityMap)
    {
        ArrayList<Pair<Integer, Integer>> relationList = new ArrayList<>();

        ArrayList<String> diseaseEntities = entityMap.get("diseaseEntities");
        ArrayList<String> drugEntities = entityMap.get("drugEntities");
        for (int i = 0; i < diseaseEntities.size(); i++) {
            for (int j = 0; j < drugEntities.size(); j++) {
                if (kgService.hasRelation(diseaseEntities.get(i), drugEntities.get(j))) {
                    relationList.add(new Pair<>(i, j));
                }
            }
        }

        return relationList;
    }

    /**
     * For The Given KG Search For Similar Graphs And Return Admission Id
     * @author XYX
     */
    public ArrayList<Long> searchForSimilarGraphs(Long kdId)
    {
        Map<String, Object> kg = kgService.getKGById(kdId);
        List<Map<String, Object>> nodes = (List<Map<String, Object>>)kg.get("nodes");
        ArrayList<String> diseaseEntities = new ArrayList<>();
        for (Map<String, Object> node : nodes) {
            if (node.get("type").equals("disease")) {
                diseaseEntities.add((String)node.get("alias"));
            }
        }
        System.out.println(diseaseEntities);//todo
        return kgService.findAdmissionHavingDiseases(diseaseEntities);
    }
}
