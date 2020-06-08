package cn.medicine.service;

import cn.medicine.pojo.Doctor;
import cn.medicine.pojo.User;
import cn.medicine.utils.MyException;

import java.util.List;

/**
 * @ClassName:
 * @Description: TODO
 * @Function List: TODO
 * @author: ytchen
 * @version:
 * @Date: 2016/9/6
 * @History: //历史修改记录
 * <author>  // 修改人
 * <time> //修改时间
 * <version> //版本
 * <desc> // 描述修改内容
 */
public interface IDoctorService {
    /**
     * 增加医生
     * @param doctor
     * @return
     */
     long add(Doctor doctor);

    /**
     * 通过identityID 查询信息
     * @param identityID
     * @return
     */
    Doctor getByIdentityID(String identityID);

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
    long update(Doctor patient);
    List<User> getPatients(String identityID);

}
