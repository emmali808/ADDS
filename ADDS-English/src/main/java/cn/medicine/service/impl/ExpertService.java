package cn.medicine.service.impl;

import cn.medicine.dao.user.IDoctorDao;
import cn.medicine.dao.user.IExpertDao;
import cn.medicine.pojo.Doctor;
import cn.medicine.pojo.Expert;
import cn.medicine.service.IDoctorService;
import cn.medicine.service.IExpertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class ExpertService implements IExpertService {
    @Autowired
    @Qualifier("expertDao")
    private IExpertDao expertDao;
    @Override
    public long add(Expert expert){
        return expertDao.add(expert);
    }
    @Override
    public Expert getByIdentityID(String identityID){
        return expertDao.getByIdentityID(identityID);
    }
    @Override
      public long delete(String  identityID){
        return expertDao.delete(identityID);
    }
    @Override
      public long update(Expert expert){

        return expertDao.update(expert);
    }

}
