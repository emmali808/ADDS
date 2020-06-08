package cn.medicine.service;

import cn.medicine.pojo.*;
import cn.medicine.utils.MyException;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName:
 * @Description: 记录有关的service 住院记录，门诊 记录等等
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
public interface IRecordService {
    /**
     * 获得病人的门诊记录
     * @param patient_ID 病人的身份证号
     * @return
     */
    List<ClinicRecord> getClinicRecordList(String patient_ID);
    /**
     * 
     * @Function:     getClinicRecordByDoctor 
     * @Description:   根据医生身份证号获得其出诊的记录  
     *                 <功能详细描述>
     *
     * @param doctoridentityID
     * @return
     */
    List<ClinicRecord> getClinicRecordByDoctor(String doctoridentityID) throws MyException;

    /**
     * 获得病人的基本体征记录
     * @param patient_ID 病人的身份证号
     * @return
     */
    List<BasicBodyRecord> getBasicBodyRecordList(String patient_ID);

    /**
     * 获得病人入院记录
     * @param patient_ID 病人的身份证号
     * @return
     */
    List<HospitalIn> getHospitalInList(String patient_ID);
    /**
     * 
     * @Function:     getHospitalInByDoctor 
     * @Description:   根据医生身份证号获取病人入院记录 
     *                 <功能详细描述>
     *
     * @param doctoridentityID
     * @return
     */
    List<HospitalIn> getHospitalInByDoctor(String doctoridentityID) throws MyException;
    /**
     * 获得病人查房记录
     * @param patient_ID 病人的身份证号
     * @return
     */
    List<HospitalCheck> getHospitalCheckList(String patient_ID);
    /**
     * 
     * @Function:     getHospitalCheckByDoctor 
     * @Description:   根据医生身份证号获取查房记录  
     *                 <功能详细描述>
     *
     * @param doctoridentityID
     * @return
     */
    List<HospitalCheck> getHospitalCheckByDoctor(String doctoridentityID) throws MyException;
    /**
     * 获得病人出院记录
     * @param patient_ID 病人的身份证号
     * @return
     */
    List<HospitalOut> getHospitalOutList(String patient_ID);
    /**
     * 
     * @Function:     getHospitalOut 
     * @Description:   根据医生身份证号获取病人出院记录 
     *                 <功能详细描述>
     *
     * @param doctoridentityID
     * @return
     */
    List<HospitalOut> getHospitalOutByDoctor(String doctoridentityID) throws MyException;


    /**
     * 添加基本体征信息
     * @param bodyRecord
     */
    int addBasicBodyRecord(BasicBodyRecord bodyRecord);
    int addClinicRecord(ClinicRecord clinicRecord);
    int addHospitalIn(HospitalIn hospitalIn);
    int addHospitalCheck(HospitalCheck hospitalCheck);
    int addHospitalOut(HospitalOut hospitalOut);


}
