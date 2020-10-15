package com.java.adds.mapper;

import com.java.adds.entity.MedicalArchiveEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * Medical Archive Mapper
 * @author XYX
 */
@Mapper
@Repository
public interface MedicalArchiveMapper {

    /**
     * Get Medical Archive By User's Id
     * @param userId user's id
     * @return A Medical Archive ArrayList
     */
    ArrayList<MedicalArchiveEntity> getMedicalArchiveByUserId(@Param("userId") Long userId);

    /**
     * Upload Medical Archive
     * @param medicalArchiveEntity medical archive entity
     */
    void uploadMedicalArchiveByUserId(MedicalArchiveEntity medicalArchiveEntity);

    /**
     * Update Medical Archive
     * @param medicalArchiveEntity medical archive entity
     */
    void updateMedicalArchive(MedicalArchiveEntity medicalArchiveEntity);
}
