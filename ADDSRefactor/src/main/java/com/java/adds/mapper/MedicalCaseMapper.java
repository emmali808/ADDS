package com.java.adds.mapper;

import com.java.adds.entity.MedicalCaseEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * Medical Case Mapper
 * @author ZY
 */
@Mapper
@Repository
public interface MedicalCaseMapper {

    /**
     * Get Medical Case By User's Id
     * @param userId user's id
     * @return A Medical Case ArrayList
     */
    ArrayList<MedicalCaseEntity> getMedicalCaseByUserId(@Param("userId") Long userId);

    /**
     * Get Medical Case By kgId
     * @param kgId kgId
     * @return A Medical Case ArrayList
     */
    ArrayList<MedicalCaseEntity> getMedicalCaseByKgId(@Param("kgId") Long kgId);


    /**
     * Upload Medical Case
     * @param medicalCaseEntity medical case entity
     */
    void uploadMedicalCaseByUserId(MedicalCaseEntity medicalCaseEntity);


    /**
     * Insert Medical Case
     * @param medicalCaseEntity medical case entity
     */
    void insertMedicalCase(MedicalCaseEntity medicalCaseEntity);


    /**
     * Update Medical Case
     * @param medicalCaseEntity medical case entity
     */
    void updateMedicalCase(MedicalCaseEntity medicalCaseEntity);

    /**
     * Get Medical Case By Case's Id
     * @param caseId medical case id
     * @return A Medical Case
     */
    MedicalCaseEntity getMedicalCaseById(Long caseId);
}
