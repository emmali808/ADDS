package com.java.adds.mapper;

import com.java.adds.entity.SimilarCasesEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * Similar Cases Mapper
 * @author ZY
 */
@Mapper
@Repository
public interface SimilarCasesMapper {

    /**
     * Find Similar Cases
     * @param kgId an kg's id
     * @return similar cases of this kg
     */
    @Select("SELECT * FROM similar_cases WHERE kgId=#{kgId}")
    @Results(id = "similarCasesMap", value = {
        @Result(property = "id", column = "id"),
        @Result(property = "kgId", column = "kgId"),
        @Result(property = "similar_case_kgIds", column = "similar_case_kgIds"),
        @Result(property = "similar_case_hadmIds", column = "similar_case_hadmIds")
    })
    ArrayList<SimilarCasesEntity> getSimilarCasesByKgId(@Param("kgId") Long kgId);

    /**
     * Insert New Similar Cases Search Result
     * @param similarCasesEntity similar cases entity
     */
    @Insert("INSERT INTO similar_cases(kgId, similar_case_kgIds, similar_case_hadmIds) " +
            "values(#{similarCasesEntity.kgId}, #{similarCasesEntity.similar_case_kgIds}, #{similarCasesEntity.similar_case_hadmIds})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "similarCasesEntity.id")
    void insertNewSimilarSearchResult(@Param("similarCasesEntity") SimilarCasesEntity similarCasesEntity);
}
