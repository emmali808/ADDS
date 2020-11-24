package com.java.adds.startup;

import java.util.*;

public class SortN {
    public List<Integer> sortIndex(List<Double> numbers){

        List<pair> list = new ArrayList();
        for(int i=0;i<numbers.size();i++){
            list.add(new pair(i,numbers.get(i)));
        }
        Collections.sort(list, new Comparator<pair>() {
            @Override
            public int compare(pair o1, pair o2) {
                if(o1.value-o2.value>0) {
                    return 1;
                }
                else {
                    return 0;
                }
            }
        });
        List<Integer> indexs = new ArrayList<>();
        List<Double> values = new ArrayList<>();
        for(pair element : list){
            indexs.add(element.index);
            values.add(element.value);
        }
        return indexs;
    }
}
