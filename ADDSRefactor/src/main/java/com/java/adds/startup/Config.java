package com.java.adds.startup;

import java.util.List;

public class Config {
    public static List<String> ques;

    public static List<String> getQues() {
        return ques;
    }

    public static void setQues(List<String> ques) {
        Config.ques = ques;
    }

    public static List<String> getAns() {
        return ans;
    }
    public static String getByIndexAns(int index){
        return ans.get(index);
    }

    public static void setAns(List<String> ans) {
        Config.ans = ans;
    }

    public static List<String> ans;



}
