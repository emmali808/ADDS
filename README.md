# ADDS

### Introduction

We develop an intelligent medical analysis system, denoted by IMAS, to support medical practice based on concept graph. The system provides an efficient medical archives processing tool and supports effective medical data searching and analysis.

The system application is located in folder *ADDSRefactor* and sample data files are located in folder *DataFile*.

### Launch Instruction

It is recommended to run the application in a Linux environment (18.04.1-Ubuntu is preferred). You need to first install supporting softwares on the Linux environment such as: MySQL (5.7.32 for Linux is preferred), Neo4j (community 3.5.22), Python (3.7.4), npm (7.0.8), Java IDE (IntelliJ IDEA), browser (Chrome).

To run the application, first clone the whole project onto you own server. Then import the database file *Database.sql* from folder *DataFile* into a new database in your MySQL and launch MySQL. Modify the configuration file *application.properties* under */ADDSRefactor/src/resources* to your own MySQL and Neo4j config (e.g. host, username, password, database name). You also need to specify a folder for uploading files onto your server as *file.upload.path* in *application.properties*. Now you can import the system into an IDE and launch the backend of the application.

To launch the frontend of the application, use console and navigate to */ADDSRefactor/frontend*. Use ``` npm install ``` to install required dependencies. Then use ``` npm run dev``` to launch the frontend.

Put the graph data file *GraphData.txt* from folder *DataFile* under your path for *file.upload.path*. (More graph data need to be processed to remove the sensitive data before they can be released) Launch the Neo4j database. Launch your browser and locate to http://localhost:8081/#/devPage and click the button. The system will start importing the graph datafile into the database.

Our system uses Baidu's API for OCR recognition. In order to use this function, you need to create an account and OCR application in https://ai.baidu.com/tech/ocr/. Generate your own access token for that in */ADDSRefactor/src/resources/python/ocr_to_txt.py*. 

The AI robot for online consulting uses API for deep learning from https://github.com/emmali808/HQADeepHelper and codes for sementic computing from https://github.com/fgaim/biosses.

### License

The application is licensed under the GPL v3.

