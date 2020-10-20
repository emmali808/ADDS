package com.java.adds.utils;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Python Utility Class
 * @author XYX
 */
@Component
public class PythonUtil {
    /**
     * run Python scripts
     * @author xyx
     * @return void
     */
    public void runPython(String pythonFile, String... params) {
        String pythonPath = this.getClass().getResource("/python/").getPath();
        try {
            ArrayList<String> args = new ArrayList<String>(Arrays.asList("python", pythonPath + pythonFile));
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
