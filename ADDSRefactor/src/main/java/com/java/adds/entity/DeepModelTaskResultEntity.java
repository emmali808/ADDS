package com.java.adds.entity;

import lombok.Data;

@Data
public class DeepModelTaskResultEntity {
    private Long id;
    private Long taskId;
    private Long modelId;
    private String modelName;
    private String ndcg1;
    private String ndcg3;
    private String ndcg5;
    private String ndcg10;
    private String map;
    private String recall3;
    private String recall5;
    private String recall10;
    private String precision1;
    private String precision3;
    private String precision5;
    private String precision10;
}
