package cn.medicine.pojo;

/**
 * @ClassName: 会诊记录
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
public class HospitalAdmissionConsultation {
    private long Id;// 记录号
    private String time;//会诊时间
    private String patient_ID;//病人身份证号码
    private String patient_name;//病人名字人身份证号
    private String doctor_ID;//医生身份证号码
    private String doctor_name;//医生名字
    private String record_doctor_ID;//记录医生身份证号码
    private String record_doctor_name;//记录医生名字
   // private String consultation_consultation;//记录医生名字
    private String physical_examination;//查体
    private String consideration ;//目前考虑
    private String current_diagnosis;//目前诊断
    private String suggestion ;//建议

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        this.Id = id;
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



    public String getPhysical_examination() {
        return physical_examination;
    }

    public void setPhysical_examination(String physical_examination) {
        this.physical_examination = physical_examination;
    }

    public String getConsideration() {
        return consideration;
    }

    public void setConsideration(String consideration) {
        this.consideration = consideration;
    }

    public String getCurrent_diagnosis() {
        return current_diagnosis;
    }

    public void setCurrent_diagnosis(String current_diagnosis) {
        this.current_diagnosis = current_diagnosis;
    }

    public String getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(String suggestion) {
        this.suggestion = suggestion;
    }
}
