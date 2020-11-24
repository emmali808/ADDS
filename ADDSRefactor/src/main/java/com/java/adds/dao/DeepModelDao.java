package com.java.adds.dao;

import com.java.adds.entity.DeepModelEntity;
import com.java.adds.mapper.DeepModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DeepModelDao {

    @Autowired
    private DeepModelMapper modelMapper;

    /**
     * QXL
     * Get All Deep Models (Only Model id and name)
     * @return A DeepModelEntity ArrayList
     */
    public ArrayList<DeepModelEntity> getDeepModelsName() {
        return  modelMapper.getDeepModelsName();
    }

    /**
     * QXL
     * Get Model by id
     * @param modelId Model id
     * @return A DeepModelEntity
     */
    public DeepModelEntity getModelById(Long modelId) {
        return modelMapper.getModelById(modelId);
    }
}
