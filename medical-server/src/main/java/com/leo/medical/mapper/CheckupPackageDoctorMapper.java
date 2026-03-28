package com.leo.medical.mapper;

import com.leo.medical.entity.CheckupPackageDoctor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CheckupPackageDoctorMapper {
    /**
     * 判断当前医生是否被医疗体验套餐关联了
     * @param ids
     * @return
     */
    List<Long> getCheckupPackageIdsByDoctorIds(List<Long> ids);

    /**
     * 保存医疗体验套餐和医生的关联关系
     * @param checkup_packageDoctores
     */
    void insertBatch(List<CheckupPackageDoctor> checkup_packageDoctores);

    /**
     * 删除医疗体验套餐餐品关系表中的数据
     * @param id
     */
    void deleteBySetmaleId(Long id);

    /**
     * 根据医疗体验套餐信息查询医生信息
     * @param id
     * @return
     */
    List<CheckupPackageDoctor> getByCheckupPackageId(Long id);
}
