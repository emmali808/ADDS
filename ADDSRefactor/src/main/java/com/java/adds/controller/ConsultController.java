package com.java.adds.controller;

import com.java.adds.service.ConsultService;
import com.java.adds.startup.*;
import org.openrdf.query.algebra.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import slib.utils.ex.SLIB_Exception;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
            String s1=msg;
            List<String> columnSet = config.getAns();

            List<Double> scores=new ArrayList<Double>();
            for(String s:columnSet){
                scores.add(consultService.calculateSimilarityByWordNet(s1,s));
            }
            consultService.setAns(columnSet,scores,10);
            columnSet = config.getAns();
            List<Double> scores2=new ArrayList<Double>();
            for(String s:columnSet){
                scores2.add(consultService.calculateSimilarityByQGram(s1,s));
            }
            consultService.setAns(columnSet,scores2,5);

            String [] query=new String[1];
            query[0]=msg;
            List<String> doc1=config.getAns();

            String result = consultService.rank_idx(query,doc1);
            if(result!="error") {
                List<String> index = new ArrayList<String>();
                index = consultService.getIndex(result);
                Integer flag = index.lastIndexOf("0");
                if (flag == -1) {
                    return "error";
                }
                return doc1.get(flag);
            }
            else {
                return "rank_idx_error";
            }
        }
        return null;
    }
}
