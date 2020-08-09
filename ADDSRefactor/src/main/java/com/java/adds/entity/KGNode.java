package com.java.adds.entity;

import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

/**
 * KG Node
 * @author QXL
 */
@NodeEntity(label = "ADDSKGNode")
@Data
public class KGNode {

    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "name")
    private String name;
}
