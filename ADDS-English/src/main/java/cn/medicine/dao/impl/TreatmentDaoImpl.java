package cn.medicine.dao.impl;

import cn.medicine.dao.TreatmentDao;
import cn.medicine.dao.TreatmentMapper;
import cn.medicine.pojo.Treatment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public class TreatmentDaoImpl implements TreatmentDao {
    private final static Logger logger= LogManager.getLogger(TreatmentDaoImpl.class);

    private boolean isDebugEnable=logger.isDebugEnabled();

    private boolean isErrorEnable=logger.isErrorEnabled();

    private TreatmentMapper treatmentMapper;
    public TreatmentDaoImpl(TreatmentMapper treatmentMapper){
        super();
        this.treatmentMapper = treatmentMapper;
    }

    public TreatmentDaoImpl(){super();}

    @Override
    public List<Treatment>  getpatientTreatment(long treatid){
        try {
            return  treatmentMapper.getpatientTreatment(treatid);
        }catch (DataAccessResourceFailureException e) {
            if (isErrorEnable)
                logger.error("error 1：" + e.getRootCause().getMessage());
        }
        return null;
    }
    @Override
    public void add(Treatment treatment){
          treatmentMapper.add(treatment);
    }
    @Override
    public long getMaxID(){
        return treatmentMapper.getMaxID();
    }

}
