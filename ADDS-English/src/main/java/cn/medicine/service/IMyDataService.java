package cn.medicine.service;

import cn.medicine.pojo.MyData;
import cn.medicine.utils.MyException;

public interface IMyDataService {
    
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
     * @Function:     add 
     * @Description:   增加  
     *                 <功能详细描述>
     *
     * @param name
     * @param content
     * @return
     */
    public int add(String name,String content);
    
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
     * @Function:     update 
     * @Description:  更新  
     *                 <功能详细描述>
     *
     * @param name
     * @param content
     * @return
     */
    public int update(String name,String content);
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
