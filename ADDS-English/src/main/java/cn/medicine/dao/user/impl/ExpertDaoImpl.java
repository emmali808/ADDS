package cn.medicine.dao.user.impl;

import cn.medicine.dao.user.DoctorMapper;
import cn.medicine.dao.user.ExpertMapper;
import cn.medicine.dao.user.IDoctorDao;
import cn.medicine.dao.user.IExpertDao;
import cn.medicine.pojo.Doctor;
import cn.medicine.pojo.Expert;

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
public class ExpertDaoImpl implements IExpertDao {
    private ExpertMapper expertMapper;
    public ExpertDaoImpl(){
        super();
    }

    public ExpertDaoImpl(ExpertMapper expertMapper){
        super();
        this.expertMapper =expertMapper;
    }
    @Override
    public int add(Expert expert){
        try{
            if(expert==null){
                return -1;
            }else{
                expertMapper.add(expert);
                return 1;
            }
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }
    @Override
    public int update(Expert expert){
        try{
            if(expert.getIdentityID().matches("")){
                return -1;
            }else{
                expertMapper.update(expert);
                return 1;
            }
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }


    }
    @Override
    public int delete(String identityID){
        try{
            if(identityID.matches("")){
                return -1;
            }else{
                expertMapper.delete(identityID);
                return 1;
            }
        }catch(Exception e){
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public  Expert getByIdentityID(String identityID){
        try{
            if(identityID.matches("")){
                return null;
            }else{
                return  expertMapper.getByIdentityID(identityID);
            }
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

}
