package cn.medicine.pojo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;


/**
 * @ClassName: 患者的其他信息
 * @Description: TODO
 * @Function List: TODO
 * @author: ytchen
 * @version:
 * @Date: 2016/8/11
 * @History: //历史修改记录
 * <author>  // 修改人
 * <time> //修改时间
 * <version> //版本
 * <desc> // 描述修改内容
 */
public class Patient implements Serializable{


    private static final long serialVersionUID = 1276110284231427081L;

    private long Id;//user id
    private String  birthday;//出生年月
    private String  marriage;//婚姻情况
    private String  nation;// 民族
    private String  birth_place;//出生地
    private String  work_place;//工作单位
    private String  contact_person;//联系人
    private String  contact_phone;//联系人电话
    private String  contact_relationship;//与联系人关系
    private String  contact_address;//联系人地址
    private String  category;//医保类别
    private String  medicare_card_id;//医保卡号
    private String identityID;//身份证号码

    public Patient() {
        super();
    }

    public Patient(long Id) {
        this.Id = Id;
    }

    public long getId() {
        return Id;
    }

    public void setId(long uid) {
        this.Id = uid;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirth_place() {
        return birth_place;
    }

    public void setBirth_place(String birth_place) {
        this.birth_place = birth_place;
    }

    public String getWork_place() {
        return work_place;
    }

    public void setWork_place(String work_place) {
        this.work_place = work_place;
    }

    public String getContact_person() {
        return contact_person;
    }

    public void setContact_person(String contact_person) {
        this.contact_person = contact_person;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getContact_relationship() {
        return contact_relationship;
    }

    public void setContact_relationship(String contact_relationship) {
        this.contact_relationship = contact_relationship;
    }

    public String getContact_address() {
        return contact_address;
    }

    public void setContact_address(String contact_address) {
        this.contact_address = contact_address;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMedicare_card_id() {
        return medicare_card_id;
    }

    public void setMedicare_card_id(String medicare_card_id) {
        this.medicare_card_id = medicare_card_id;
    }

    public String getIdentityID() {
        return identityID;
    }

    public void setIdentityID(String identityID) {
        this.identityID = identityID;
    }

    @Override
    public String toString() {
        JSONObject patient=new JSONObject();
        patient.put("Id", Id);
        patient.put("marriage", marriage);
        patient.put("identityID", identityID);
        patient.put("nation", nation);
        patient.put("birth_place", birth_place);
        patient.put("work_place", work_place);
        patient.put("contact_person", contact_person);
        patient.put("contact_phone", contact_phone);
        patient.put("contact_relationship", contact_relationship);
        patient.put("contact_address", contact_address);
        patient.put("category", category);
        patient.put("medicare_card_id", medicare_card_id);
        return patient.toString();
    }
}
