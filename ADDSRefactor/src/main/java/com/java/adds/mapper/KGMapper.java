package com.java.adds.mapper;

import com.java.adds.entity.KGEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * KG Mapper
 * @author QXL
 */
@Mapper
@Repository
public interface KGMapper {

    /**
     * Upload Knowledge-Graph file
     * @param userId user's id
     * @return A KGEntity ArrayList
     */
    @Select("SELECT * FROM kg_upload WHERE user_id=#{userId}")
    @Results(id = "kgMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "userId", column = "user_id"),
            @Result(property = "name", column = "name"),
            @Result(property = "desc", column = "kg_desc"),
            @Result(property = "filePath", column = "file_path")
    })
    ArrayList<KGEntity> getKGListByUserId(@Param("userId") Long userId);

    /**
     * Upload Knowledge-Graph file
     * @param kgEntity KG file info
     */
    @Insert("INSERT INTO kg_upload(user_id, name, kg_desc, file_path) " +
            "values(#{kgEntity.userId}, #{kgEntity.name}, #{kgEntity.desc}, #{kgEntity.filePath})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "kgEntity.id")
    void uploadKGFile(@Param("kgEntity") KGEntity kgEntity);
}
