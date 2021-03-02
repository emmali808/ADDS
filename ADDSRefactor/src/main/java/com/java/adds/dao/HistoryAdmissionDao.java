package com.java.adds.dao;

import com.java.adds.entity.HistoryAdmssionEntity;
import com.java.adds.entity.MedicalCaseEntity;
import com.java.adds.mapper.HistoryAdmissionMapper;
import com.java.adds.mapper.DoctorDiagnosisMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class HistoryAdmissionDao {
    @Autowired
    DoctorDiagnosisMapper doctorDiagnosisMapper;

    @Autowired
    HistoryAdmissionMapper caseMapper;

    /**
     * Get NOTE EVENTS by HADM_ID
     * @return doctor' diagnosis
     */
    public ArrayList<String> getNoteEventsByHadmId(String HadmId)
    {
        return caseMapper.getNoteEventsByHadmId(HadmId);
    }

    public void insertHistoryAdmissions(HistoryAdmssionEntity historyAdmssionEntity) {
        caseMapper.insertMedicalCase(historyAdmssionEntity);
    }

    /**
     * Get History Admission by HADM_ID
     * @param HadmId HadmId
     * @return A similar graphs entity
     */
    public HistoryAdmssionEntity getHistoryAdmssionEntityById(String HadmId) {
        HistoryAdmssionEntity historyAdmssionEntity = caseMapper.getHistoryAdmissionByHadmId(HadmId);
        return historyAdmssionEntity;
    }

}
