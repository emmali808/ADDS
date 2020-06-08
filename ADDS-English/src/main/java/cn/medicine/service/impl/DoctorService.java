package cn.medicine.service.impl;

import cn.medicine.dao.user.IDoctorDao;
import cn.medicine.dao.user.IPatientDao;
import cn.medicine.pojo.Doctor;
import cn.medicine.pojo.Patient;
import cn.medicine.pojo.User;
import cn.medicine.service.IDoctorService;
import cn.medicine.service.IPatientService;
import cn.medicine.utils.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName:
 * @Description: TODO
 * @Function List: TODO
 * @author: ytchen
 * @version:
 * @Date: 2016/8/11
 * @History: //历史修改记录
 * <author>  // 修改人
 * <time> //修改时间
 * <version> //版本
 * <desc> // 描述修改内容
 */
@Service
public class DoctorService implements IDoctorService {
    @Autowired
    @Qualifier("doctorDao")
    private IDoctorDao doctorDao;
    @Override
    public long add(Doctor doctor){
        return doctorDao.add(doctor);
    }
    @Override
    public Doctor getByIdentityID(String identityID){
        return doctorDao.getByIdentityID(identityID);
    }
    @Override
      public long delete(String  identityID){
        return doctorDao.delete(identityID);
    }
    @Override
      public long update(Doctor doctor){

        return doctorDao.update(doctor);
    }
    @Override
    public List<User> getPatients(String identityID)  {
        return doctorDao.getPatients(identityID);
    }
}
