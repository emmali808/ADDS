package com.java.adds.startup;

import java.util.List;

public class Config {
    public static List<String> getAns() {
        return ans;
    }

    public static void setAns(List<String> ans) {
        Config.ans = ans;
    }

    public static List<String> ans;

    public static List<String> demoList1;

    public static List<String> getDemoList1() {
        return demoList1;
    }

    public static void setDemoList1(List<String> demoList1) {
        Config.demoList1 = demoList1;
    }

    public static List<String> getDemoList2() {
        return demoList2;
    }

    public static void setDemoList2(List<String> demoList2) {
        Config.demoList2 = demoList2;
    }

    public static List<String> getDemoList3() {
        return demoList3;
    }

    public static void setDemoList3(List<String> demoList3) {
        Config.demoList3 = demoList3;
    }

    public static List<String> demoList2;
    public static List<String> demoList3;

    public static List<String> getQuery() {
        return query;
    }

    public static void setQuery(List<String> query) {
        Config.query = query;
    }




    public static List<String> query;

    public static List<String> getDoc() {
        return doc;
    }

    public static void setDoc(List<String> doc) {
        Config.doc = doc;
    }

    public static List<String> doc;

    public static List<List<String>> getRes() {
        return res;
    }

    public static void setRes(List<List<String>> res) {
        Config.res = res;
    }

    public static List<List<String>> res;



}
