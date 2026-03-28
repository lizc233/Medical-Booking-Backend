package com.leo.medical.service;

import com.leo.medical.dto.CheckupPackageDTO;
import com.leo.medical.dto.CheckupPackagePageQueryDTO;
import com.leo.medical.entity.CheckupPackage;
import com.leo.medical.result.PageResult;
import com.leo.medical.vo.DoctorItemVO;
import com.leo.medical.vo.CheckupPackageVO;

import java.util.List;

public interface CheckupPackageService {
    /**
     * 新增医疗体验套餐
     * @param checkup_packageDTO
     */
    void saveWithDoctor(CheckupPackageDTO checkup_packageDTO);

    /**
     * 分页查询
     * @param checkup_packagePageQueryDTO
     * @return
     */
    PageResult pageQuery(CheckupPackagePageQueryDTO checkup_packagePageQueryDTO);

    /**
     * 批量删除医疗体验套餐
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询医疗体验套餐
     * @param id
     * @return
     */
    CheckupPackageVO getByIdWithDoctor(Long id);

    /**
     * 修改医疗体验套餐
     * @param checkup_packageDTO
     */
    void update(CheckupPackageDTO checkup_packageDTO);

    /**
     * 医疗体验套餐在岗离岗
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 条件查询
     * @param checkup_package
     * @return
     */
    List<CheckupPackage> list(CheckupPackage checkup_package);

    /**
     * 根据id查询医生选项
     * @param id
     * @return
     */
    List<DoctorItemVO> getDoctorItemById(Long id);
}
