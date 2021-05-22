package com.java.adds.service;

import com.alibaba.fastjson.JSONObject;
import com.java.adds.config.UploadFileConfig;
import com.java.adds.dao.MedicalCaseDao;
import com.java.adds.entity.MedicalCaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Medical Case Service
 * @author ZY
 */
@Service
public class MedicalCaseService {

    @Autowired
    private MedicalCaseDao medicalCaseDao;

    @Autowired
    private OCRService ocrService;

    @Autowired
    private UploadFileConfig fileConfig;

    /**
     * Get Medical Case By User's Id
     * @param userId user's id
     * @return A KG ArrayList
     */
    public ArrayList<MedicalCaseEntity> getMedicalCaseByUserId(Long userId) {
        return medicalCaseDao.getMedicalCaseByUserId(userId);
    }

    /**
     * Upload Medical Case
     * @param medicalCase medical case
     * @return medical case id
     */
    public Long uploadMedicalCase(MedicalCaseEntity medicalCase) {
        Long medicalCaseId = medicalCaseDao.uploadMedicalCaseByUserId(medicalCase);
        if (medicalCaseId >= 0) {
            return medicalCaseId;
        } else {
            return -1L;
        }
    }

    /**
     * Update Medical Case
     * @param medicalCase medical case
     * @return medical case id
     */
    public Long updateMedicalCase(MedicalCaseEntity medicalCase) {
        Long medicalCaseId = medicalCaseDao.updateMedicalCase(medicalCase);
        if (medicalCaseId >= 0) {
            return medicalCaseId;
        } else {
            return -1L;
        }
    }

    /**
     * Get Medical Case By Id
     * @param caseId medical case id
     * @return medical case id
     */
    public MedicalCaseEntity getMedicalCaseById(Long caseId) {
        return medicalCaseDao.getMedicalCaseById(caseId);
    }

    public List<JSONObject>  getClusterResult() {
        List<JSONObject> seriesList = new ArrayList<JSONObject>();

        //read from cluster_res.txt and return data for echart

        try {
            String resultFilePath = fileConfig.getResultForClusterPath()+"cluster_res.txt";

            String encoding="GBK";

            File file=new File(resultFilePath);

            if(file.isFile() && file.exists()){ //判断文件是否存在

                InputStreamReader read = new InputStreamReader(

                        new FileInputStream(file),encoding);//考虑到编码格式

                BufferedReader bufferedReader = new BufferedReader(read);

                String lineTxt = null;

                while((lineTxt = bufferedReader.readLine()) != null){

//                    System.out.println(lineTxt);
                    // split by space
                    // bingli id, bingli juleiId, x, y

                    String[] splitTextList = lineTxt.split("\t");
                    String CASE_ID = splitTextList[0];
                    Integer Label_ID = Integer.parseInt(splitTextList[1]);
                    Float X = Float.parseFloat(splitTextList[2]);
                    Float Y = Float.parseFloat(splitTextList[3]);
                    Float coordinate[] = {X,Y};
                    JSONObject echart_item = new JSONObject();
                    JSONObject color_item = new JSONObject();
                    color_item.put("color",Integer.toString(Label_ID));
                    echart_item.put("value",coordinate);
                    JSONObject itemStyle = new JSONObject();
                    itemStyle.put("color",color_item);
                    echart_item.put("itemStyle",itemStyle);
                    echart_item.put("name",CASE_ID);
//                    if (CASE_ID.equals("156857")) {
//                        echart_item.put("diagnosis", "<br>1.  Continue further workup for neurologic, metabolic,<br>    cardiac, and genetic etiologies of the infant's medical<br>    condition.<br>2.  The infant is on TPM.<br>3.  The infant has received no immunizations thus far.<br><br>");
//                    } else if (CASE_ID.equals("112086")) {
//                        echart_item.put("diagnosis", "<br>s/p AVR<br>AS<br>elev. chol. HTN<br>UTI<br>NIDDM<br>diverticulosis<br>GERD<br>hiatal hernia<br>obesity<br>s/p bladder suspension<br><br><br>");
//                    } else if (CASE_ID.equals("127870")) {
//                        echart_item.put("diagnosis", "<br>1.  Status post redo coronary artery bypass grafting times<br>two and aortic valve replacement.<br>2.  Coronary artery disease.<br>3.  Hypercholesterolemia.<br>4.  Renal artery stenosis.<br><br>");
//                    } else if (CASE_ID.equals("127203")) {
//                        echart_item.put("diagnosis", "NICU TRIAGE NOTE<br>     BABY BOY [**Known lastname 391**] ADM TO TRIAGE FOR SEPSIS EVAL.  PLEASE SEE ABOVE FOR HX.  PALE/[**Known lastname **] QUIET ALERT INFANT.  AVXX.  ADM CARE DONE.  CBC AND BLOOD CX SENT.  ONE TOUCH 44, FED 35CC E20 WITH FE.  AMPICILLIN AND GENTAMYCIN GIVEN AS ORDERED. TRANSFERRED TO 6 NURSERY AT 0020.<br>");
//                    } else{
//
//                        echart_item.put("diagnosis", ".");
//
//                    }
                    seriesList.add(echart_item);
                }
                read.close();

            }else{
                JSONObject echart_item = new JSONObject();
                echart_item.put("res","找不到指定的文件");
                seriesList.add(echart_item);
                System.out.println("找不到指定的文件");

            }

        } catch (Exception e) {

            JSONObject echart_item = new JSONObject();
            echart_item.put("res","读取文件内容出错");
            seriesList.add(echart_item);
            System.out.println("读取文件内容出错");
            e.printStackTrace();
        }
        return seriesList;
    }

}
