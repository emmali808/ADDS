package com.java.adds.dao;

import com.java.adds.entity.MedicalCaseEntity;
import com.java.adds.mapper.MedicalCaseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * Medical Case Dao
 * @author XYX
 */
@Repository
public class MedicalCaseDao {

    @Autowired
    private MedicalCaseMapper medicalCaseMapper;


    /**
     * Get Medical Case By User's Id
     * @param kgId user's id
     * @return A Medical Case ArrayList
     */
    public ArrayList<MedicalCaseEntity> getMedicalCaseByKgId(Long kgId) {
        ArrayList<MedicalCaseEntity> medicalCaseEntities = medicalCaseMapper.getMedicalCaseByKgId(kgId);
        return medicalCaseEntities;
    }

    /**
     * Get Medical Case By User's Id
     * @param userId user's id
     * @return A Medical Case ArrayList
     */
    public ArrayList<MedicalCaseEntity> getMedicalCaseByUserId(Long userId) {
        ArrayList<MedicalCaseEntity> medicalCaseEntities = medicalCaseMapper.getMedicalCaseByUserId(userId);
        return medicalCaseEntities;
    }

    /**
     * Upload Medical Case
     * @param medicalCase medical Case
     * @return KG id
     */
    public Long uploadMedicalCaseByUserId(MedicalCaseEntity medicalCase) {
        medicalCaseMapper.uploadMedicalCaseByUserId(medicalCase);
        return medicalCase.getId();
    }

    /**
     * Insert Medical Case
     * @param medicalCase medical Case
     * @return KG id
     */
    public Long insertMedicalCase(MedicalCaseEntity medicalCase) {
        medicalCaseMapper.insertMedicalCase(medicalCase);
        return medicalCase.getId();
    }

    /**
     * Update Medical Case
     * @param medicalCase medical Case
     * @return KG id
     */
    public Long updateMedicalCase(MedicalCaseEntity medicalCase) {
        medicalCaseMapper.updateMedicalCase(medicalCase);
        return medicalCase.getId();
    }

    /**
     * Get Medical Case By Id
     * @param CaseId medical Case id
     * @return medical Case id
     */
    public MedicalCaseEntity getMedicalCaseById(Long CaseId) {
        return medicalCaseMapper.getMedicalCaseById(CaseId);
    }
}
