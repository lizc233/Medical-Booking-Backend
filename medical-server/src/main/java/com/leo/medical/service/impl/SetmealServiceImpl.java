package com.leo.medical.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leo.medical.constant.MessageConstant;
import com.leo.medical.constant.StatusConstant;
import com.leo.medical.dto.SetmealDTO;
import com.leo.medical.dto.SetmealPageQueryDTO;
import com.leo.medical.entity.Doctor;
import com.leo.medical.entity.Setmeal;
import com.leo.medical.entity.SetmealDoctor;
import com.leo.medical.exception.DeletionNotAllowedException;
import com.leo.medical.exception.SetmealEnableFailedException;
import com.leo.medical.mapper.DoctorMapper;
import com.leo.medical.mapper.SetmealDoctorMapper;
import com.leo.medical.mapper.SetmealMapper;
import com.leo.medical.result.PageResult;
import com.leo.medical.service.SetmealService;
import com.leo.medical.vo.DoctorItemVO;
import com.leo.medical.vo.SetmealVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDoctorMapper setmealDoctorMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    /**
     * 新增套餐
     * @param setmealDTO
     */
    @Override
    @Transactional
    public void saveWithDoctor(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

//        向套餐表插入数据
        setmealMapper.insert(setmeal);

//        获取生成的套餐id
        Long id = setmeal.getId();

//        设置id
        List<SetmealDoctor> setmealDoctores = setmealDTO.getSetmealDoctores();
        setmealDoctores.forEach(setmealDoctor -> setmealDoctor.setSetmealId(id));

//        保存套餐和医生的关联关系
        setmealDoctorMapper.insertBatch(setmealDoctores);
    }

    /**
     * 分页查询
     * @param setmealPageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(), setmealPageQueryDTO.getPageSize());

        Page<SetmealVO> page = setmealMapper.pageQuery(setmealPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Override
    public void deleteBatch(List<Long> ids) {
//        起售中的套餐不能删除
        ids.forEach(id->{
            Setmeal setmeal = setmealMapper.getById(id);
            if (StatusConstant.ENABLE == setmeal.getStatus()) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        ids.forEach(id->{
//            删除套餐表中的数据
            setmealMapper.deleteById(id);

//            删除套餐餐品关系表中的数据
            setmealDoctorMapper.deleteBySetmaleId(id);
        });

    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO getByIdWithDoctor(Long id) {
        SetmealVO setmealVO = new SetmealVO();

//        查询套餐基本信息
        Setmeal setmeal = setmealMapper.getById(id);
        BeanUtils.copyProperties(setmeal, setmealVO);

//        根据套餐信息查询医生信息
        List<SetmealDoctor> setmealDoctorList = setmealDoctorMapper.getBySetmealId(id);
        setmealVO.setSetmealDoctores(setmealDoctorList);

        return setmealVO;
    }

    /**
     * 修改套餐
     * @param setmealDTO
     */
    @Override
    public void update(SetmealDTO setmealDTO) {
        Setmeal setmeal = new Setmeal();
        BeanUtils.copyProperties(setmealDTO, setmeal);

//        1.修改套餐表，执行update
        setmealMapper.update(setmeal);

//        套餐id
        Long id = setmealDTO.getId();

//        2.删除套餐和医生的关联关系
        setmealDoctorMapper.deleteBySetmaleId(setmealDTO.getId());

        List<SetmealDoctor> setmealDoctores = setmealDTO.getSetmealDoctores();
        setmealDoctores.forEach(setmealDoctor -> setmealDoctor.setSetmealId(id));

//        3.重新插入套餐和医生的关联关系
        setmealDoctorMapper.insertBatch(setmealDoctores);
    }

    /**
     * 套餐起售停售
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
//        起售套餐时，判断套餐内是否有停售医生，有停售医生提示"套餐内包含未启售医生，无法启售"
        if (status == StatusConstant.ENABLE) {
            List<Doctor> doctorList = doctorMapper.getBySetmealId(id);
            if (doctorList != null && doctorList.size() > 0) {
                doctorList.forEach(doctor -> {
                    if (StatusConstant.DISABLE == doctor.getStatus()) { // 有停售商品
                        throw new SetmealEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }

        Setmeal setmeal = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        setmealMapper.update(setmeal);
    }

    /**
     * 条件查询
     * @param setmeal
     * @return
     */
    @Override
    public List<Setmeal> list(Setmeal setmeal) {
        return setmealMapper.list(setmeal);
    }

    /**
     * 根据id查询医生选项
     * @param id
     * @return
     */
    @Override
    public List<DoctorItemVO> getDoctorItemById(Long id) {
        return setmealMapper.getDoctorItemBySetmealId(id);
    }
}
