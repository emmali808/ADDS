package com.java.adds.service;

import com.java.adds.config.UploadFileConfig;
import com.java.adds.dao.DoctorDiagnosisDao;
import com.java.adds.dao.SimilarGraphsDao;
import com.java.adds.entity.*;
import com.java.adds.utils.CPPUtil;
import com.java.adds.utils.FileUtil;
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
    private UploadFileConfig fileConfig;

    @Autowired
    FileUtil fileUtil;

    @Autowired
    KGService kgService;

    @Autowired
    DoctorDiagnosisDao doctorDiagnosisDao;

    @Autowired
    SimilarGraphsDao similarGraphsDao;

    @Autowired
    CPPUtil cppUtil;

    /**
     * Create Knowledge Graph From Medical Archive Text
     * @author XYX
     */
    public Long createGraph(MedicalArchiveEntity medicalArchive)
    {
        String filePath = medicalArchive.getTxtFilePath();
        ArrayList<String> fileContentLines = fileUtil.readFileIntoList(filePath);
        String fileContent = String.join(" ", fileContentLines);
        Map <String, ArrayList<String>> entityMap = this.extractEntity(fileContent);
        entityMap = this.validateEntity(entityMap);
        ArrayList<Map.Entry<Integer, Integer>> relationList = this.findRelation(entityMap);
        Long kgId = kgService.createGraph(entityMap, relationList, medicalArchive);
        this.createGraphInTxt(entityMap, relationList, kgId);
        return kgId;
    }

    /**
     * Extract Drug And Disease Entity From Given File Content
     * @author XYX
     */
    public Map extractEntity(String fileContent)
    {
        String staticPath = this.getClass().getResource("/entityDict/").getPath();

        String diseaseDictPath = staticPath + "DiseaseDict.txt";
        ArrayList<String> diseaseDict = fileUtil.readFileIntoList(diseaseDictPath);
        ArrayList<String> diseaseEntities = new ArrayList<>();
        for (String disease : diseaseDict) {
            if (fileContent.toLowerCase().contains(disease.toLowerCase())) {
                diseaseEntities.add(disease);
            }
        }

        String drugDictPath = staticPath + "DrugDict.txt";
        ArrayList<String> drugDict = fileUtil.readFileIntoList(drugDictPath);
        ArrayList<String> drugEntities = new ArrayList<>();
        for (String drug : drugDict) {
            if (fileContent.toLowerCase().contains(drug.toLowerCase())) {
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
        ArrayList<String> idList = new ArrayList<>();
        ArrayList<String> diseaseEntities = entityMap.get("diseaseEntities");
        ListIterator iter = diseaseEntities.listIterator();
        while(iter.hasNext()) {
            String id = kgService.hasDiseaseWithAlias((String)iter.next());
            if (id == null) {
                iter.remove();
            } else {
                idList.add(id);
            }
        }

        ArrayList<String> drugEntities = entityMap.get("drugEntities");
        iter = drugEntities.listIterator();
        while(iter.hasNext()) {
            String id = kgService.hasDrugWithAlias((String)iter.next());
            if (id == null) {
                iter.remove();
            } else {
                idList.add(id);
            }
        }

        Map <String, ArrayList<String>> validatedMap = new HashMap<>();
        validatedMap.put("diseaseEntities", diseaseEntities);
        validatedMap.put("drugEntities", drugEntities);
        validatedMap.put("idList", idList);
        return validatedMap;
    }

    /**
     * Find Relations Between Diseases And Drugs
     * @author XYX
     */
    public ArrayList<Map.Entry<Integer, Integer>> findRelation(Map <String, ArrayList<String>> entityMap)
    {
        ArrayList<Map.Entry<Integer, Integer>> relationList = new ArrayList<>();

        ArrayList<String> diseaseEntities = entityMap.get("diseaseEntities");
        ArrayList<String> drugEntities = entityMap.get("drugEntities");
        for (int i = 0; i < diseaseEntities.size(); i++) {
            for (int j = 0; j < drugEntities.size(); j++) {
                if (kgService.hasRelation(diseaseEntities.get(i), drugEntities.get(j))) {
                    relationList.add(new AbstractMap.SimpleEntry<>(i, j));
                }
            }
        }

        return relationList;
    }

    /**
     * Construct TXT File for Graph Search
     * @author XYX
     */
    public Boolean createGraphInTxt(Map <String, ArrayList<String>> entityMap, ArrayList<Map.Entry<Integer, Integer>> relationList, Long kgId)
    {
        ArrayList<String> lines = new ArrayList<>();
        ArrayList<String> idList = entityMap.get("idList");
        ArrayList<String> diseaseEntities = entityMap.get("diseaseEntities");
        lines.add("t " + kgId);
        for (int i = 0; i < idList.size(); i++) {
            lines.add("v " + i + " node");
            lines.add("a id " + idList.get(i));
        }
        Integer offset = diseaseEntities.size();
        for (Map.Entry<Integer, Integer> relation : relationList) {
            lines.add("e " + relation.getKey() + " " + (relation.getValue() + offset));
        }
        return fileUtil.writeListIntoFile(fileConfig.getUploadFilePath() + "graph" + kgId + ".txt", lines);
    }

    /**
     * For The Given KG Search For Similar Graphs And Return Admission Id
     * @author XYX
     */
    public ArrayList<String> searchForSimilarGraphs(Long kgId)
    {
        SimilarGraphsEntity similarGraphsEntity = similarGraphsDao.getSimilarGraphsByKgId(kgId);
        if (similarGraphsEntity != null) {
            String similarGraphsString = similarGraphsEntity.getSimilar_graphs();
            return new ArrayList<>(Arrays.asList(similarGraphsString.split(",")));
        } else {
            String dbFile = this.getClass().getResource("/cpp/").getPath() + "allGraphs.txt";
            String graphFile = fileConfig.getUploadFilePath() + "graph" + kgId.toString() + ".txt";
            ArrayList<String> similarGraphs = cppUtil.runCPP("rangeexp", dbFile, graphFile);
            similarGraphs = kgService.findAdmissionWithIds(similarGraphs);
            StringBuilder sb = new StringBuilder();
            for (String s : similarGraphs) {
                sb.append(s);
                sb.append(",");
            }
            similarGraphsEntity = new SimilarGraphsEntity();
            similarGraphsEntity.setKgId(kgId);
            similarGraphsEntity.setSimilar_graphs(sb.toString());
            similarGraphsDao.insertNewSimilarSearchResult(similarGraphsEntity);
            return similarGraphs;
        }
    }

    public String getDoctorDiagnosis(Long admissionId)
    {
        ArrayList<DoctorDiagnosisEntity> doctorDiagnosisEntities = doctorDiagnosisDao.getDiagnosisByAdmissionId(admissionId);
        if (doctorDiagnosisEntities.size() != 0) {
            return doctorDiagnosisEntities.get(0).getDoctor_diagnosis();
        } else {
            return "";
        }
    }
}
