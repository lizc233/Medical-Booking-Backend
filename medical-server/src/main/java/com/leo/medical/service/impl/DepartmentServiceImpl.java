package com.leo.medical.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leo.medical.constant.MessageConstant;
import com.leo.medical.constant.StatusConstant;
import com.leo.medical.context.BaseContext;
import com.leo.medical.dto.DepartmentDTO;
import com.leo.medical.dto.DepartmentPageQueryDTO;
import com.leo.medical.entity.Department;
import com.leo.medical.exception.DeletionNotAllowedException;
import com.leo.medical.mapper.DepartmentMapper;
import com.leo.medical.mapper.DoctorMapper;
import com.leo.medical.mapper.CheckupPackageMapper;
import com.leo.medical.result.PageResult;
import com.leo.medical.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 科室业务层
 */
@Service
@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private DoctorMapper doctorMapper;
    @Autowired
    private CheckupPackageMapper checkup_packageMapper;

    /**
     * 新增科室
     * @param departmentDTO
     */
    public void save(DepartmentDTO departmentDTO) {
        Department department = new Department();
        //属性拷贝
        BeanUtils.copyProperties(departmentDTO, department);

        //科室状态默认为禁用状态0
        department.setStatus(StatusConstant.DISABLE);

        //设置创建时间、修改时间、创建人、修改人
//        department.setCreateTime(LocalDateTime.now());
//        department.setUpdateTime(LocalDateTime.now());
//        department.setCreateUser(BaseContext.getCurrentId());
//        department.setUpdateUser(BaseContext.getCurrentId());

        departmentMapper.insert(department);
    }

    /**
     * 分页查询
     * @param departmentPageQueryDTO
     * @return
     */
    public PageResult pageQuery(DepartmentPageQueryDTO departmentPageQueryDTO) {
        PageHelper.startPage(departmentPageQueryDTO.getPage(),departmentPageQueryDTO.getPageSize());
        //下一条sql进行分页，自动加入limit关键字分页
        Page<Department> page = departmentMapper.pageQuery(departmentPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据id删除科室
     * @param id
     */
    public void deleteById(Long id) {
        //查询当前科室是否关联了医生，如果关联了就抛出业务异常
        Integer count = doctorMapper.countByDepartmentId(id);
        if(count > 0){
            //当前科室下有医生，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DOCTOR);
        }

        //查询当前科室是否关联了医疗体验套餐，如果关联了就抛出业务异常
        count = checkup_packageMapper.countByDepartmentId(id);
        if(count > 0){
            //当前科室下有医生，不能删除
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        }

        //删除科室数据
        departmentMapper.deleteById(id);
    }

    /**
     * 修改科室
     * @param departmentDTO
     */
    public void update(DepartmentDTO departmentDTO) {
        Department department = new Department();
        BeanUtils.copyProperties(departmentDTO,department);

        //设置修改时间、修改人
//        department.setUpdateTime(LocalDateTime.now());
//        department.setUpdateUser(BaseContext.getCurrentId());

        departmentMapper.update(department);
    }

    /**
     * 启用、禁用科室
     * @param status
     * @param id
     */
    public void startOrStop(Integer status, Long id) {
        Department department = Department.builder()
                .id(id)
                .status(status)
                .updateTime(LocalDateTime.now())
                .updateUser(BaseContext.getCurrentId())
                .build();
        departmentMapper.update(department);
    }

    /**
     * 根据类型查询科室
     * @param type
     * @return
     */
    public List<Department> list(Integer type) {
        return departmentMapper.list(type);
    }
}
