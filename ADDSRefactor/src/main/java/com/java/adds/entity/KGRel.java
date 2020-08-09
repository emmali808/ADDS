package com.java.adds.entity;

import lombok.Data;
import org.neo4j.ogm.annotation.*;

/**
 * KG Relationship
 * @author QXL
 */
@RelationshipEntity(type = "ADDSKGRel")
@Data
public class KGRel {

    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "name")
    private String name;

    @StartNode
    private KGNode startNode;

    @EndNode
    private KGNode endNode;
}
