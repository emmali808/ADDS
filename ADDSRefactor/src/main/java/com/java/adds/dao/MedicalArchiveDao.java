package com.java.adds.dao;

import com.java.adds.entity.KGEntity;
import com.java.adds.entity.KGNode;
import com.java.adds.entity.KGRel;
import com.java.adds.entity.MedicalArchiveEntity;
import com.java.adds.mapper.KGMapper;
import com.java.adds.mapper.KGRepository;
import com.java.adds.mapper.MedicalArchiveMapper;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.neo4j.driver.StatementResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
