package com.java.adds.utils;

import com.java.adds.entity.DeepModelEntity;
import com.java.adds.entity.DeepModelTaskEntity;
import com.java.adds.entity.DeepModelTaskResultEntity;
import com.java.adds.mapper.DeepModelTaskMapper;
import com.java.adds.mapper.DeepModelTaskResultMapper;
import com.java.adds.mapper.MetricMapper;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


@Component
public class FileUtil {

    @Value("/home/lf/桌面/SIGIR_QA/HAR-master/data/pinfo/config.py")
    String fileToBeChange;

    @Autowired
    DeepModelTaskResultMapper deepModelTaskResultMapper;

    @Autowired
    DeepModelTaskMapper deepModelTaskMapper;

    @Autowired
    MetricMapper metricMapper;

    /**ljy
     *检查数据集格式
     */
    public boolean checkDataset(File file)//检查数据集格式
    {
        int flag=0;
        try{
            BufferedReader in= new BufferedReader(new FileReader(file));
            String[] texts=in.readLine().split("\t");
            if(texts.length!=3||texts[0].length()!=1||!Character.isDigit(texts[0].charAt(0)))//格式错误
                flag=1;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if(flag==1)
            return false;
        else
            return true;
    }

    /**ljy
     *生成处理数据的配置文件
     */
    public void createDataSetConfig(String dstdir,String train,String test,String dev,String modelDstDir)
    {
        try {
            File file =new File(fileToBeChange);
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(file.getName());
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write("{"+"\n");
            bufferWritter.write("\t"+"\"dstdir\" : "+dstdir+",\n");
            bufferWritter.write("\t"+"\"traindir\":"+train+",\n");
            bufferWritter.write("\t"+"\"testdir\":"+test+",\n");
            bufferWritter.write("\t"+"\"devdir\":"+dev+",\n");
            bufferWritter.write("\t"+"\"model_dst_dir\" : "+modelDstDir+",\n");
            bufferWritter.write("\t"+"\"text1_maxlen\": 20,"+"\n");
            bufferWritter.write("\t"+"\"text2_maxlen\": 300,"+"\n");
            bufferWritter.write("\t"+"\"metrics\": [ \"ndcg@1\", \"ndcg@3\", \"ndcg@5\", \"ndcg@10\", \"map\", \"recall@3\", \"recall@3\", \"recall@5\", \"recall@10\", \"precision@1\", \"precision@3\", \"precision@5\", \"precision@10\""+"\n");
            bufferWritter.write("\t"+"\"base_metric\":\"map\","+"\n");
            bufferWritter.write("\t"+"\"weights_dir\":\"examples/pinfo/hqa_sample/\","+"\n");
            bufferWritter.write("}"+"\n");

            bufferWritter.close();

            System.out.println("Done");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }


    /**ljy
     *深度学习模型配置文件生成
     */
    public void createPythonConfig(String configPath, String configFile, DeepModelTaskEntity deepModelTaskEntity, DeepModelEntity deepModelEntity)
    {
        String config=configPath+configFile;
        String dataSetPath="";
        if(deepModelTaskEntity.getDatasetId()==1)   //默认数据集hqa
            ;
        else
            dataSetPath="/data_"+deepModelTaskEntity.getDatasetId().toString();
        try {
            File file =new File(config);
            if(!file.exists()){
                file.createNewFile();
            }
            FileWriter fileWritter = new FileWriter(file.getName());
            String modelName=deepModelTaskMapper.getTaskNameById(deepModelTaskEntity.getModelId());
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            if(deepModelTaskEntity.getModelType()==1&&!(modelName.equals("PACRR")||modelName.equals("KNRM")||modelName.equals("DRMMTKS")))
            {
                bufferWritter.write("{\n" +
                        "  \"net_name\": " + modelName + ",\n" +
                        "  \"global\":{\n" +
                        "      \"model_type\": \"PY\",\n" +
                        "      \"weights_file\": \"" + modelName.toLowerCase() + "pinfo.weights\",\n" +
                        "      \"save_weights_iters\": 10,\n" +
                        "      \"num_iters\": 400,\n" +
                        "      \"display_interval\": 10,\n" +
                        "      \"test_weights_iters\": 400,\n" +
                        "      \"optimizer\": \"adadelta\",\n" +
                        "      \"learning_rate\": 1.0\n" +
                        "  },\n" +
                        "  \"inputs\": {\n" +
                        "    \"share\": {\n" +
                        "        \"text1_corpus\": \"./data/pinfo/hqa_sample" + dataSetPath + "/corpus_preprocessed.txt\",\n" +
                        "        \"text2_corpus\": \"./data/pinfo/hqa_sample" + dataSetPath + "/corpus_preprocessed.txt\",\n" +
                        "        \"use_dpool\": false,\n" +
                        "        \"embed_size\": 300,\n" +
                        "        \"embed_path\": \"./data/pinfo/hqa_sample" + dataSetPath + "/embed.idf\",\n" +
                        "        \"vocab_size\": 19597,\n" +
                        "        \"train_embed\": false,\n" +
                        "        \"target_mode\": \"ranking\",\n" +
                        "        \"bin_num\": 20,\n" +
                        "        \"text1_maxlen\": 15,\n" +
                        "        \"text2_maxlen\": 300\n" +
                        "    },\n" +
                        "    \"train\": {\n" +
                        "        \"input_type\": \"DRMM_PairGenerator\",\n" +
                        "        \"phase\": \"TRAIN\",\n" +
                        "        \"use_iter\": false,\n" +
                        "        \"query_per_iter\": 50,\n" +
                        "        \"batch_per_iter\": 5,\n" +
                        "        \"batch_size\": 100,\n" +
                        "        \"relation_file\": \"./data/pinfo/hqa_sample" + dataSetPath + "/relation_train.txt\",\n" +
                        "        \"hist_feats_file\": \"./data/pinfo/hqa_sample" +dataSetPath + "/relation_train.binsum-20.txt\"\n" +
                        "    },\n" +
                        "    \"valid\": {\n" +
                        "        \"input_type\": \"DRMM_ListGenerator\",\n" +
                        "        \"phase\": \"EVAL\",\n" +
                        "        \"batch_list\": 10,\n" +
                        "        \"relation_file\": \"./data/pinfo/hqa_sample" + dataSetPath + "/relation_valid.txt\",\n" +
                        "        \"hist_feats_file\": \"./data/pinfo/hqa_sample" + dataSetPath + "/relation_valid.binsum-20.txt\"\n" +
                        "    },\n" +
                        "    \"test\": {\n" +
                        "        \"input_type\": \"DRMM_ListGenerator\",\n" +
                        "        \"phase\": \"EVAL\",\n" +
                        "        \"batch_list\": 10,\n" +
                        "        \"relation_file\": \"./data/pinfo/hqa_sample" + dataSetPath + "/relation_test.txt\",\n" +
                        "        \"hist_feats_file\": \"./data/pinfo/hqa_sample" + dataSetPath + "/relation_test.binsum-20.txt\"\n" +
                        "    },\n" +
                        "    \"predict\": {\n" +
                        "        \"input_type\": \"DRMM_ListGenerator\",\n" +
                        "        \"phase\": \"PREDICT\",\n" +
                        "        \"batch_list\": 10,\n" +
                        "        \"relation_file\": \"./data/pinfo/hqa_sample" + dataSetPath + "/relation_test.txt\",\n" +
                        "        \"hist_feats_file\": \"./data/pinfo/hqa_sample" + dataSetPath + "/relation_test.binsum-20.txt\"\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"outputs\": {\n" +
                        "    \"predict\": {\n" +
                        "      \"save_format\": \"TREC\",\n" +
                        "      \"save_path\": \"predict.test.anmm.pinfo.txt\"\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"model\": {\n" +
                        "    \"model_path\": \"./matchzoo/models/\",\n" +
                        "    \"model_py\": \"" + deepModelEntity.getModelPy() + "\",\n" +
                        "    \"setting\": {\n" +
                        "        \"num_layers\": 2,\n" +
                        "        \"hidden_sizes\": [20, 1],\n" +
                        "\t    \"dropout_rate\": 0.0\n" +
                        "    }\n" +
                        "  },\n" +
                        "  \"losses\": [\n" +
                        "    {\n" +
                        "       \"object_name\": \"rank_hinge_loss\" ,\n" +
                        "       \"object_params\": {\n" +
                        "            \"margin\": 1.0\n" +
                        "       }\n" +
                        "    }\n" +
                        "  ],\n" +
                        "  \"metrics\": [ \"ndcg@1\", \"ndcg@3\", \"ndcg@5\", \"ndcg@10\", \"map\", \"recall@3\", \"recall@3\", \"recall@5\", \"recall@10\", \"precision@1\", \"precision@3\", \"precision@5\", \"precision@10\"  ]\n" +
                        "}\n");

            }
            else if(deepModelTaskEntity.getModelType()==2)
            {
                bufferWritter.write("{\n"+
                    "    \"data_dir\" : \"../HAR-master/data/pinfo/hqa_sample"+dataSetPath+"/\" ,\n"+
                    " \"learning_rate\":0.001,\n"+
                    " \"bert_learning_rate\": 2e-5,\n"+
                    " \"MAX_EPOCH\":100,\n"+
                    " \"BATCH_SIZE\": 16,\n"+
                    " \"BATCHES_PER_EPOCH\": 32,\n"+
                    " \"text1_maxlen\": "+deepModelTaskEntity.getQueryLength()+",\n"+
                    " \"text2_maxlen\": "+deepModelTaskEntity.getDocumentLength()+",\n"+
                    " \"base_metric\":\""+metricMapper.getMetricById(deepModelTaskEntity.getMetricId())+"\",\n"+
                    " \"model_out_dir\":\"all-weight/weight\",\n"+
                    " \"metrics\": [ \"ndcg@1\", \"ndcg@3\", \"ndcg@5\", \"ndcg@10\", \"map\", \"recall@3\", \"recall@3\", \"recall@5\", \"recall@10\", \"precision@1\", \"precision@3\", \"precision@5\", \"precision@10\"]\n"+ "}\n");

            }
            else if(deepModelTaskEntity.getModelType()==1||deepModelTaskEntity.getModelType()==3)
            {
                bufferWritter.write("{\n"+
                        "    \"data_dir\" : \"../HAR-master/data/pinfo/hqa_sample"+dataSetPath+"/\" ,\n"+
                        " \"learning_rate\":0.001,\n"+
                        " \"word_dim\": 300,\n"+
                        " \"MAX_EPOCH\":100,\n"+
                        " \"BATCH_SIZE\": 16,\n"+
                        " \"BATCHES_PER_EPOCH\": 32,\n"+
                        " \"text1_maxlen\": "+deepModelTaskEntity.getQueryLength()+",\n"+
                        " \"text2_maxlen\": "+deepModelTaskEntity.getDocumentLength()+",\n"+
                        " \"base_metric\":\""+metricMapper.getMetricById(deepModelTaskEntity.getMetricId())+"\",\n"+
                        " \"model_out_dir\":\"all-weight/nf_drmmtks_glove_weight\",\n"+
                        " \"metrics\": [ \"ndcg@1\", \"ndcg@3\", \"ndcg@5\", \"ndcg@10\", \"map\", \"recall@3\", \"recall@3\", \"recall@5\", \"recall@10\", \"precision@1\", \"precision@3\", \"precision@5\", \"precision@10\"]\n"+ "}\n");

            }
            else  //第四种
            {
                bufferWritter.write("{\n"+
                        "    \"data_dir\" : \"../HAR-master/data/pinfo/hqa_sample"+dataSetPath+"/\" ,\n"+
                        " \"learning_rate\":0.001,\n"+
                        " \"bert_learning_rate\": 2e-5,\n"+
                        " \"MAX_EPOCH\":100,\n"+
                        " \"BATCH_SIZE\": 16,\n"+
                        " \"BATCHES_PER_EPOCH\": 32,\n"+
                        " \"text1_maxlen\": "+deepModelTaskEntity.getQueryLength()+",\n"+
                        " \"text2_maxlen\": "+deepModelTaskEntity.getDocumentLength()+",\n"+
                        " \"atten_dim\": 500,\n"+
                        " \"entity_dim\": 100,\n"+
                        " \"kg_path\": \"kg_embed\",\n"+
                        " \"content_entity\": \"content2entity.f\",\n"+
                        " \"base_metric\":\""+metricMapper.getMetricById(deepModelTaskEntity.getMetricId())+"\",\n"+
                        " \"model_out_dir\":\"all-weight/nf_drmmtks_glove_weight\",\n"+
                        " \"metrics\": [ \"ndcg@1\", \"ndcg@3\", \"ndcg@5\", \"ndcg@10\", \"map\", \"recall@3\", \"recall@3\", \"recall@5\", \"recall@10\", \"precision@1\", \"precision@3\", \"precision@5\", \"precision@10\"]\n"+ "}\n");

            }
            bufferWritter.close();

            System.out.println("Done");
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }


    /**ljy
     *从结果文件中抽取模型运行结果
     */
    public Long extractResult(String outputPath,long taskId, Integer modelType,String modelName)
    {
        String result="";
        DeepModelTaskResultEntity deepModelTaskResultEntity=new DeepModelTaskResultEntity();
        FileReader fileReader = null;
        try
        {
            fileReader = new FileReader(outputPath);//"C:\\Users\\yin\\Desktop\\res.txt"
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        Scanner sc = new Scanner(fileReader);
        String line = null;
        while((sc.hasNextLine()&&(line=sc.nextLine())!=null))
        {
            if(!sc.hasNextLine()){
                result=line;
            }
        }
        sc.close();
        if(modelType==1&&!(modelName.equals("PACRR")||modelName.equals("KNRM")||modelName.equals("DRMMTKS")))
        {
            String temp[]=result.split("\t");
            deepModelTaskResultEntity.setNdcg1(temp[2].split("=")[1]);
            deepModelTaskResultEntity.setNdcg3(temp[3].split("=")[1]);
            deepModelTaskResultEntity.setNdcg5(temp[4].split("=")[1]);
            deepModelTaskResultEntity.setNdcg10(temp[5].split("=")[1]);
            deepModelTaskResultEntity.setMap(temp[6].split("=")[1]);
            deepModelTaskResultEntity.setRecall3(temp[7].split("=")[1]);
            deepModelTaskResultEntity.setRecall5(temp[8].split("=")[1]);
            deepModelTaskResultEntity.setRecall10(temp[9].split("=")[1]);
            deepModelTaskResultEntity.setPrecision1(temp[10].split("=")[1]);
            deepModelTaskResultEntity.setPrecision3(temp[11].split("=")[1]);
            deepModelTaskResultEntity.setPrecision5(temp[12].split("=")[1]);
            deepModelTaskResultEntity.setPrecision10(temp[13].split("=")[1]);
            deepModelTaskResultEntity.setTaskId(taskId);
        }
        else
        {
            String temp[]=result.split("\t");
            System.out.println(temp.length);
            deepModelTaskResultEntity.setNdcg1(temp[0].split(" ")[5]);
            deepModelTaskResultEntity.setNdcg3(temp[1].split(" ")[2]);
            deepModelTaskResultEntity.setNdcg5(temp[2].split(" ")[2]);
            deepModelTaskResultEntity.setNdcg10(temp[3].split(" ")[2]);
            deepModelTaskResultEntity.setMap(temp[4].split(" ")[2]);
            deepModelTaskResultEntity.setRecall3(temp[5].split(" ")[2]);
            deepModelTaskResultEntity.setRecall5(temp[7].split(" ")[2]);
            deepModelTaskResultEntity.setRecall10(temp[8].split(" ")[2]);
            deepModelTaskResultEntity.setPrecision1(temp[9].split(" ")[2]);
            deepModelTaskResultEntity.setPrecision3(temp[10].split(" ")[2]);
            deepModelTaskResultEntity.setPrecision5(temp[11].split(" ")[2]);
            deepModelTaskResultEntity.setPrecision10(temp[12].split(" ")[2]);
        }
        Long taskResultId=deepModelTaskResultMapper.insertDeepModelTaskResult(deepModelTaskResultEntity);//将结果插入数据库
        return taskResultId;
    }

    /**
     * QXL
     * 判断文件类型（后缀名）是否是 csv 文件
     * @param file file
     * @return true / false
     */
    public boolean csvFileType(File file) {
        String fileName = file.getName();
        if (fileName.contains(".")) {
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
            return fileType.toLowerCase().equals("csv");
        }
        return false;
    }

    /**
     * QXL
     * 判断 KG 文件（前提是 csv 文件）的数据格式是否正确
     * 目前判断规则是：csv 文件必须有header，必须有 3 列，且为 header / relation / tail
     * 否则均视为格式错误
     * @param file file
     * @return true / false
     */
    public boolean kgFileFormat(File file) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String[] csvHeaders = reader.readLine().split(",");
            if (csvHeaders.length == 3) {
                return csvHeaders[0].equals("header") && csvHeaders[1].equals("relation") && csvHeaders[2].equals("tail");
            }
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * QXL
     * 删除文件
     * @param file file
     */
    public void deleteFile(File file) {
        if (file.exists()) {
            boolean result = file.delete();
            if (result) {
                System.out.println("File is deleted successfully! ");
            } else {
                System.out.println("Fail to delete file... ");
            }
        } else {
            System.out.println("File is non-exist. ");
        }
    }

    /**
     * QXL
     * 数据导出至 Excel 表格
     * @param outputStream HttpServletResponse outputStream
     * @param data Task result data
     * @param sheetName Excel sheet name
     * @param columnWidth Excel sheet column width
     */
    public static void exportDataToExcel(OutputStream outputStream, List<List<Object>> data, String sheetName, int columnWidth) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet(sheetName);
        sheet.setDefaultColumnWidth(columnWidth);

        int rowIndex = 0;
        for (List<Object> rowData : data) {
            HSSFRow row = sheet.createRow(rowIndex++);
            for (int i = 0; i < rowData.size(); i++) {
                HSSFCell cell = row.createCell(i);
                HSSFRichTextString text = new HSSFRichTextString((String) rowData.get(i));
                cell.setCellValue(text);
            }
        }
        workbook.write(outputStream);
        workbook.close();
    }

    /**
     * Create File Name With Time Stamp
     * @param prefix prefix
     * @param suffix suffix
     * @return file name
     * @author XYX
     */
    public String getFileNameWithTimeStamp(String prefix, String suffix)
    {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        Date date = new Date();
        String currentTime = format.format(date);
        String fileName = prefix + '_' + currentTime + '_' + suffix;
        return fileName;
    }

    /**
     * Read Lines Of File Into A List
     * @param filePath the path of the file to be read
     * @return lines of file content as an array list
     * @author XYX
     */
    public ArrayList<String> readFileIntoList(String filePath)
    {
        ArrayList<String> lines = new ArrayList<>();

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    /**
     * Write List of Strings into a File
     * @param filePath the path of the file to write
     * @param lines the list of the strings to write
     * @return successfully write the file or not
     * @author XYX
     */
    public Boolean writeListIntoFile(String filePath, ArrayList<String> lines)
    {
        try {
            File file = new File(filePath);
            file.createNewFile();
            FileWriter fileWriter = new FileWriter(filePath);
            for (String line : lines) {
                fileWriter.write(line + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
