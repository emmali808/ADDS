package cn.medicine.dao.impl;

import cn.medicine.dao.PreviousMHDao;
import cn.medicine.dao.PreviousMHMapper;
import cn.medicine.pojo.LabTest;
import cn.medicine.pojo.PreviousMH;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessResourceFailureException;

import java.lang.Override;import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public class PreviousMHDaoImpl implements PreviousMHDao {
    private final static Logger logger= LogManager.getLogger(PreviousMHDaoImpl.class);

    private boolean isDebugEnable=logger.isDebugEnabled();

    private boolean isErrorEnable=logger.isErrorEnabled();

    private PreviousMHMapper previousMHMapper;
    public PreviousMHDaoImpl(PreviousMHMapper previousMHMapper){
        super();
        this. previousMHMapper =  previousMHMapper;
    }

    public PreviousMHDaoImpl(){super();}

    @Override
    public List<PreviousMH> getpatientPreviousMH(long preid){
        try {
            return  previousMHMapper.getpatientPreviousMH(preid);
        }catch (DataAccessResourceFailureException e) {
            if (isErrorEnable)
                logger.error("error 1：" + e.getRootCause().getMessage());
        }
        return null;
    }
    @Override
    public void add(PreviousMH previousMH){
        previousMHMapper.add(previousMH);
    }
    @Override
    public long getMaxID(){
        return previousMHMapper.getMaxID();
    }
}
