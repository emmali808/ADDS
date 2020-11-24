package com.java.adds.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * CPP Utility Class
 * @author XYX
 */
@Component
public class CPPUtil {

    @Autowired
    FileUtil fileUtil;

    /**
     * run Python scripts
     * @author xyx
     * @return void
     */
    public ArrayList<String> runCPP(String cppFile, String... params) {
        ArrayList<String> result = new ArrayList<>();
        String cppPath = this.getClass().getResource("/cpp/").getPath();
        try {
            ArrayList<String> args = new ArrayList<String>(Arrays.asList(cppPath + cppFile));
            args.addAll(Arrays.asList(params));
            String[] temp = new String[args.size()];
            Process proc = Runtime.getRuntime().exec(args.toArray(temp));
            BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
            in.close();
            proc.waitFor();
            result = fileUtil.readFileIntoList("result.txt");
            File file = new File("result.txt");
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
