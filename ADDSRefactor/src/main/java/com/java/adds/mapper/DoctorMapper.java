package com.java.adds.mapper;


import com.java.adds.entity.DoctorEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface DoctorMapper {

    /**ljy
     * 管理员获取所有医生信息
     */
    public ArrayList<DoctorEntity> getAllDoctors();

    /**ljy
     * 根据科室id获取科室下所有医生
     */
    public ArrayList<DoctorEntity> getDoctorsByDepartment(@Param("did") Long did);

    /**ljy
     * 根据id获取医生信息
     */
    public DoctorEntity getDoctorById(@Param("id") Long doctorId);
}
