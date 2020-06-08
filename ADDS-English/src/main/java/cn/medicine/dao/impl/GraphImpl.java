package cn.medicine.dao.impl;

import cn.medicine.dao.PatientGraphDao;
import cn.medicine.dao.PatientGraphMapper;
import cn.medicine.pojo.Diagnose;
import cn.medicine.pojo.Graph;
import cn.medicine.utils.MyException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DataAccessResourceFailureException;

import java.lang.Override;import java.lang.String;import java.util.List;

/**
 * Created by Administrator on 2016/9/2.
 */
public class GraphImpl implements PatientGraphDao {
    private final static Logger logger= LogManager.getLogger(GraphImpl.class);

    private boolean isDebugEnable=logger.isDebugEnabled();

    private boolean isErrorEnable=logger.isErrorEnabled();


    private PatientGraphMapper patientgraphMapper;
    public GraphImpl(PatientGraphMapper patientGraphMapper){
        super();
        this.patientgraphMapper = patientGraphMapper;
    }

    public GraphImpl() {super();}

    @Override
    public List<Graph> getpatientRecord(String pgID, String g_time) throws MyException {
        try {
            return patientgraphMapper.getpatientRecord(pgID, g_time);
        }catch (DataAccessResourceFailureException e) {
            if (isErrorEnable)
                logger.error("error 1：" + e.getRootCause().getMessage());
        }
        return null;
    }
    @Override
    public void add(Graph graph){
        patientgraphMapper.add(graph);
    }
    @Override
    public long getMaxID(){
        return patientgraphMapper.getMaxID();
    }
    @Override
    public List<Graph> getpatientGraph(String pgID){
        return patientgraphMapper.getpatientGraph(pgID);
    }
}