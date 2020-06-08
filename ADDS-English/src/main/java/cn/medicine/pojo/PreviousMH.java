package cn.medicine.pojo;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/5.
 */
public class PreviousMH implements Serializable {

    private static final long serialVersionUID = -6881607238565972662L;

    private long preid;
    private String previousMH;
    private String pre_attribute;

    public PreviousMH(){super();}

    public PreviousMH(String previousMH, String pre_attribute, long preid) {
        this.previousMH = previousMH;
        this.pre_attribute = pre_attribute;
        this.preid = preid;
    }
    public PreviousMH(String previousMH, String pre_attribute) {
        this.previousMH = previousMH;
        this.pre_attribute = pre_attribute;
    }
    public long getPreid() {
        return preid;
    }

    public void setPreid(long preid) {
        this.preid = preid;
    }

    public String getPreviousMH() {
        return previousMH;
    }

    public void setPreviousMH(String previousMH) {
        this.previousMH = previousMH;
    }

    public String getPre_attribute() {
        return pre_attribute;
    }

    public void setPre_attribute(String pre_attribute) {
        this.pre_attribute = pre_attribute;
    }

    @Override
    public String toString() {
        Map<String, String> map = new HashMap<>();
        Map<String, String> map1 = new HashMap<>();
        JSONObject pmh = new JSONObject();

        // diagnose1.put("category:5 ,diagnose", diagnose);
        String index,index1;
        index = ArraryList.getIndex().toString();
        map.put("index",index);
        map.put("name", previousMH);
        map.put("category", "1");
        //pmh.put(index, map);
        ArraryList.PointList(map);
        index1 = ArraryList.addIndex().toString();
        ArraryList.STList(index,ArraryList.getInitsource());
        ArraryList.addIndex();
        if (pre_attribute.length() != 0) {
            map1.put("index",index1);
            map1.put("name", pre_attribute);
            map1.put("category", "5");
            //pmh.put(index1,map1);
            ArraryList.PointList(map1);
            ArraryList.STList(index,index1);
            ArraryList.addIndex();
        }
        return null;
    }
}
