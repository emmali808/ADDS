package cn.medicine.dao.impl;

import cn.medicine.dao.DiagnoseDao;
import cn.medicine.dao.DiagnoseMapper;
import cn.medicine.pojo.Diagnose;
import cn.medicine.pojo.Treatment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessResourceFailureException;

import java.lang.Override;import java.util.List;

/**
 * Created by Administrator on 2016/9/3.
 */
public class DiagnoseDaoImpl implements DiagnoseDao {
    private final static Logger logger= LogManager.getLogger(DiagnoseDaoImpl.class);

    private boolean isDebugEnable=logger.isDebugEnabled();

    private boolean isErrorEnable=logger.isErrorEnabled();

    private DiagnoseMapper diagnoseMapper;
    public DiagnoseDaoImpl( DiagnoseMapper diagnoseMapper){
        super();
        this.diagnoseMapper = diagnoseMapper;
    }

    public DiagnoseDaoImpl(){super();}

    @Override
    public List<Diagnose> getpatientDiagnose(long diagid){
        try {
            return diagnoseMapper.getpatientDiagnose(diagid);
        }catch (DataAccessResourceFailureException e) {
            if (isErrorEnable)
                logger.error("error 1：" + e.getRootCause().getMessage());
        }
        return null;
    }
    @Override
    public void add(Diagnose diagnose){
        diagnoseMapper.add(diagnose);
    }
    @Override
    public long getMaxID(){
        return diagnoseMapper.getMaxID();
    }

}
