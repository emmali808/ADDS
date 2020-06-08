package cn.medicine.dao;

import java.util.List;

import cn.medicine.pojo.HospitalDepartment;

public interface HospitalDepartmentMapper {
    
    /**
     * 
     * @Function:     add 
     * @Description:  增加医院科室  
     *                 <功能详细描述>
     *
     * @param hdepart
     * @return
     */
    public int add(HospitalDepartment hdepart);
    /**
     * 
     * @Function:     update 
     * @Description:   更新医院科室  
     *                 <功能详细描述>
     *
     * @param hdepart
     * @return
     */
    public int update(HospitalDepartment hdepart);
    /**
     * 
     * @Function:     deleteByName 
     * @Description:   通过医院科室名字删除该科室  
     *                 <功能详细描述>
     *
     * @param name
     * @return
     */
    public int deleteByName(String name);
    /**
     * 
     * @Function:     getByName 
     * @Description:   通过医院科室名字查询该科室 
     *                 <功能详细描述>
     *
     * @param name
     * @return
     */
    public HospitalDepartment getByName(String name);
    /**
     * 
     * @Function:     getAll 
     * @Description:  返回所有的科室 
     *                 <功能详细描述>
     *
     * @return
     */
    public List<HospitalDepartment> getAll();

}
