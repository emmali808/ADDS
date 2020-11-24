package com.java.adds.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface MetricMapper {

    /**ljy
     * 根据id获取metric
     * @return
     */
    public String getMetricById(@Param("id") Long id);
}
