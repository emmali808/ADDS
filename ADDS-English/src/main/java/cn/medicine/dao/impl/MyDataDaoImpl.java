package cn.medicine.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.medicine.dao.IMyDataDao;
import cn.medicine.dao.MyDataMapper;
import cn.medicine.pojo.MyData;
import cn.medicine.utils.MyException;

public class MyDataDaoImpl implements IMyDataDao{
    
    private final static Logger logger= LogManager.getLogger(MyDataDaoImpl.class);
    private boolean isDebugEnable=logger.isDebugEnabled();

    private boolean isErrorEnable=logger.isErrorEnabled();
    
    private MyDataMapper myDataMapper;
    
    public MyDataDaoImpl(){
        super();
    }
    
    public MyDataDaoImpl(MyDataMapper myDataMapper){
        super();
        this.myDataMapper=myDataMapper;
    }

    @Override
    public int add(MyData data) throws MyException {
        try{
            logger.info("add方法："+data.toString());
            myDataMapper.add(data);
            return 1;
        }catch(Exception e){
            if(isErrorEnable)
                logger.error("MyData添加异常("+data.toString()+"):"+e.getMessage());
            throw new MyException(e.getMessage());
        }      
    }

    @Override
    public int update(MyData data) throws MyException {
        try{
            logger.info("update方法："+data.toString());
            myDataMapper.update(data);
            return 1;
        }catch(Exception e){
            if(isErrorEnable)
                logger.error("MyData更新异常("+data.toString()+"):"+e.getMessage());
            throw new MyException(e.getMessage());
            
        }
    }

    @Override
    public String get(String name) throws MyException {
        try{
            String content=myDataMapper.get(name);
            return content;
        }catch(Exception e){
            if(isErrorEnable){
                logger.error("MyData查询异常(name:"+name+")："+e.getMessage());
            }
            throw new MyException(e.getMessage());
        }
    }
}
