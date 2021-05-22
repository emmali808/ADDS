package com.java.adds.controller;

import com.alibaba.fastjson.JSONObject;
import com.java.adds.config.UploadFileConfig;
import com.java.adds.entity.HistoryAdmssionEntity;
import com.java.adds.entity.MedicalArchiveEntity;
import com.java.adds.entity.MedicalCaseEntity;
import com.java.adds.entity.MedicalCaseEntity;
import com.java.adds.service.CaseSearchService;
import com.java.adds.service.HistoryAdmissionService;
import com.java.adds.service.MedicalCaseService;
import com.java.adds.service.MedicalCaseService;
import com.java.adds.utils.FileUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Medical Case Controller
 * @author ZY
 */
@RestController
@RequestMapping(value = "/case")
public class MedicalCaseController {
    @Autowired
    private MedicalCaseService medicalCaseService;

    @Autowired
    private HistoryAdmissionService historyAdmissionService;

    @Autowired
    private UploadFileConfig fileConfig;

    @Autowired
    private FileUtil fileUtil;

    final static Logger logger = Logger.getLogger(CaseSearchService.class);


    /**
     * Author: ZY
     * Show clustering Result
     */

    @RequestMapping(value = "/cluster/result", method = RequestMethod.GET)
    public List<JSONObject> getClusterResult() {
        return medicalCaseService.getClusterResult();
    }

    /**
     * Author: ZY
     * Upload Dataset for clustering
     */
    @RequestMapping(value = "/dataset/{userId}", method = RequestMethod.POST)
    public Map<String, Object> uploadDatasetForClustering(HttpServletResponse response,
                                                          @RequestParam("file") MultipartFile file,

//                                                          @RequestParam("title") String title,
//                                                          @RequestParam("category") String category,
                                                          @RequestParam("cluster_num") Long cluster_num,
                                                          @PathVariable Long userId) {
        Map<String, Object> res = new HashMap<>();
        String fileName = fileUtil.getFileNameWithTimeStamp(userId.toString(), file.getOriginalFilename());
        String filePath = fileConfig.getDatasetForClusterCsvPath() + fileName;
        File dest = new File(filePath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            response.setStatus(501);
            res.put("error", "文件保存失败");
            return res;
        }

        String query_save_path = filePath;
        String cluster_res_path = fileConfig.getResultForClusterPath();


        String result = "";
        String execline ="cd /home/lf/桌面/MIMIC全/mimic_tester; python train_cluster.py --batch_size 256 --train_data all_dataset/train_pa_include_remove_addm_undirect_onto_data.pt --test_data all_dataset/test_pa_include_remove_addm_undirect_onto_data.pt --valid_data all_dataset/valid_pa_include_remove_addm_undirect_onto_data.pt  --use_glb 2 --train_input_data all_dataset/train_saved_pa_include_remove_addm_undirect_onto_data.pt --use_emb 2 --output_dir pkdd_output/test_conv_2 --do_cluster_query " +
                "--cluster_nums " +
                cluster_num +
                " --cluster_res_path " +
                cluster_res_path+"cluster_res.txt " +
                "--query_save_path " +
                query_save_path;


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

        logger.error(result);

        res.put("success","successfully cluster!");

        return res;
    }


    /**
     * Author: XYX
     * Get Medical Case List By User Id
     */
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public ArrayList<MedicalCaseEntity> getCaseList(@PathVariable("userId") Long userId) {
        //todo: implement vo for front end
        return medicalCaseService.getMedicalCaseByUserId(userId);
    }

    /**
     * Author: ZY
     * Upload Medical Case
     */
    @RequestMapping(value = "/history_admissions/{userId}", method = RequestMethod.POST)
    public Map<String, Object> uploadHistoryAdmissions(HttpServletResponse response, @RequestParam("file") MultipartFile file, @RequestParam("title") String title, @RequestParam("category") String category, @RequestParam("desc") String desc, @PathVariable Long userId) {
        Map<String, Object> res = new HashMap<>();
        String fileName = fileUtil.getFileNameWithTimeStamp(userId.toString(), file.getOriginalFilename());
        String filePath = fileConfig.getHistoryAdmissionsCsvPath() + fileName;
        File dest = new File(filePath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }
        try {
            file.transferTo(dest);
        } catch (IOException e) {
            response.setStatus(501);
            res.put("error", "文件保存失败");
            return res;
        }

        //read from csv and write to mysql
        HistoryAdmssionEntity historyAdmssionEntity = new HistoryAdmssionEntity();

        try {

            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            reader.readLine();
            String line = null;
            while((line=reader.readLine())!=null){
                String item[] = line.split(",");
                historyAdmssionEntity.setSUBJECT_ID(item[0]);
                historyAdmssionEntity.setHADM_ID(item[1]);
                historyAdmssionEntity.setICD9_DIAG(item[2]);
                historyAdmssionEntity.setICD9_PROCE(item[3]);
                historyAdmssionEntity.setNDC(item[4]);
                historyAdmssionEntity.setDisease(item[5]);
                historyAdmssionEntity.setATC(item[6]);
                historyAdmssionEntity.setTEXT(item[7]);
                historyAdmissionService.insertHistoryAdmissions(historyAdmssionEntity);
            }
            res.put("success","successfully save to database");

        } catch (Exception e) {
            e.printStackTrace();
            res.put("error", "数据库写入失败");
        }

        return res;
    }



    /**
     * Author: ZY
     * Upload Medical Case
     */
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.POST)
    public Map<String, Object> uploadMedicalCase(HttpServletResponse response, @RequestParam("file") MultipartFile file, @RequestParam("title") String title, @RequestParam("category") String category, @RequestParam("desc") String desc, @PathVariable Long userId) {
        Map<String, Object> res = new HashMap<>();

        String fileName = fileUtil.getFileNameWithTimeStamp(userId.toString(), file.getOriginalFilename());
        String filePath = fileConfig.getMedicalCaseFilePath() + fileName;
        File dest = new File(filePath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }

        try {
            file.transferTo(dest);
        } catch (IOException e) {
            response.setStatus(501);
            res.put("error", "文件保存失败");
            return res;
        }

        MedicalCaseEntity medicalCase = new MedicalCaseEntity();
        medicalCase.setUserId(userId);
        medicalCase.setTitle(title);
        medicalCase.setCategory(category);
        medicalCase.setDescription(desc);
        medicalCase.setZipFilePath(filePath);
        medicalCase.setStatus(false);

        Long medicalCaseId = medicalCaseService.uploadMedicalCase(medicalCase);
        if (medicalCaseId >= 0) {
            response.setStatus(200);
            res.put("medicalCaseId", medicalCaseId);
        } else {
            response.setStatus(502);
            res.put("error", "数据库写入失败");
        }
        return res;
    }

    /**
     * Author: ZY
     * Download Sample Csv For Clustering
     */
    @RequestMapping(value = "/download/SampleCsvFileForCluster", method = RequestMethod.GET)
    public Map<String, Object> downloadSampleCsvFileForCluster(HttpServletResponse response) {
        Map<String, Object> res = new HashMap<>();

        String filePath = fileConfig.getDatasetForClusteringSampleCsvPath();

        String[] filePathParts = filePath.split("/");
        String fileName = filePathParts[filePathParts.length - 1];

        response.addHeader("Content-Disposition","attachment;filename=" + fileName);
        try {
            FileCopyUtils.copy(new FileInputStream(filePath), response.getOutputStream());
            response.setStatus(200);
            res.put("success", "successfully download!");
        } catch (IOException e) {
            response.setStatus(504);
            res.put("error", "文件下载失败");
        }
        return res;
    }


    /**
     * Author: XYX
     * Download Medical Archive By Archive's Id
     */
    @RequestMapping(value = "/download/SampleCsvFile", method = RequestMethod.GET)
    public Map<String, Object> downloadSampleCsvFile(HttpServletResponse response) {
        Map<String, Object> res = new HashMap<>();

        String filePath = fileConfig.getMedicalCaseSearchSampleCsvPath();

        String[] filePathParts = filePath.split("/");
        String fileName = filePathParts[filePathParts.length - 1];

        response.addHeader("Content-Disposition","attachment;filename=" + fileName);
        try {
            FileCopyUtils.copy(new FileInputStream(filePath), response.getOutputStream());
            response.setStatus(200);
            res.put("success", "successfully download!");
        } catch (IOException e) {
            response.setStatus(504);
            res.put("error", "文件下载失败");
        }
        return res;
    }
    /**
     * Author: XYX
     * Download Medical Archive By Archive's Id
     */
    @RequestMapping(value = "/download/ClusterReport", method = RequestMethod.GET)
    public Map<String, Object> downloadClusterReport(HttpServletResponse response) throws IOException {

        Map<String, Object> res = new HashMap<>();
//        MedicalArchiveEntity medicalArchiveEntity = medicalArchiveService.getMedicalArchiveById(archiveId);
//        if (medicalArchiveEntity == null) {
//            response.setStatus(404);
//            res.put("error", "找不到文件");
//            return res;
//        }
//        String filePath = medicalArchiveEntity.getTxtFilePath();
//        String filePath = fileConfig.getClusterReportZipPath();

        String txtfilePath =  fileConfig.getResultForClusterPath()+"cluster_res.txt";
        String[] filePathParts = txtfilePath.split("/");
        String txtfileName = filePathParts[filePathParts.length - 1];

        response.addHeader("Content-Disposition","attachment;filename=" + txtfileName);
        try {
            FileCopyUtils.copy(new FileInputStream(txtfilePath), response.getOutputStream());
            response.setStatus(200);
            res.put("success", "successfully download!");
        } catch (IOException e) {
            response.setStatus(504);
            res.put("error", "文件下载失败");
        }

        return res;
    }

    /**
     * Author: XYX
     * Download Medical Archive By Archive's Id
     */
    @RequestMapping(value = "/download/SampleImageFile", method = RequestMethod.GET)
    public Map<String, Object> downloadSampleImageFile(HttpServletResponse response) {
        Map<String, Object> res = new HashMap<>();
//
//        MedicalArchiveEntity medicalArchiveEntity = medicalArchiveService.getMedicalArchiveById(archiveId);
//        if (medicalArchiveEntity == null) {
//            response.setStatus(404);
//            res.put("error", "找不到文件");
//            return res;
//        }

//        String filePath = medicalArchiveEntity.getTxtFilePath();

        String filePath = fileConfig.getMedicalCaseSearchSampleImagePath();

        String[] filePathParts = filePath.split("/");
        String fileName = filePathParts[filePathParts.length - 1];

        response.addHeader("Content-Disposition","attachment;filename=" + fileName);
        try {
            FileCopyUtils.copy(new FileInputStream(filePath), response.getOutputStream());
            response.setStatus(200);
            res.put("success", "successfully download!");
        } catch (IOException e) {
            response.setStatus(504);
            res.put("error", "文件下载失败");
        }
        return res;
    }

}
