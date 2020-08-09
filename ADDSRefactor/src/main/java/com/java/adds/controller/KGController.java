package com.java.adds.controller;

import com.java.adds.config.UploadFileConfig;
import com.java.adds.entity.KGEntity;
import com.java.adds.service.KGService;
import com.java.adds.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * KG Controller
 * @author QXL
 */
@RestController
@RequestMapping(value = "/kg")
public class KGController {
    @Autowired
    private KGService kgService;

    @Autowired
    private UploadFileConfig fileConfig;

    @Autowired
    private FileUtil fileUtil;

    /**
     * Author: QXL
     * Get Knowledge-Graph list by user id
     */
    @RequestMapping(value = "/doctor/{doctorId}", method = RequestMethod.GET)
    public ArrayList<KGEntity> getKGList(@PathVariable("doctorId") Long doctorId) {
        return kgService.getKGListByUserId(doctorId);
    }

    /**
     * Author: QXL
     * Get Knowledge-Graph by KG id
     */
    @RequestMapping(value = "/graph/{kgId}", method = RequestMethod.GET)
    public Map<String, Object> getKGById(@PathVariable("kgId") Long kgId) {
        return kgService.getKGById(kgId);
    }

    /**
     * Author: QXL
     * Get Knowledge-Graph relational nodes by node id
     */
    @RequestMapping(value = "/graph/relNodes/{nodeId}", method = RequestMethod.GET)
    public Map<String, Object> getRelNodes(@PathVariable("nodeId") Long nodeId) {
        return kgService.getRelNodes(nodeId);
    }

    @RequestMapping(value = "/createGraph", method = RequestMethod.GET)
    public void createGraph() {
        kgService.createGraph();
    }

    /**
     * Author: QXL
     * Upload Knowledge-Graph
     */
    @RequestMapping(value = "/upload/{doctorId}", method = RequestMethod.POST)
    public Map<String, Object> uploadKG(HttpServletResponse response, @RequestParam("file") MultipartFile file, @RequestParam("name") String name, @RequestParam("desc") String desc, @PathVariable Long doctorId) {
        Map<String, Object> res = new HashMap<>();

        String fileName = file.getOriginalFilename();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date date = new Date();
        String currentTime = format.format(date);
        fileName = doctorId.toString() + currentTime + fileName;
        String filePath = fileConfig.getKgFilePath() + fileName;
        File dest = new File(filePath);
        if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }

        if (!fileUtil.csvFileType(dest)) {
            response.setStatus(400);
            res.put("error", "文件类型错误");
            return res;
        }

        try {
            file.transferTo(dest);
        } catch (IOException e) {
            response.setStatus(501);
            res.put("error", "文件保存失败");
            return res;
        }

        if (!fileUtil.kgFileFormat(dest)) {
            // 如果格式不正确，删除已保存的文件
//            fileUtil.deleteFile(dest);
            response.setStatus(401);
            res.put("error", "文件数据格式错误");
            return res;
        }

        Long kgId = kgService.uploadKG(doctorId, name, desc, filePath);
        response.setStatus(200);
        res.put("success", kgId);
        return res;
    }
}
