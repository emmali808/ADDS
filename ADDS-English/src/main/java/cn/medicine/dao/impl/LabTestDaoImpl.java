package cn.medicine.dao.impl;

import cn.medicine.dao.LabTestDao;
import cn.medicine.dao.LabTestMapper;
import cn.medicine.pojo.Graph;
import cn.medicine.pojo.LabTest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.List;

/**
 * Created by Administrator on 2016/9/13.
 */
public class LabTestDaoImpl implements LabTestDao {
    private final static Logger logger= LogManager.getLogger(LabTestDaoImpl.class);

    private boolean isDebugEnable=logger.isDebugEnabled();

    private boolean isErrorEnable=logger.isErrorEnabled();

    private LabTestMapper labTestMapper;
    public LabTestDaoImpl( LabTestMapper labTestMapper){
        super();
        this.labTestMapper = labTestMapper;
    }

    public  LabTestDaoImpl(){super();}

    @Override
    public List<LabTest> getpatientLabTest(long labid){
        try {
            return labTestMapper.getpatientLabTest(labid);
        }catch (DataAccessResourceFailureException e) {
            if (isErrorEnable)
                logger.error("error 1：" + e.getRootCause().getMessage());
        }
        return null;
    }
    @Override
    public void add(LabTest labTest){
        labTestMapper.add(labTest);
    }
    @Override
    public long getMaxID(){
        return labTestMapper.getMaxID();
    }
}
