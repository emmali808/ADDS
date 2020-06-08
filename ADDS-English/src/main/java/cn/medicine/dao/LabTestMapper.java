package cn.medicine.dao;

import cn.medicine.pojo.Graph;
import cn.medicine.pojo.LabTest;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/9/13.
 */
public interface LabTestMapper {
    /**give me service...
     *
     * @Function:    getpatientLabTest
     * @Description:   获得用户检测结果  
     *                 <功能详细描述>
     *
     * @param labid
     * @return
     */

    public List<LabTest> getpatientLabTest(@Param("labid") long labid);
    void add(LabTest labTest);
    long getMaxID();
}
