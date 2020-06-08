package cn.medicine.dao;

import cn.medicine.pojo.PreviousMH;

import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface PreviousMHDao {
    /**give me service...
     *
     * @Function:    getpatientTreatment
     * @Description:   获得用户治疗方法  
     *                 <功能详细描述>
     *
     * @param preid
     * @return
     */

    public List<PreviousMH> getpatientPreviousMH(long preid);
    void add(PreviousMH previousMH);
    long getMaxID();
}
