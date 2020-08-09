package com.java.adds.dao;

import com.java.adds.entity.DeepModelMetricEntity;
import com.java.adds.mapper.DeepModelMetricMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DeepModelMetricDao {
    @Autowired
    DeepModelMetricMapper deepModelMetricMapper;

    /**ljy
     * 获取所有模型评价指标
     * @return
     */
    public ArrayList<DeepModelMetricEntity> getMetrics()
    {
        return deepModelMetricMapper.getMetrics();
    }
}
