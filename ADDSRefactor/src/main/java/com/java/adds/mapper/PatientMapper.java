package com.java.adds.mapper;


import com.java.adds.entity.PatientEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface PatientMapper {
    /**ljy
     * 管理员获取所有病人信息
     */
    public ArrayList<PatientEntity> getAllPatients();
}
