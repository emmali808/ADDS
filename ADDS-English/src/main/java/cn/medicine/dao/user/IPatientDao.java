package cn.medicine.dao.user;

import cn.medicine.pojo.ClinicRecord;
import cn.medicine.pojo.Patient;
import cn.medicine.pojo.User;

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
public interface IPatientDao {
    int add(Patient patient);

    /**
     * 更新
     * @param patient
     * @return
     */
    int update(Patient patient);

    /**
     * 通过身份证号删除
     * @param identityID
     * @return
     */
    int delete(String identityID);

    /**
     * 通过身份证获得
     * @param identityID
     * @return Patient
     */
    Patient getPatientByIdentityID(String identityID);

}
