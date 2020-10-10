package com.java.adds.mapper;


import com.java.adds.entity.DepartmentEntity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface DepartmentMapper {
    /**ljy
     * 获取所有科室
     */
    public ArrayList<DepartmentEntity> getAllDepartment();
}
