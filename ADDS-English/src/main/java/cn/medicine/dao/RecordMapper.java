package cn.medicine.dao;

import cn.medicine.pojo.*;

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
public interface RecordMapper {
    List<ClinicRecord> getClinicRecord(String identityID);
    /**
     * 
     * @Function:     getClinicRecordByDoctor 
     * @Description:   根据医生身份证号获得其出诊的记录  
     *                 <功能详细描述>
     *
     * @param doctoridentityID
     * @return
     */
    List<ClinicRecord> getClinicRecordByDoctor(String doctoridentityID);
    
    List<BasicBodyRecord> getBasicBodyRecordList(String identityID);
    List<HospitalIn> getHospitalIn(String patient_ID);
    /**
     * 
     * @Function:     getHospitalInByDoctor 
     * @Description:   根据医生身份证号获取病人入院记录 
     *                 <功能详细描述>
     *
     * @param doctoridentityID
     * @return
     */
    List<HospitalIn> getHospitalInByDoctor(String doctoridentityID);
    /**
     * 获得病人查房记录
     * @param patient_ID 病人的身份证号
     * @return
     */
    List<HospitalCheck> getHospitalCheck(String patient_ID);
    /**
     * 
     * @Function:     getHospitalCheckByDoctor 
     * @Description:   根据医生身份证号获取查房记录  
     *                 <功能详细描述>
     *
     * @param doctoridentityID
     * @return
     */
    List<HospitalCheck> getHospitalCheckByDoctor(String doctoridentityID);
    /**
     * 获得病人出院记录
     * @param patient_ID 病人的身份证号
     * @return
     */
    List<HospitalOut> getHospitalOut(String patient_ID);
    /**
     * 
     * @Function:     getHospitalOut 
     * @Description:   根据医生身份证号获取病人出院记录 
     *                 <功能详细描述>
     *
     * @param doctoridentityID
     * @return
     */
    List<HospitalOut> getHospitalOutByDoctor(String doctoridentityID);

    /**
     * 添加基本体征信息
     * @param bodyRecord
     */
    void addBasicBodyRecord(BasicBodyRecord bodyRecord);
    void addClinicRecord(ClinicRecord clinicRecord);
    void addHospitalIn(HospitalIn hospitalIn);
    void addHospitalCheck(HospitalCheck hospitalCheck);
    void addHospitalOut(HospitalOut hospitalOut);
}
