package cn.medicine.factory;

import cn.medicine.dao.*;
import cn.medicine.dao.impl.*;
import cn.medicine.dao.user.*;
import cn.medicine.dao.user.impl.*;


public class DaoBuildFactory {

    public IUserDao getUserDao(UserMapper userMapper){
        IUserDao userdao=new UserDaoImpl(userMapper);
        return userdao;
    }
    public IPatientDao getPatientDao(PatientMapper patientMapper){
        IPatientDao patientDao=new PatientDaoImpl(patientMapper);
        return patientDao;
    }
    public IDoctorDao getDoctorDao(DoctorMapper doctorMapper){
        IDoctorDao doctordao=new DoctorDaoImpl(doctorMapper);
        return doctordao;
    }
    public IExpertDao getExpertDao(ExpertMapper expertMapper){
        IExpertDao expertdao=new ExpertDaoImpl(expertMapper);
        return expertdao;
    }
    public IHospitalDepartmentDao getHospitalDepartmentDao(HospitalDepartmentMapper hospitalDepartmentMapper){
        IHospitalDepartmentDao hdDao=new HospitalDepartmentImpl(hospitalDepartmentMapper);
        return hdDao;
    }
    public IRecordDao getRecordDao(RecordMapper recordMapper){
        IRecordDao recordDao=new RecordDaoImpl(recordMapper);
        return recordDao;
    }

    public IQuestionDao getQuestionDao(QuestionMapper questionMapper){
        IQuestionDao questionDao=new QuestionDaoImpl(questionMapper);
        return questionDao;
    }
    
    public IQuestionResultDao getQuestionResultDao(QuestionResultMapper questionResultMapper){
        IQuestionResultDao questionResultDao=new QuestionResultDaoImpl(questionResultMapper);
        return questionResultDao;
    }
    
    public IMyDataDao getMyDataDao(MyDataMapper myDataMapper){
        IMyDataDao myDataDao=new MyDataDaoImpl(myDataMapper);
        return myDataDao;
    }

    public PatientGraphDao getpatientRecord(PatientGraphMapper patientgraphMapper){
        PatientGraphDao pgDao=new GraphImpl(patientgraphMapper);
        return pgDao;
    }

    public DiagnoseDao getDiagnoseDao(DiagnoseMapper diagnoseMapper){
        DiagnoseDao diagDao=new DiagnoseDaoImpl(diagnoseMapper);
        return diagDao;
    }
    public SymptomsDao getSymptomsDao( SymptomsMapper symptomsMapper){
        SymptomsDao symDao= new SymptomsDaoImpl(symptomsMapper);
        return symDao;
    }

    public LabTestDao getLabTestDao( LabTestMapper labTestMapper){
        LabTestDao labDao= new LabTestDaoImpl(labTestMapper);
        return labDao;
    }

    public PreviousMHDao getPreviousMHDao( PreviousMHMapper previousMHMapper){
        PreviousMHDao preDao= new PreviousMHDaoImpl(previousMHMapper);
        return preDao;
    }

    public TreatmentDao getTreatmentDao(TreatmentMapper treatmentMapper){
        TreatmentDao treatDao= new TreatmentDaoImpl(treatmentMapper);
        return treatDao;
    }
    public IMessageDao getMessageDao(MessageMapper messageMapper){
        IMessageDao messageDao=new MessageDaoImpl(messageMapper);
        return messageDao;
    }
}
