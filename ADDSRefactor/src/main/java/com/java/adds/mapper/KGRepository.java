package com.java.adds.mapper;

import com.java.adds.entity.KGNode;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * KG Neo4j Repository
 * @author QXL
 */
@Repository
public interface KGRepository extends Neo4jRepository<KGNode, Long> {

    @Query("MATCH (x)-[r]-(y) WHERE id(x)={nodeId} RETURN x, r, y;")
    List<Map<String, Object>> getNodeAndRelNodes(Long nodeId);
}
