package cn.medicine.dao.user;

import cn.medicine.pojo.Doctor;
import cn.medicine.pojo.User;

import javax.print.Doc;
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
public interface DoctorMapper {
    void add(Doctor doctor);
    void update(Doctor doctor);
    void delete(String identityID);
    Doctor getByIdentityID(String identityID);
    List<User> getPatients(String identityID);
}
