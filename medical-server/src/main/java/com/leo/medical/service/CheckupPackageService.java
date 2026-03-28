package com.leo.medical.service;

import com.leo.medical.dto.SetmealDTO;
import com.leo.medical.dto.SetmealPageQueryDTO;
import com.leo.medical.entity.Setmeal;
import com.leo.medical.result.PageResult;
import com.leo.medical.vo.DoctorItemVO;
import com.leo.medical.vo.SetmealVO;

import java.util.List;

public interface CheckupPackageService {
    /**
     * 新增套餐
     * @param checkup_packageDTO
     */
    void saveWithDoctor(SetmealDTO checkup_packageDTO);

    /**
     * 分页查询
     * @param checkup_packagePageQueryDTO
     * @return
     */
    PageResult pageQuery(SetmealPageQueryDTO checkup_packagePageQueryDTO);

    /**
     * 批量删除套餐
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    SetmealVO getByIdWithDoctor(Long id);

    /**
     * 修改套餐
     * @param checkup_packageDTO
     */
    void update(SetmealDTO checkup_packageDTO);

    /**
     * 套餐起售停售
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 条件查询
     * @param checkup_package
     * @return
     */
    List<Setmeal> list(Setmeal checkup_package);

    /**
     * 根据id查询医生选项
     * @param id
     * @return
     */
    List<DoctorItemVO> getDoctorItemById(Long id);
}
