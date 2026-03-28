package com.leo.medical.service;

import com.leo.medical.dto.DoctorDTO;
import com.leo.medical.dto.DoctorPageQueryDTO;
import com.leo.medical.entity.Doctor;
import com.leo.medical.result.PageResult;
import com.leo.medical.vo.DoctorVO;

import java.util.List;

public interface DoctorService {
    /**
     * 新增医生
     * @param doctorDTO
     */
    void saveWithFlavor(DoctorDTO doctorDTO);

    /**
     * 医生分页查询
     * @param doctorPageQueryDTO
     * @return
     */
    PageResult pageQuery(DoctorPageQueryDTO doctorPageQueryDTO);

    /**
     * 医生批量删除
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询医生
     * @param id
     * @return
     */
    DoctorVO getByIdWithFlavor(Long id);

    /**
     * 修改医生
     * @param doctorDTO
     */
    void updateWithFlavor(DoctorDTO doctorDTO);

    /**
     * 根据科室id查询医生
     * @param departmentId
     * @return
     */
    List<Doctor> list(Long departmentId);

    /**
     * 条件查询医生和特长标签
     * @param doctor
     * @return
     */
    List<DoctorVO> listWithFlavor(Doctor doctor);

    /**
     * 医生起售停售
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);
}
