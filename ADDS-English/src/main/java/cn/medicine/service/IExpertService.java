package cn.medicine.service;

import cn.medicine.pojo.Doctor;
import cn.medicine.pojo.Expert;

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
public interface IExpertService {
    /**
     * 增加
     * @param expert
     * @return
     */
     long add(Expert expert);

    /**
     * 通过identityID 查询信息
     * @param identityID
     * @return
     */
    Expert getByIdentityID(String identityID);

    /**
     * 通过identityID删除
     * @param identityID
     * @return
     */
    long delete(String identityID);

    /**
     * 更新expert
     * @param expert
     * @return
     */
    long update(Expert expert);


}
