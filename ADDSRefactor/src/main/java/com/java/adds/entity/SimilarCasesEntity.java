package com.java.adds.entity;

import lombok.Data;

/**
 * Similar Cases Entity
 * @author ZY
 */
@Data
public class SimilarCasesEntity {
    private Long id;
    private Long kgId;
    private String similar_case_kgIds;
    private String similar_case_hadmIds;
}
