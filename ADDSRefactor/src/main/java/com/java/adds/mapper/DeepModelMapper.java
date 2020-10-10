package com.java.adds.mapper;

import com.java.adds.entity.DeepModelEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface DeepModelMapper {

    /**
     * QXL
     * Get Models of Category
     * @param categoryId Category id
     * @return A DeepModelEntity ArrayList
     */
    @Select("SELECT * FROM deep_model WHERE model_category=#{categoryId}")
    @Results(id = "deepModelMap", value = {
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "model_name"),
            @Result(property = "intro", column = "model_introduction"),
            @Result(property = "articleTitle", column = "model_article_title"),
            @Result(property = "articleUrl", column = "model_article_url"),
            @Result(property = "architectureUrl", column = "model_architecture_url"),
            @Result(property = "codeUrl", column = "model_code_url"),
            @Result(property = "categoryId", column = "model_category"),
            @Result(property = "configFile", column = "config_file"),
            @Result(property = "modelPy", column = "model_py")
    })
    ArrayList<DeepModelEntity> getModelsByCategoryId(@Param("categoryId") Long categoryId);

    /**
     * QXL
     * Get All Deep Models (Only Model id and name)
     * @return A DeepModelEntity ArrayList
     */
    @Select("SELECT id, model_name FROM deep_model")
    @ResultMap("deepModelMap")
    ArrayList<DeepModelEntity> getDeepModelsName();

    /**
     * QXL
     * Get Model by id
     * @param modelId Model id
     * @return A DeepModelEntity
     */
    @Select("SELECT * FROM deep_model WHERE id=#{modelId}")
    @ResultMap("deepModelMap")
    DeepModelEntity getModelById(@Param("modelId") Long modelId);
}
