package com.java.adds.service;

import com.java.adds.dao.HistoryAdmissionDao;
import com.java.adds.dao.MedicalCaseDao;
import com.java.adds.entity.HistoryAdmssionEntity;
import com.java.adds.entity.MedicalCaseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * History Admission Service
 * @author ZY
 */
@Service
public class HistoryAdmissionService {

    @Autowired
    private HistoryAdmissionDao historyAdmissionDao;

    public void insertHistoryAdmissions(HistoryAdmssionEntity historyAdmssionEntity) {
        historyAdmissionDao.insertHistoryAdmissions(historyAdmssionEntity);
    }


}
