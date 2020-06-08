package cn.medicine.dao.user.impl;

import cn.medicine.dao.user.DoctorMapper;
import cn.medicine.dao.user.IDoctorDao;
import cn.medicine.dao.user.PatientMapper;
import cn.medicine.pojo.Doctor;
import cn.medicine.pojo.Patient;
import cn.medicine.pojo.User;
import cn.medicine.utils.MyException;

import java.util.List;

/**
 * @ClassName:
 * @Description: TODO
 * @Function List: TODO
 * @author: ytchen
 * @version:
 * @Date: 2016/8/12
 * @History: //历史修改记录
 * <author>  // 修改人
 * <time> //修改时间
 * <version> //版本
 * <desc> // 描述修改内容
 */
public class DoctorDaoImpl implements IDoctorDao{
    private DoctorMapper doctorMapper;
    public DoctorDaoImpl(){
        super();
    }

    public DoctorDaoImpl(DoctorMapper doctorMapper){
        super();
        this.doctorMapper =doctorMapper;
    }
    @Override
    public int add(Doctor doctor){
        try{
            if(doctor==null){
                return -1;
            }else{
                doctorMapper.add(doctor);
                return 1;
            }
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    @Override
    public int update(Doctor doctor){
        try{
            if(doctor.getIdentityID().matches("")){
                return -1;
            }else{
                doctorMapper.update(doctor);
                return 1;
            }
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }


    }
    @Override
    public int delete(String identityID){
        try{
            if(identityID.matches("")){
                return -1;
            }else{
                doctorMapper.delete(identityID);
                return 1;
            }
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public  Doctor getByIdentityID(String identityID){
        try{
            if(identityID.matches("")){
                return null;
            }else{
                return  doctorMapper.getByIdentityID(identityID);
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public List<User> getPatients(String identityID)  {
        try{
            if(identityID.matches("")){
                return null;
            }else{
                return  doctorMapper.getPatients(identityID);
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
