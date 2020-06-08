package cn.medicine.dao.user.impl;

import cn.medicine.dao.user.IPatientDao;
import cn.medicine.dao.user.PatientMapper;
import cn.medicine.pojo.Patient;

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
public class PatientDaoImpl implements IPatientDao{
    private PatientMapper patientMapper;
    public PatientDaoImpl(){
        super();
    }

    public PatientDaoImpl(PatientMapper patientMapper){
        super();
        this.patientMapper=patientMapper;
    }
    @Override
    public int add(Patient patient){
        try{
            if( patient== null|| patient.getIdentityID().matches("")){
                return -1;
            }else{
                patientMapper.add(patient);
                return 1;
            }
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    @Override
    public int update(Patient patient){
        try{
            if(patient.getIdentityID().matches("")){
                return -1;
            }else{
                patientMapper.update(patient);
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
                patientMapper.delete(identityID);
                return 1;
            }
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public  Patient getPatientByIdentityID(String identityID){
        try{
            if(identityID.matches("")){
                return null;
            }else{
                return  patientMapper.getByIdentityID(identityID);
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
