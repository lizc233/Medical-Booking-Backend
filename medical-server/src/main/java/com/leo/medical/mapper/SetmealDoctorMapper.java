package com.leo.medical.mapper;

import com.leo.medical.entity.SetmealDoctor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetmealDoctorMapper {
    /**
     * 判断当前医生是否被套餐关联了
     * @param ids
     * @return
     */
    List<Long> getSetmealIdsByDoctorIds(List<Long> ids);

    /**
     * 保存套餐和医生的关联关系
     * @param checkup_packageDoctores
     */
    void insertBatch(List<SetmealDoctor> checkup_packageDoctores);

    /**
     * 删除套餐餐品关系表中的数据
     * @param id
     */
    void deleteBySetmaleId(Long id);

    /**
     * 根据套餐信息查询医生信息
     * @param id
     * @return
     */
    List<SetmealDoctor> getBySetmealId(Long id);
}
