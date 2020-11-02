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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        ExcelUtil excelUtil=new ExcelUtil();
        List<String> que =excelUtil.getColumnSet("src/main/resources/data/questions.xlsx", 1, 1);
        config.setQues(que);
        List<String> ans =excelUtil.getColumnSet("src/main/resources/data/answers.xlsx", 1, 1);
        config.setAns(ans);
        System.out.println("data import finished!");
    }
}


