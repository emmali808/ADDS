package cn.medicine.ocr;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.io.File;

public class Demo {

	public void ocr(String path) {
		//python代码地址
		String pycode_path = "/root/temp/pythonCode/";
		//上传压缩包文件的地址
		String case_path ="/root/temp/case/upload";

		Demo demo = new Demo();
		demo.unzip(pycode_path,case_path);
		demo.extra_png_from_word(pycode_path,case_path);
		demo.ocr(pycode_path,case_path);
	}

	public void unzip(String file_path, String unzip_dir_path){
		System.out.println("unzip");
		try {
			String[] cmd = new String[] { "python3", file_path+"unzip.py", unzip_dir_path};
			Process proc = Runtime.getRuntime().exec(cmd);

			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));

			in.close();
			int re = proc.waitFor();
			System.out.println(re);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}


	public void extra_png_from_word(String file_path, String extra_dir_path){
		System.out.println("finding case.png");
		try {
			String[] args = new String[] { "python3", file_path+"extra_png_from_word.py", extra_dir_path};
			Process proc = Runtime.getRuntime().exec(args);

			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));

			in.close();
			proc.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void ocr(String file_path, String ocr_dir_path){
		System.out.println("doing ocr,please wait!");
		try {
			String[] args = new String[] { "python3", file_path+"ocr.py", ocr_dir_path};
			Process proc = Runtime.getRuntime().exec(args);

			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));

			in.close();
			proc.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}



}
