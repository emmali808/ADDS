package com.java.adds.entity;

import lombok.Data;

/**
 * Medical Archive Entity
 * @author XYX
 */
@Data
public class MedicalArchiveEntity {
    private Long id;
    private Long userId;
    private String title;
    private String category;
    private String description;
    private boolean status;
    private String zipFilePath;
    private String txtFilePath;
}
