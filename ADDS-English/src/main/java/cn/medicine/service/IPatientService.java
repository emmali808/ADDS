package cn.medicine.service;

import cn.medicine.pojo.ClinicRecord;
import cn.medicine.pojo.Patient;
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
public interface IPatientService {
    /**
     * 增加病人
     * @param patient
     * @return
     */
   long add(Patient patient);

    /**
     * 通过identityID 查询信息
     * @param identityID
     * @return
     */
    Patient getPatientByIdentityID(String identityID);

    /**
     * 通过identityID删除
     * @param identityID
     * @return
     */
    long delete(String identityID);

    /**
     * 更新patient
     * @param patient
     * @return
     */
    long update(Patient patient);


}
