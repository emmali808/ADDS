package cn.medicine.pojo;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;import java.lang.Override;import java.lang.String;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName: 个性化图谱
 * @Description: TODO
 * @Function List: TODO
 * @author: wms
 * @version:
 * @Date: 2016/8/25
 * @History: //历史修改记录
 * <author>  // 修改人
 * <time> //修改时间
 * <version> //版本
 * <desc> // 描述修改内容
 */
public class Graph implements Serializable {

    private static final long serialVersionUID = -6853409355503220350L;

    private long gid; //id
    private String g_time; //诊断时间
    private String g_diagnose; //诊断结果
    private String g_symptom; //症状
    private String g_treatment; //治疗方法
    private String g_labtest; //病人检测结果
    private String g_previousMH; //历史病例
    private String pgID; //病人身份证号

    public Graph(){super();}

    public Graph(long gid, String pgID, String g_previousMH, String g_labtest, String g_treatment, String g_symptom, String g_diagnose, String g_time) {
        this.gid = gid;
        this.pgID = pgID;
        this.g_previousMH = g_previousMH;
        this.g_labtest = g_labtest;
        this.g_treatment = g_treatment;
        this.g_symptom = g_symptom;
        this.g_diagnose = g_diagnose;
        this.g_time = g_time;
    }

    public void setGid(long gid) {
        this.gid = gid;
    }

    public void setG_time(String g_time) {
        this.g_time = g_time;
    }

    public void setG_diagnose(String g_diagnose) {
        this.g_diagnose = g_diagnose;
    }

    public void setG_symptom(String g_symptom) {
        this.g_symptom = g_symptom;
    }

    public void setG_treatment(String g_treatment) {
        this.g_treatment = g_treatment;
    }

    public void setG_labtest(String g_labtest) { this.g_labtest = g_labtest; }
    public void setG_previousMH(String g_previousMH) {
        this.g_previousMH = g_previousMH;
    }

    public void setPgID(String pgID) {
        this.pgID = pgID;
    }

    public String getPgID() {
        return pgID;
    }

    public String getG_previousMH() {
        return g_previousMH;
    }

    public String getG_labtest() { return g_labtest; }

    public String getG_treatment() {
        return g_treatment;
    }

    public String getG_symptom() {
        return g_symptom;
    }

    public String getG_diagnose() {
        return g_diagnose;
    }

    public String getG_time() {
        return g_time;
    }

    public long getGid() {
        return gid;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }



    @Override
    public String toString() {
        JSONObject graph=new JSONObject();
        graph.put("gid", gid);
        graph.put(" g_time", g_time);
        graph.put("g_diagnose", g_diagnose);
        graph.put("g_symptom", g_symptom);
        graph.put("g_treatment", g_treatment);
        graph.put("g_labtest", g_labtest);
        graph.put("g_previousMH", g_previousMH);
        graph.put("pgID", pgID);
        return graph.toString();
    }
}