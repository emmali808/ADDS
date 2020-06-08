package cn.medicine.dao.user;

import cn.medicine.pojo.ClinicRecord;
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
public interface PatientMapper {
    void add(Patient patient);
    void update(Patient patient);
    void delete(String identityID);
    Patient getByIdentityID(String identityID);
}
