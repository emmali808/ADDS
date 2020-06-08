package cn.medicine.dao;

import cn.medicine.pojo.Graph;
import cn.medicine.utils.MyException;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.lang.String;import java.util.List;

/**
 * Created by Administrator on 2016/9/1.
 */
//@Repository
public interface PatientGraphDao {
    /**give me service...
     *
     * @Function:    getpatientRccord 
     * @Description:   获得用户记录  
     *                 <功能详细描述>
     *
     * @param pgID
     * @param g_time
     * @return
     */
    public List<Graph> getpatientRecord(String pgID,String g_time) throws MyException;
    void add(Graph graph);
    long getMaxID();
    List<Graph> getpatientGraph(String pgID);
}
