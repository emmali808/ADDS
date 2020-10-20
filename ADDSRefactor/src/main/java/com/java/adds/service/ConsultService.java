package com.java.adds.service;

import com.java.adds.startup.Config;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import java.util.List;


@Service
public class ConsultService {

    public String consultReuslt(String que){
        Config config=new Config();
        int num=config.getQuery().indexOf(que);
        System.out.println(num);
        List<String> index = new ArrayList<String>();
        index=config.getRes().get(num);
        Integer flag = index.lastIndexOf("0");
        if (flag == -1) {
            return "error";
        }
        return config.getDoc().get(flag);

    }

}
