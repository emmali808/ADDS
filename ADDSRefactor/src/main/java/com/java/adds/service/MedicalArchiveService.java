package com.java.adds.service;

import com.java.adds.dao.MedicalArchiveDao;
import com.java.adds.entity.MedicalArchiveEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * Medical Archive Service
 * @author XYX
 */
@Service
public class MedicalArchiveService {

    @Autowired
    private MedicalArchiveDao medicalArchiveDao;

    @Autowired
    private OCRService ocrService;

    /**
     * Get Medical Archive By User's Id
     * @param userId user's id
     * @return A KG ArrayList
     */
    public ArrayList<MedicalArchiveEntity> getMedicalArchiveByUserId(Long userId) {
        return medicalArchiveDao.getMedicalArchiveByUserId(userId);
    }

    /**
     * Upload Medical Archive
     * @param medicalArchive medical archive
     * @return medical archive id
     */
    public Long uploadMedicalArchive(MedicalArchiveEntity medicalArchive) {
        Long medicalArchiveId = medicalArchiveDao.uploadMedicalArchiveByUserId(medicalArchive);
        if (medicalArchiveId >= 0) {
            ocrService.ocrMedicalArchive(medicalArchive);
            return medicalArchiveId;
        } else {
            return -1L;
        }
    }

    /**
     * Update Medical Archive
     * @param medicalArchive medical archive
     * @return medical archive id
     */
    public Long updateMedicalArchive(MedicalArchiveEntity medicalArchive) {
        Long medicalArchiveId = medicalArchiveDao.updateMedicalArchive(medicalArchive);
        if (medicalArchiveId >= 0) {
            return medicalArchiveId;
        } else {
            return -1L;
        }
    }
}
