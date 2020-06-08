package cn.medicine.service.impl;

import cn.medicine.dao.*;
import cn.medicine.pojo.*;
import cn.medicine.service.PatientGraphService;
import cn.medicine.utils.MyException;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.security.auth.Subject;
import java.lang.Integer;
import java.lang.NumberFormatException;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2016/9/1.
 */
@Service
public class PGService implements PatientGraphService{
    private static Logger logger = LoggerFactory.getLogger(PGService.class);

    //@Resource
    @Resource
    private PatientGraphDao pgDao;
    @Resource
    private DiagnoseDao diagDao;
    @Resource
    private SymptomsDao symDao;
    @Resource
    private PreviousMHDao preDao;
    @Resource
    private TreatmentDao treatDao;
    @Resource
    private LabTestDao labDao;

    public int[] d2;
    public String d; //诊断结果
    public String s; //症状
    public String p; //历史病例
    public String t; //治疗方法
    public String l; //检测结果

    public static List<String> strd = new ArrayList();
    public static List<String> strs = new ArrayList();
    public static List<String> strp = new ArrayList();
    public static List<String> strt = new ArrayList();
    public static List<String> strl = new ArrayList();
    public static List<String> strtime = new ArrayList();

    @Override
    public List<Graph> getpatientRecord(String pgID, String g_time) throws MyException{
        // Graph record = new ;
        //logger.info("================================================={}", pgDao);
        //System.out.println(pgDao);

        //ArrayList List = new ArrayList();
/*        for(int i = 0; i< pgDao.getpatientRecord(pgID,g_time).size();i++){*/
        try {
            List<Graph> resultList = pgDao.getpatientRecord(pgID,g_time);
            for(Graph obj:resultList){
                strtime.add(obj.getG_time());
                //System.out.println(strtime);
                strd.add(obj.getG_diagnose());
                strs.add(obj.getG_symptom());
                strp.add(obj.getG_previousMH());
                strt.add(obj.getG_treatment());
                strl.add(obj.getG_labtest());
            }
/*            strtime.add(pgDao.getpatientRecord(pgID, g_time).get(i).getG_time());
            strd.add(pgDao.getpatientRecord(pgID, g_time).get(i).getG_diagnose());
                strs.add(pgDao.getpatientRecord(pgID,g_time).get(i).getG_symptom());
            strp.add(pgDao.getpatientRecord(pgID,g_time).get(i).getG_previousMH());
            strt.add(pgDao.getpatientRecord(pgID,g_time).get(i).getG_treatment());
            strl.add(pgDao.getpatientRecord(pgID,g_time).get(i).getG_labtest());*/

                //System.out.println(strd);


        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
      /*  }*/

        return pgDao.getpatientRecord(pgID,g_time);
    }

    public  void StringtoInt(String i){
        String[] d3;
        d3 = i.split(" ");
        d2 = new int[d3.length];
        for(int j=0;j<d3.length;j++){
            d2[j]=Integer.parseInt(d3[j]);
        }
       // return d2;
    }
    @Override
    public List<Diagnose> getpatientDiagnose(long diagid){


        return diagDao.getpatientDiagnose(diagid);

    }
    @Override
    public List<Symptoms> getpatientSymptoms(long symid){

        return symDao.getpatientSymptoms(symid);
    }

    @Override
    public List<PreviousMH> getpatientPreviousMH(long preid){

        return preDao.getpatientPreviousMH(preid);
    }

    @Override
    public List<Treatment> getpatientTreatment(long treatid){

        return treatDao.getpatientTreatment(treatid);

    }

    @Override
    public List<LabTest> getpatientLabTest(long labid){

        return labDao.getpatientLabTest(labid);

    }

    @Override
    public List getd(){
        List list=new ArrayList();
        if(d.length()!=0) {
            StringtoInt(d);
            for (int i = 0; i < d2.length; i++) {
                list = ArraryList.Getlist(list, diagDao.getpatientDiagnose(d2[i]));
            }
            d2=null;
            //return list;
        }
        return list;
    }
    @Override
    public List gets(){
        List list = new ArrayList();
        if(s.length()!=0) {
            StringtoInt(s);
            for (int i = 0; i < d2.length; i++) {
                list = ArraryList.Getlist(list, symDao.getpatientSymptoms(d2[i]));
            }
            d2 = null;
        }
        return list;
    }
    @Override
    public List gett(){
        List list = new ArrayList();
        if(t.length()!=0) {
            StringtoInt(t);
            for (int i = 0; i < d2.length; i++) {
                list = ArraryList.Getlist(list, treatDao.getpatientTreatment(d2[i]));
            }
            d2 = null;
        }
        return list;
    }
    @Override
    public List getp(){
        List list=new ArrayList();
        if(p.length()!=0) {
            StringtoInt(p);
            for (int i = 0; i < d2.length; i++) {
                list = ArraryList.Getlist(list, preDao.getpatientPreviousMH(d2[i]));
            }
            d2 = null;
        }
        return list;
    }
    @Override
    public List getl(){
        List list=new ArrayList();
        if(l.length()!=0) {
            StringtoInt(l);
            for (int i = 0; i < d2.length; i++) {
                list = ArraryList.Getlist(list, labDao.getpatientLabTest(d2[i]));
            }
            d2 = null;
        }
        return list;
    }
    @Override
    public List getall(){
        List list=new ArrayList();
        for(int i = 0; i < strd.size();i++)
        {
            ArraryList.strtime = strtime;
            d = strd.get(i);
            s = strs.get(i);
            p = strp.get(i);
            t = strt.get(i);
            l = strl.get(i);
            list.addAll(getd());
            list.addAll(gets());
            list.addAll(getp());
            list.addAll(gett());
            list.addAll(getl());
         //   ArraryList.getAllInformation();
        }
        return list;
    }
    @Override
    public void addClinic(Diagnose diagnose,List<Symptoms> symptoms,List<LabTest> labTest,List<PreviousMH> previousMH,List<Treatment> treatment,Graph graph){
      /*  treatDao.add(treatment);
        System.out.println("treatDao.getMaxID():"+treatDao.getMaxID());*/
        diagDao.add(diagnose);
        graph.setG_diagnose(diagDao.getMaxID()+"");
        String indexString="";
        for(Symptoms sym:symptoms){
            symDao.add(sym);
            indexString+=" "+symDao.getMaxID();
        }
        graph.setG_symptom(indexString.substring(1));
        indexString="";

        for(LabTest lab:labTest){
            labDao.add(lab);
            indexString+=" "+labDao.getMaxID();
        }

        graph.setG_labtest(indexString.substring(1));
        indexString="";

        for(PreviousMH previousMH1:previousMH){
            preDao.add(previousMH1);
            indexString+=" "+preDao.getMaxID();
        }
        graph.setG_previousMH(indexString.substring(1));
        indexString="";

        for(Treatment treatment1:treatment){
            treatDao.add(treatment1);
            indexString+=" "+treatDao.getMaxID();
        }
        graph.setG_treatment(indexString.substring(1));
        indexString="";

        pgDao.add(graph);
    }
    @Override
    public  List<PatientGraph>  getPatientGraph(String pgID){
        List<Graph> graphs=pgDao.getpatientGraph(pgID);
        List<PatientGraph> list=new ArrayList<>();

        String[] strings; //症状
        int max=9;
        int min=1;
        Random random = new Random();


        for(Graph graph:graphs){
            int index=0;
            PatientGraph patientGraph=new PatientGraph();
            JSONArray nodes=new JSONArray();
            JSONArray links=new JSONArray();
            patientGraph.setTime(graph.getG_time());//时间
            JSONObject jsonObject1=new JSONObject();
            //加入根节点
            int root=index;
            jsonObject1.put("index",index);
            jsonObject1.put("name",diagDao.getpatientDiagnose(Long.parseLong(graph.getG_diagnose())).get(0).getDiagnose());
            jsonObject1.put("value", random.nextInt(max)%(max-min+1) + min);
            jsonObject1.put("category", 0);
            nodes.add(jsonObject1);
            index++;
            //加入症状
            strings=graph.getG_symptom().split(" ");
            for(int i=0;i<strings.length;i++){
                Symptoms symptoms=symDao.getpatientSymptoms(Long.parseLong(strings[i])).get(0);
                int symptom=index;
                JSONObject jsonObject2=new JSONObject();

                jsonObject2.put("index",index);
                jsonObject2.put("name",symptoms.getSymptoms());
                jsonObject2.put("value", random.nextInt(max)%(max-min+1) + min);
                jsonObject2.put("category", 2);
                nodes.add(jsonObject2);
                index++;
                JSONObject jsonObject3=new JSONObject();
                jsonObject3.put("source", root);
                jsonObject3.put("target", index - 1);
                links.add(jsonObject3);
                if(symptoms.getS_attribute().equals("1")==false){
                    System.out.println(symptoms.getS_attribute());

                    JSONObject jsonObject11=new JSONObject();

                    jsonObject11.put("index",index);
                    jsonObject11.put("name",symptoms.getS_attribute());
                    jsonObject11.put("value", random.nextInt(max)%(max-min+1) + min );
                    jsonObject11.put("category", 5);
                    nodes.add(jsonObject11);
                    index++;
                    JSONObject jsonObject12=new JSONObject();

                    jsonObject12.put("target", symptom);
                    jsonObject12.put("source", index - 1);
                    links.add(jsonObject12);
                }


            }

            //加入历史病
            strings=graph.getG_previousMH().split(" ");
            for(int i=0;i<strings.length;i++){
                PreviousMH previousMH=preDao.getpatientPreviousMH(Long.parseLong(strings[i])).get(0);
                if(previousMH.getPre_attribute().matches("有")){
                    JSONObject jsonObject4=new JSONObject();
                    jsonObject4.put("index",index);
                    jsonObject4.put("name",previousMH.getPreviousMH());
                    jsonObject4.put("value",random.nextInt(max)%(max-min+1) + min );
                    jsonObject4.put("category", 1);
                    nodes.add(jsonObject4);
                    index++;
                    JSONObject jsonObject5=new JSONObject();
                    jsonObject5.put("target", root);
                    jsonObject5.put("source", index - 1);
                    links.add(jsonObject5);
                }

            }

            //加入LabTest
            strings=graph.getG_labtest().split(" ");
            for(int i=0;i<strings.length;i++){
                LabTest labTest=labDao.getpatientLabTest(Long.parseLong(strings[i])).get(0);
                int labtest=index;
                JSONObject jsonObject5=new JSONObject();

                jsonObject5.put("index",index);
                jsonObject5.put("name",labTest.getLabtest());
                jsonObject5.put("value", random.nextInt(max)%(max-min+1) + min );
                jsonObject5.put("category", 3);
                nodes.add(jsonObject5);
                index++;
                JSONObject jsonObject6=new JSONObject();
                jsonObject6.put("target", root);
                jsonObject6.put("source", index - 1);
                links.add(jsonObject6);

                JSONObject jsonObject13=new JSONObject();

                jsonObject13.put("index",index);
                jsonObject13.put("name",labTest.getLab_attribute());
                jsonObject13.put("value", random.nextInt(max)%(max-min+1) + min );
                jsonObject13.put("category", 5);
                nodes.add(jsonObject13);
                index++;
                JSONObject jsonObject14=new JSONObject();

                jsonObject14.put("target", labtest);
                jsonObject14.put("source", index - 1);
                links.add(jsonObject14);
            }
            //加入治疗
            strings=graph.getG_treatment().split(" ");
            for(int i=0;i<strings.length;i++){
                Treatment treatment=treatDao.getpatientTreatment(Long.parseLong(strings[i])).get(0);
                int medical=index;
                JSONObject jsonObject7=new JSONObject();

                jsonObject7.put("index",index);
                jsonObject7.put("name",treatment.getTreatment());
                jsonObject7.put("value", random.nextInt(max)%(max-min+1) + min );
                jsonObject7.put("category", 4);
                nodes.add(jsonObject7);
                index++;
                JSONObject jsonObject8=new JSONObject();
                jsonObject8.put("target", root);
                jsonObject8.put("source", index - 1);
                links.add(jsonObject8);
                //加入药物和剂量关系
                JSONObject jsonObject9=new JSONObject();

                jsonObject9.put("index",index);
                jsonObject9.put("name",treatment.getT_attribute());
                jsonObject9.put("value", random.nextInt(max)%(max-min+1) + min );
                jsonObject9.put("category", 5);
                nodes.add(jsonObject9);
                index++;
                JSONObject jsonObject10=new JSONObject();

                jsonObject10.put("target", medical);
                jsonObject10.put("source", index - 1);
                links.add(jsonObject10);
            }

            System.out.println("links:"+links.toJSONString());
            System.out.println("nodes:"+nodes.toJSONString());

            patientGraph.setLinks(links);
             patientGraph.setNodes(nodes);

            list.add(patientGraph);
        }
        return list;
    }


}
