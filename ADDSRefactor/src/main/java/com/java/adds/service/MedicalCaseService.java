package com.java.adds.service;

import com.java.adds.dao.MedicalCaseDao;
import com.java.adds.entity.MedicalCaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Medical Case Service
 * @author ZY
 */
@Service
public class MedicalCaseService {

    @Autowired
    private MedicalCaseDao medicalCaseDao;

    @Autowired
    private OCRService ocrService;

    /**
     * Get Medical Case By User's Id
     * @param userId user's id
     * @return A KG ArrayList
     */
    public ArrayList<MedicalCaseEntity> getMedicalCaseByUserId(Long userId) {
        return medicalCaseDao.getMedicalCaseByUserId(userId);
    }

    /**
     * Upload Medical Case
     * @param medicalCase medical case
     * @return medical case id
     */
    public Long uploadMedicalCase(MedicalCaseEntity medicalCase) {
        Long medicalCaseId = medicalCaseDao.uploadMedicalCaseByUserId(medicalCase);
        if (medicalCaseId >= 0) {
            return medicalCaseId;
        } else {
            return -1L;
        }
    }

    /**
     * Update Medical Case
     * @param medicalCase medical case
     * @return medical case id
     */
    public Long updateMedicalCase(MedicalCaseEntity medicalCase) {
        Long medicalCaseId = medicalCaseDao.updateMedicalCase(medicalCase);
        if (medicalCaseId >= 0) {
            return medicalCaseId;
        } else {
            return -1L;
        }
    }

    /**
     * Get Medical Case By Id
     * @param caseId medical case id
     * @return medical case id
     */
    public MedicalCaseEntity getMedicalCaseById(Long caseId) {
        return medicalCaseDao.getMedicalCaseById(caseId);
    }
}
