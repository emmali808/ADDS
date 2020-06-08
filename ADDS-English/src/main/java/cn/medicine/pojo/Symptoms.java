package cn.medicine.pojo;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/5.
 */
public class Symptoms implements Serializable {

    private static final long serialVersionUID = -842295595351262558L;

    private long symid;
    private String symptoms;
    private String s_attribute;

    public Symptoms(){super();}
    public Symptoms(long symid, String symptoms, String s_attribute) {
        this.symid = symid;
        this.symptoms = symptoms;
        this.s_attribute = s_attribute;
    }
    public Symptoms(String symptoms, String s_attribute) {
        this.symptoms = symptoms;
        this.s_attribute = s_attribute;
    }
    public long getSymid() {
        return symid;
    }

    public void setSymid(long symid) {
        this.symid = symid;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getS_attribute() {
        return s_attribute;
    }

    public void setS_attribute(String s_attribute) {
        this.s_attribute = s_attribute;
    }

    @Override
    public String toString() {
        JSONObject symptom=new JSONObject();
        symptom.put("symptoms", symptoms);
        symptom.put("s_attribute", s_attribute);
        getstm();
        return symptom.toString();


    }
    public String getstm() {
        Map<String, String> map = new HashMap<>();
        Map<String, String> map1 = new HashMap<>();
        JSONObject symptom = new JSONObject();

        // diagnose1.put("category:5 ,diagnose", diagnose);
        String index,index1;
        index = ArraryList.getIndex().toString();
        map.put("index",index);
        map.put("name", symptoms);
        map.put("category", "2");
        //symptom.put(index, map);
        ArraryList.PointList(map);
        index1 = ArraryList.addIndex().toString();
        ArraryList.STList(ArraryList.getInitsource(),index);
        if (s_attribute.length() != 0) {
            map1.put("index",index1);
            map1.put("name", s_attribute);
            map1.put("category", "5");
            //symptom.put(index1, map1);
            ArraryList.PointList(map1);
            ArraryList.STList(index, index1);
            ArraryList.addIndex();
        }
        return null;

    }
}
