import sys
import pymysql
import urllib
import urllib.request
import json
import base64
import time
import os
import re


def save_to_mysql(case_img):

	payload = "image=img&classifierId=1"

	url = 'https://aip.baidubce.com/rest/2.0/solution/v1/iocr/recognise?access_token=24.2cf76018223159eb73f49e434564affb.2592000.1556698880.282335-15596170'

	f = open(case_img, 'rb')
	img = base64.b64encode(f.read())

	params = {"image": img,
	          "classifierId":"1"
	          }
	params = urllib.parse.urlencode(params).encode("utf-8")
	request = urllib.request.Request(url, params)
	request.add_header('Content-Type', 'application/x-www-form-urlencoded')
	response = urllib.request.urlopen(request)
	content = response.read()

	if content.decode("utf-8")[10]=='r':
		data = json.loads(content.decode("utf-8"))['data']['ret']	
		



		if data[0]['word_name']=='病人ID':
			global patient_ID
			patient_ID=data[0]['word']

			for item in data:
				if item['word_name']=='姓名':
					patient_name=item['word']
				if item['word_name']=='民族':
					patient_nation=item['word']
				if item['word_name']=='性别':
					patient_sex=item['word']
				if item['word_name']=='住院号':
					patient_hospital_ID=item['word']
			sql = """INSERT INTO patient_info(patient_ID,patient_name,patient_sex,patient_nation,patient_hospital_ID)
			         VALUES('%s','%s','%s','%s','%s')"""%(patient_ID,patient_name,patient_sex,patient_nation,patient_hospital_ID)
			mysql(sql)
			print(patient_ID,patient_name,patient_sex,patient_nametion,patient_hospital_ID)



		if data[0]['word_name']=='入院科室':

			for item in data:
				if item['word_name']=='入院科室':
					inpatient_department=item['word']
				if item['word_name']=='入院诊断':
					inpatient_diagnose=item['word']
				if item['word_name']=='门诊诊断':
					outpatient_diagnose=item['word']
				if item['word_name']=='过敏药物':
					allergy=item['word']
				if item['word_name']=='出院科室':
					discharge_department=item['word']

			sql = """INSERT INTO hospitalization(patient_ID,inpatient_department,inpatient_diagnose,outpatient_diagnose,allergy,discharge_department)
			         VALUES('%s','%s','%s','%s','%s','%s')"""%(patient_ID,inpatient_department,inpatient_diagnose,outpatient_diagnose,allergy,discharge_department)
			mysql(sql)
			print(patient_ID,inpatient_department,inpatient_diagnose,outpatient_diagnose,allergy,discharge_department)




		if data[0]['word_name']=='检验主题':
			check_subject=data[0]['word']
			check_item=''
			check_result=''
		
	
			for item in data:
				# if item['word_name'][-1]=='目':

				if re.sub("[0-9\#\-\,\。]", "", item['word_name'])=='检验项目':
					check_item=item['word']
				if re.sub("[0-9\#\-\,\。]", "", item['word_name'])=='结果':
					check_result=re.sub("[0-9\-\+\.]", "", item['word'])
					sql = """INSERT INTO check_info(patient_ID,check_subject,check_item,check_result)
						VALUES('%s','%s','%s','%s')"""%(patient_ID,check_subject,check_item,check_result)
					mysql(sql)
					print(patient_ID,check_subject,check_item,check_result)
					
				
		if re.sub("[0-9\#\-\,\。]", "", data[0]['word_name'])=='医嘱类别':
			treatment_item='处置'
		
			for item in data:
				if re.sub("[0-9\#\-\,\。]", "", item['word_name'])=='医嘱类别':
					treatment_item=re.sub("[0-9\:\-\,\。]", "", item['word'])
				if re.sub("[0-9\#\-\,\。]", "", item['word_name'])=='医嘱内容':
					treatment_result=item['word']
					if treatment_result!='':
						sql = """INSERT INTO treatment(patient_ID,treatment_item,treatment_result)
							VALUES('%s','%s','%s')"""%(patient_ID,treatment_item,treatment_result)
						mysql(sql)
						print(patient_ID,treatment_item,treatment_result)
				
		

		if re.sub("[0-9\#\-\,\。]", "", data[0]['word_name'])=='诊断史诊断类别':
			treatment_item=''
		
			for item in data:
				if re.sub("[0-9\#\-\,\。]", "", item['word_name'])=='诊断史诊断类别':
					diagnose_item=item['word']
				if re.sub("[0-9\#\-\,\。]", "", item['word_name'])=='诊断史诊断结果':
					diagnose_result=item['word']
					sql = """INSERT INTO diagnose(patient_ID,diagnose_item,diagnose_result)
						VALUES('%s','%s','%s')"""%(patient_ID,diagnose_item,diagnose_result)
					mysql(sql)
					print(patient_ID,diagnose_item,diagnose_result)		


			# # print(patient_ID,patient_name,patient_sex,patient_nation,patient_hospital_ID)





def find_file(path, ext, file_list=[]):
    dir = os.listdir(path)
    for i in dir:
        i = os.path.join(path, i)
        if os.path.isdir(i):
            find_file(i, ext, file_list) 
            # print(i)
        else:
            if ext == os.path.splitext(i)[1]:
                file_list.append(i)
    return file_list


def mysql(sql):
	conn = pymysql.connect(host='47.94.174.82', user='root', passwd='Tan980712', db='OCR_RESULT')
	cursor = conn.cursor()
	cursor.execute(sql)
	conn.commit()
	conn.close()





if __name__ == '__main__':
    dir_path = sys.argv[-1]
    print("save to mysql!")
    file_list = find_file(dir_path,ext = ".png")
    patient_ID=''

    for file in file_list:

    	try:
    		save_to_mysql(file)
    	except:
    		pass
    	continue 

	
