package cn.medicine.dao;

import cn.medicine.pojo.Symptoms;
import cn.medicine.pojo.Treatment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface SymptomsMapper {
    /**give me service...
     *
     * @Function:    getpatientSymptoms
     * @Description:   获得用户症状  
     *                 <功能详细描述>
     *
     * @param symid
     * @return
     */

     List<Symptoms> getpatientSymptoms(@Param("symid") long symid);
    void add(Symptoms symptoms);
    long getMaxID();
}
