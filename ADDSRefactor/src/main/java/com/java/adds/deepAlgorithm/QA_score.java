package com.java.adds.deepAlgorithm;


import org.springframework.stereotype.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class QA_score {
    public static String rank_idx(String[] query_text,String[] doc_text) {
        List<String> command = new ArrayList<String>();
        command.add("python");
        command.add("main.py");
        for(int i=0;i<query_text.length;i++){
            command.add("\""+query_text[i]+"\"");
        }
        command.add("and");
        for(int i=0;i<doc_text.length;i++){
            command.add("\""+doc_text[i]+"\"");
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

    public String do_QA_research(String[] query,String[] doc)
    {
        String result = rank_idx(query,doc);
        return result;
    }
}
