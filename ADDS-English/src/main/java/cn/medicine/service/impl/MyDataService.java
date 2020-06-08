package cn.medicine.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.medicine.dao.IMyDataDao;
import cn.medicine.pojo.MyData;
import cn.medicine.service.IMyDataService;
import cn.medicine.utils.MyException;
@Service
public class MyDataService implements IMyDataService{
    
    @Autowired
    @Qualifier("myDataDao")
    private IMyDataDao myDataDao;

    @Override
    public int add(MyData data) {
        try {
            return myDataDao.add(data);
        } catch (MyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public int update(MyData data) {
        try {
            return myDataDao.update(data);
        } catch (MyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    @Override
    public String get(String name) {
        try {
            String content=myDataDao.get(name);
            if(content!=null)
                return content;
            else 
                return "";
        } catch (MyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public int update(String name, String content) {
        MyData myData=new MyData(name,content);
        return update(myData);
    }

    @Override
    public int add(String name, String content) {
        MyData myData=new MyData(name,content);
        return add(myData);
    }

}
