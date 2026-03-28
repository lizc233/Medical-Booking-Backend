package com.leo.medical.mapper;

import com.github.pagehelper.Page;
import com.leo.medical.annotation.AutoFill;
import com.leo.medical.dto.SetmealPageQueryDTO;
import com.leo.medical.entity.Setmeal;
import com.leo.medical.enumeration.OperationType;
import com.leo.medical.vo.DoctorItemVO;
import com.leo.medical.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface SetmealMapper {

    /**
     * 根据科室id查询套餐的数量
     * @param departmentId
     * @return
     */
    Integer countByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * 向套餐表插入数据
     * @param checkup_package
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal checkup_package);

    /**
     * 分页查询
     * @param checkup_packagePageQueryDTO
     * @return
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO checkup_packagePageQueryDTO);

    /**
     * 根据id查询数据
     * @param id
     * @return
     */
    Setmeal getById(Long id);

    /**
     * 根据id删除数据
     * @param id
     */
    void deleteById(Long id);

    /**
     * 修改套餐表
     * @param checkup_package
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal checkup_package);

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
    List<DoctorItemVO> getDoctorItemBySetmealId(Long id);

    /**
     * 根据条件统计套餐数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);
}
