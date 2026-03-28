package com.leo.medical.mapper;

import com.github.pagehelper.Page;
import com.leo.medical.annotation.AutoFill;
import com.leo.medical.enumeration.OperationType;
import com.leo.medical.dto.DepartmentPageQueryDTO;
import com.leo.medical.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DepartmentMapper {

    /**
     * 插入数据
     * @param department
     */
    @AutoFill(OperationType.INSERT)
    void insert(Department department);

    /**
     * 分页查询
     * @param departmentPageQueryDTO
     * @return
     */
    Page<Department> pageQuery(DepartmentPageQueryDTO departmentPageQueryDTO);

    /**
     * 根据id删除科室
     * @param id
     */
    void deleteById(@Param("id") Long id);

    /**
     * 根据id修改科室
     * @param department
     */
    @AutoFill(OperationType.UPDATE)
    void update(Department department);

    /**
     * 根据类型查询科室
     * @param type
     * @return
     */
    List<Department> list(Integer type);
}
