package cn.medicine.service.impl;

import cn.medicine.dao.user.IPatientDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import cn.medicine.dao.user.IUserDao;
import cn.medicine.pojo.User;
import cn.medicine.service.IUserService;
import cn.medicine.utils.MyException;

import java.util.List;

@Service
public class UserService implements IUserService{
    
    @Autowired
    @Qualifier("patientDao")
    private IPatientDao patientDao;

    @Autowired
    @Qualifier("userDao")
    private IUserDao userdao;
    @Override
    public User login(String username, String password) {
        User result=userdao.login(username, password);
        return result;
    }


    @Override
    public int add(User user) throws MyException {
        
        return userdao.add(user);
    }


    @Override
    public long update(User user) throws MyException {
        return userdao.update(user);
    }


    @Override
    public long delete(long id) throws MyException {
        return userdao.delete(id);
    }


    @Override
    public User getUserByUsername(String username) throws MyException {
        return userdao.getUserByUsername(username);
    }
    @Override
    public List<User> getAllUser(){
        return userdao.getAllUser();
    }


    @Override
    public List<User> getPatients() throws MyException {
        return userdao.getPatients();
    }


    @Override
    public List<User> getDoctors() throws MyException {
        return userdao.getDoctors();
    }

    @Override
    public List<User> getAllDoctorNotPass() throws MyException{
        return userdao.getAllDoctorNotPass();
    }

    @Override
    public List<User> getExperts() throws MyException {
        return userdao.getExperts();
    }

    @Override
    public int checkDoctor(String login_name) throws MyException
    {
        return userdao.checkDoctor(login_name);
    }

}
