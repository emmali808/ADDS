package cn.medicine.service;

import org.springframework.stereotype.Service;

import cn.medicine.pojo.User;
import cn.medicine.utils.MyException;

import java.util.List;

@Service
public interface IUserService {
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
     * @Function:     addUser 
     * @Description:   增加新用户 
     *                 <功能详细描述>
     *
     * @param user
     * @return
     */
    public int add(User user) throws MyException;
    /**
     * 
     * @Function:     updateUser 
     * @Description:   更新用户信息 
     *                 <功能详细描述>
     *
     * @param user
     * @return
     */
    public long update(User user) throws MyException;
    /**
     * 
     * @Function:     deleteUser 
     * @Description:   删除用户  
     *                 <功能详细描述>
     *
     * @param id
     * @return
     */
    public long delete(long id) throws MyException;
    /**
     * 
     * @Function:     getUserByUsername 
     * @Description:   通过用户名查询用户  
     *                 <功能详细描述>
     *
     * @param username
     * @return
     */
    public User getUserByUsername(String username) throws MyException;

    /**
     * 获得所有用户
     * @return
     */
    List<User> getAllUser();

    
    /**
     * 
     * @Function:     getPatients 
     * @Description:   获取所有的病人
     *                 <功能详细描述>
     *
     * @return
     */
    public List<User> getPatients() throws MyException;
    /**
     * 
     * @Function:     getDoctors 
     * @Description:   获取所有的医生  
     *                 <功能详细描述>
     *
     * @return
     */
    public List<User> getDoctors() throws MyException;
    /**
     * @Function:     getAllDoctorNotPass
     * @Description:   获得所有待审核的医生
     *
     * @return
     */
    public List<User> getAllDoctorNotPass() throws MyException;
    /**
     * 
     * @Function:     getExperts 
     * @Description:   获取所有的专家
     *                 <功能详细描述>
     *
     * @return
     */
    public List<User> getExperts() throws MyException;

    /**
     *
     * @Function:     checkDoctor 
     * @Description:   医生审核
     *                 <功能详细描述>
     *
     * @return
     */
    public int checkDoctor(String login_name) throws MyException;
}
