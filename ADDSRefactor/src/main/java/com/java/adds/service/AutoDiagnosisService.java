package com.java.adds.service;

import com.java.adds.controller.vo.FilterQuestionVO;
import com.java.adds.controller.vo.QuestionAnswerVO;
import com.java.adds.dao.DoctorDao;
import com.java.adds.entity.DataSetsEntity;
import com.java.adds.entity.DeepModelTaskEntity;
import com.java.adds.entity.DoctorEntity;
import com.java.adds.entity.QuestionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AutoDiagnosisService {

    @Autowired
    OCRService ocrService;

    /**
     * Diagnose
     * @author xyx
     */
    public void diagnose(String filePath)
    {
        String file = "/ocr_result.txt";

        this.runOCR(filePath);
        this.createGraph(filePath + file);
    }

    /**
     * Run OCR recognition to transform images in DOC file to text
     * @author xyx
     */
    public void runOCR(String filePath)
    {
        ocrService.runPython("unzip.py", filePath);
        ocrService.runPython("extra_png_from_word.py", filePath);
        ocrService.runPython("ocr_to_txt.py", filePath);
    }

    /**
     * Create Knowledge Graph from given clinical case history text
     * @author xyx
     */
    public void createGraph(String filePath)
    {

    }
}
