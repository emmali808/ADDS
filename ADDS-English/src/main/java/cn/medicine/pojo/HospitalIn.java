package cn.medicine.pojo;

import java.io.Serializable;

/**
 * @ClassName: 首次病程记录
 * @Description: TODO
 * @Function List: TODO
 * @author: ytchen
 * @version:
 * @Date: 2016/8/30
 * @History: //历史修改记录
 * <author>  // 修改人
 * <time> //修改时间
 * <version> //版本
 * <desc> // 描述修改内容
 */
public class HospitalIn implements Serializable {

	private static final long serialVersionUID = 158623897825339217L;
	private long Id;// id
    private String time;//入院时间
    private String patient_ID;//病人身份证号码
    private String patient_name;//病人名字
    private String doctor_ID;//医生身份证号码
    private String doctor_name;//医生名字
    private String causes;//入院原因
    private String course;//病程
    private String symptoms;//症状表现
    private String physical_examination;//查体
    private String medical_history;//既往史
    private String assistant_examination;//辅助检查
    private String preliminary_diagnosis;//初步诊断
    private String diagnostic_basis;//诊断依据
    private String differential_diagnosis;//鉴别诊断
    private String assessment_plan;//诊疗计划

    public HospitalIn(){
        super();
    }
    public HospitalIn(String time, String patient_ID, String patient_name, String doctor_ID, String doctor_name, String causes, String course, String symptoms, String physical_examination, String medical_history, String assistant_examination, String preliminary_diagnosis, String diagnostic_basis, String differential_diagnosis, String assessment_plan) {
        this.time = time;
        this.patient_ID = patient_ID;
        this.patient_name = patient_name;
        this.doctor_ID = doctor_ID;
        this.doctor_name = doctor_name;
        this.causes = causes;
        this.course = course;
        this.symptoms = symptoms;
        this.physical_examination = physical_examination;
        this.medical_history = medical_history;
        this.assistant_examination = assistant_examination;
        this.preliminary_diagnosis = preliminary_diagnosis;
        this.diagnostic_basis = diagnostic_basis;
        this.differential_diagnosis = differential_diagnosis;
        this.assessment_plan = assessment_plan;
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

    public String getPatient_name() {
        return patient_name;
    }

    public void setPatient_name(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getDoctor_ID() {
        return doctor_ID;
    }

    public void setDoctor_ID(String doctor_ID) {
        this.doctor_ID = doctor_ID;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getCauses() {
        return causes;
    }

    public void setCauses(String causes) {
        this.causes = causes;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getPhysical_examination() {
        return physical_examination;
    }

    public void setPhysical_examination(String physical_examination) {
        this.physical_examination = physical_examination;
    }

    public String getMedical_history() {
        return medical_history;
    }

    public void setMedical_history(String medical_history) {
        this.medical_history = medical_history;
    }

    public String getAssistant_examination() {
        return assistant_examination;
    }

    public void setAssistant_examination(String assistant_examination) {
        this.assistant_examination = assistant_examination;
    }

    public String getPreliminary_diagnosis() {
        return preliminary_diagnosis;
    }

    public void setPreliminary_diagnosis(String preliminary_diagnosis) {
        this.preliminary_diagnosis = preliminary_diagnosis;
    }

    public String getDiagnostic_basis() {
        return diagnostic_basis;
    }

    public void setDiagnostic_basis(String diagnostic_basis) {
        this.diagnostic_basis = diagnostic_basis;
    }

    public String getDifferential_diagnosis() {
        return differential_diagnosis;
    }

    public void setDifferential_diagnosis(String differential_diagnosis) {
        this.differential_diagnosis = differential_diagnosis;
    }

    public String getAssessment_plan() {
        return assessment_plan;
    }

    public void setAssessment_plan(String assessment_plan) {
        this.assessment_plan = assessment_plan;
    }
}
