package com.java.adds.dao;

import com.java.adds.entity.MedicalArchiveEntity;
import com.java.adds.mapper.MedicalArchiveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * Medical Archive Dao
 * @author XYX
 */
@Repository
public class MedicalArchiveDao {

    @Autowired
    private MedicalArchiveMapper medicalArchiveMapper;

    /**
     * Get Medical Archive By User's Id
     * @param userId user's id
     * @return A Medical Archive ArrayList
     */
    public ArrayList<MedicalArchiveEntity> getMedicalArchiveByUserId(Long userId) {
        ArrayList<MedicalArchiveEntity> medicalArchiveEntities = medicalArchiveMapper.getMedicalArchiveByUserId(userId);
        return medicalArchiveEntities;
    }

    /**
     * Upload Medical Archive
     * @param medicalArchive medical archive
     * @return KG id
     */
    public Long uploadMedicalArchiveByUserId(MedicalArchiveEntity medicalArchive) {
        medicalArchiveMapper.uploadMedicalArchiveByUserId(medicalArchive);
        return medicalArchive.getId();
    }

    /**
     * Update Medical Archive
     * @param medicalArchive medical archive
     * @return KG id
     */
    public Long updateMedicalArchive(MedicalArchiveEntity medicalArchive) {
        medicalArchiveMapper.updateMedicalArchive(medicalArchive);
        return medicalArchive.getId();
    }

    /**
     * Get Medical Archive By Id
     * @param archiveId medical archive id
     * @return medical archive id
     */
    public MedicalArchiveEntity getMedicalArchiveById(Long archiveId) {
        return medicalArchiveMapper.getMedicalArchiveById(archiveId);
    }
}
