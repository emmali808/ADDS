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

    /**
     * Get Medical Archive By User's Id
     * @param userId user's id
     * @return A KG ArrayList
     */
    public ArrayList<MedicalArchiveEntity> getMedicalArchiveByUserId(Long userId) {
        return medicalArchiveDao.getMedicalArchiveByUserId(userId);
    }
}
