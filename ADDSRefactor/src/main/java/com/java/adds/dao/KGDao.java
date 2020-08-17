package com.java.adds.dao;

import com.java.adds.entity.KGEntity;
import com.java.adds.entity.KGNode;
import com.java.adds.entity.KGRel;
import com.java.adds.mapper.KGMapper;
import com.java.adds.mapper.KGRepository;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.StatementResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * KG Dao
 * @author QXL
 */
@Repository
public class KGDao {

    @Autowired
    private KGMapper kgMapper;

    @Autowired
    private Driver neo4jDriver;

    @Autowired
    private KGRepository kgRepository;

    /**
     * Upload Knowledge-Graph file
     * @param userId user's id
     * @return A KG ArrayList
     */
    public ArrayList<KGEntity> getKGListByUserId(Long userId) {
        ArrayList<KGEntity> kgEntities = kgMapper.getKGListByUserId(userId);
        for (KGEntity kgEntity : kgEntities) {
            kgEntity.setFilePath("");
        }
        return kgEntities;
    }

    /**
     * Upload Knowledge-Graph data file info
     * @param userId user id
     * @param kgName KG name
     * @param kgFilePath KG data file path
     * @return KG id
     */
    public Long uploadKGFile(Long userId, String kgName, String kgDesc, String kgFilePath) {
        KGEntity kgEntity = new KGEntity();
        kgEntity.setUserId(userId);
        kgEntity.setName(kgName);
        kgEntity.setDesc(kgDesc);
        kgEntity.setFilePath(kgFilePath);
        kgMapper.uploadKGFile(kgEntity);
        return kgEntity.getId();
    }

    /**
     * Get Knowledge-Graph central node by KG id
     * @param kgId KG id
     * @return central node id
     */
    public Long getCentralNodeByKGId(Long kgId) {
        String getCentralityListCypher =
                "MATCH (x:kgId" + kgId + ")-[r]-(y) " +
                        "CALL algo.betweenness.sampled.stream(\"kgId" + kgId + "\", \"ADDSKGRel\", {strategy: \"degree\", direction: \"out\"}) " +
                        "YIELD nodeId, centrality " +
                        "RETURN nodeId " +
                        "ORDER BY centrality DESC " +
                        "LIMIT 1";

        StatementResult result = executeCypher(getCentralityListCypher);
        if (result.hasNext()) {
            return result.list().get(0).fields().get(0).value().asLong();
        } else {
            return -1L;
        }
    }


    /**
     * Get Knowledge-Graph node by node content
     * @param node node content
     * @return node id
     */
    public Long getNodeByContent(String node) {
        String getNodeCypher =
                "MATCH (x:ADDSKGNode) " +
                        "WHERE x.alias CONTAINS \'" + node + "\' OR x.drug_alias CONTAINS \'" + node + "\'" +
                        "RETURN id(x) " +
                        "LIMIT 1";

        StatementResult result = executeCypher(getNodeCypher);
        if (result.hasNext()) {
            return result.list().get(0).fields().get(0).value().asLong();
        } else {
            return -1L;
        }
    }

    /**
     * Get Knowledge-Graph node and relational nodes by node id
     * @param nodeId node id
     * @return KG data(partial): A String-Object Map for d3
     */
    public Map<String, Object> getNodeAndRelNodes(Long nodeId) {
        List<Map<String, Object>> kg = kgRepository.getNodeAndRelNodes(nodeId);
        return toD3Format(kg, true);
    }

    /**
     * Get Knowledge-Graph node's relational nodes by node id (without this node)
     * @param nodeId node id
     * @return KG data(partial): A String-Object Map for d3
     */
    public Map<String, Object> getRelNodes(Long nodeId) {
        List<Map<String, Object>> kg = kgRepository.getNodeAndRelNodes(nodeId);
        if (kg.size() == 0) {
            return noDataFormat();
        }
        return toD3Format(kg, false);
    }

    /**
     * Upload Knowledge-Graph data
     * Notification: [Neo4j Configuration] Allow user to upload a .csv file from any file path
     * @param kgFilePath KG data file path
     * @param kgId KG id
     */
    public void uploadKGData(String kgFilePath, Long kgId) {
        // Step1: Create an index in node's property "name"
        String createNodeIndexCypher = "CREATE INDEX ON :ADDSKGNode(name);";

        // Step2: Upload start nodes
        String uploadStartNodeCypher =
                "USING PERIODIC COMMIT 500 " +
                        "LOAD CSV WITH HEADERS FROM \"file:" + kgFilePath + "\" AS line " +
                        "MERGE (:ADDSKGNode:kgId" + kgId + "{name:line.header});";

        // Step3: Upload end nodes
        String uploadEndNodeCypher =
                "USING PERIODIC COMMIT 500 " +
                        "LOAD CSV WITH HEADERS FROM \"file:" + kgFilePath + "\" AS line " +
                        "MERGE (:ADDSKGNode:kgId" + kgId + "{name:line.tail});";

        // Step4: Upload relationships
        String uploadRelCypher =
                "USING PERIODIC COMMIT 500 " +
                        "LOAD CSV WITH HEADERS FROM \"file:" + kgFilePath + "\" AS line " +
                        "MATCH (x:ADDSKGNode:kgId" + kgId + "{name:line.header}) " +
                        "MATCH (y:ADDSKGNode:kgId" + kgId + "{name:line.tail}) " +
                        "MERGE (x)-[r:ADDSKGRel{name:line.relation}]->(y);";

        // Execute cyphers
        executeCypher(createNodeIndexCypher);
        executeCypher(uploadStartNodeCypher);
        executeCypher(uploadEndNodeCypher);
        executeCypher(uploadRelCypher);
    }

    public void createNode(Long kgId, String uid, String type, Map<String, String> attributes) {
        String createNodeCypher = "MERGE (n:ADDSKGNode:kgId" + kgId + "{uid:\'" + uid + "\', type:\'" + type + "\'";
        for (String key : attributes.keySet())
            createNodeCypher += (", " + key + ": \'" + attributes.get(key) + "\'");
        createNodeCypher += "});";
        executeCypher(createNodeCypher);
    }

    public void createRel(Long kgId, String uid1, String uid2) {
        String createRelCypher =
                "MATCH (x:ADDSKGNode:kgId" + kgId + "{uid:\'" + uid1 + "\'}), (y:ADDSKGNode:kgId" + kgId + "{uid:\'" + uid2 + "\'})" +
                        "MERGE (x)-[r:ADDSKGRel{name:\'has\'}]->(y);";
        executeCypher(createRelCypher);
    }

    /**
     * Execute Neo4j cypher
     * @param cql cypher
     * @return result
     */
    public StatementResult executeCypher(String cql) {
        StatementResult result = null;
        try (Session session = neo4jDriver.session()) {
            result = session.run(cql);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public Map<String, Object> toD3Format(List<Map<String, Object>> kg, boolean withOriginNode) {
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> rels = new ArrayList<>();

        KGNode startNode = (KGNode) kg.get(0).get("x");
        if (withOriginNode) {
            nodes.add(map(startNode));
        }

        for (Map<String, Object> triad : kg) {
            KGNode endNode = (KGNode) triad.get("y");
            KGRel rel = (KGRel) triad.get("r");
            nodes.add(map(endNode));
            rels.add(map3("source", startNode.getId(), "target", endNode.getId(), "value", rel.getName()));
        }
        return map2("nodes", nodes, "links", rels);
    }

    private Map<String, Object> map(KGNode node) {
        Map<String, Object> result = new HashMap<String, Object>(19);
        result.put("id", node.getId());
        result.put("type", node.getType());
        result.put("patient_id", node.getPatient_id());
        result.put("gender", node.getGender());
        result.put("religion", node.getReligion());
        result.put("ethnicity", node.getEthnicity());
        result.put("birth_time", node.getBirth_time());
        result.put("admission_id", node.getAdmission_id());
        result.put("admit_time", node.getAdmit_time());
        result.put("duration", node.getDuration());
        result.put("flag", node.getFlag());
        result.put("admit_age", node.getAdmit_age());
        result.put("disease_id", node.getDisease_id());
        result.put("alias", node.getAlias());
        result.put("drug_id", node.getDrug_id());
        result.put("count", node.getCount());
        result.put("frequency", node.getFrequency());
        result.put("drug_alias", node.getDrug_alias());
        result.put("dose_val_rx", node.getDose_val_rx());
        result.put("dose_unit_rx", node.getDose_unit_rx());
        return result;
    }

    private Map<String, Object> map2(String key1, Object value1, String key2, Object value2) {
        Map<String, Object> result = new HashMap<String, Object>(2);
        result.put(key1, value1);
        result.put(key2, value2);
        return result;
    }

    private Map<String, Object> map3(String key1, Object value1, String key2, Object value2, String key3, Object value3) {
        Map<String, Object> result = new HashMap<String, Object>(3);
        result.put(key1, value1);
        result.put(key2, value2);
        result.put(key3, value3);
        return result;
    }

    /**
     * KG data format when there's no data
     * @return KG data(partial): A String-Object Map for d3
     */
    public Map<String, Object> noDataFormat() {
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> rels = new ArrayList<>();
        return map2("nodes", nodes, "links", rels);
    }
}
