package com.java.adds.service;

import com.java.adds.startup.*;
import org.springframework.stereotype.Service;
import org.simmetrics.StringMetric;
import org.simmetrics.metrics.StringMetrics;

import java.io.IOException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;


@Service
public class ConsultService {
    List<String> stopWordsList;
    FileUtil fileUtil;


    public ConsultService() throws IOException {
        fileUtil = new FileUtil();
        stopWordsList = fileUtil.readStopWordsList();
    }

    public double calculateSimilarityScoreForGivenPair(String s1, String s2, int methodType) {
        double similarityScore = 0;


        String preprocessedS1 = fileUtil.removeStopWordsFromSentence(s1, stopWordsList);
        String preprocessedS2 = fileUtil.removeStopWordsFromSentence(s2, stopWordsList);

        StringMetric metric = StringMetrics.qGramsDistance();
        similarityScore = metric.compare(preprocessedS1, preprocessedS2);

        return similarityScore;
    }
}