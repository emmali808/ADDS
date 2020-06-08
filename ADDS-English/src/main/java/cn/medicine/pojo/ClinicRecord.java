package cn.medicine.pojo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

/**
 * @ClassName: 门诊记录
 * @Description: TODO
 * @Function List: TODO
 * @author: ytchen
 * @version:
 * @Date: 2016/8/12
 * @History: //历史修改记录
 * <author>  // 修改人
 * <time> //修改时间
 * <version> //版本
 * <desc> // 描述修改内容
 */
public class ClinicRecord implements Serializable{
        
    private static final long serialVersionUID = 5067165575501067724L;
    
    private long Id;// id
    private long clinic_record_Id;// 诊断号
    private String time;//诊断时间
    private String patient_ID;//病人身份证号码
    private String patient_name;//病人名字
    private String doctor_ID;//医生身份证号码
    private String doctor_name;//医生名字
    private String content;//诊断内容
    private String present_illness;//现病史
    private String medical_history;//既往史
    private String examination;//检查内容
    private String inspection;//检验内容
    private String prescription ;//药方、处方
    private int type ;//0:初次门诊，1：复诊
    public ClinicRecord(){super();}
    public ClinicRecord(String time, String patient_ID, String doctor_ID, String content, String prescription) {
        this.time = time;
        this.patient_ID = patient_ID;
        this.doctor_ID = doctor_ID;
        this.content = content;
        this.prescription = prescription;
    }

    public String getPresent_illness() {
        return present_illness;
    }

    public void setPresent_illness(String present_illness) {
        this.present_illness = present_illness;
    }

    public String getMedical_history() {
        return medical_history;
    }

    public void setMedical_history(String medical_history) {
        this.medical_history = medical_history;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPatient_ID() {
        return patient_ID;
    }

    public void setPatient_ID(String patient_ID) {
        this.patient_ID = patient_ID;
    }

    public String getDoctor_ID() {
        return doctor_ID;
    }

    public void setDoctor_ID(String doctor_ID) {
        this.doctor_ID = doctor_ID;
    }

    public String getExamination() {
        return examination;
    }

    public void setExamination(String examination) {
        this.examination = examination;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInspection() {
        return inspection;
    }

    public void setInspection(String inspection) {
        this.inspection = inspection;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public long getClinic_record_Id() {
        return clinic_record_Id;
    }

    public void setClinic_record_Id(long clinic_record_Id) {
        this.clinic_record_Id = clinic_record_Id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    @Override
    public String toString() {
        JSONObject patient=new JSONObject();
        patient.put("Id", Id);
        patient.put("clinic_record_Id", clinic_record_Id);
        patient.put("time", time);
        patient.put("patient_ID", patient_ID);
        patient.put("doctor_ID", doctor_ID);
        patient.put("content", content);
        patient.put("examination", examination);
        patient.put("inspection", inspection);
        patient.put("prescription", prescription);
        patient.put("type", type);

        return patient.toString();
    }


}
