package com.java.adds.mapper;


import com.java.adds.entity.DeepModelMetricEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface DeepModelMetricMapper {
    /**ljy
     * 获取所有模型评价指标
     * @return
     */
    public ArrayList<DeepModelMetricEntity> getMetrics();
}
