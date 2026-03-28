package com.leo.medical.mapper;

import com.leo.medical.entity.SpecialTag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SpecialTagMapper {
//    批量插入特长标签数据
    void insertBatch(List<SpecialTag> flavors);

//    删除菜单关联的特长标签数据
    void deleteByDoctorId(Long id);

//    根据医生id查询特长标签数据
    List<SpecialTag> getByDoctorId(Long id);
}
