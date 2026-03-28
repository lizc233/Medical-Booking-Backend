package com.leo.medical.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leo.medical.constant.MessageConstant;
import com.leo.medical.constant.StatusConstant;
import com.leo.medical.dto.DoctorDTO;
import com.leo.medical.dto.DoctorPageQueryDTO;
import com.leo.medical.entity.Doctor;
import com.leo.medical.entity.DoctorFlavor;
import com.leo.medical.entity.Setmeal;
import com.leo.medical.exception.DeletionNotAllowedException;
import com.leo.medical.mapper.DoctorFlavorMapper;
import com.leo.medical.mapper.DoctorMapper;
import com.leo.medical.mapper.SetmealDoctorMapper;
import com.leo.medical.mapper.SetmealMapper;
import com.leo.medical.result.PageResult;
import com.leo.medical.service.DoctorService;
import com.leo.medical.vo.DoctorVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorServiceImpl implements DoctorService {

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private DoctorFlavorMapper doctorFlavorMapper;

    @Autowired
    private SetmealDoctorMapper setmealDoctorMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    /**
     * 新增医生
     * @param doctorDTO
     */
    @Override
    @Transactional
    public void saveWithFlavor(DoctorDTO doctorDTO) {
        Doctor doctor = new Doctor();
        BeanUtils.copyProperties(doctorDTO, doctor);

//        向医生表插入1条数据
        doctorMapper.insert(doctor);

//        获取insert语句生成的主键值
        Long doctorId = doctor.getId();

        List<DoctorFlavor> flavors = doctorDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(doctorFlavor -> doctorFlavor.setDoctorId(doctorId));
//            向口味表插入n条数据
            doctorFlavorMapper.insertBatch(flavors);
        }
    }

    /**
     * 医生分页查询
     * @param doctorPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(DoctorPageQueryDTO doctorPageQueryDTO) {
        PageHelper.startPage(doctorPageQueryDTO.getPage(), doctorPageQueryDTO.getPageSize());
        Page<DoctorVO> page=doctorMapper.pageQuery(doctorPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 医生批量删除
     * @param ids
     */
    @Override
    @Transactional
    public void deleteBatch(List<Long> ids) {
//        判断当前医生是否能够删除---是否存在起售中的医生？？
        ids.forEach(id->{
            Doctor doctor = doctorMapper.getById(id);
            if (doctor.getStatus() == StatusConstant.ENABLE) {
//                当前医生处于起售中，不能删除
                throw new DeletionNotAllowedException(MessageConstant.DOCTOR_ON_SALE);
            }
        });

//        判断当前医生是否能够删除---是否被套餐关联了？？
        List<Long> setmealIds = setmealDoctorMapper.getSetmealIdsByDoctorIds(ids);
        if (setmealIds != null && setmealIds.size() > 0) {
//            当前医生被套餐关联了，不能删除
            throw new DeletionNotAllowedException(MessageConstant.DOCTOR_BE_RELATED_BY_SETMEAL);
        }

//        删除医生表中的医生数据
        ids.forEach(id->{
            doctorMapper.deleteById(id);

//            删除菜单关联的口味数据
            doctorFlavorMapper.deleteByDoctorId(id);
        });

    }

    /**
     * 根据id查询医生
     * @param id
     * @return
     */
    @Override
    public DoctorVO getByIdWithFlavor(Long id) {
//        根据id查询医生数据
        Doctor doctor = doctorMapper.getById(id);

//        根据医生id查询口味数据
        List<DoctorFlavor> doctorFlavorList = doctorFlavorMapper.getByDoctorId(id);

//        将查询到的数据封装到vo
        DoctorVO doctorVO = new DoctorVO();
        BeanUtils.copyProperties(doctor, doctorVO);
        doctorVO.setFlavors(doctorFlavorList);

        return doctorVO;
    }

    /**
     * 修改医生
     * @param doctorDTO
     */
    @Override
    public void updateWithFlavor(DoctorDTO doctorDTO) {
        Doctor doctor = new Doctor();
        BeanUtils.copyProperties(doctorDTO, doctor);

//        修改医生基本信息
        doctorMapper.update(doctor);

//        删除原有的口味信息
        doctorFlavorMapper.deleteByDoctorId(doctorDTO.getId());

//        重新插入口味数据
        List<DoctorFlavor> flavors = doctorDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(doctorFlavor -> doctorFlavor.setDoctorId(doctorDTO.getId()));

//            向口味表插入n条数据
            doctorFlavorMapper.insertBatch(flavors);
        }

    }

    /**
     * 根据科室id查询医生
     * @param departmentId
     * @return
     */
    @Override
    public List<Doctor> list(Long departmentId) {
        Doctor doctor = Doctor.builder()
                .departmentId(departmentId)
                .status(StatusConstant.ENABLE)
                .build();
        return doctorMapper.list(doctor);
    }

    /**
     * 条件查询医生和口味
     * @param doctor
     * @return
     */
    @Override
    public List<DoctorVO> listWithFlavor(Doctor doctor) {
        List<Doctor> doctorList = doctorMapper.list(doctor);

        ArrayList<DoctorVO> doctorVOArrayList = new ArrayList<>();

        doctorList.forEach(d->{
            DoctorVO doctorVO = new DoctorVO();
            BeanUtils.copyProperties(d, doctorVO);

//            根据医生id查询对应的口味
            List<DoctorFlavor> flavors = doctorFlavorMapper.getByDoctorId(d.getId());

            doctorVO.setFlavors(flavors);
            doctorVOArrayList.add(doctorVO);
        });

        return doctorVOArrayList;
    }

    /**
     * 医生起售停售
     * @param status
     * @param id
     */
    @Override
    @Transactional
    public void startOrStop(Integer status, Long id) {
        Doctor doctor = Doctor.builder()
                .id(id)
                .status(status)
                .build();
        doctorMapper.update(doctor);

        if (status == StatusConstant.DISABLE) {
            // 如果是停售操作，还需要将包含当前医生的套餐也停售
            List<Long> doctorIds = new ArrayList<>();
            doctorIds.add(id);
            // select setmeal_id from setmeal_doctor where doctor_id in (?,?,?)
            List<Long> setmealIds = setmealDoctorMapper.getSetmealIdsByDoctorIds(doctorIds);
            if (setmealIds != null && setmealIds.size() > 0) {
                for (Long setmealId : setmealIds) {
                    Setmeal setmeal = Setmeal.builder()
                            .id(setmealId)
                            .status(StatusConstant.DISABLE)
                            .build();
                    setmealMapper.update(setmeal);
                }
            }
        }
    }

}
