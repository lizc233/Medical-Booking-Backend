package com.leo.medical.mapper;

import com.github.pagehelper.Page;
import com.leo.medical.annotation.AutoFill;
import com.leo.medical.dto.DoctorPageQueryDTO;
import com.leo.medical.entity.Doctor;
import com.leo.medical.enumeration.OperationType;
import com.leo.medical.vo.DoctorVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface DoctorMapper {

    /**
     * 根据科室id查询医生数量
     * @param departmentId
     * @return
     */
    Integer countByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * 添加医生
     * @param doctor
     */
    @AutoFill(OperationType.INSERT)
    void insert(Doctor doctor);

    /**
     * 医生分页查询
     * @param doctorPageQueryDTO
     * @return
     */
    Page<DoctorVO> pageQuery(DoctorPageQueryDTO doctorPageQueryDTO);

    /**
     * 获取医生
     * @param id
     * @return
     */
    Doctor getById(Long id);

    /**
     * 删除医生表中的医生数据
     * @param id
     */
    void deleteById(Long id);

    /**
     * 修改医生基本信息
     * @param doctor
     */
    @AutoFill(OperationType.UPDATE)
    void update(Doctor doctor);

    /**
     * 根据科室id查询医生
     * @param doctor
     * @return
     */
    List<Doctor> list(Doctor doctor);

    /**
     * 根据医疗体验套餐id查询医生
     * @param id
     * @return
     */
    List<Doctor> getByCheckupPackageId(Long id);

    /**
     * 根据条件统计医生数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
