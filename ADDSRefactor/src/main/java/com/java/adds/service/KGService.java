package com.java.adds.service;

import com.java.adds.dao.KGDao;
import com.java.adds.entity.KGEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * KG Service
 * @author QXL
 */
@Service
public class KGService {

    @Autowired
    private KGDao kgDao;

    /**
     * Upload Knowledge-Graph file
     * @param userId user's id
     * @return A KG ArrayList
     */
    public ArrayList<KGEntity> getKGListByUserId(Long userId) {
        return kgDao.getKGListByUserId(userId);
    }

    /**
     * Upload Knowledge-Graph
     * @param userId user id
     * @param kgName KG name
     * @param kgFilePath KG data file path
     * @return KG id
     */
    public Long uploadKG(Long userId, String kgName, String kgDesc, String kgFilePath) {
        Long kgId = kgDao.uploadKGFile(userId, kgName, kgDesc, kgFilePath);
        if (kgId >= 0) {
            kgDao.uploadKGData(kgFilePath, kgId);
            return kgId;
        } else {
            return -1L;
        }
    }

    public void createGraph() {
        File folder = new File("/home/adds/文档/4-medicalgraph（编码）/need to import");
        File[] fileList = folder.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            System.out.println("start importing " + fileList[i]);
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader((fileList[i])));
                String idPatternString = "t (\\d+)";
                String vPatternString = "v (\\d+) (.*)";
                String aPatternString = "a (\\w+) (.*)";
                String ePatternString = "e (\\d+) (\\d+)";

                Pattern idPattern = Pattern.compile(idPatternString);
                Pattern vPattern = Pattern.compile(vPatternString);
                Pattern aPattern = Pattern.compile(aPatternString);
                Pattern ePattern = Pattern.compile(ePatternString);

                Long graphId = (long) 0;
                String tempString = null;
                tempString = reader.readLine();
                while (tempString != null) {
                    Matcher idMatcher = idPattern.matcher(tempString);
                    Matcher vMatcher = vPattern.matcher(tempString);
                    Matcher eMatcher = ePattern.matcher(tempString);
                    if (idMatcher.find()) {
                        graphId = Long.parseLong(idMatcher.group(1));
                        System.out.println("graph " + graphId);
                    } else if (vMatcher.find()) {
                        Map<String, String> attributes = new LinkedHashMap<>();
                        tempString = reader.readLine();
                        while (tempString != null) {
                            Matcher aMatcher = aPattern.matcher(tempString);
                            if (aMatcher.find()) {
                                if (!aMatcher.group(1).equals("count") && !aMatcher.group(1).equals("frequency")) {
                                    attributes.put(aMatcher.group(1), aMatcher.group(2));
                                }
                                tempString = reader.readLine();
                            } else {
                                kgDao.createNode(graphId, vMatcher.group(1), vMatcher.group(2), attributes);
                                break;
                            }
                        }
                        continue;
                    } else if (eMatcher.find()) {
                        kgDao.createRel(graphId, eMatcher.group(1), eMatcher.group(2));
                    }
                    tempString = reader.readLine();
                }
                reader.close();
                System.out.println("finish importing " + fileList[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Get Knowledge-Graph by KG id
     * @param kgId KG id
     * @return KG data(partial): A String-Object Map format for D3
     */
    public Map<String, Object> getKGById(Long kgId) {
//        Long nodeId = kgDao.getCentralNodeByKGId(kgId);
        Long nodeId = kgDao.getRandomAdmissionNode();
        if (nodeId < 0) {
            return kgDao.noDataFormat();
        } else {
            return kgDao.getNodeAndRelNodes(nodeId);
        }
    }

    /**
     * Get Knowledge-Graph by searching for a specific node
     * @param node node content
     * @return KG data(partial): A String-Object Map format for D3
     */
    public Map<String, Object> getKGByNode(String node) {
        Long nodeId = kgDao.getNodeByContent(node);
        if (nodeId < 0) {
            return kgDao.noDataFormat();
        } else {
            return kgDao.getNodeAndRelNodes(nodeId);
        }
    }

    /**
     * Get Knowledge-Graph node's relational nodes by node id (without this node)
     * @param nodeId KG node id
     * @return KG data(partial): A String-Object Map format for D3
     */
    public Map<String, Object> getRelNodes(Long nodeId) {
        return kgDao.getRelNodes(nodeId);
    }

    /**
     * Get statistics of the whole graph
     * for the number of all nodes and edges and some type of nodes
     * @return statistics map
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> result = new HashMap<String, Object>(19);
        result.put("numOfNodes", kgDao.getNumberOfAllNodes());
        result.put("numOfEdges", kgDao.getNumberOfAllEdges());
        result.put("numOfPatients", kgDao.getNumberOfTypeOfNodes("patient"));
        result.put("numOfAdmissions", kgDao.getNumberOfTypeOfNodes("admission"));
        result.put("numOfDiseases", kgDao.getNumberOfTypeOfNodes("disease"));
        result.put("numOfDrugs", kgDao.getNumberOfTypeOfNodes("drug"));
        return result;
    }

    /**
     * Validate if a drug node with this drug_alias exists
     * @param drugAlias the drug_alias
     * @return the node exists or not
     */
    public Boolean hasDrugWithAlias(String drugAlias) {
        return kgDao.hasDrugWithAlias(drugAlias);
    }

    /**
     * Validate if a disease node with this alias exists
     * @param alias the alias
     * @return the node exists or not
     */
    public Boolean hasDiseaseWithAlias(String alias) {
        return kgDao.hasDiseaseWithAlias(alias);
    }

    /**
     * Validate if a relation between drug and disease exists
     * @param alias the alias of the disease
     * @param drugAlias the alias of the drug
     * @return the relation exists or not
     */
    public Boolean hasRelation(String alias, String drugAlias) {
        return kgDao.hasRelation(alias, drugAlias);
    }
}
