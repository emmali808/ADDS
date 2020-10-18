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
     * Get Knowledge-Graph admission node randomly
     * @return admission node id
     */
    public Long getRandomAdmissionNode() {
        String getCentralityListCypher =
                "MATCH (x:kgId0)-[r]-(y) " +
                        "WHERE x.type = \"admission\" " +
                        "RETURN id(x), rand() as r " +
                        "ORDER BY r " +
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
                "MATCH (x:ADDSKGNode:kgId0) " +
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
     * Get number of all nodes
     * @return node number
     */
    public Integer getNumberOfAllNodes() {
        String getNumberCypher = "MATCH (n:ADDSKGNode:kgId0) RETURN COUNT(n)";

        StatementResult result = executeCypher(getNumberCypher);
        if (result.hasNext()) {
            return result.list().get(0).fields().get(0).value().asInt();
        } else {
            return -1;
        }
    }

    /**
     * Get number of some type of nodes
     * @return node number
     */
    public Integer getNumberOfTypeOfNodes(String type) {
        String getNumberCypher = "MATCH (n:ADDSKGNode:kgId0) where n.type = \'" + type + "\' RETURN COUNT(n)";

        StatementResult result = executeCypher(getNumberCypher);
        if (result.hasNext()) {
            return result.list().get(0).fields().get(0).value().asInt();
        } else {
            return -1;
        }
    }

    /**
     * Get number of all edges
     * @return edge number
     */
    public Integer getNumberOfAllEdges() {
        String getNumberCypher = "MATCH (:kgId0)-[x:ADDSKGRel]->() RETURN COUNT(x)";

        StatementResult result = executeCypher(getNumberCypher);
        if (result.hasNext()) {
            return result.list().get(0).fields().get(0).value().asInt();
        } else {
            return -1;
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
     * Get Knowledge-Graph node and relational nodes by KG id
     * @param kgId id
     * @return KG data(partial): A String-Object Map for d3
     */
    public Map<String, Object> getKGById(Long kgId) {
        List<Map<String, Object>> kgForNode = kgRepository.getNodeByKGId(kgId);
        List<Map<String, Object>> kgForRel = kgRepository.getRelByKGId(kgId);
        return toD3Format(kgForNode, kgForRel);
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


    /**
     * Create Node Into The MIMIC III Graph
     * @param kgId admission id
     * @param uid node id
     * @param type node type
     * @param attributes attribute list
     */
    public void createNodeSingular(Long kgId, String uid, String type, Map<String, String> attributes) {
        String createNodeCypher = "MERGE (n:ADDSKGNode:kgId0 {type:\'" + type + "\'";
        for (String key : attributes.keySet())
            createNodeCypher += (", " + key + ": \'" + attributes.get(key) + "\'");
        createNodeCypher += "}) SET n.uid = \'" + kgId + "_" + uid + "\';";
        executeCypher(createNodeCypher);
    }

    /**
     * Create Relation Between Nodes In The MIMIC III Graph
     * @param kgId admission id
     * @param uid1 node1 id
     * @param uid2 node2 id
     */
    public void createRelSingular(Long kgId, String uid1, String uid2) {
        String createRelCypher =
                "MATCH (x:ADDSKGNode:kgId0 {uid:\'" + kgId + "_" + uid1 + "\'}), (y:ADDSKGNode:kgId0 {uid:\'" + kgId + "_" + uid2 + "\'})" +
                        "MERGE (x)-[r:ADDSKGRel{name:\'has\'}]->(y);";
        executeCypher(createRelCypher);
    }

    /**
     * Create Node In An Independent Graph
     * @param kgId graph id
     * @param uid node id
     * @param type node type
     * @param attributes attribute list
     */
    public void createNode(Long kgId, String uid, String type, Map<String, String> attributes) {
        String createNodeCypher = "MERGE (n:ADDSKGNode:kgId" + kgId + "{uid:\'" + uid + "\', type:\'" + type + "\', kgId:\'" + kgId + "\'";
        for (String key : attributes.keySet())
            createNodeCypher += (", " + key + ": \'" + attributes.get(key) + "\'");
        createNodeCypher += "});";
        executeCypher(createNodeCypher);
    }

    /**
     * Create Relation Between Nodes In Independent Graph
     * @param kgId graph id
     * @param uid1 disease id
     * @param uid2 drug id
     */
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

    public Map<String, Object> toD3Format(List<Map<String, Object>> kgForNode, List<Map<String, Object>> kgForRel) {
        List<Map<String, Object>> nodes = new ArrayList<>();
        List<Map<String, Object>> rels = new ArrayList<>();

        for (Map<String, Object> triad : kgForNode) {
            KGNode node = (KGNode) triad.get("x");
            nodes.add(map(node));
        }

        for (Map<String, Object> triad : kgForRel) {
            KGRel rel = (KGRel) triad.get("r");
            rels.add(map3("source", rel.getStartNode().getId(), "target", rel.getEndNode().getId(), "value", rel.getName()));
        }
        return map2("nodes", nodes, "links", rels);
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

    /**
     * Validate if a drug with this alias exists
     * @return the node exists or not
     */
    public Boolean hasDrugWithAlias(String drugAlias) {
        String validateCypher =
                "MATCH (n:ADDSKGNode:kgId0)" + "WHERE n.drug_alias = \'" + drugAlias + "\' return n";
        StatementResult result = executeCypher(validateCypher);
        return result.hasNext();
    }

    /**
     * Validate if a disease with this alias exists
     * @return the node exists or not
     */
    public Boolean hasDiseaseWithAlias(String alias) {
        String validateCypher =
                "MATCH (n:ADDSKGNode:kgId0)" + "WHERE n.alias = \'" + alias + "\' return n";
        StatementResult result = executeCypher(validateCypher);
        return result.hasNext();
    }

    /**
     * Validate if a relation between drug and disease exists
     * @return the relation exists or not
     */
    public Boolean hasRelation(String alias, String drugAlias) {
        String validateCypher =
                "MATCH (n:ADDSKGNode:kgId0)-[r:ADDSKGRel]-(m:ADDSKGNode:kgId0)" +
                "WHERE n.alias = \'" + alias + "\' and m.drug_alias = \'" + drugAlias +
                "\' return r";
        StatementResult result = executeCypher(validateCypher);
        return result.hasNext();
    }

    /**
     * Validate if a relation between drug and disease exists
     * @return the relation exists or not
     */
    public Boolean hasKG(Long kgId) {
        ArrayList<KGEntity> kg = kgMapper.getKGByKGId(kgId);
        if (!kg.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
