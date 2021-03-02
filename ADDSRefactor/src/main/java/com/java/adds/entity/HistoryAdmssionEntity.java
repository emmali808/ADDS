package com.java.adds.entity;

import lombok.Data;

/**
 * History Admission Entity
 * @author ZY
 */
@Data
public class HistoryAdmssionEntity {
    private String SUBJECT_ID;
    private String HADM_ID;
    private String ICD9_DIAG;
    private String ICD9_PROCE;
    private String NDC;
    private String disease;
    private String ATC;
    private String TEXT;
}
