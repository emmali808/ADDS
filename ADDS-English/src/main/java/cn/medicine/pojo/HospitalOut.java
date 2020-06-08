package cn.medicine.pojo;

import java.io.Serializable;

/**
 * @ClassName: 出院登记
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
public class HospitalOut implements Serializable {
    
	private static final long serialVersionUID = -8266580954990116394L;
	private long Id;// 记录号
    private String time_in;//入院时间
    private String time_out;//出院时间
    private String patient_ID;//病人身份证号码
    private String patient_name;//病人名字人身份证号
    private String doctor_ID;//医生身份证号码
    private String doctor_name;//医生名字
    private String record_doctor_ID;//记录医生身份证号码
    private String record_doctor_name;//记录医生名字
    private String treatment_procedure;//诊疗经过

    private String admission_diagnosis;//入院诊断
    private String discharge_diagnosis;//出院诊断
    private String medical_advice;//出院医嘱

    public String getTime_out() {
        return time_out;
    }

    public void setTime_out(String time_out) {
        this.time_out = time_out;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        this.Id = id;
    }

    public String getTime_in() {
        return time_in;
    }

    public void setTime_in(String time_in) {
        this.time_in = time_in;
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

    public String getRecord_doctor_ID() {
        return record_doctor_ID;
    }

    public void setRecord_doctor_ID(String record_doctor_ID) {
        this.record_doctor_ID = record_doctor_ID;
    }

    public String getRecord_doctor_name() {
        return record_doctor_name;
    }

    public void setRecord_doctor_name(String record_doctor_name) {
        this.record_doctor_name = record_doctor_name;
    }

    public String getTreatment_procedure() {
        return treatment_procedure;
    }

    public void setTreatment_procedure(String treatment_procedure) {
        this.treatment_procedure = treatment_procedure;
    }



    public String getAdmission_diagnosis() {
        return admission_diagnosis;
    }

    public void setAdmission_diagnosis(String admission_diagnosis) {
        this.admission_diagnosis = admission_diagnosis;
    }

    public String getDischarge_diagnosis() {
        return discharge_diagnosis;
    }

    public void setDischarge_diagnosis(String discharge_diagnosis) {
        this.discharge_diagnosis = discharge_diagnosis;
    }

    public String getMedical_advice() {
        return medical_advice;
    }

    public void setMedical_advice(String medical_advice) {
        this.medical_advice = medical_advice;
    }
}
