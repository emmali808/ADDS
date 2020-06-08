package cn.medicine.pojo;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/12.
 */
public class LabTest  implements Serializable {

    private static final long serialVersionUID = 8218597423736741730L;

    private long labid;
    private String labtest;
    private String lab_attribute;

    public LabTest(){super();}
    public LabTest(long labid, String labtest, String lab_attribute) {
        this.labid = labid;
        this.labtest = labtest;
        this.lab_attribute = lab_attribute;
    }
    public LabTest( String labtest, String lab_attribute) {
        this.labtest = labtest;
        this.lab_attribute = lab_attribute;
    }
    public long getLabid() {
        return labid;
    }

    public void setLabid(long labid) {
        this.labid = labid;
    }

    public String getLabtest() {
        return labtest;
    }

    public void setLabtest(String labtest) {
        this.labtest = labtest;
    }

    public String getLab_attribute() {
        return lab_attribute;
    }

    public void setLab_attribute(String lab_attribute) {
        this.lab_attribute = lab_attribute;
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
        map.put("name", labtest);
        map.put("category", "3");
        //treatment1.put(index, map);
        ArraryList.PointList(map);
        index1 = ArraryList.addIndex().toString();
        ArraryList.STList(index,ArraryList.getInitsource());
        if (lab_attribute.length() != 0) {
            map1.put("index",index1);
            map1.put("name", lab_attribute);
            map1.put("category", "5");
            //treatment1.put(index1,map1);
            ArraryList.PointList(map1);
            ArraryList.STList(index,index1);
            ArraryList.addIndex();
        }
        return null;
    }
}
