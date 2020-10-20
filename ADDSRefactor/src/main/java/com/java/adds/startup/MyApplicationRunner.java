package com.java.adds.startup;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class MyApplicationRunner implements ApplicationRunner {
    private Config config=new Config();
    private   String rank_idx(List<String> query_text,List<String> doc_text) {
        List<String> command = new ArrayList<String>();
        command.add("python");
        command.add("main.py");
        for(int i=0;i<query_text.size();i++){
            command.add("\""+query_text.get(i)+"\"");
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
    private void getIndex(String str){

        String regex="(\\[\\[.*\\]\\])\\,";
        String result;
        Pattern pattern=Pattern.compile(regex);
        Matcher matcher=pattern.matcher(str);
        String demosub=null;
        if(matcher.find()) {
            result = matcher.group(1);


            String demo = result.replace(" ", "");
            demosub = demo.substring(1, demo.length() - 1);
//            System.out.println(demosub);

        }
        String regex1="(\\[.*\\]),(\\[.*\\]),(\\[.*\\])";
        Pattern pattern1=Pattern.compile(regex1);
        Matcher matcher1=pattern1.matcher(demosub);
        String result1;
        String result2;
        String result3;
        if(matcher1.find()) {
            result1 = matcher1.group(1);
            result2 = matcher1.group(2);
            result3 = matcher1.group(3);


            String demo1 = result1.replace(" ", "");
            String demosub1 = demo1.substring(1, demo1.length() - 1);
            System.out.println(demosub1);
            String demoArray1[] = demosub1.split(",");
            config.setDemoList1(Arrays.asList(demoArray1));

            String demo2 = result2.replace(" ", "");
            String demosub2 = demo2.substring(1, demo2.length() - 1);
            System.out.println(demosub2);
            String demoArray2[] = demosub2.split(",");
            config.setDemoList2(Arrays.asList(demoArray2));

            String demo3 = result3.replace(" ", "");
            String demosub3 = demo3.substring(1, demo3.length() - 1);
            System.out.println(demosub3);
            String demoArray3[] = demosub1.split(",");
            config.setDemoList3(Arrays.asList(demoArray3));
            List<List<String>> res1=Arrays.asList(config.getDemoList1(),config.getDemoList2(),config.getDemoList3());
            config.setRes(res1);

        }
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
//        ExcelUtil excelUtil=new ExcelUtil();
//        List<String> doc =excelUtil.getColumnSet("src/main/resources/data/ans.xlsx", 1, 1);
//        Config config=new Config();
//        config.setAns(doc);
        List<String> q=new ArrayList();;
        q.add("What is the usual length of treatment for Anticoagulants ?");
        q.add("When are anticoagulants used ?");
        q.add("What are the heart rhythm problems that may occur?");
        config.setQuery(q);
        config.setDoc(Arrays.asList("This depends on what you are taking an anticoagulant for . Sometimes you only need to take it for a few weeks ( after surgery ) or months ( DVT ) . On the other hand , some people need to take an anticoagulant for the rest of their lives ( for example , people with atrial fibrillation or a mechanical heart valve ) . Your doctor will advise .",
                "Anticoagulants are prescribed if you already have a blood clot , the most common cause being a deep vein thrombosis ( DVT ) and/or a clot on the lung , called a pulmonary embolus ( PE ) . in these cases , they prevent the clot from becoming bigger . The other reason they are used is if you are at risk of having a blood clot ( prevention ) . Examples of people who are at risk of having a blood clot include anyone who has : A fast irregular heartbeat ( atrial fibrillation ) . Having atrial fibrillation is one of the most common reasons for taking an anticoagulant . A mechanical heart valve . Infection of the inside of the heart ( endocarditis ) . A valve in the heart which does not open fully ( mitral stenosis ) . Certain blood disorders that affect how your blood clots ( inherited thrombophilia , antiphospholipid syndrome ) . Had surgery to replace a hip or knee .",
                "The heart 's regular rhythm is maintained by a conducting system that carries electrical signals to different parts of the heart muscle to make them squeeze ( contract ) at the right time . Various things can disrupt this electrical conduction . The effects will depend upon which part of the heart muscle is affected . Heart rhythm problems are called arrhythmias . Many people have the experience that their heart is missing a beat . In fact , it is more often an extra beat with a slightly longer pause after it , and it is almost always completely normal . Sometimes people become more aware of extra beats when they are feeling anxious or stressed , and then the worry about the extra heartbeats can make them feel worse . However , it is quite normal and does not indicate a problem with the heart . This is a normal variation in heart rate which occurs mostly in young people . The heart rate speeds up as you breathe in and slows again as you breathe out . The most common arrhythmia which causes problems is atrial fibrillation . The atria quiver , rather than beating regularly , and the ventricles still pump normally , although not in a regular rhythm . Sometimes the ventricles beat too fast , and medication such as beta - blockers helps to slow it down . Atrial fibrillation can be intermittent ( paroxysmal ) or permanent . Sometimes it can be reversed using an electric shock ( cardioversion ) . If it is permanent , the quivering atrial muscle can lead to the formation of small blood clots . If these leave the heart they can cause a stroke , so it is recommended that people who have atrial fibrillation take an anticoagulant , such as warfarin , or one of the newer anticoagulants , to stop these small clots forming . If the heart muscle is damaged , most commonly by a heart attack ( myocardial infarction ) , then the electrical signals will not get through normally : Ventricular tachycardia occurs when the heart is beating very fast and not very efficiently . It can often lead to ventricular fibrillation , which means that the muscle quivers rather than beating . It can sometimes be reversed with a defibrillator ."));

        String result = rank_idx(config.getQuery(),config.getDoc());
        if(result!="error") {
            getIndex(result);
            System.out.println("Search finish!");
        }
    }
}


