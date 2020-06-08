package cn.medicine.pojo;

import com.alibaba.fastjson.JSONObject;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/5.
 */
public class Treatment implements Serializable {

    private static final long serialVersionUID = -4730541290898398993L;

    private long treatid;
    private String treatment;
    private String t_attribute;

    public Treatment(){super();}

    public Treatment(String t_attribute, String treatment, long treatid) {
        this.t_attribute = t_attribute;
        this.treatment = treatment;
        this.treatid = treatid;
    }
    public Treatment( String treatment,String t_attribute) {
        this.t_attribute = t_attribute;
        this.treatment = treatment;
    }
    public String getT_attribute() {
        return t_attribute;
    }

    public void setT_attribute(String t_attribute) {
        this.t_attribute = t_attribute;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public long getTreatid() {
        return treatid;
    }

    public void setTreatid(long treatid) {
        this.treatid = treatid;
    }

    @Override
    public String toString() {
        Map<String, String> map = new HashMap<>();
        Map<String, String> map1 = new HashMap<>();
        JSONObject treatment1 = new JSONObject();

        // diagnose1.put("category:5 ,diagnose", diagnose);
        String index,index1;
        index = ArraryList.getIndex().toString();
        map.put("index",index);
        map.put("name", treatment);
        map.put("category", "4");
        ArraryList.PointList(map);
        //treatment1.put(index, map);
        index1 = ArraryList.addIndex().toString();
        ArraryList.STList(index,ArraryList.getInitsource());
        if (t_attribute.length() != 0) {
            map1.put("index",index1);
            map1.put("name", t_attribute);
            map1.put("category", "5");
            //treatment1.put(index1,map1);
            ArraryList.PointList(map1);
            ArraryList.STList(index,index1);
            ArraryList.addIndex();
        }
        return null;
    }
}
