package com.java.adds.dao;

import com.java.adds.entity.DepartmentEntity;
import com.java.adds.entity.DoctorEntity;
import com.java.adds.mapper.DepartmentMapper;
import com.java.adds.mapper.DoctorMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class DepartmentDao {
    @Autowired
    DoctorMapper doctorMapper;

    @Autowired
    DepartmentMapper departmentMapper;

    /**ljy
     * 根据科室id获取科室下所有医生
     */
    public ArrayList<DoctorEntity> getDoctorsByDepartment(Long did)
    {
        return doctorMapper.getDoctorsByDepartment(did);
    }

    /**ljy
     * 获取所有科室
     */
    public ArrayList<DepartmentEntity> getAllDepartment()
    {
        return departmentMapper.getAllDepartment();
    }
}
