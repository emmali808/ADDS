package com.java.adds.mapper;

import com.java.adds.entity.DeepModelCategoryEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface DeepModelCategoryMapper {

    /**
     * QXL
     * Get Model Categories (with Models)
     * @return A DeepModelCategoryEntity ArrayList
     */
    @Select("SELECT * FROM deep_model_category")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "name", column = "category"),
            @Result(property = "models", column = "id",
                    many = @Many(select = "com.java.adds.mapper.DeepModelMapper.getModelsByCategoryId"))
    })
    ArrayList<DeepModelCategoryEntity> getModelCategory();
}
