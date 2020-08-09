package com.java.adds.dao;

import com.java.adds.entity.DeepModelCategoryEntity;
import com.java.adds.entity.DeepModelEntity;
import com.java.adds.mapper.DeepModelCategoryMapper;
import com.java.adds.mapper.DeepModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DeepModelCategoryDao {

    @Autowired
    DeepModelCategoryMapper categoryMapper;

    /**
     * QXL
     * Get Model Categories (with Models)
     * @return A DeepModelCategoryEntity ArrayList
     */
    public ArrayList<DeepModelCategoryEntity> getModelCategory() {
        return categoryMapper.getModelCategory();
    }
}
