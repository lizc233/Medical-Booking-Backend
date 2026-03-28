package com.leo.medical.mapper;

import com.leo.medical.entity.DoctorFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DoctorFlavorMapper {
//    批量插入口味数据
    void insertBatch(List<DoctorFlavor> flavors);

//    删除菜单关联的口味数据
    void deleteByDoctorId(Long id);

//    根据医生id查询口味数据
    List<DoctorFlavor> getByDoctorId(Long id);
}
