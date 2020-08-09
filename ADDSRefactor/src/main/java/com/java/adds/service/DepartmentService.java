package com.java.adds.service;


import com.java.adds.dao.DepartmentDao;
import com.java.adds.entity.DepartmentEntity;
import com.java.adds.entity.DoctorEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class DepartmentService {
    @Autowired
    DepartmentDao departmentDao;

    /**ljy
     * 根据科室id获取科室下所有医生
     */
    public ArrayList<DoctorEntity> getDoctorsByDepartment(Long did)
    {
        return departmentDao.getDoctorsByDepartment(did);
    }

    /**ljy
     * 获取所有科室
     */
    public ArrayList<DepartmentEntity> getAllDepartment()
    {
        return departmentDao.getAllDepartment();
    }
}
