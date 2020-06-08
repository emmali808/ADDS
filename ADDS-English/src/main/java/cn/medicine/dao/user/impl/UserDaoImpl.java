package cn.medicine.dao.user.impl;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessResourceFailureException;

import cn.medicine.dao.user.IUserDao;
import cn.medicine.dao.user.UserMapper;
import cn.medicine.pojo.User;
import cn.medicine.utils.MyException;

import java.util.List;


public class UserDaoImpl implements IUserDao{
    
    private final static Logger logger= LogManager.getLogger(UserDaoImpl.class);
    
    private boolean isDebugEnable=logger.isDebugEnabled();

    private boolean isErrorEnable=logger.isErrorEnabled();
        
    private UserMapper usermapper;

    public UserDaoImpl(){
        super();
    }
    
    public UserDaoImpl(UserMapper userMapper){
        super();
        this.usermapper=userMapper;
    }

    @Override
    public User login(String username, String password) {
//        Integer result=0;
//        if("user1".equals(username)&&"123456".equals(password))
//            result=1;
        try {
            return usermapper.login(username, password);
        }catch (DataAccessResourceFailureException e){
            if(isErrorEnable)
                logger.error("error 1：" + e.getRootCause().getMessage());
        }catch (Exception e){
            if(isErrorEnable)
                logger.error("error2 ："+e.getMessage());
        }
        return null;
    }

    @Override
    public int add(User user) {
        try{
            if(usermapper.isDuplicate(user.getLogin_name())!=null){
                System.out.println("user.getLogin_name():"+user.getLogin_name());
                return -1;
            }else{
                usermapper.add(user);
                	
                return 1;
            }
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public long update(User user) throws MyException {
        try{
            usermapper.update(user);
            return user.getId();
        }catch(Exception e){
            if(isErrorEnable)
                logger.error("根据id更新异常："+e.getMessage());
              throw new MyException(e.getMessage());
        }
    }

    @Override
    public long delete(long id) throws MyException {
        try{
            usermapper.delete(id);
            return id;
        }catch(Exception e){
            if(isErrorEnable)
                logger.error("删除异常："+e.getMessage());
              throw new MyException(e.getMessage());
        }
    }

    @Override
    public User getUserByUsername(String username) throws MyException {
        try{
            return usermapper.getByUsername(username);
        }catch(Exception e){
            if(isErrorEnable)
                logger.error("查询异常："+e.getMessage());
              throw new MyException(e.getMessage());
        }
    }
    @Override
    public List<User> getAllUser(){
        try{
            return usermapper.getAllUser();
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public List<User> getAllUser(int start, int pagesize) throws MyException {
        try{
            return usermapper.getAllUserByPage(start, pagesize);
        }catch(Exception e){
            if(isErrorEnable)
                logger.error("获取所有用户（分页）异常："+e.getMessage());
              throw new MyException("获取所有用户（分页）异常："+e.getMessage());
        }
    }

    @Override
    public List<User> getPatients() throws MyException {
        try{
            return usermapper.getPatients();
        }catch(Exception e){
            if(isErrorEnable)
                logger.error("获取所有病人异常："+e.getMessage());
              throw new MyException("获取所有病人异常："+e.getMessage());
        }
    }

    @Override
    public List<User> getDoctors() throws MyException {
        try{
            return usermapper.getDoctors();
        }catch(Exception e){
            if(isErrorEnable)
                logger.error("获取所有医生异常："+e.getMessage());
              throw new MyException("获取所有医生异常："+e.getMessage());
        }
    }
    @Override
    public List<User>getAllDoctorNotPass() throws MyException{
        try{
            return usermapper.getAllDoctorNotPass();
        }catch(Exception e){
            logger.error("获取未激活医生异常："+e.getMessage());
            throw new MyException("获取未激活医生异常："+e.getMessage());
        }
    }

    @Override
    public List<User> getExperts() throws MyException {
        try{
            return usermapper.getExperts();
        }catch(Exception e){
            if(isErrorEnable)
                logger.error("获取所有专家异常："+e.getMessage());
              throw new MyException("获取所有专家异常："+e.getMessage());
        }
    }

    @Override
    public int checkDoctor(String login_name)throws MyException{
        try{
            return usermapper.getDoctorNotCheck(login_name);
        }catch(Exception e){
            if(isErrorEnable)
                logger.error("审核未审核医生异常："+e.getMessage());
            throw new MyException("审核未审核医生所有异常："+e.getMessage());
        }
    }

}
