package cn.medicine.dao;

import cn.medicine.pojo.Symptoms;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface SymptomsDao {
    /**give me service...
     *
     * @Function:    getpatientSymptoms
     * @Description:   获得用户症状  
     *                 <功能详细描述>
     *
     * @param symid
     * @return
     */

    List<Symptoms> getpatientSymptoms(long symid);
    void add(Symptoms symptoms);
    long getMaxID();
}
