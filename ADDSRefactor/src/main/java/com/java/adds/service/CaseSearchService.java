package com.java.adds.service;

import com.java.adds.config.UploadFileConfig;
import com.java.adds.dao.*;
import com.java.adds.entity.HistoryAdmssionEntity;
import com.java.adds.entity.MedicalCaseEntity;
import com.java.adds.entity.SimilarCasesEntity;
import com.java.adds.mapper.HistoryAdmissionMapper;
import com.java.adds.utils.CPPUtil;
import com.java.adds.utils.FileUtil;
import lombok.extern.log4j.Log4j;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

/**
 * Case Search Service
 * @author ZY
 */
@Log4j
@Service
public class CaseSearchService {

    @Autowired
    private UploadFileConfig fileConfig;

    @Autowired
    FileUtil fileUtil;

    @Autowired
    KGService kgService;

    @Autowired
    DoctorDiagnosisDao doctorDiagnosisDao;

    @Autowired
    HistoryAdmissionDao historyAdmissionDao;

    @Autowired
    SimilarGraphsDao similarGraphsDao;

    @Autowired
    MedicalCaseDao medicalCaseDao;

    @Autowired
    CPPUtil cppUtil;

    @Autowired
    HistoryAdmissionMapper historyAdmissionMapper;

    @Autowired
    private SimilarCasesDao similarCasesDao;

    final static Logger logger = Logger.getLogger(CaseSearchService.class);

    /**
     * Create Knowledge Graph From Medical Case Text
     *
     * @author ZY
     */
    public Long createGraph(MedicalCaseEntity medicalCase) {
        String filePath = medicalCase.getTxtFilePath();
        ArrayList<String> fileContentLines = fileUtil.readFileIntoList(filePath);
        String fileContent = String.join(" ", fileContentLines);

        Map<String, ArrayList<String>> entityMap = this.extractEntityIDList(fileContent);

        ArrayList<String> diagnosisEntities = entityMap.get("diagnosisEntities");
        int diag_size =diagnosisEntities.size();
        String ICD9_DIAG ="";
        for (int i = 0; i < diag_size-1 ; i++) {
           ICD9_DIAG  = ICD9_DIAG+ diagnosisEntities.get(i)+";";
        }
        ICD9_DIAG = ICD9_DIAG + diagnosisEntities.get(diag_size-1);

        ArrayList<String> procedureEntities = entityMap.get("procedureEntities");
        String ICD9_PROCE ="";
        int size =procedureEntities.size();
        for (int j = 0; j < size-1; j++) {
            ICD9_PROCE = ICD9_PROCE + procedureEntities.get(j)+";";
        }
        ICD9_PROCE = ICD9_PROCE + procedureEntities.get(size-1);

        ArrayList<String> drugEntities = entityMap.get("drugEntities");
        String ATC ="";
        int drug_size =drugEntities.size();
        for (int j = 0; j < drug_size-1; j++) {
            ATC = ATC + drugEntities.get(j)+";";
        }
        ATC = ATC + drugEntities.get(drug_size-1);

        ArrayList<Map.Entry<String, String>> relationListBetweenDiagAndDrug = this.findRelationBetweenDiagAndDrug(entityMap);
        ArrayList<Map.Entry<String, String>> relationListBetweenDiagAndProc = this.findRelationBetweenDiagAndProc(entityMap);

        Long kgId = kgService.createMedicalCaesGraph(relationListBetweenDiagAndDrug,relationListBetweenDiagAndProc, medicalCase);

        medicalCase.setICD9_DIAG(ICD9_DIAG);
        medicalCase.setICD9_PROCE(ICD9_PROCE);
        medicalCase.setATC(ATC);
        medicalCase.setKgId(kgId);
        medicalCaseDao.updateMedicalCase(medicalCase);

        //prepare query_case.csv for similar graphs Search
       this.createQueryCaseInCsv(entityMap, kgId);
       return kgId;
    }


    /**
     * Extract Drug, Diagnosis and Procedure Entity From Given File Content
     *
     * @author ZY
     */
    public Map extractEntityIDList(String fileContent) {

        String staticPath = this.getClass().getResource("/entityDict/").getPath();

        String diagnosisDictPath = staticPath + "diag_dict.txt";
        ArrayList<String> diagnosisDict = fileUtil.readFileIntoList(diagnosisDictPath);
        ArrayList<String> diagnosisEntities = new ArrayList<>();
        for (String diagnosis : diagnosisDict) {

            String diag_name = diagnosis.split(",")[1];
            String diag_id = diagnosis.split(",")[0];

            if (fileContent.toLowerCase().contains(diag_name.toLowerCase())) {
                diagnosisEntities.add(diag_id);
            }
        }

        String drugDictPath = staticPath + "drug_atc_dict.txt";
        ArrayList<String> drugDict = fileUtil.readFileIntoList(drugDictPath);
        ArrayList<String> drugEntities = new ArrayList<>();
        System.out.println(fileContent.toLowerCase());
        for (String drug : drugDict) {

            String drug_name = drug.split(",")[1];
            String drug_ID = drug.split(",")[0];

            if (fileContent.toLowerCase().contains(drug_name.toLowerCase())) {
                drugEntities.add(drug_ID);
            }

        }


        String procedureDictPath = staticPath + "proc_dict.txt";
        ArrayList<String> procedureDict = fileUtil.readFileIntoList(procedureDictPath);
        ArrayList<String> procedureEntities = new ArrayList<>();
        for (String procedure : procedureDict) {

            String proc_name = procedure.split(",")[1];
            String proc_id = procedure.split(",")[0];

            if (fileContent.toLowerCase().contains(proc_name.toLowerCase())) {
                procedureEntities.add(proc_id);
            }
        }


//        ArrayList<String> diagnosisEntities = new ArrayList<>();
//        /*ICD9_DIAG: V3000,7706,V290,V053*/
//        diagnosisEntities.add("diag_V3000");
//        diagnosisEntities.add("diag_7706");
//        diagnosisEntities.add("diag_V290");
//        diagnosisEntities.add("diag_V053");
//
//
//        ArrayList<String> drugEntities = new ArrayList<>();
//        /*ATC: 2,392,041,721,474*/
//        drugEntities.add("pres_2");
//        drugEntities.add("pres_392");
//        drugEntities.add("pres_041");
//        drugEntities.add("pres_721");
//        drugEntities.add("pres_474");
//
//
//        ArrayList<String> procedureEntities = new ArrayList<>();
//        /*ICD9_PROCE 9955*/
//        procedureEntities.add("proc_9955");

        Map<String, ArrayList<String>> resultMap = new HashMap<>();
        resultMap.put("diagnosisEntities", diagnosisEntities);
        resultMap.put("drugEntities", drugEntities);
        resultMap.put("procedureEntities", procedureEntities);
        return resultMap;

    }

    /**
     * Validate If An Entity Exists In The Knowledge Map
     *
     * @author XYX
     */
    public Map<String, ArrayList<String>> validateEntity(Map<String, ArrayList<String>> entityMap) {
        ArrayList<String> idList = new ArrayList<>();
        ArrayList<String> diseaseEntities = entityMap.get("diseaseEntities");
        ListIterator iter = diseaseEntities.listIterator();
        while (iter.hasNext()) {
            String id = kgService.hasDiseaseWithAlias((String) iter.next());
            if (id == null) {
                iter.remove();
            } else {
                idList.add(id);
            }
        }

        ArrayList<String> drugEntities = entityMap.get("drugEntities");
        iter = drugEntities.listIterator();
        while (iter.hasNext()) {
            String id = kgService.hasDrugWithAlias((String) iter.next());
            if (id == null) {
                iter.remove();
            } else {
                idList.add(id);
            }
        }

        Map<String, ArrayList<String>> validatedMap = new HashMap<>();
        validatedMap.put("diseaseEntities", diseaseEntities);
        validatedMap.put("drugEntities", drugEntities);
        validatedMap.put("idList", idList);
        return validatedMap;
    }
    /**
     * Find Relations Between DIAGNOSIS And PROCEDURE
     *
     * @author ZY
     */
    public ArrayList<Map.Entry<String, String>> findRelationBetweenDiagAndProc(Map<String, ArrayList<String>> entityMap) {
        ArrayList<Map.Entry<String, String>> relationList = new ArrayList<>();

        ArrayList<String> diagnosisEntities = entityMap.get("diagnosisEntities");
        ArrayList<String> procedureEntities = entityMap.get("procedureEntities");

        for (int i = 0; i < diagnosisEntities.size(); i++) {
            for (int j = 0; j < procedureEntities.size(); j++) {
                if (kgService.findRelationBetweenDiagAndProc("diag_"+diagnosisEntities.get(i), "proc_"+procedureEntities.get(j))) {
                    relationList.add(new AbstractMap.SimpleEntry<>("diag_"+diagnosisEntities.get(i), "proc_"+procedureEntities.get(j)));
                }
            }
        }

        return relationList;
    }

    /**
     * Find Relations Between DIAGNOSIS And DRUG
     *
     * @author ZY
     */
    public ArrayList<Map.Entry<String, String>> findRelationBetweenDiagAndDrug(Map<String, ArrayList<String>> entityMap) {
        ArrayList<Map.Entry<String, String>> relationList = new ArrayList<>();

        ArrayList<String> diagnosisEntities = entityMap.get("diagnosisEntities");
        ArrayList<String> drugEntities = entityMap.get("drugEntities");
        for (int i = 0; i < diagnosisEntities.size(); i++) {
            for (int j = 0; j < drugEntities.size(); j++) {
                if (kgService.findRelationBetweenDiagAndDrug("diag_"+diagnosisEntities.get(i), "pres_"+drugEntities.get(j))) {
                    relationList.add(new AbstractMap.SimpleEntry<>("diag_"+diagnosisEntities.get(i), "pres_"+drugEntities.get(j)));
                }
            }
        }

        return relationList;
    }


//    /**
//     * Find Relations Between Diseases And Drugs
//     *
//     * @author XYX
//     */
//    public ArrayList<Map.Entry<Integer, Integer>> findRelation(Map<String, ArrayList<String>> entityMap) {
//        ArrayList<Map.Entry<Integer, Integer>> relationList = new ArrayList<>();
//
//        ArrayList<String> diseaseEntities = entityMap.get("diseaseEntities");
//        ArrayList<String> drugEntities = entityMap.get("drugEntities");
//        for (int i = 0; i < diseaseEntities.size(); i++) {
//            for (int j = 0; j < drugEntities.size(); j++) {
//                if (kgService.hasRelation(diseaseEntities.get(i), drugEntities.get(j))) {
//                    relationList.add(new AbstractMap.SimpleEntry<>(i, j));
//                }
//            }
//        }
//
//        return relationList;
//    }

    /**
     * Construct CSV File for Graph Search
     *
     * @author ZY
     */
    public Boolean createQueryCaseInCsv(Map<String, ArrayList<String>> entityMap, Long kgId) {
        ArrayList<String> lines = new ArrayList<>();

        lines.add("SUBJECT_ID,HADM_ID,ICD9_DIAG,ICD9_PROCE,NDC,disease,ATC");

        String line ="1,2,";

        ArrayList<String> diagnosisEntities = entityMap.get("diagnosisEntities");
        int diag_size = diagnosisEntities.size();

        if(diag_size>1) {
            line = line + "\"";
            for (int i = 0; i < diag_size - 1; i++) {
                line = line + diagnosisEntities.get(i) + ",";
            }
            line = line + diagnosisEntities.get(diag_size - 1) + "\"";
        }
        else if(diag_size == 1 ){
            line = line + diagnosisEntities.get(0);
        }
        line = line + ",";

        ArrayList<String> procedureEntities = entityMap.get("procedureEntities");
        int proc_size = procedureEntities.size();
        if(proc_size>1) {
            line = line + "\"";
            for (int i = 0; i < proc_size - 1; i++) {
                line = line + procedureEntities.get(i)+ ",";
            }
            line = line + procedureEntities.get(proc_size - 1) + "\"";
        }
        else if( proc_size ==1 ){
            line = line + procedureEntities.get(0);
        }
        line = line+",";


        line = line+"1,"; //NDC
        line = line+"1,"; //disease


        ArrayList<String> drugEntities = entityMap.get("drugEntities");
        int drug_size = drugEntities.size();
        if(drug_size>1) {
            line = line + "\"";

            for (int i = 0; i < drug_size-1; i++) {
                line = line + drugEntities.get(i) + ",";
            }
            line = line + drugEntities.get(drug_size-1) + "\"";
        }
        else if(drug_size == 1){
            line = line + drugEntities.get(0);
        }

        lines.add(line);


        //read all history admissions from mysql
        ArrayList<HistoryAdmssionEntity> HistoryAdmssionEntities = historyAdmissionMapper.getAllHistoryAdmissions();

        for(int i=0;i<HistoryAdmssionEntities.size();i++)
        {
            String SubjectId = HistoryAdmssionEntities.get(i).getSUBJECT_ID();
            String HadmId = HistoryAdmssionEntities.get(i).getHADM_ID();
            String Icd9_diag = HistoryAdmssionEntities.get(i).getICD9_DIAG().replace(";",",");
            String Icd9_proc = HistoryAdmssionEntities.get(i).getICD9_PROCE().replace(";",",");
            String ndc = HistoryAdmssionEntities.get(i).getNDC().replace(";",",");
            String disease = HistoryAdmssionEntities.get(i).getDisease();
            if(disease == ""){
                logger.error("");
            }
            String atc = HistoryAdmssionEntities.get(i).getATC().replace(";",",");
            line = SubjectId+","+HadmId+","+"\""+Icd9_diag+"\""+","+"\""+Icd9_proc+"\""+","+"\""+ndc+"\""+","+"\""+disease+"\""+","+"\""+atc+"\"";
            lines.add(line);
        }

        String query_case_file_path = fileConfig.getQueryCaseFilePath() + "query_case_" + kgId + ".csv";
        fileUtil.writeListIntoFile(query_case_file_path, lines);
        return true;
    }


    public Map getEntityIDList(String similarhadmId) {

        //get similar entity by similar hadmID
        HistoryAdmssionEntity historyAdmssionEntity = historyAdmissionDao.getHistoryAdmssionEntityById(similarhadmId);

        String diagnosesString = historyAdmssionEntity.getICD9_DIAG();
        ArrayList<String> diagnosisEntities = new ArrayList<>(new HashSet<>(Arrays.asList(diagnosesString.split(";"))));

        String drugString = historyAdmssionEntity.getATC();
        ArrayList<String> drugEntities = new ArrayList<>(new HashSet<>((Arrays.asList(drugString.split(";")))));

        String procedureString = historyAdmssionEntity.getICD9_PROCE();
        ArrayList<String> procedureEntities = new ArrayList<>(new HashSet<>((Arrays.asList(procedureString.split(";")))));

        Map<String, ArrayList<String>> resultMap = new HashMap<>();
        resultMap.put("diagnosisEntities", diagnosisEntities);
        resultMap.put("drugEntities", drugEntities);
        resultMap.put("procedureEntities", procedureEntities);
        return resultMap;
    }

    /**
     * For The Given KG Search For Similar Graphs And Return Admission Id
     *
     * @author ZY
     */
    public SimilarCasesEntity searchForSimilarGraphs(Long kgId) {

        ArrayList<SimilarCasesEntity> similarCasesEntities = similarCasesDao.getSimilarCasesByKgId(kgId);

        if (similarCasesEntities.size() == 0) {

            String query_case_file_path = fileConfig.getQueryCaseFilePath() + "query_case_" + kgId + ".csv";

            String result = "";
//            String execline = "echo 'hello'";
           String execline = "python /home/lf/桌面/MIMIC全/mimic_tester/retrieve_test.py" +
                    " --query_data " + query_case_file_path +
                    " --query_save_data /home/lf/桌面/MIMIC全/mimic_tester/no_p_a_dataset/"+kgId+".pt";

//            String execline =
//                    "python /home/lf/桌面/MIMIC全/mimic_tester/retrieve_test.py " +
//                            "--query_data /home/lf/桌面/MIMIC全/self_dataset/test_query_case.csv " +
//                            "--query_save_data /home/lf/桌面/MIMIC全/mimic_tester/no_p_a_dataset/test_result.pt";

          //  String execline ="";
            ///home/anaconda3/etc/profie.d/
           // /home/lf/anaconda3/pkgs/conda-4.7.12-py37_0/lib/python3.7/site-packages/conda/shell/etc/profile.d

            String condaPath = "/home/lf/anaconda3/pkgs/conda-4.7.12-py37_0/etc/profile.d/";

          //  String condaPath = "/home/lf/anaconda3/pkgs/conda-4.7.12-py37_0/lib/python3.7/site-packages/conda/shell/etc/profile.d/";
            String[] cmdArr=new String[3];
            cmdArr[0]="sh";
            cmdArr[1]="-c";
            cmdArr[2]=". "+condaPath+"conda2.sh;conda activate pytorch;"+execline;
            Runtime rt = Runtime.getRuntime();


            Process proc;
            try {
                proc = Runtime.getRuntime().exec(cmdArr);// 执行py文件
                //用输入输出流来截取结果
                proc.waitFor();
                BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                String line = null;
                while ((line = in.readLine()) != null) {
                    result = result + line;
                }
                in.close();
                logger.error(proc.getErrorStream());
                proc.waitFor();
            } catch (IOException e) {
                e.printStackTrace();
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }

            logger.error("result：" + result);
            String replacement1 = "loading dataset"+query_case_file_path+"  construct  0  empty graphs.";
            String replacement2 = "loading dataset";

            result = result.replace(replacement1,"");
            result = result.replace(replacement2,"");

            ArrayList<String> similarHadmIds = new ArrayList<>();

            ArrayList<String> strs = new ArrayList<>(Arrays.asList(result.split(";")));
            if(strs.size()==0)
            {
                return null;
            }

//            for(int i=0;i<strs.size();i++)
//            {
//                String hadmId = strs.get(i).split(" ")[1];
//                similarHadmIds.add(hadmId);
//            }

            //return 5 hadmIds
            for(int i=0;i<5;i++)
            {
                String hadmId = strs.get(i).split(" ")[1];
                similarHadmIds.add(hadmId);
            }

            //suppose return result
//            similarHadmIds.add("104518");
//            similarHadmIds.add("156857");
//            similarHadmIds.add("112086");
//            similarHadmIds.add("158569");
//            similarHadmIds.add("190243");
//            similarHadmIds.add("160891");
//            similarHadmIds.add("170324");
//            similarHadmIds.add("127870");
//            similarHadmIds.add("127203");
//            similarHadmIds.add("164174");
//            similarHadmIds.add("164174");


            ArrayList<String> similarcasesKgIds = new ArrayList<>();

            for (String similarHadmId : similarHadmIds) {

             //   System.out.println("create similar graphs for"+similarHadmId);

                Map<String, ArrayList<String>> entityMap = getEntityIDList(similarHadmId);

                ArrayList<String> diagnosisEntities = entityMap.get("diagnosisEntities");
                int diag_size =diagnosisEntities.size();
                String ICD9_DIAG ="";
                for (int i = 0; i < diag_size-1 ; i++) {
                    ICD9_DIAG  = ICD9_DIAG+ diagnosisEntities.get(i)+";";
                }
                ICD9_DIAG = ICD9_DIAG + diagnosisEntities.get(diag_size-1);

                ArrayList<String> procedureEntities = entityMap.get("procedureEntities");
                String ICD9_PROCE ="";
                int size =procedureEntities.size();
                for (int j = 0; j < size-1; j++) {
                    ICD9_PROCE = ICD9_PROCE + procedureEntities.get(j)+";";
                }
                ICD9_PROCE = ICD9_PROCE + procedureEntities.get(size-1);

                ArrayList<String> drugEntities = entityMap.get("drugEntities");
                String ATC ="";
                int drug_size =drugEntities.size();
                for (int j = 0; j < drug_size-1; j++) {
                    ATC = ATC + drugEntities.get(j)+";";
                }
                ATC = ATC + drugEntities.get(drug_size-1);

                ArrayList<Map.Entry<String, String>> relationListBetweenDiagAndDrug = this.findRelationBetweenDiagAndDrug(entityMap);

        //        System.out.println("hadmId:"+similarHadmId+"relation size="+relationListBetweenDiagAndDrug.size());


                ArrayList<Map.Entry<String, String>> relationListBetweenDiagAndProc = this.findRelationBetweenDiagAndProc(entityMap);

        //        System.out.println("hadmId:"+similarHadmId+"relation size="+relationListBetweenDiagAndProc.size());


                MedicalCaseEntity medicalCase = new MedicalCaseEntity();
                medicalCase.setUserId((long) 11111111);
                medicalCase.setTitle("similar_graph_to" + kgId.toString());
                medicalCase.setCategory("");
                medicalCase.setDescription("");
                medicalCase.setZipFilePath("");
                medicalCase.setStatus(false);
                Long similarkgId = kgService.createMedicalCaesGraph(relationListBetweenDiagAndDrug, relationListBetweenDiagAndProc, medicalCase);
                medicalCase.setKgId(similarkgId);
                medicalCase.setATC(ATC);
                medicalCase.setICD9_DIAG(ICD9_DIAG);
                medicalCase.setICD9_PROCE(ICD9_PROCE);
                medicalCaseDao.insertMedicalCase(medicalCase);
                similarcasesKgIds.add(similarkgId.toString());
            }

            //write in similar_cases

            StringBuilder kgIds = new StringBuilder();
            for (String s : similarcasesKgIds) {
                kgIds.append(s);
                kgIds.append(",");
            }
            String kgIdStr = kgIds.toString();
            if (kgIdStr.length()>0) kgIdStr = kgIdStr.substring(0, kgIds.length()-1);

            StringBuilder hadmIds = new StringBuilder();
            for (String s : similarHadmIds) {
                hadmIds.append(s);
                hadmIds.append(",");
            }
            String hadmIdStr = hadmIds.toString();
            if (hadmIdStr.length()>0) hadmIdStr = hadmIdStr.substring(0, hadmIds.length()-1);

            SimilarCasesEntity similarCasesEntity = new SimilarCasesEntity();
            similarCasesEntity.setKgId(kgId);
            similarCasesEntity.setSimilar_case_kgIds(kgIdStr);
            similarCasesEntity.setSimilar_case_hadmIds(hadmIdStr);
            similarCasesDao.insertNewSimilarSearchResult(similarCasesEntity);
            return similarCasesEntity;
        }

        else {
            return similarCasesEntities.get(0);
        }
    }

//    public String getDoctorDiagnosis(Long admissionId) {
//        ArrayList<DoctorDiagnosisEntity> doctorDiagnosisEntities = doctorDiagnosisDao.getDiagnosisByAdmissionId(admissionId);
//        if (doctorDiagnosisEntities.size() != 0) {
//            return doctorDiagnosisEntities.get(0).getDoctor_diagnosis();
//        } else {
//            return "";
//        }
//    }


    public ArrayList<String> getNoteEventsByHadmId(String hadmId) {

        ArrayList<String> noteEvents = historyAdmissionDao.getNoteEventsByHadmId(hadmId);
        ArrayList<String> new_noteEvents = new ArrayList<>();

        if(noteEvents.size()!=0)
        {
            for(String s: noteEvents)
            {
                s = s.replace("<br>","\n");
                new_noteEvents.add(s);
            }
        }
        return new_noteEvents;
    }

}
