package cn.medicine.pojo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

/**
 * 普通用户
 */
public class User implements Serializable{       
    private static final long serialVersionUID = 346758421969685198L;


    private long uid;
    private String login_name;//用户名
    private String username;//真实姓名
    private String password;
    private int age;//年龄
    private String gender;//性别,m:男生  f：女生
    private String phone;//手机号
    private String email;//邮箱
    private String picture;//头像
    private String identityID;//身份证号码
    private String birthday;//出生年月
    private String  marriage;//婚姻情况
    private String  nation;// 民族
    private String  birth_place;//出生地
    private String  work_place;//工作单位
    private int type;//0 管理员 1 医生 2病人 3专家 4普通用户
    private boolean state;//是否可用  1：可用，2：不用

    public User(){
        super();
        this.state=true;
    }
    
    public User(String username,String password){
        this(username,password,2);
    }
    
    public User(String username,String password,int type){
        super();
        this.username=username;
        this.password=password;
        this.type=type;
        this.state=true;
    }
    public User(String username,String password,String gender,String phone,int type){
        super();
        this.username=username;
        this.password=password;
        this.gender=gender;
        this.phone=phone;
        this.type=type;
        this.state=true;
    }
    public User(String username,String password,int age,String gender,String phone,int type){
        super();
        this.username=username;
        this.password=password;
        this.age=age;
        this.gender=gender;
        this.phone=phone;
        this.type=type;
        this.state=true;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getLogin_name() {
        return login_name;
    }

    public void setLogin_name(String login_name) {
        this.login_name = login_name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    
    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isState() {
        return state;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public long getId() {
        return uid;
    }

    public void setId(long id) {
        this.uid = id;
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

    @Override
    public String toString() {
        JSONObject user=new JSONObject();
        user.put("uid", uid);
        user.put("identityID", identityID);
        user.put("username", username);
        user.put("age", age);
        user.put("gender", gender);
        user.put("phone", phone);
        user.put("type", type);
        user.put("email", email);
        user.put("type", type);
        user.put("state", state);
        return user.toString();
    }

}
