package cn.medicine.dao;

import cn.medicine.pojo.LabTest;

import java.util.List;

/**
 * Created by Administrator on 2016/9/13.
 */
public interface LabTestDao {
    /**give me service...
     *
     * @Function:    getpatientLabTest
     * @Description:   获得用户检测结果  
     *                 <功能详细描述>
     *
     * @param labid
     * @return
     */

    public List<LabTest> getpatientLabTest(long labid);
    void add(LabTest labTest);
    long getMaxID();
}
