import sys
import zipfile
from pathlib import Path
import os

def unzip(case_zip):
    zip_File = zipfile.ZipFile(case_zip, "r")
    for zfile in zip_File.namelist():
        filename = zfile.encode('cp437').decode('gbk')
        zip_File.extract(zfile, unzip_path)
        os.chdir(unzip_path)
        os.rename(zfile,filename)
    zip_File.close()



def find_file(path, ext, file_list=[]):
    dir = os.listdir(path)
    for i in dir:
        i = os.path.join(path, i)
        if os.path.isdir(i):
            find_file(i, ext, file_list) 
         
        else:
            if ext == os.path.splitext(i)[1]:
                file_list.append(i)
    return file_list

 
if __name__ == '__main__':

    # dir_path = "D:\GraProject\copy0419"
    dir_path = sys.argv[-1]
    unzip_path = os.path.join(dir_path, 'dezip')

    print("unzip!")
    file_list = find_file(dir_path, ext=".zip")

    for file in file_list:
        unzip(file)
        os.remove(file)
