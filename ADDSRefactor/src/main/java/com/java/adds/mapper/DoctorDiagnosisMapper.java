package com.java.adds.mapper;

import com.java.adds.entity.DoctorDiagnosisEntity;
import com.java.adds.entity.KGEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * Doctor Diagnosis Mapper
 * @author XYX
 */
@Mapper
@Repository
public interface DoctorDiagnosisMapper {

    /**
     * Get Doctor's Diagnosis
     * @param admissionId an admission's id
     * @return doctor's diagnosis on this admission
     */
    @Select("SELECT * FROM doctor_diagnosis WHERE admission_id=#{admissionId}")
    @Results(id = "diagnosisMap", value = {
        @Result(property = "id", column = "id"),
        @Result(property = "admission_id", column = "admission_id"),
        @Result(property = "doctor_diagnosis", column = "doctor_diagnosis"),
    })
    ArrayList<DoctorDiagnosisEntity> getDiagnosisByAdmissionId(@Param("admissionId") Long admissionId);
}
