package com.java.adds.dao;

import com.java.adds.entity.MedicalArchiveEntity;
import com.java.adds.entity.SimilarGraphsEntity;
import com.java.adds.mapper.MedicalArchiveMapper;
import com.java.adds.mapper.SimilarGraphsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * Similar Graphs Dao
 * @author XYX
 */
@Repository
public class SimilarGraphsDao {

    @Autowired
    private SimilarGraphsMapper similarGraphsMapper;

    /**
     * Get Similar Graphs by KgId
     * @param kgId KgId
     * @return A similar graphs entity
     */
    public SimilarGraphsEntity getSimilarGraphsByKgId(Long kgId) {
        SimilarGraphsEntity similarGraphsEntity = similarGraphsMapper.getSimilarGraphsByKgId(kgId);
        return similarGraphsEntity;
    }

    /**
     * Insert New Similar Search Result
     * @param similarGraphsEntity similar graphs entity
     * @return similar graphs entity id
     */
    public Long insertNewSimilarSearchResult(SimilarGraphsEntity similarGraphsEntity) {
        similarGraphsMapper.insertNewSimilarSearchResult(similarGraphsEntity);
        return similarGraphsEntity.getId();
    }
}
