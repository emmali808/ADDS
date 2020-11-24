package com.java.adds.controller;

import com.java.adds.entity.DeepModelCategoryEntity;
import com.java.adds.entity.DeepModelEntity;
import com.java.adds.security.annotation.PassToken;
import com.java.adds.service.DeepModelCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("modelCategory")
public class DeepModelCategoryController {

    @Autowired
    DeepModelCategoryService deepModelCategoryService;

    /**ljy
     * 获取所有深度学习模型的类别
     * @return
     */
    @GetMapping("")
    public ArrayList<DeepModelCategoryEntity> getAllModelCategory() {
        return deepModelCategoryService.getModelCategory();
    }

    /**
     * QXL
     * Get All Deep Models (Only Model id and name)
     * @return A DeepModelEntity ArrayList
     */
    @GetMapping("modelName")
    public ArrayList<DeepModelEntity> getDeepModelsName() {
        return deepModelCategoryService.getDeepModelsName();
    }

    /**
     * QXL
     * 根据 id 获取模型信息
     * @param mId Deep Model id
     * @return DeepModelEntity
     */
    @GetMapping("model/{mId}")
    public DeepModelEntity getModelById(@PathVariable Long mId) {
        return deepModelCategoryService.getModelById(mId);
    }
}
