package com.java.adds.controller;


import com.java.adds.entity.DepartmentEntity;
import com.java.adds.entity.DoctorEntity;
import com.java.adds.service.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@RequestMapping("department")
public class DepartmentController {
    @Autowired
    DepartmentService departmentService;

    /**ljy
     * 根据科室id获取科室下所有医生
     */
    @GetMapping("{did}/doctor")
    public ArrayList<DoctorEntity> getDoctorsByDepartment(@PathVariable Long did)
    {
        return departmentService.getDoctorsByDepartment(did);
    }

    /**ljy
     * 获取所有科室
     */
    @GetMapping("")
    public ArrayList<DepartmentEntity> getAllDepartment()
    {
        return departmentService.getAllDepartment();
    }
}
