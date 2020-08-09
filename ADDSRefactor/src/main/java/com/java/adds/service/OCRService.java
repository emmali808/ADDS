package com.java.adds.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service
public class OCRService {

    /**
     * run Python scripts
     * @author xyx
     * @return void
     */
    public void runPython(String pythonFile, String ocrFilePath){
        String pythonPath = this.getClass().getResource("/python/").getPath();

        try {
            String[] args = new String[] { "python", pythonPath.substring(1) + pythonFile, ocrFilePath};
            Process proc = Runtime.getRuntime().exec(args);
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
