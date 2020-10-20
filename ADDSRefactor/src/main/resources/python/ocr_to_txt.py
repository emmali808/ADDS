import sys
import urllib
import urllib.request
import json
import base64
import shutil
import time
import os
import socket



def ocr_to_txt(case_img, dir_path, file_name):
    payload = "image=img"

    url = 'https://aip.baidubce.com/rest/2.0/ocr/v1/general_basic?access_token=24.ee6055b798485612927e2379dee6acd8.2592000.1605290408.282335-21160588'

    f = open(case_img, 'rb')
    img = base64.b64encode(f.read())

    params = {"image": img
              }

    params = urllib.parse.urlencode(params).encode("utf-8")
    request = urllib.request.Request(url,params)
    request.add_header('Content-Type', 'application/x-www-form-urlencoded')
    response = urllib.request.urlopen(request,timeout = 50)
    content = response.read()
    data = json.loads(content.decode("utf-8"))

    if data['words_result_num']>0:
        case_txt=os.path.join(dir_path, file_name)

        f=open(case_txt,'a+')
        f.write('\n')
        for item in data['words_result']:
            f.write(item['words'])
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

def reorder(list):
    for i in range(len(list)):
        original_name = list[i]
        path = original_name.split('/')
        name = path[-1].split('.')
        id = int(name[0][5:])
        if id < 10:
            name[0] = name[0][:5] + '0' + name[0][5:]
        path[-1] = '.'.join(name)
        list[i] = '/'.join(path)
        os.rename(original_name, list[i])
    return list.sort()

if __name__ == '__main__':
    dir_path = sys.argv[-2]
    file_name = sys.argv[-1]
    print("ocr to txt!")


    file_list = find_file(dir_path, ext = ".png")
    reorder(file_list)

    for file in file_list:
        try:
            ocr_to_txt(file, dir_path, file_name)
        except:
            print("failed to ocr")
        continue

    unzip_path = os.path.join(dir_path, 'unzipped')
    shutil.rmtree(unzip_path)
    print("finish ocr")
    
    