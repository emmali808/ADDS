package com.java.adds.service;

import com.java.adds.dao.DeepModelCategoryDao;
import com.java.adds.dao.DeepModelDao;
import com.java.adds.entity.DeepModelCategoryEntity;
import com.java.adds.entity.DeepModelEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DeepModelCategoryService {

    @Autowired
    private DeepModelCategoryDao categoryDao;

    @Autowired
    private DeepModelDao modelDao;

    /**
     * QXL
     * Get Model Categories (with Models)
     * @return A DeepModelCategoryEntity ArrayList
     */
    public ArrayList<DeepModelCategoryEntity> getModelCategory() {
        return categoryDao.getModelCategory();
    }

    /**
     * QXL
     * Get All Deep Models (Only Model id and name)
     * @return A DeepModelEntity ArrayList
     */
    public ArrayList<DeepModelEntity> getDeepModelsName() {
        return modelDao.getDeepModelsName();
    }

    /**
     * QXL
     * Get Model by id
     * @param modelId Model id
     * @return A DeepModelEntity
     */
    public DeepModelEntity getModelById(Long modelId) {
        return modelDao.getModelById(modelId);
    }
}
