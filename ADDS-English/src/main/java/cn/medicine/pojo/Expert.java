package cn.medicine.pojo;

import java.io.Serializable;

/**
 * @ClassName: 医生的其他信息
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
public class Expert implements Serializable{

	private static final long serialVersionUID = -2289184577308327877L;
	private long Id;// id
    private String identityID;//身份证号码
    private String birthday;//出生年月
    private String  marriage;//婚姻情况 0:未婚 1：已婚 2：离异 3：丧偶
    private String  nation;// 民族
    private String  birth_place;//出生地
    private String  work_place;//工作单位
    private String  education;//学历
    private String  title;//职称
    private String  skill;//擅长方向
    private String  introduction;//其他介绍
    public Expert() {
        super();
    }
    public Expert(String identityID) {
        this.identityID = identityID;
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


    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
