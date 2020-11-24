package com.java.adds.entity;

import lombok.Data;

/**
 * KG Entity
 * @author QXL
 */
@Data
public class KGEntity {
    private Long id;
    private Long userId;
    private String name;
    private String desc;
    private String filePath;
}
