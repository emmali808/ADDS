package com.java.adds.service;

import com.java.adds.config.UploadFileConfig;
import com.java.adds.dao.MedicalArchiveDao;
import com.java.adds.entity.MedicalArchiveEntity;
import com.java.adds.utils.FileUtil;
import com.java.adds.utils.PythonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OCRService {

    @Autowired
    PythonUtil pythonUtil;

    @Autowired
    FileUtil fileUtil;

    @Autowired
    UploadFileConfig fileConfig;

    @Autowired
    MedicalArchiveDao medicalArchiveDao;


    /**
     * ocr recognize images in a zipped docx file and create a text file
     * @author XYX
     */
    public MedicalArchiveEntity ocrMedicalArchive(MedicalArchiveEntity medicalArchive)
    {
        String zipFilePath = medicalArchive.getZipFilePath();
        String doctorId = medicalArchive.getUserId().toString();
        String title = medicalArchive.getTitle().replace(' ', '_') + ".txt";
        String txtFileName = fileUtil.getFileNameWithTimeStamp(doctorId, title);

        pythonUtil.runPython("unzip.py", fileConfig.getUploadFilePath());
        pythonUtil.runPython("extra_png_from_word.py", fileConfig.getUploadFilePath());
        pythonUtil.runPython("ocr_to_txt.py", fileConfig.getUploadFilePath(), txtFileName);

        String txtFilePath = fileConfig.getUploadFilePath() + txtFileName;
        medicalArchive.setTxtFilePath(txtFilePath);
        medicalArchive.setStatus(true);
        medicalArchiveDao.updateMedicalArchive(medicalArchive);
        return medicalArchive;
    }
}
