package com.java.adds.entity;


public class DataSetsEntity {
    private Long id;
    private String dataset_name;
    private String dataset_desc;
    private Long user_id;
    private String train_path;
    private String train_name;
    private String test_path;
    private String test_name;
    private String dev_path;
    private String dev_name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDataset_name() {
        return dataset_name;
    }

    public void setDataset_name(String dataset_name) {
        this.dataset_name = dataset_name;
    }

    public String getDataset_desc() {
        return dataset_desc;
    }

    public void setDataset_desc(String dataset_desc) {
        this.dataset_desc = dataset_desc;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public String getTrain_path() {
        return train_path;
    }

    public void setTrain_path(String train_path) {
        this.train_path = train_path;
    }

    public String getTrain_name() {
        return train_name;
    }

    public void setTrain_name(String train_name) {
        this.train_name = train_name;
    }

    public String getTest_path() {
        return test_path;
    }

    public void setTest_path(String test_path) {
        this.test_path = test_path;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }

    public String getDev_path() {
        return dev_path;
    }

    public void setDev_path(String dev_path) {
        this.dev_path = dev_path;
    }

    public String getDev_name() {
        return dev_name;
    }

    public void setDev_name(String dev_name) {
        this.dev_name = dev_name;
    }
}
