package cn.medicine.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.medicine.dao.IHospitalDepartmentDao;
import cn.medicine.pojo.HospitalDepartment;
import cn.medicine.service.IHospitalDepartmentService;
import cn.medicine.utils.MyException;

@Service
public class HospitalDepartmentService implements IHospitalDepartmentService{
    
    @Autowired
    @Qualifier("hospitalDepartmentDao")
    private IHospitalDepartmentDao hdDao;

    @Override
    public int add(HospitalDepartment hdepart) throws MyException {
        
        return hdDao.add(hdepart);
    }

    @Override
    public int update(HospitalDepartment hdepart) throws MyException {
        
        return hdDao.update(hdepart);
    }

    @Override
    public int delete(String name) throws MyException {
        
        return hdDao.delete(name);
    }

    @Override
    public HospitalDepartment get(String name) throws MyException {
        
        return hdDao.get(name);
    }

    @Override
    public List<HospitalDepartment> getAll() throws MyException {
        return hdDao.getAll();
    }

}
