package com.java.adds.entity;

import lombok.Data;

import java.util.ArrayList;

@Data
public class DeepModelCategoryEntity {
    private Integer id;
    private String name;
    private ArrayList<DeepModelEntity> models;
}
