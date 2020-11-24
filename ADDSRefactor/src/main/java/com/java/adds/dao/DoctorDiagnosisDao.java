package com.java.adds.dao;

import com.java.adds.entity.DoctorDiagnosisEntity;
import com.java.adds.mapper.DoctorDiagnosisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DoctorDiagnosisDao {
    @Autowired
    DoctorDiagnosisMapper doctorDiagnosisMapper;

    /**XYX
     * Get Doctor's diagnosis by admission's id
     * @return doctor' diagnosis
     */
    public ArrayList<DoctorDiagnosisEntity> getDiagnosisByAdmissionId(Long admissionId)
    {
        return doctorDiagnosisMapper.getDiagnosisByAdmissionId(admissionId);
    }

}
