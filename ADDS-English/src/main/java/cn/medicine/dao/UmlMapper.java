package cn.medicine.dao;

import cn.medicine.graph.entity.UmlEntity;
import cn.medicine.pojo.Diagnose;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/3.
 */
@Repository
public interface UmlMapper {

    public ArrayList<UmlEntity> getAllUmlEntity();

    public UmlEntity searchUmlEntity(Long id);

    public ArrayList<Long> searchEquality(Long id);

    public ArrayList<Long> searchInverse_is_a(Long id);

    public ArrayList<Long> searchIs_a(Long id);

    public ArrayList<Long> searchRelated(Long id);
}
