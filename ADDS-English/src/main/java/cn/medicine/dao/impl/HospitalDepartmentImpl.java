package cn.medicine.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import cn.medicine.dao.HospitalDepartmentMapper;
import cn.medicine.dao.IHospitalDepartmentDao;
import cn.medicine.pojo.HospitalDepartment;
import cn.medicine.utils.MyException;

public class HospitalDepartmentImpl implements IHospitalDepartmentDao{
    
    private final static Logger logger= LogManager.getLogger(HospitalDepartmentImpl.class);
    private boolean isDebugEnable=logger.isDebugEnabled();

    private boolean isErrorEnable=logger.isErrorEnabled();
    
    private HospitalDepartmentMapper hdMapper;
    
    public HospitalDepartmentImpl(){
        super();
    }
    
    public HospitalDepartmentImpl(HospitalDepartmentMapper hospitalDepartmentMapper){
        super();
        this.hdMapper=hospitalDepartmentMapper;
    }

    @Override
    public int add(HospitalDepartment hdepart) throws MyException {
        try{
            int result=hdMapper.add(hdepart);
            return result;
        }catch(Exception e){
            if(isErrorEnable)
                logger.error("添加异常："+e.getMessage());
            throw new MyException(e.getMessage());
        }
    }

    @Override
    public int update(HospitalDepartment hdepart) throws MyException {
        try{
            return hdMapper.update(hdepart);
        }catch(Exception e){
            if(isErrorEnable)
                logger.error("更新异常："+e.getMessage());
            throw new MyException(e.getMessage());
        }
    }

    @Override
    public int delete(String name) throws MyException {
        try{
            return hdMapper.deleteByName(name);
        }catch(Exception e){
            if(isErrorEnable)
                logger.error("删除异常(name:"+name+")"+e.getMessage());
            throw new MyException(e.getMessage());
        }
    }

    @Override
    public HospitalDepartment get(String name) throws MyException {
        try{
            return hdMapper.getByName(name);
        }catch(Exception e){
            if(isErrorEnable)
                logger.error("get方法查询异常(name:"+name+")"+e.getMessage());
            throw new MyException(e.getMessage());
        }
    }

    @Override
    public List<HospitalDepartment> getAll() throws MyException {
        try{
            return hdMapper.getAll();
        }catch(Exception e){
            if(isErrorEnable)
                logger.error("getAll方法查询异常:"+e.getMessage());
            throw new MyException(e.getMessage());
        }
    }

}
