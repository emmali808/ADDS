package com.java.adds.dao;

import com.java.adds.entity.SimilarCasesEntity;
import com.java.adds.mapper.SimilarCasesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * Similar Cases Dao
 * @author XYX
 */
@Component
public class SimilarCasesDao {

    @Autowired
    private SimilarCasesMapper similarCasesMapper;

    /**
     * Get Similar Cases by KgId
     * @param kgId KgId
     * @return A similar graphs entity
     */
    public ArrayList<SimilarCasesEntity> getSimilarCasesByKgId(Long kgId) {
        ArrayList<SimilarCasesEntity> result = similarCasesMapper.getSimilarCasesByKgId(kgId);
        return result;
    }

    /**
     * Insert New Similar Search Result
     * @param similarCasesEntity similar graphs entity
     * @return similar graphs entity id
     */
    public Long insertNewSimilarSearchResult(SimilarCasesEntity similarCasesEntity) {
        similarCasesMapper.insertNewSimilarSearchResult(similarCasesEntity);
        return similarCasesEntity.getId();
    }
}
