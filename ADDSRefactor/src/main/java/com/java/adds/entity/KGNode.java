package com.java.adds.entity;

import lombok.Data;
import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;

/**
 * KG Node
 * @author xyx
 */
@NodeEntity(label = "ADDSKGNode")
@Data
public class KGNode {

    @Id
    @GeneratedValue
    private Long id;

    @Property(name = "type")
    private String type;

    @Property(name = "patient_id")
    private String patient_id;

    @Property(name = "gender")
    private String gender;

    @Property(name = "religion")
    private String religion;

    @Property(name = "ethnicity")
    private String ethnicity;

    @Property(name = "birth_time")
    private String birth_time;

    @Property(name = "admission_id")
    private String admission_id;

    @Property(name = "admit_time")
    private String admit_time;

    @Property(name = "duration")
    private String duration;

    @Property(name = "flag")
    private String flag;

    @Property(name = "admit_age")
    private String admit_age;

    @Property(name = "disease_id")
    private String disease_id;

    @Property(name = "alias")
    private String alias;

    @Property(name = "drug_id")
    private String drug_id;

    @Property(name = "count")
    private String count;

    @Property(name = "frequency")
    private String frequency;

    @Property(name = "drug_alias")
    private String drug_alias;

    @Property(name = "dose_val_rx")
    private String dose_val_rx;

    @Property(name = "dose_unit_rx")
    private String dose_unit_rx;
}
