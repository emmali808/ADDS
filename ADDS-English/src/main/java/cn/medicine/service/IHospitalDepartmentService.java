package cn.medicine.service;

import java.util.List;

import cn.medicine.pojo.HospitalDepartment;
import cn.medicine.utils.MyException;

public interface IHospitalDepartmentService {
    
    /**
     * 
     * @Function:     add 
     * @Description:  增加医院科室  
     *                 <功能详细描述>
     *
     * @param hdepart
     * @return
     */
    public int add(HospitalDepartment hdepart) throws MyException;
    /**
     * 
     * @Function:     update 
     * @Description:   更新医院科室  
     *                 <功能详细描述>
     *
     * @param hdepart
     * @return
     */
    public int update(HospitalDepartment hdepart) throws MyException;
    /**
     * 
     * @Function:     delete 
     * @Description:   通过医院科室名字删除该科室  
     *                 <功能详细描述>
     *
     * @param name
     * @return
     */
    public int delete(String name) throws MyException;
    /**
     * 
     * @Function:     get 
     * @Description:   通过医院科室名字查询该科室 
     *                 <功能详细描述>
     *
     * @param name
     * @return
     */
    public HospitalDepartment get(String name) throws MyException;
    /**
     * 
     * @Function:     getAll 
     * @Description:  返回所有的科室 
     *                 <功能详细描述>
     *
     * @return
     */
    public List<HospitalDepartment> getAll() throws MyException;

}
