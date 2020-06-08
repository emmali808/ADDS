package cn.medicine.dao;

import cn.medicine.pojo.Diagnose;
import cn.medicine.pojo.Graph;

import java.util.List;

/**
 * Created by Administrator on 2016/9/3.
 */
public interface DiagnoseDao {
    /**give me service...
     *
     * @Function:    getpatientDiagnse
     * @Description:   获得用户诊断结果  
     *                 <功能详细描述>
     *
     * @param diagid
     * @return
     */
    List<Diagnose> getpatientDiagnose(long diagid);
    void add(Diagnose diagnose);
    long getMaxID();
}
