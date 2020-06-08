package cn.medicine.ocr;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Component
public class Demo1 {

	public void ocr(String path) {
		// TODO Auto-generated method stub
		String file_path = "/usr/local/apache-tomcat-7.0.91/webapps/ocr-py/";
		//path = "I:\\1xuexi\\medical\\case1";
		Demo1 demo = new Demo1();
        demo.unzip(file_path,path);
		demo.doc_to_docx(file_path,path);
		demo.extra_png_from_word(file_path,path);
		demo.ocr_recognise_fp(file_path,path);
        demo.save_to_mysql(file_path,path);
	}

	public void unzip(String file_path, String unzip_dir_path){
		try {
			String[] args = new String[] { "python", file_path+"unzip.py", unzip_dir_path};
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

	
	public void doc_to_docx(String file_path, String doc_dir_path){
		try {
			//String[] args = new String[] { "python", file_path+"hello.py", "hello"};
			String[] args = new String[] { "python", file_path+"doc_to_docx.py", doc_dir_path};
			Process proc = Runtime.getRuntime().exec(args);// ִ��py�ļ�

			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			/*
			String line = null;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}
			*/
			in.close();
			proc.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void extra_png_from_word(String file_path, String extra_dir_path){
		try {
			String[] args = new String[] { "python", file_path+"extra_png_from_word.py", extra_dir_path};
			Process proc = Runtime.getRuntime().exec(args);// ִ��py�ļ�

			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			/*
			String line = null;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}*/
			in.close();
			proc.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void ocr_recognise_fp(String file_path, String ocr_dir_path){
		try {
			String[] args = new String[] { "python", file_path+"ocr_to_txt.py", ocr_dir_path};
			Process proc = Runtime.getRuntime().exec(args);// ִ��py�ļ�

			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			/*
			String line = null;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}*/
			in.close();
			proc.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void save_to_mysql(String file_path, String mysql_dir_path){
		try {
			String[] args = new String[] { "python", file_path+"save_to_mysql.py", mysql_dir_path};
			Process proc = Runtime.getRuntime().exec(args);// ִ��py�ļ�

			BufferedReader in = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			/*String line = null;
			while ((line = in.readLine()) != null) {
				System.out.println(line);
			}*/
			in.close();
			proc.waitFor();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
