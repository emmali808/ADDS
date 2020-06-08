package cn.medicine.pojo;

import java.io.Serializable;

/**
 * @ClassName: 诊疗操作记录、手术记录
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
public class HospitalOperationRecord implements Serializable {
   
	private static final long serialVersionUID = -5127911509065367671L;
	private long Id;// 记录号
    private String time; //诊疗操作日期
    private String content;// 诊疗操作名称
    private String patient_ID;//病人身份证号码
    private String patient_name;//病人名字人身份证号
    private String doctor_name;//医生 列表
    private String operation_steps;// 操作步骤
    private String post_process;// 操作后处理及注意事项
    private String record_doctor_name; // 记录医师

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getDoctor_name() {
        return doctor_name;
    }

    public void setDoctor_name(String doctor_name) {
        this.doctor_name = doctor_name;
    }

    public String getOperation_steps() {
        return operation_steps;
    }

    public void setOperation_steps(String operation_steps) {
        this.operation_steps = operation_steps;
    }

    public String getPost_process() {
        return post_process;
    }

    public void setPost_process(String post_process) {
        this.post_process = post_process;
    }

    public String getRecord_doctor_name() {
        return record_doctor_name;
    }

    public void setRecord_doctor_name(String record_doctor_name) {
        this.record_doctor_name = record_doctor_name;
    }
}
