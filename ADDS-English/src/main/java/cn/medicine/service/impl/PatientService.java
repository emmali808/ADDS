package cn.medicine.service.impl;

import cn.medicine.dao.user.IPatientDao;
import cn.medicine.pojo.Patient;
import cn.medicine.service.IPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
public class PatientService implements IPatientService{
    @Autowired
    @Qualifier("patientDao")
    private IPatientDao patientDao;
    @Override
    public long add(Patient patient){
        return patientDao.add(patient);
    }
    @Override
    public Patient getPatientByIdentityID(String identityID){
        return patientDao.getPatientByIdentityID(identityID);
    }
    @Override
      public long delete(String  identityID){
        return patientDao.delete(identityID);
    }
    @Override
      public long update(Patient patient){

        return patientDao.update(patient);
    }

}
