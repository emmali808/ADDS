import sys
import urllib
import urllib.request
import json
import base64
import time
import os
import socket



def ocr_to_txt(case_img):

	payload = "image=img&classifierId=1"

	url = 'https://aip.baidubce.com/rest/2.0/solution/v1/iocr/recognise?access_token=24.2cf76018223159eb73f49e434564affb.2592000.1556698880.282335-15596170'

	f = open(case_img, 'rb')
	img = base64.b64encode(f.read())

	params = {"image": img,
	          "classifierId":"1"
	          }


	         
	params = urllib.parse.urlencode(params).encode("utf-8")
	request = urllib.request.Request(url,params)
	request.add_header('Content-Type', 'application/x-www-form-urlencoded')
	response = urllib.request.urlopen(request,timeout = 50)
	content = response.read()

	if content.decode("utf-8")[10]=='r':
		data = json.loads(content.decode("utf-8"))['data']['ret']

	

		#写到D:\GraProject\case\case_copy\ocr_key//name12345_ocr_key.txt中
		filename=[file.split('\\')[-4],"ocr.txt"]
		case_txt=os.path.join("D:\GraProject\case\case_copy\ocr_result03",'_'.join(filename))
	
		f=open(case_txt,'a+')
		print(case_txt)
		# print(data)
		f.write('\n')
		for item in data:
			if 'word_name' in item:
				# print (item['word_name'],':',item['word'] )
				f.write(item['word_name'])
				f.write(':')
				f.write(item['word'])
				f.write('\n')
			
		f.close()
	response.close()		

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

 
if __name__ == '__main__':
    dir_path = sys.argv[-1]
    print("ocr to txt!")

    file_list = find_file(dir_path, ext = ".png")

    for file in file_list:
    	try:
    		ocr_to_txt(file)
    	except:
    		pass
    	continue 
	
	