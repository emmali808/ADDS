package cn.medicine.pojo;

import java.io.Serializable;

/**
 * @ClassName:
 * @Description: 基本体征
 * @Function List: TODO
 * @author: ytchen
 * @version:
 * @Date: 2016/8/25
 * @History: //历史修改记录
 * <author>  // 修改人
 * <time> //修改时间
 * <version> //版本
 * <desc> // 描述修改内容
 */
public class BasicBodyRecord implements Serializable {

   
	private static final long serialVersionUID = -3065247051723176242L;
	private long Id;
    private String identityID;//病人身份证号码
    private String time;//时间
    private int pulse;//脉搏
    private int defection_frequency;//大便次数
    private int urine_volume;//尿量
    private float oral;//口表 温度
    private float rectal;// 肛门 温度
    private float axillary;// 腋窝 温度
    private float hypothermia;// 降低体温
    private float heart_rate;//心率
    private float weight;//体重
    private float height;//身高
    private float blood_pressure_low;//血压
    private float blood_pressure_high;//血压
    private float blood_glucose;//血糖
    public BasicBodyRecord(){
        super();
    }
    public BasicBodyRecord(String identityID, String time) {
        this.identityID = identityID;
        this.time = time;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getIdentityID() {
        return identityID;
    }

    public void setIdentityID(String identityID) {
        this.identityID = identityID;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPulse() {
        return pulse;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public int getDefection_frequency() {
        return defection_frequency;
    }

    public void setDefection_frequency(int defection_frequency) {
        this.defection_frequency = defection_frequency;
    }

    public int getUrine_volume() {
        return urine_volume;
    }

    public void setUrine_volume(int urine_volume) {
        this.urine_volume = urine_volume;
    }

    public float getRectal() {
        return rectal;
    }

    public void setRectal(float rectal) {
        this.rectal = rectal;
    }

    public float getOral() {
        return oral;
    }

    public void setOral(float oral) {
        this.oral = oral;
    }

    public float getAxillary() {
        return axillary;
    }

    public void setAxillary(float axillary) {
        this.axillary = axillary;
    }

    public float getHypothermia() {
        return hypothermia;
    }

    public void setHypothermia(float hypothermia) {
        this.hypothermia = hypothermia;
    }

    public float getHeart_rate() {
        return heart_rate;
    }

    public void setHeart_rate(float heart_rate) {
        this.heart_rate = heart_rate;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getBlood_pressure_low() {
        return blood_pressure_low;
    }

    public void setBlood_pressure_low(float blood_pressure_low) {
        this.blood_pressure_low = blood_pressure_low;
    }

    public float getBlood_pressure_high() {
        return blood_pressure_high;
    }

    public void setBlood_pressure_high(float blood_pressure_high) {
        this.blood_pressure_high = blood_pressure_high;
    }

    public float getBlood_glucose() {
        return blood_glucose;
    }

    public void setBlood_glucose(float blood_glucose) {
        this.blood_glucose = blood_glucose;
    }
}
