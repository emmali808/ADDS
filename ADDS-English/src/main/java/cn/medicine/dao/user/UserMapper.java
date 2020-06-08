package cn.medicine.dao.user;

import cn.medicine.pojo.User;
import cn.medicine.utils.MyException;

import java.util.List;


public interface UserMapper {
    /**
     * 
     * @Function:     login 
     * @Description:   用户登录  
     *                 <功能详细描述>
     *
     * @param username
     * @param password
     * @return
     */
    public User login(String username, String password);
    /**
    *
    * @Function: add
    * @Description: 新增用户，state默认为true
    *
    *
    * @param user 用户
    */

   void add(User user);
   /**
   *
   * @Function: isDuplicate
   * @Description: 判断用户名是否重复
   *
   * @param userName 用户名
   * @return 1重复 0不重复
   */
  public Integer isDuplicate(String userName);
  
  /**
  *
  * @Function: delete
  * @Description: 删除用户
  *
  *
  * @param id 用户id
  */
   void delete(long id);
   
   /**
   *
   * @Function: updateState
   * @Description: 修改用户状态
   *
   * @param userId 用户id
   * @param state 状态
   */
  void updateState(long userId, boolean state);
   /**
    * 
    * @Function:     update 
    * @Description:   更新用户信息  
    *                 <功能详细描述>
    *
    * @param user
    */
   void update(User user);
   /**
    * 
    * @Function:     getByUsername 
    * @Description:   通过用户名查找用户  
    *                 <功能详细描述>
    *
    * @param username
    * @return
    */
   User getByUsername(String username);
    /**
     * 获得所有用户
     * @return
     */
    List<User> getAllUser();
    /**
     * 
     * @Function:     getAllUser 
     * @Description:   分页，获取所有用户
     *                 <功能详细描述>
     *
     * @param start
     * @param pagesize
     * @return
     */
    public List<User> getAllUserByPage(int start,int pagesize);
    /**
     * 
     * @Function:     getAllUserNumber 
     * @Description:   <一句话功能描述>  
     *                 <功能详细描述>
     *
     * @return
     */
    public int getAllUserNumber();
    /**
     * 
     * @Function:     getPatients 
     * @Description:   获取所有的病人
     *                 <功能详细描述>
     *
     * @return
     */
    public List<User> getPatients();
    /**
     * 
     * @Function:     getDoctors 
     * @Description:   获取所有的医生  
     *                 <功能详细描述>
     *
     * @return
     */
    public List<User> getDoctors();
    /**
     * @Function:     getAllDoctorNotPass
     * @Description:   获得所有待审核的医生
     *
     * @return
     */
    public List<User> getAllDoctorNotPass();
    /**
     * 
     * @Function:     getExperts 
     * @Description:   获取所有的专家
     *                 <功能详细描述>
     *
     * @return
     */
    public List<User> getExperts();


    /**
     *
     * @Function:     getDoctorNotCheck 
     * @Description:   审核未审核医生
     *                 <功能详细描述>
     *
     * @return
     */
    public int getDoctorNotCheck(String login_name);
}
