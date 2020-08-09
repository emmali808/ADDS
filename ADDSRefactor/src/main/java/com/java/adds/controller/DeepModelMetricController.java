package com.java.adds.controller;


import com.java.adds.entity.DeepModelMetricEntity;
import com.java.adds.service.DeepModelMetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("modelMetric")
public class DeepModelMetricController {
    @Autowired
    DeepModelMetricService deepModelMetricService;

    /**ljy
     * 获取所有模型评价指标
     * @return
     */
    @GetMapping("")
    public ArrayList<DeepModelMetricEntity> getMetrics()
    {
        return deepModelMetricService.getMetrics();
    }

}
