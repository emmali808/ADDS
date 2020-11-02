package com.java.adds.controller;

import com.java.adds.service.ConsultService;
import com.java.adds.startup.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping(value="", produces = "application/json;charset=UTF-8")
public class ConsultController {
    @Autowired
    private ConsultService consultService;


    @PostMapping("/consult/online")
    public Object consultOnline(HttpServletRequest request, @RequestParam String msg)  {
        if (msg.length() > 0) {
            Config config=new Config();
            ExcelUtil excelUtil=new ExcelUtil();
            String s1=msg;
            List<String> columnSet = config.getQues();

//        List<String> sentences=new ArrayList();
//        sentences.add("Who should be my birth supporter ?");
//        sentences.add("What natural pain control can I use in labour ?");
//        sentences.add("What is a spinal injection like ?");
            List<Double> scores=new ArrayList<Double>();
            for(String s:columnSet){
                scores.add(consultService.calculateSimilarityScoreForGivenPair(s1,s,4));
            }
            double maxValue= Collections.max(scores);
            int index=scores.indexOf(maxValue);
            //System.out.println("result is "+columnSet.get(index));
            return config.getByIndexAns(index);
        }
        return null;
    }
}

