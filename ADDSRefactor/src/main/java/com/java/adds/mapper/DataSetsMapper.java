package com.java.adds.mapper;


import com.java.adds.entity.DataSetsEntity;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Mapper
@Repository
public interface DataSetsMapper {
    /**ljy
     * 医生新建一个数据集
     * @return
     */
    @Insert("insert into data_sets_upload(dataset_name, dataset_desc, user_id) values(#{dataSetsEntity.dataset_name}, #{dataSetsEntity.dataset_desc}, #{uId})")
    @Options(useGeneratedKeys = true, keyColumn = "id", keyProperty = "dataSetsEntity.id")
    public Integer newDataSet(@Param("uId") Integer doctorId, @Param("dataSetsEntity") DataSetsEntity dataSetsEntity);

    /**ljy
     * 医生上传数据集-train
     * @return
     */
    public void uploadTrainDataSet(@Param("dId") Integer dId,@Param("uId") Integer doctorId,@Param("filePath") String filePath,@Param("fileName") String fileName);

    /**ljy
     * 医生上传数据集-test
     * @return
     */
    public void uploadTestDataSet(@Param("dId") Integer dId,@Param("uId") Integer doctorId, @Param("filePath") String filePath,@Param("fileName") String fileName);

    /**ljy
     * 医生上传数据集-dev
     * @return
     */
    public void uploadDevDataSet(@Param("dId") Integer dId,@Param("uId") Integer doctorId, @Param("filePath") String filePath,@Param("fileName") String fileName);


    /**ljy
     * 医生获取全部数据集
     * @return
     */
    public ArrayList<DataSetsEntity> getDataSets(@Param("uId") Long doctorId);

    /**ljy
     * 医生获取可用数据集
     * @return
     */
    public ArrayList<DataSetsEntity> getAvailableDataSets(@Param("uId") Long doctorId);

    /**ljy
     * 根据id获取dataset
     * @return
     */
    public DataSetsEntity getDataSetsById(@Param("id") Long id);


}
