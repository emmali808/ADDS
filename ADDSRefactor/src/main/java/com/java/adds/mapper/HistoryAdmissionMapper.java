package com.java.adds.mapper;

import com.java.adds.dao.HistoryAdmissionDao;
import com.java.adds.entity.DoctorDiagnosisEntity;
import com.java.adds.entity.HistoryAdmssionEntity;
import com.java.adds.entity.MedicalCaseEntity;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * Note Event Mapper
 * @author ZY
 */
@Mapper
@Repository
public interface HistoryAdmissionMapper {


    /**
     * Get all history admissions
     */
    @Select("SELECT distinct SUBJECT_ID,HADM_ID,ICD9_DIAG,ICD9_PROCE,NDC,disease,ATC FROM history_admissions")
    @Results(id = "admissionMap", value = {
        @Result(property = "SUBJECT_ID", column = "SUBJECT_ID"),
        @Result(property = "HADM_ID", column = "HADM_ID"),
        @Result(property = "ICD9_DIAG", column = "ICD9_DIAG"),
        @Result(property = "ICD9_PROCE", column = "ICD9_PROCE"),
        @Result(property = "NDC", column = "NDC"),
        @Result(property = "disease", column = "disease"),
        @Result(property = "ATC", column = "ATC")
    })
    ArrayList<HistoryAdmssionEntity> getAllHistoryAdmissions();


    /**
     * Insert Medical Case
     * @param historyAdmssionEntity history admission entity
     */
    @Select("INSERT INTO history_admissions(SUBJECT_ID,HADM_ID,ICD9_DIAG,ICD9_PROCE,NDC,disease,ATC,TEXT)"+
            "values(#{historyAdmssionEntity.SUBJECT_ID}," +
            " #{historyAdmssionEntity.HADM_ID}," +
            " #{historyAdmssionEntity.ICD9_DIAG}," +
            " #{historyAdmssionEntity.ICD9_PROCE}," +
            " #{historyAdmssionEntity.NDC},"+
            " #{historyAdmssionEntity.disease},"+
            " #{historyAdmssionEntity.ATC},"+
            " #{historyAdmssionEntity.TEXT}"+
    ")")
    void insertMedicalCase(@Param("historyAdmssionEntity")HistoryAdmssionEntity historyAdmssionEntity);

    /**
     * Get Doctor's Notes
     * @param HadmId an admission's id
     * @return doctor's notes on this admission
     */
    @Select("SELECT TEXT FROM history_admissions WHERE HADM_ID=#{HadmId}")
    ArrayList<String> getNoteEventsByHadmId(@Param("HadmId") String HadmId);


    /**
     * Get Medical Case By Case's Id
     * @param HadmId medical case id
     * @return A Medical Case
     */
    @Select("SELECT distinct SUBJECT_ID,HADM_ID,ICD9_DIAG,ICD9_PROCE,NDC,disease,ATC FROM history_admissions WHERE HADM_ID=#{HadmId}")
    HistoryAdmssionEntity getHistoryAdmissionByHadmId(@Param("HadmId") String HadmId);

}
