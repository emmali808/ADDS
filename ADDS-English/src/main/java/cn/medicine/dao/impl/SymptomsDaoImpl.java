package cn.medicine.dao.impl;

import cn.medicine.dao.SymptomsDao;
import cn.medicine.dao.SymptomsMapper;
import cn.medicine.pojo.Symptoms;
import cn.medicine.pojo.Treatment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessResourceFailureException;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public class SymptomsDaoImpl implements SymptomsDao {
    private final static Logger logger= LogManager.getLogger(SymptomsDaoImpl.class);

    private boolean isDebugEnable=logger.isDebugEnabled();

    private boolean isErrorEnable=logger.isErrorEnabled();

    private SymptomsMapper symptomsMapper;
    public SymptomsDaoImpl(SymptomsMapper symptomsMapper){
        super();
        this.symptomsMapper = symptomsMapper;
    }

    public SymptomsDaoImpl(){super();}

    @Override
    public List<Symptoms> getpatientSymptoms(long symid){
        try {
            return  symptomsMapper.getpatientSymptoms(symid);
        }catch (DataAccessResourceFailureException e) {
            if (isErrorEnable)
                logger.error("error 1：" + e.getRootCause().getMessage());
        }
        return null;
    }
    @Override
    public void add( Symptoms symptoms){
        symptomsMapper.add(symptoms);
    }
    @Override
    public long getMaxID(){
        return symptomsMapper.getMaxID();
    }
}
