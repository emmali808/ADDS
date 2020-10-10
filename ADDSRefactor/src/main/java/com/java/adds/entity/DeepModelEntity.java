package com.java.adds.entity;

import lombok.Data;

@Data
public class DeepModelEntity {
    private Long id;
    private String name;
    private String intro;
    private String articleTitle;
    private String articleUrl;
    private String architectureUrl;
    private String codeUrl;
    private String configFile;
    private Long categoryId;
    private String modelPy;
}
