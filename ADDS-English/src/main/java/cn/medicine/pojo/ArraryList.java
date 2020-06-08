package cn.medicine.pojo;

import cn.medicine.service.impl.PGService;
import com.alibaba.fastjson.JSONObject;

import java.util.*;

/**
 * Created by Administrator on 2016/9/5.
 */
public class ArraryList {

    private int[] d;

    private static Integer index = 0;

    private static Integer timei = 0;

    private static String initsource;

    private static String source;

    private static String target;

    private static List<String> strs=new ArrayList<String>();

    private static List<Map<String, String>> strs1=new ArrayList<Map<String, String>>();

    public static List<String> strtime = new ArrayList<String>();

    public ArraryList() {
    }

    public ArraryList(int[] d) {
        this.d = d;
    }

    public int[] getD() {
        return d;
    }

    public void setD(int[] d) {
        this.d = d;
    }

    public static Integer getTimei() {
        return timei;
    }

    public static void setTimei(Integer timei) {
        ArraryList.timei = timei;
    }

    public static List<String> getStrtime() {
        return strtime;
    }

    public static void setStrtime(List<String> strtime) {
        ArraryList.strtime = strtime;
    }

    public static List<String> getStrs() {
        return strs;
    }

    public static void setStrs(List<String> strs) {
        ArraryList.strs = strs;
    }

    public static List<Map<String, String>> getStrs1() {
        return strs1;
    }

    public static void setStrs1(List<Map<String, String>> strs1) {
        ArraryList.strs1 = strs1;
    }

    public static String getInitsource() {
        return initsource;
    }

    public static void setInitsource(String initsource) {
        ArraryList.initsource = initsource;
    }

    public static String getSource() {
        return source;
    }

    public static void setSource(String source) {
        ArraryList.source = source;
    }

    public static String getTarget() {
        return target;
    }

    public static void setTarget(String target) {
        ArraryList.target = target;
    }

    public static List STList(String source,String target){
        String str1="Source:" + source;
        String str2="Target:" + target;
        strs.add(str1);
        strs.add(str2);
        return strs;
    }
    public static List PointList(Map<String, String> plist){

        strs1.add(plist);
        return strs1;
    }
    @Override
    public String toString() {
        JSONObject List=new JSONObject();
        List.put("d",d);
        return List.toString();
    }

    public static List Getlist(List diag1,List diag2){

        diag1.addAll(diag2);
        //System.out.println(diag1);
        return diag1;
    }

    public static Integer getIndex() {
        return index;
    }

    public static void setIndex(Integer index) {
        ArraryList.index = index;
    }

    public static Integer addIndex() {
        index++;
        return index;
    }

    public static Integer addtimeI() {
        timei++;
        return timei;
    }

    public static void initall(){

        setTimei(0);
        setStrtime(new ArrayList<String>());
        setIndex(0);
        setStrs(new ArrayList<String>());
        setStrs1(new ArrayList<Map<String, String>>());
        PGService.strtime=new ArrayList();
        PGService.strt=new ArrayList();
        PGService.strd=new ArrayList();
        PGService.strl=new ArrayList();
        PGService.strp=new ArrayList();
        PGService.strs=new ArrayList();
    }
}
