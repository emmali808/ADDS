package cn.medicine.pojo;

import cn.medicine.service.impl.PGService;
import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016/9/3.
 */
public class Diagnose  implements Serializable {


    private static final long serialVersionUID = 7976597501708743062L;
    
    private long diagid; //id
    private String diagnose; //诊断结果

    public Diagnose() {
        super();
    }
    public Diagnose(long diagid, String diagnose) {
        this.diagid = diagid;
        this.diagnose = diagnose;
    }

    public long getDiagid() {
        return diagid;
    }

    public String getDiagnose() {
        return diagnose;
    }


    public void setDiagid(long diagid) {
        this.diagid = diagid;
    }

    public void setDiagnose(String diagnose) {
        this.diagnose = diagnose;
    }

    @Override
    public String toString(){


        Map<String,String> map1 = new HashMap<>();
        Map<String,String> map = new HashMap<>();
        JSONObject diagnose1=new JSONObject();

       // diagnose1.put("category:5 ,diagnose", diagnose);
        String index,time;
        Integer inti;
        inti = ArraryList.getTimei();
        time = ArraryList.getStrtime().get(inti);

        map1.put("time",time);
        ArraryList.addtimeI();
        ArraryList.PointList(map1);
        index =ArraryList.getIndex().toString();
        if(diagnose.length()!=0){
            map.put("index",index);
            map.put("name",diagnose);
            map.put("category","0");
            //diagnose1.put(index,map);
            ArraryList.setInitsource(index);
            ArraryList.addIndex();
            ArraryList.PointList(map);
        }
        return null;
    }
}
