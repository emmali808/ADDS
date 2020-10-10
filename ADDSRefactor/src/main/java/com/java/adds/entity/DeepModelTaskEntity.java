package com.java.adds.entity;

import lombok.Data;

@Data
public class DeepModelTaskEntity {
    private Long id;
    private String taskName;
    private Long datasetId;
    private Long kgId;
    private Integer queryLength;
    private Integer documentLength;
    private Integer modelType;  //模型类型
    private Long modelId;
    private Long metricId;
    private Integer status;  //0:正在运行模型，1：模型运行结束
    private Long userId;
    private Long resultId;
    private String resultFilePath; //结果文件存放路径

}
