package cn.medicine.pojo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
/**
 * 
 * @ClassName:HospitalDepartment
 * @Description: 医院科室
 * @Function List:TODO 主要函数及其功能
 *
 * @author   ztxu
 * @version  
 * @Date	 2016-8-13下午4:26:44
 *
 * @History:// 历史修改记录 
 * <author>  // 修改人
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述修改内容
 */
public class HospitalDepartment implements Serializable{
    private static final long serialVersionUID = 4098403586557367886L;
    
    private long hdid;
    private String name;//科室名字
    private String description;//科室描述
    private String remark;//备注
    
    public HospitalDepartment(){
        super();
    }
    
    public HospitalDepartment(String name,String description,String remark){
        super();
        this.name=name;
        this.description=description;
        this.remark=remark;
    }
    
    public HospitalDepartment(long hdid,String name,String description,String remark){
        super();
        this.hdid=hdid;
        this.name=name;
        this.description=description;
        this.remark=remark;
    }

    public long getHdid() {
        return hdid;
    }

    public void setHpid(long hdid) {
        this.hdid = hdid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        JSONObject hd=new JSONObject();
        hd.put("hdid", this.getHdid());
        hd.put("name", this.getName());
        hd.put("description", this.getDescription());
        hd.put("remark", this.getRemark());
        return hd.toString();
    }
    
    

}
