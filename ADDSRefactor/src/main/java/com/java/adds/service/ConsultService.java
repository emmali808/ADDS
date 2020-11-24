package com.java.adds.service;

import com.java.adds.startup.Config;
import com.java.adds.startup.FileUtil;
import com.java.adds.combinedOntologyMethod.CombinedOntologyMethod;
import com.java.adds.startup.SortN;
import org.simmetrics.StringMetric;
import org.simmetrics.metrics.StringMetrics;
import org.springframework.stereotype.Service;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class ConsultService {
    List<String> stopWordsList;
    FileUtil fileUtil;

    public static HashMap<String, Double> paragraphVecHash;

    public ConsultService() throws IOException {
        fileUtil = new FileUtil();
        stopWordsList = fileUtil.readStopWordsList();
    }

    public double calculateSimilarityByQGram(String s1, String s2) {
        double similarityScore = 0;


        String preprocessedS1 = fileUtil.removeStopWordsFromSentence(s1, stopWordsList);
        String preprocessedS2 = fileUtil.removeStopWordsFromSentence(s2, stopWordsList);

        StringMetric metric = StringMetrics.qGramsDistance();
        similarityScore = metric.compare(preprocessedS1, preprocessedS2);

        return similarityScore;
    }
    public double calculateSimilarityByWordNet(String s1, String s2) {
        double similarityScore = 0;
        CombinedOntologyMethod measureOfWordNet = new CombinedOntologyMethod(stopWordsList);
        similarityScore = measureOfWordNet.getSimilarityForWordnet(s1, s2);
        return similarityScore;
    }
    public void setAns(List<String> columnSet, List<Double> scores, Integer n){
        Config config=new Config();
        SortN sortN=new SortN();
        List<Integer> indexs=new ArrayList<Integer>();
        indexs=sortN.sortIndex(scores);
        List<String> ans=new ArrayList<String>();
        for(Integer index=0;index<n;index++){
            ans.add(columnSet.get(indexs.get(index)));
        }
        config.setAns(ans);
    }
    public    String rank_idx(String[] query_text,List<String> doc_text) {
        List<String> command = new ArrayList<String>();
        command.add("python");
        command.add("main.py");
        for(int i=0;i<query_text.length;i++){
            command.add("\""+query_text[i]+"\"");
        }
        command.add("and");
        for(int i=0;i<doc_text.size();i++){
            command.add("\""+doc_text.get(i)+"\"");
        }
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            File dir = new File("/home/lf/桌面/qa_project/");
            builder.directory(dir);
            Process proc = builder.start();

            BufferedReader in = new BufferedReader((new InputStreamReader((proc.getInputStream()))));
            String result = in.readLine();
            in.close();

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "error";
    }
    public List<String> getIndex(String str){
//        String str="([[1, 0]], [[14.567728042602539, 21.839862823486328], [-23.143768310546875, -27.353540420532227], [5.182803630828857, 16.994691848754883]])";
        String regex="\\[(\\[.*\\])\\]\\,";
        String result;
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(str);
        if(matcher.find()){
            result=matcher.group(1);


            String demo = result.replace(" ","");
            String demosub = demo.substring(1,demo.length()-1);
            String demoArray[] = demosub.split(",");
            List<String> demoList = Arrays.asList(demoArray);
//            List<Integer> index=new ArrayList<Integer>();
//            for (String str1:demoList) {
//
//                index.add(Integer.valueOf(str1));
//            }
            return demoList;
        }
        return null;
    }

}
