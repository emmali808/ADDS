package cn.medicine.service.impl;

import cn.medicine.dao.IRecordDao;
import cn.medicine.pojo.*;
import cn.medicine.service.IRecordService;
import cn.medicine.utils.MyException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.awt.event.HierarchyBoundsAdapter;
import java.util.List;

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
@Service
public class RecordService implements IRecordService {
    @Autowired
    @Qualifier("recordDao")
    private IRecordDao recordDao;

    @Override
    public List<ClinicRecord> getClinicRecordList(String patient_ID){
        return recordDao.getClinicRecordList(patient_ID);
    }
    @Override
    public List<ClinicRecord> getClinicRecordByDoctor(String doctoridentityID)
            throws MyException {        
        return recordDao.getClinicRecordByDoctor(doctoridentityID);
    }
    @Override
    public List<BasicBodyRecord> getBasicBodyRecordList(String patient_ID){
        return recordDao.getBasicBodyRecordList(patient_ID);
    }
    @Override
    public List<HospitalIn> getHospitalInList(String patient_ID){
        return recordDao.getHospitalInList(patient_ID);
    }
    @Override
    public List<HospitalIn> getHospitalInByDoctor(String doctoridentityID)
            throws MyException {
        return recordDao.getHospitalInByDoctor(doctoridentityID);
    }
    @Override
    public List<HospitalCheck> getHospitalCheckList(String patient_ID){
        return recordDao.getHospitalCheckList(patient_ID);
    }
    @Override
    public List<HospitalCheck> getHospitalCheckByDoctor(String doctoridentityID)
            throws MyException {
        return recordDao.getHospitalCheckByDoctor(doctoridentityID);
    }
    @Override
    public List<HospitalOut> getHospitalOutList(String patient_ID){
        return recordDao.getHospitalOutList(patient_ID);
    }

    @Override
    public List<HospitalOut> getHospitalOutByDoctor(String doctoridentityID)
            throws MyException {
        return recordDao.getHospitalOutByDoctor(doctoridentityID);
    }
    
    @Override
    public int addBasicBodyRecord(BasicBodyRecord bodyRecord){
        return recordDao.addBasicBodyRecord(bodyRecord);
    }
    @Override
    public int addClinicRecord(ClinicRecord clinicRecord){
        return recordDao.addClinicRecord(clinicRecord);
    }
    @Override
    public int addHospitalIn(HospitalIn hospitalIn){
        return recordDao.addHospitalIn(hospitalIn);
    }

    @Override
    public int addHospitalCheck(HospitalCheck hospitalCheck){
        return recordDao.addHospitalCheck(hospitalCheck);
    }
    @Override
    public int addHospitalOut(HospitalOut hospitalOut){
        return recordDao.addHospitalOut(hospitalOut);
    }
    
  
}
