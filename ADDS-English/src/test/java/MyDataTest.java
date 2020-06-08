import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.medicine.Contant.Contant;
import cn.medicine.pojo.Message;
import cn.medicine.pojo.Question;
import cn.medicine.pojo.QuestionAndResult;
import cn.medicine.service.IMessageService;
import cn.medicine.service.IMyDataService;
import cn.medicine.service.IQuestionResultService;
import cn.medicine.service.IQuestionService;
import cn.medicine.utils.JsonUtil;
import cn.medicine.utils.MyException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"file:src/main/webapp/WEB-INF/spring-mybatis.xml"})
public class MyDataTest {
    
    private final static Logger logger = LogManager.getLogger(MyDataTest.class);
    
    @Autowired
    IMyDataService dataService;
    
    @Autowired
    IMessageService messageService;
    
    @Autowired
    IQuestionService questionService;
    @Autowired
    IQuestionResultService qrService;
    
    @Test
    public void testadd(){
        JSONArray array=new JSONArray();
        JSONObject object=new JSONObject();
        object.put("id", 5);
        object.put("name", "xxx");
        array.add(object);
        dataService.add(Contant.websocketuser,array.toJSONString());
    }
    @Test
    public void testupdate(){
        String content=dataService.get(Contant.websocketuser);
        logger.info("content:"+content);
        JSONArray array=JsonUtil.getJSONArrayFromJsonString(content);
        JSONObject object=new JSONObject();
        object.put("id", 2);
        object.put("name", "ttt");
        array.add(object);
        
        dataService.update(Contant.websocketuser,array.toJSONString());
    }
    
    @Test
    public void testmessageAdd(){
        Message m=new Message(3l,"aaaa",4l,"nihao",new Date());
        try {
            messageService.add(m);
        } catch (MyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    @Test
    public void testquestion(){
        List<Long> idlist=new ArrayList<Long>();
        idlist.add(1l);
        idlist.add(7l);
        try {
            Map<Long,Question> qmap=questionService.getByQuestionidlist(idlist);
            
            System.out.println(qmap.size());
        } catch (MyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Test
    public void testquestionresult(){
        long userid=5l;
        try {
            List<QuestionAndResult> qars=qrService.getQuestionAndResultByUserid(userid);
            System.out.println(qars.size());
            Iterator<QuestionAndResult> it=qars.iterator();
            while(it.hasNext()){
                System.out.println(it.next().toString());
            }
        } catch (MyException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
