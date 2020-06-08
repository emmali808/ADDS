
package cn.medicine.dao;

import cn.medicine.pojo.Graph;
import cn.medicine.pojo.PreviousMH;
import cn.medicine.utils.MyException;
import org.apache.ibatis.annotations.Param;

import java.lang.String;import java.util.List;


/**
 * Created by Administrator on 2016/9/2.
 */
public interface PatientGraphMapper {
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
    public List<Graph> getpatientRecord(@Param("pgID") String pgID, @Param("g_time") String g_time);
    void add(Graph graph);
    long getMaxID();
    List<Graph> getpatientGraph(String pgID);
}

