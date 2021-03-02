package com.java.adds.entity;

import lombok.Data;

/**
 * Medical Case Entity
 * @author ZY
 */
@Data
public class MedicalCaseEntity {
    private Long id;
    private Long userId;
    private String title;
    private String category;
    private String description;
    private boolean status;
    private String zipFilePath;
    private String txtFilePath;
    private String ICD9_DIAG;
    private String ICD9_PROCE;
    private String ATC;
    private Long kgId;
}
