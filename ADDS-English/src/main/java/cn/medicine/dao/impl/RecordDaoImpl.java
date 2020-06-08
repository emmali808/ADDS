package cn.medicine.dao.impl;

import cn.medicine.dao.IRecordDao;
import cn.medicine.dao.RecordMapper;
import cn.medicine.pojo.*;
import cn.medicine.utils.MyException;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @ClassName:
 * @Description: TODO
 * @Function List: TODO
 * @author: ytchen
 * @version:
 * @Date: 2016/8/25
 * @History: //历史修改记录
 * <author>  // 修改人
 * <time> //修改时间
 * <version> //版本
 * <desc> // 描述修改内容
 */
public class RecordDaoImpl implements IRecordDao{
    private final static Logger logger= LogManager.getLogger(RecordDaoImpl.class);
    private boolean isDebugEnable=logger.isDebugEnabled();

    private boolean isErrorEnable=logger.isErrorEnabled();
    
    private RecordMapper recordMapper;
    public RecordDaoImpl(){
        super();
    }

    public RecordDaoImpl(RecordMapper recordMapper){
        super();
        this.recordMapper=recordMapper;
    }
    @Override
    public List<ClinicRecord> getClinicRecordList(String patient_ID){
        try{
            if(patient_ID.matches("")){
                return null;
            }else{
                return  recordMapper.getClinicRecord(patient_ID);
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public List<ClinicRecord> getClinicRecordByDoctor(String doctoridentityID) throws MyException {
        try{
            if(doctoridentityID==null||doctoridentityID.equals("")){
                return null;
            }else{
                return recordMapper.getClinicRecordByDoctor(doctoridentityID);
            }
        }catch(Exception e){
            if(isErrorEnable)
                logger.error("根据医生身份证号获取出诊记录异常，doctoridentityID="+doctoridentityID+"错误信息："+e.getMessage());
            throw new MyException("根据医生身份证号获取出诊记录异常，doctoridentityID="+doctoridentityID+"错误信息："+e.getMessage());            
        }
    }
    @Override
    public List<BasicBodyRecord> getBasicBodyRecordList(String patient_ID){
        try{
            if(patient_ID.matches("")){
                return null;
            }else{
                return  recordMapper.getBasicBodyRecordList(patient_ID);
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }

    }
    @Override
    public List<HospitalIn> getHospitalInList(String patient_ID){
        try{
            if(patient_ID.matches("")){
                return null;
            }else{
                return  recordMapper.getHospitalIn(patient_ID);
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public List<HospitalIn> getHospitalInByDoctor(String doctoridentityID) throws MyException {
        try{
            if(doctoridentityID==null||doctoridentityID.equals("")){
                return null;
            }else{
                return recordMapper.getHospitalInByDoctor(doctoridentityID);
            }
        }catch(Exception e){
            if(isErrorEnable)
                logger.error("根据医生身份证号获取病人入院记录异常，doctoridentityID="+doctoridentityID+"错误信息："+e.getMessage());
            throw new MyException("根据医生身份证号获取病人入院记录异常，doctoridentityID="+doctoridentityID+"错误信息："+e.getMessage());            
        }
    }

    @Override
    public List<HospitalCheck> getHospitalCheckList(String patient_ID){
        try{
            if(patient_ID.matches("")){
                return null;
            }else{
                return  recordMapper.getHospitalCheck(patient_ID);
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public List<HospitalCheck> getHospitalCheckByDoctor(String doctoridentityID) throws MyException {
        try{
            if(doctoridentityID==null||doctoridentityID.equals("")){
                return null;
            }else{
                return recordMapper.getHospitalCheckByDoctor(doctoridentityID);
            }
        }catch(Exception e){
            if(isErrorEnable)
                logger.error("根据医生身份证号获取查房记录异常，doctoridentityID="+doctoridentityID+"错误信息："+e.getMessage());
            throw new MyException("根据医生身份证号获取查房记录异常，doctoridentityID="+doctoridentityID+"错误信息："+e.getMessage());            
        }
    }
    @Override
    public List<HospitalOut> getHospitalOutList(String patient_ID){
        try{
            if(patient_ID.matches("")){
                return null;
            }else{
                return  recordMapper.getHospitalOut(patient_ID);
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    @Override
    public List<HospitalOut> getHospitalOutByDoctor(String doctoridentityID) throws MyException {
        try{
            if(doctoridentityID==null||doctoridentityID.equals("")){
                return null;
            }else{
                return recordMapper.getHospitalOutByDoctor(doctoridentityID);
            }
        }catch(Exception e){
            if(isErrorEnable)
                logger.error("根据医生身份证号获取病人出院记录异常，doctoridentityID="+doctoridentityID+"错误信息："+e.getMessage());
            throw new MyException("根据医生身份证号获取病人出院记录异常，doctoridentityID="+doctoridentityID+"错误信息："+e.getMessage());            
        }
    }
    @Override
    public  int addBasicBodyRecord(BasicBodyRecord bodyRecord){
        try{
            recordMapper.addBasicBodyRecord(bodyRecord);
            return 1;
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    @Override
    public  int addClinicRecord(ClinicRecord clinicRecord){
        try{
            recordMapper.addClinicRecord(clinicRecord);
            return 1;
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    @Override
    public  int addHospitalIn(HospitalIn hospitalAdmissionFirst){
        try{
            recordMapper.addHospitalIn(hospitalAdmissionFirst);
            return 1;
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    @Override
    public  int addHospitalCheck(HospitalCheck hospitalAdmissionCheck){
        try{
            recordMapper.addHospitalCheck(hospitalAdmissionCheck);
            return 1;
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    @Override
    public  int addHospitalOut(HospitalOut hospitalDischarge){
        try{
            recordMapper.addHospitalOut(hospitalDischarge);
            return 1;
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }     
}
