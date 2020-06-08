package cn.medicine.service;


import cn.medicine.pojo.*;
import cn.medicine.utils.MyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;

/**
 * Created by Administrator on 2016/9/1.
 */
@Service
public interface PatientGraphService {
    /**
     *
     * @Function:    getpatientRccord 
     * @Description:   获得用户记录  
     *                 <功能详细描述>
     *
     * @param pgID
     * @param g_time
     * @return
     */
    public List<Graph> getpatientRecord(String pgID,String g_time)throws MyException;
    public List<Diagnose> getpatientDiagnose(long diagid);
    public List<Symptoms> getpatientSymptoms(long symid);
    public List<PreviousMH> getpatientPreviousMH(long preid);
    public List<Treatment> getpatientTreatment(long treatid);
    public List<LabTest> getpatientLabTest(long labid);
    public List getd();
    public List gets();
    public List gett();
    public List getp();
    public List getl();
    public List getall();

    void addClinic(Diagnose diagnose,List<Symptoms> symptoms,List<LabTest> labTest,List<PreviousMH> previousMH,List<Treatment> treatment,Graph graph);
    List<PatientGraph> getPatientGraph(String pgID);
}
