package com.java.adds.entity;

import lombok.Data;

/**
 * Doctor Diagnosis Entity
 * @author XYX
 */
@Data
public class DoctorDiagnosisEntity {
    private Long id;
    private Long admission_id;
    private String doctor_diagnosis;
}
