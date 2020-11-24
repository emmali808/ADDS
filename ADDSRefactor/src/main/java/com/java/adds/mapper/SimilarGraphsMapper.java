package com.java.adds.mapper;

import com.java.adds.entity.DoctorDiagnosisEntity;
import com.java.adds.entity.SimilarGraphsEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * Similar Graphs Mapper
 * @author XYX
 */
@Mapper
@Repository
public interface SimilarGraphsMapper {

    /**
     * Find Similar Graphs
     * @param kgId an kg's id
     * @return similar graphs of this kg
     */
    @Select("SELECT * FROM similar_graphs WHERE kgId=#{kgId}")
    @Results(id = "similarGraphsMap", value = {
        @Result(property = "id", column = "id"),
        @Result(property = "kgId", column = "kgId"),
        @Result(property = "similar_graphs", column = "similar_graphs"),
    })
    SimilarGraphsEntity getSimilarGraphsByKgId(@Param("kgId") Long kgId);


    /**
     * Insert New Similar Graphs Search Result
     * @param similarGraphsEntity similar graphs entity
     */
    @Insert("INSERT INTO similar_graphs(kgId, similar_graphs) " +
            "values(#{similarGraphsEntity.kgId}, #{similarGraphsEntity.similar_graphs})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "similarGraphsEntity.id")
    void insertNewSimilarSearchResult(@Param("similarGraphsEntity") SimilarGraphsEntity similarGraphsEntity);
}
