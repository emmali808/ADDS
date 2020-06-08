import sys
from docx import Document
import os
import zipfile

 
from docx import Document
import os
import zipfile


def extra_png_from_word(path):

	if os.path.splitext(path)[1] == ".docx":
		newname = os.path.splitext(path)[0] + ".zip"
		os.rename(path,newname)
		f=zipfile.ZipFile(newname,'r')
		for ffname in f.namelist():
			f.extract(ffname, os.path.split(path)[0])   #解压文件
		f.close()


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


 
#dir_path = "D:\GraProject\workplace"  #找该文件夹下所有ext文件

if __name__ == '__main__':
    dir_path = sys.argv[-1]
    print("extra_img_from_word")
    file_list = find_file(dir_path, ext=".docx")

    for file in file_list:

        extra_png_from_word(file)
