package com.leo.medical.service;

import com.leo.medical.dto.DepartmentDTO;
import com.leo.medical.dto.DepartmentPageQueryDTO;
import com.leo.medical.entity.Department;
import com.leo.medical.result.PageResult;
import java.util.List;

public interface DepartmentService {

    /**
     * 新增科室
     * @param departmentDTO
     */
    void save(DepartmentDTO departmentDTO);

    /**
     * 分页查询
     * @param departmentPageQueryDTO
     * @return
     */
    PageResult pageQuery(DepartmentPageQueryDTO departmentPageQueryDTO);

    /**
     * 根据id删除科室
     * @param id
     */
    void deleteById(Long id);

    /**
     * 修改科室
     * @param departmentDTO
     */
    void update(DepartmentDTO departmentDTO);

    /**
     * 启用、禁用科室
     * @param status
     * @param id
     */
    void startOrStop(Integer status, Long id);

    /**
     * 根据类型查询科室
     * @param type
     * @return
     */
    List<Department> list(Integer type);
}
