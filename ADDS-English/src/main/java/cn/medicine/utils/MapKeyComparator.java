package cn.medicine.utils;

import java.util.Comparator;
import java.util.Date;

class MapKeyComparator implements Comparator<Date>{

    @Override
    public int compare(Date o1, Date o2) {
        // TODO Auto-generated method stub
        return o1.compareTo(o2);
    }
    
}
