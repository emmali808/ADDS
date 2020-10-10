package com.java.adds.dao;

import com.java.adds.controller.vo.FilterQuestionVO;
import com.java.adds.controller.vo.QuestionAnswerVO;
import com.java.adds.entity.*;
import com.java.adds.mapper.*;
import com.java.adds.utils.EmailUtil;
import com.java.adds.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@Component
public class DoctorDao {
    @Autowired
    DoctorMapper doctorMapper;

    @Autowired
    QuestionMapper questionMapper;

    @Autowired
    QuestionResultMapper questionResultMapper;

    @Autowired
    QuestionDetailAnswerMapper questionDetailAnswerMapper;

    @Autowired
    DataSetsMapper dataSetsMapper;

    @Autowired
    KGMapper kgMapper;

    @Autowired
    DeepModelTaskMapper deepModelTaskMapper;

    @Autowired
    EmailUtil emailUtil;

    @Autowired
    FileUtil fileUtil;

    @Autowired
    DeepModelMapper deepModelMapper;

    @Autowired
    DeepModelTaskResultMapper deepModelTaskResultMapper;

    //    @Value("E://医疗项目//大创//ADDS重构//ADDS//src//main//resources//dataSets//")
//    String dataSetsPathInServer;

    @Value("../SIGIR_QA/HAR-master/data/pinfo/hqa-sample/")
    String dataSetsPathInServer;

//    @Value("home/lf/桌面/SIGIR_OA")
//    String deepModelSamePath;  //所有深度学习模型存放的共同路径

    @Value("examples/pinfo/config/")
    String featureBasedModelConfigPath;  //feature-based模型配置文件的前缀路径

    @Value("configs/")
    String contextBasedModelConfigPath;  //context-based模型配置文件的前缀路径

    @Value("configs/")
    String knowledgeEmbeddingModelConfigPath;  //knowledge-embedding模型配置文件的前缀路径

    @Value("configs/")
    String ourJointModelConfigPath;  //our-joint模型配置文件的前缀路径

    @Value("../SIGIR_QA/HAR-master")
    String featureBasedPythonPath;  //运行feature-based模型命令的前缀路径

    @Value("../SIGIR_QA/cedr-master")
    String contextBasedPythonPath;  //运行context-based模型命令的前缀路径

    @Value("../SIGIR_QA/ernie_model/glove_embed")
    String knowledgeEmbeddingPythonPath;  //运行knowledge-embedding模型命令的前缀路径

    @Value("../SIGIR_QA/ernie_model")
    String ourJointPythonPath;  //运行our joint模型命令的前缀路径


    //模型运行结果路径
    //E://医疗项目//大创//ADDS重构//ADDS//src//main//resources//DMResultFile//
    @Value("C:\\Users\\yin\\Desktop\\")
    String deepModelResultPathInServer;

    @Value("/ADDS/deepModelResult/**")
    String deepModelResultPath;

    /**ljy
     * 管理员获取所有医生信息
     */
    public ArrayList<DoctorEntity> getAllDoctors()
    {
        return doctorMapper.getAllDoctors();
    }

    /**ljy
     * 医生获取问题（回答与否，问题类型）
     * @return
     */
    public ArrayList<QuestionEntity> getFilterQuestion(FilterQuestionVO filterQuestionVO, Long doctorId)
    {
        //1:已经回答，2：还未回答
        //1:选择题，2：详细解答题
        if(filterQuestionVO.getAnsweredOrNot()==1&&filterQuestionVO.getQuestionType()==1)//已经回答的选择题
            return questionMapper.getQuestionAnswered((filterQuestionVO.getStart()-1)* filterQuestionVO.getLimit(), filterQuestionVO.getLimit(),doctorId);
        else if(filterQuestionVO.getAnsweredOrNot()==1&&filterQuestionVO.getQuestionType()==2)//已经回答的详细解答题
            return questionMapper.getDetailQuestionsAnswered((filterQuestionVO.getStart()-1)* filterQuestionVO.getLimit(), filterQuestionVO.getLimit(),doctorId);
        else if(filterQuestionVO.getAnsweredOrNot()==2&&filterQuestionVO.getQuestionType()==1)//还未回答的选择题
            return questionMapper.getChoiceQuestionsNotAnswered((filterQuestionVO.getStart()-1)* filterQuestionVO.getLimit(), filterQuestionVO.getLimit(),doctorId);
        else//还未回答的详细解答题
            return questionMapper.getDetailQuestionsNotAnswered((filterQuestionVO.getStart()-1)* filterQuestionVO.getLimit(), filterQuestionVO.getLimit(),doctorId);
    }



    /**ljy
     *获取某一个科室下的未回答问题
     * @return
     */
    public ArrayList<QuestionEntity> getQuestionsInHosDepartment(Long uid, Long hdId)
    {
        ArrayList<QuestionEntity> questions=questionMapper.getQuestionsInHosDepartment(hdId);
        ArrayList<QuestionEntity> questionsAnswered=questionMapper.getAllQuestionAnswered(uid);
        for(int i=0;i<questions.size();i++) {
            for (int j = 0; j < questionsAnswered.size(); j++) {
                if (questions.get(i).getQid() == questionsAnswered.get(j).getQid()) {
                    questions.remove(i);
                    i--;
                    break;
                }
            }
        }
        return questions;
    }

    /**ljy
     * 医生回答某个问题
     * @return
     */
    public boolean insertAnswer(Long uid, Long qid, QuestionAnswerVO questionAnswerVO)
    {
        if(questionAnswerVO.getType()==1)  //选择题
        {
            int answer=0;
            if(questionAnswerVO.getAnswer().equals("yes"))  //1:yes,2:no
                answer=1;
            else
                answer=2;
            questionResultMapper.insertChoiceAnswer(uid,qid,answer, questionAnswerVO.getRemark());
        }
        else
            questionDetailAnswerMapper.insertDetailAnswer(uid,qid, questionAnswerVO.getAnswer(), questionAnswerVO.getRemark());

        return true;
    }


    /**ljy
     * 医生新建一个数据集
     * @return
     */
    public Integer newDataSet(Integer doctorId, DataSetsEntity dataSetsEntity)
    {
        dataSetsMapper.newDataSet(doctorId, dataSetsEntity);
        return Math.toIntExact(dataSetsEntity.getId());
    }


    /**ljy
     * 医生上传数据集
     * @return
     */
    public void uploadDataSet(Integer dId,Integer doctorId, String fileName,String filePath,String fileType)
    {
        if(fileType.equals("train"))
            dataSetsMapper.uploadTrainDataSet(dId,doctorId,filePath,fileName);
        else if(fileType.equals("test"))
            dataSetsMapper.uploadTestDataSet(dId,doctorId,filePath,fileName);
        else
            dataSetsMapper.uploadDevDataSet(dId,doctorId,filePath,fileName);

        //数据处理
        DataSetsEntity dataSetsEntity=dataSetsMapper.getDataSetsById(dId.longValue());
        if(!dataSetsEntity.getTrain_name().equals(null) && !dataSetsEntity.getTest_name().equals(null) && !dataSetsEntity.getDev_name().equals(null))
        {
            String train=dataSetsPathInServer+dataSetsEntity.getTrain_name();
            String test=dataSetsPathInServer+dataSetsEntity.getTest_name();
            String dev=dataSetsPathInServer+dataSetsEntity.getDev_name();
            String dstdir=dataSetsPathInServer+"data_"+dId.toString();
            String modelDstDir=dataSetsPathInServer+"data_"+dId.toString();
            //生成处理数据的配置文件
            fileUtil.createDataSetConfig(dstdir,train,test,dev,modelDstDir);
            String path[]={"sh","-c","cd ../../../../../"};
            String cmd[]={"chmod","../SIGIR_QA/HAR-master/data/pinfo/run_data.sh"};  //运行.sh文件的命令
            Runtime rt = Runtime.getRuntime();
            try
            {
                rt.exec(path);
                rt.exec(cmd);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**ljy
     * 医生上传知识图谱
     * @return
     */
//    public Long uploadKG(Long doctorId,String fileName,String filePath)
//    {
//        return kgMapper.uploadKG(doctorId,fileName,filePath);
//    }

    /**ljy
     * 医生获取全部数据集
     * @return
     */
    public ArrayList<DataSetsEntity> getDataSets(Long doctorId) {
        return dataSetsMapper.getDataSets(doctorId);
    }

    /**ljy
     * 医生获取可用数据集
     * @return
     */
    public ArrayList<DataSetsEntity> getAvailableDataSets(Long doctorId) {
        return dataSetsMapper.getAvailableDataSets(doctorId);
    }

    /**ljy
     * 医生获获取知识图谱
     * @return
     */
//    public ArrayList<DataSetsEntity> getKGS(Long doctorId)
//    {
//        return kgMapper.getKGS(doctorId);
//    }


    /**ljy
     * 医生运行一个深度学习模型
     * @return
     */
    @Async
    public void doDeepModelTask(Long doctorId, DeepModelTaskEntity deepModelTaskEntity)  //异步线程调用
    {

        Long taskResultId=null;  //任务结果id
        String configPath=null;  //配置文件路径
        String configFile=null;  //配置文件名称
        String command="";//运行深度学习的命令
        String outputPath="../SIGIR_QA/output/"; // 输出结果存放路径
        String nowData="";//现在时间
        String[] changeEnvironment = new String[] {"sh","-c","conda activate pytorch"};  //切换环境
        String[] nowPath=new String[]{"sh","-c","cd ../../../../../"};  //进入项目根目录（假设项目和深度学习模型放在同一目录下）
        //String[] nowPath=new String[]{"sh","-c","cd ../../../../../../../../../"};  //进入项目根目录(针对服务器上的项目位置)
        String[] modelPath=new String[3];  //进入模型目录
        modelPath[0]="sh";
        modelPath[1]="-c";
        String[] cmdArr=new String[3];//模型运行
        cmdArr[0]="sh";
        cmdArr[1]="-c";
        Runtime rt = Runtime.getRuntime();

        //向数据库中插入一条深度学习模型信息
        Long taskId=deepModelTaskMapper.doDeepModelTask(doctorId,deepModelTaskEntity.getTaskName(),deepModelTaskEntity.getDatasetId(),deepModelTaskEntity.getKgId(),deepModelTaskEntity.getModelId(),deepModelTaskEntity.getMetricId(),0);
        //查找是否已经有了相同的模型运行结果
        ArrayList<DeepModelTaskEntity> tempDeepModelTask=deepModelTaskMapper.getSimilarityModelTask(deepModelTaskEntity.getDatasetId(),deepModelTaskEntity.getKgId(),deepModelTaskEntity.getModelId(),deepModelTaskEntity.getMetricId());

        if(tempDeepModelTask==null)  //没有找到相同的模型结果
        {
           // DataSetsEntity tempDataSet=dataSetsMapper.getDataSetsById(deepModelTaskEntity.getDatasetId());

            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");  //记录时间
            Date date = new Date();
            nowData = format.format(date);  //记录时间
            outputPath = "../output/" + deepModelTaskEntity.getId().toString() + nowData + ".txt";  //模型结果存放的路径

            DeepModelEntity deepModelEntity = deepModelMapper.getModelById(deepModelTaskEntity.getModelId());  //获取模型信息
            String modelName=deepModelTaskMapper.getTaskNameById(deepModelTaskEntity.getModelId());  //获取模型名称

            if(deepModelTaskEntity.getModelType()==1&&!(modelName.equals("PACRR")||modelName.equals("KNRM")||modelName.equals("DRMMTKS")))  //first type Feature-based
            {
                configPath=featureBasedModelConfigPath;
                configFile=deepModelEntity.getConfigFile();
                modelPath[2]=featureBasedPythonPath;
                command = "python matchzoo/main.py --phase train --model_file "+featureBasedModelConfigPath + deepModelEntity.getConfigFile() + " >>" + outputPath;
            }
            else if(deepModelTaskEntity.getModelType()==2) //context-based
            {
                configPath=contextBasedModelConfigPath;
                configFile=deepModelEntity.getConfigFile();
                modelPath[2]=contextBasedPythonPath;
                command = "python train.py --model "+deepModelEntity.getModelPy()+" --config " + contextBasedModelConfigPath + deepModelEntity.getConfigFile() + " >>" + outputPath;

            }
            else if(deepModelTaskEntity.getModelType()==1||deepModelTaskEntity.getModelType()==3)//knowledge-embedding
            {
                String dataset="";
                if(deepModelTaskEntity.getDatasetId()==2)  //不同数据集的配置
                    dataset="nf_";
                configPath=knowledgeEmbeddingModelConfigPath;
                configFile=dataset+deepModelEntity.getConfigFile();
                modelPath[2]=knowledgeEmbeddingPythonPath;

                command="python train_kg.py --model "+deepModelEntity.getModelPy() +" --config "+ knowledgeEmbeddingModelConfigPath +configFile + " >>" + outputPath;

            }
            else   //our joint model
            {
                configPath=ourJointModelConfigPath+deepModelEntity.getConfigFile().split("/")[0]+"/";
                configFile=deepModelEntity.getConfigFile().split("/")[1];
                modelPath[2]=ourJointPythonPath;
                if(deepModelTaskEntity.getDatasetId()==2)
                {
                    String tempPath[]=deepModelEntity.getConfigFile().split("/");
                    String confFile[]=tempPath[1].split("_");
                    configFile=confFile[0];
                    for(int i=1;i<confFile.length;i++)
                    {
                        if(i==confFile.length-2)
                            configFile+="_nf";
                        else
                            configFile+="_"+confFile[i];
                    }
                }



                command="python train.py --model "+deepModelEntity.getModelPy()+" --config "+configPath+configFile+ " >>" + outputPath;

            }

            fileUtil.createPythonConfig(configPath,configFile,deepModelTaskEntity,deepModelEntity);   //生成配置文件

            try {
                rt.exec(nowPath);// 进入ADDS目录
                rt.exec(modelPath);  //进入模型运行目录
                rt.exec(changeEnvironment);//切换服务器环境
                cmdArr[2]=command;
                rt.exec(cmdArr);  //运行深度学习模型

                //从文本中提取出结果存入数据库
                taskResultId=fileUtil.extractResult(outputPath,taskId,deepModelTaskEntity.getModelType(),modelName);
                }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            //修改数据库信息
            deepModelTaskMapper.updateTask(taskId,1,taskResultId);
        }
        else
            deepModelTaskMapper.updateTask(taskId,1,tempDeepModelTask.get(0).getResultId());

        //向用户发送模型运行完毕的邮件
        DoctorEntity doctorEntity=doctorMapper.getDoctorById(doctorId);
        emailUtil.sendSimpleEmail("ADDS system task completion notification","You have a new completed task, please log in the ADDS system for viewing!",doctorEntity.getEmail());
    }

    /**
     * 医生获取所有任务
     * @author ljy
     * @return
     */
    public ArrayList<DeepModelTaskEntity> getDMTasks(Integer doctorId)
    {
        return deepModelTaskMapper.getDMTasks(doctorId);
    }
}
