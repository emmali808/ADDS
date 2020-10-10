package com.java.adds.service;


import com.java.adds.dao.DeepModelMetricDao;
import com.java.adds.entity.DeepModelMetricEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DeepModelMetricService {
    @Autowired
    DeepModelMetricDao deepModelMetricDao;

    /**ljy
     * 获取所有模型评价指标
     * @return
     */
    public ArrayList<DeepModelMetricEntity> getMetrics()
    {
        return deepModelMetricDao.getMetrics();
    }
}
