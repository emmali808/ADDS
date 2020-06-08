package cn.medicine.pojo;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;
/**
 * 
 * @ClassName:MyData
 * @Description: 一些重要数据的存储，适合name和content都是字符串类型的数据，有点类似于key-value
 * @Function List:TODO 主要函数及其功能
 *
 * @author   ztxu
 * @version  
 * @Date	 2016-9-4下午4:13:13
 *
 * @History:// 历史修改记录 
 * <author>  // 修改人
 * <time>    // 修改时间
 * <version> // 版本
 * <desc>    // 描述修改内容
 */
public class MyData implements Serializable{
    
    private static final long serialVersionUID = 80046362660927429L;
    
    private String name;
    private String content;
    
    public MyData(){
        super();
    }
    
    public MyData(String name,String content){
        super();
        this.name=name;
        this.content=content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        JSONObject object=new JSONObject();
        object.put("name", name);
        object.put("content", content);
        return object.toString();
    }
    
    

}
