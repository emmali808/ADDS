package cn.medicine.dao;

import cn.medicine.pojo.Diagnose;
import cn.medicine.pojo.Graph;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Administrator on 2016/9/3.
 */
public interface DiagnoseMapper {
    /**give me service...
     *
     * @Function:    getpatientDiagnse
     * @Description:   获得用户诊断结果  
     *                 <功能详细描述>
     *
     * @param diagid
     * @return
     */

    public List<Diagnose> getpatientDiagnose(@Param("diagid") long diadig);
    void add(Diagnose diagnose);
    long getMaxID();
}
