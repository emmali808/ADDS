package cn.medicine.dao;

import cn.medicine.pojo.Treatment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface TreatmentMapper {
    /**give me service...
     *
     * @Function:    getpatientTreatment
     * @Description:   获得用户治疗方法  
     *                 <功能详细描述>
     *
     * @param treatid
     * @return
     */

    List<Treatment> getpatientTreatment(@Param("treatid") long treatid);
    void add(Treatment treatment);
    long getMaxID();
}
