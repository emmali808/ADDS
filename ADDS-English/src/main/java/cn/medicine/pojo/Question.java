package cn.medicine.pojo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @ClassName:Question
 * @Description: 在线问答系统问题
 * @Function List:TODO 主要函数及其功能
 *
 * @author   ztxu
 * @version  
 * @Date	 2016-8-21下午4:51:49
 *
 * @History:// 历史修改记录 
 * <author>  // 修改人
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述修改内容
 */
public class Question implements Serializable{
    
    private static final long serialVersionUID = 5193768656159186190L;
    
    private long qid;//问题id
    private String content;//问题内容
    private long hospitalDepartmentId;//属于哪个科室的问题
    private int type;//问题类型  1：单选    2：详细回答问题
    private long userid;//问题提出者
    private String remark;//备注
    
    public Question(){
        super();
    }
    public Question(String content,long hdid,int type,String remark){
        super();
        this.content=content;
        this.hospitalDepartmentId=hdid;
        this.type=type;
        this.remark=remark;
    }
    public Question(String content,long hdid,int type,long userid){
        super();
        this.content=content;
        this.hospitalDepartmentId=hdid;
        this.type=type;
        this.userid=userid;
    }
    public Question(String content,long hdid,int type,long userid,String remark){
        super();
        this.content=content;
        this.hospitalDepartmentId=hdid;
        this.type=type;
        this.userid=userid;
        this.remark=remark;
    }
    public Question(long qid,String content,long hdid,int type,String remark){
        super();
        this.qid=qid;
        this.content=content;
        this.hospitalDepartmentId=hdid;
        this.type=type;
        this.remark=remark;
    }
    public Question(long qid,String content,long hdid,int type,long userid,String remark){
        super();
        this.qid=qid;
        this.content=content;
        this.hospitalDepartmentId=hdid;
        this.type=type;
        this.userid=userid;
        this.remark=remark;
    }

    public long getQid() {
        return qid;
    }
    public void setQid(long qid) {
        this.qid = qid;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
    public long getHospitalDepartmentId() {
        return hospitalDepartmentId;
    }
    public void setHospitalDepartmentId(long hospitalDepartmentId) {
        this.hospitalDepartmentId = hospitalDepartmentId;
    }
    
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }

    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark;
    }
    @Override
    public String toString() {
        JSONObject question=new JSONObject();
        question.put("qid", this.getQid());
        question.put("content", this.getContent());
        question.put("hospitalDepartmentId", this.getHospitalDepartmentId());
        question.put("type", this.getType());
        question.put("userid",this.getUserid());
        question.put("remark", this.getRemark());
        return question.toString();
    }        
}
