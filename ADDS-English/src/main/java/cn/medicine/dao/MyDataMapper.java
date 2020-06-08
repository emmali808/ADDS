package cn.medicine.dao;

import cn.medicine.pojo.MyData;

public interface MyDataMapper {
    /**
     * 
     * @Function:     add 
     * @Description:   增加 
     *                 <功能详细描述>
     *
     * @param data
     * @return
     */
    public int add(MyData data);
    
    /**
     * 
     * @Function:     update 
     * @Description:   更新 
     *                 <功能详细描述>
     *
     * @param data
     * @return
     */
    public int update(MyData data);
   
    /**
     * 
     * @Function:     get 
     * @Description:   查找
     *                 <功能详细描述>
     *
     * @param name
     * @return
     */
    public String get(String name);

}
