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
import com.leo.medical.service.CheckupPackageService;
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
public class CheckupPackageServiceImpl implements CheckupPackageService {

    @Autowired
    private SetmealMapper checkup_packageMapper;

    @Autowired
    private SetmealDoctorMapper checkup_packageDoctorMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    /**
     * 新增套餐
     * @param checkup_packageDTO
     */
    @Override
    @Transactional
    public void saveWithDoctor(SetmealDTO checkup_packageDTO) {
        Setmeal checkup_package = new Setmeal();
        BeanUtils.copyProperties(checkup_packageDTO, checkup_package);

//        向套餐表插入数据
        checkup_packageMapper.insert(checkup_package);

//        获取生成的套餐id
        Long id = checkup_package.getId();

//        设置id
        List<SetmealDoctor> checkup_packageDoctores = checkup_packageDTO.getSetmealDoctores();
        checkup_packageDoctores.forEach(checkup_packageDoctor -> checkup_packageDoctor.setSetmealId(id));

//        保存套餐和医生的关联关系
        checkup_packageDoctorMapper.insertBatch(checkup_packageDoctores);
    }

    /**
     * 分页查询
     * @param checkup_packagePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(SetmealPageQueryDTO checkup_packagePageQueryDTO) {
        PageHelper.startPage(checkup_packagePageQueryDTO.getPage(), checkup_packagePageQueryDTO.getPageSize());

        Page<SetmealVO> page = checkup_packageMapper.pageQuery(checkup_packagePageQueryDTO);
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
            Setmeal checkup_package = checkup_packageMapper.getById(id);
            if (StatusConstant.ENABLE == checkup_package.getStatus()) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        ids.forEach(id->{
//            删除套餐表中的数据
            checkup_packageMapper.deleteById(id);

//            删除套餐餐品关系表中的数据
            checkup_packageDoctorMapper.deleteBySetmaleId(id);
        });

    }

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    @Override
    public SetmealVO getByIdWithDoctor(Long id) {
        SetmealVO checkup_packageVO = new SetmealVO();

//        查询套餐基本信息
        Setmeal checkup_package = checkup_packageMapper.getById(id);
        BeanUtils.copyProperties(checkup_package, checkup_packageVO);

//        根据套餐信息查询医生信息
        List<SetmealDoctor> checkup_packageDoctorList = checkup_packageDoctorMapper.getBySetmealId(id);
        checkup_packageVO.setSetmealDoctores(checkup_packageDoctorList);

        return checkup_packageVO;
    }

    /**
     * 修改套餐
     * @param checkup_packageDTO
     */
    @Override
    public void update(SetmealDTO checkup_packageDTO) {
        Setmeal checkup_package = new Setmeal();
        BeanUtils.copyProperties(checkup_packageDTO, checkup_package);

//        1.修改套餐表，执行update
        checkup_packageMapper.update(checkup_package);

//        套餐id
        Long id = checkup_packageDTO.getId();

//        2.删除套餐和医生的关联关系
        checkup_packageDoctorMapper.deleteBySetmaleId(checkup_packageDTO.getId());

        List<SetmealDoctor> checkup_packageDoctores = checkup_packageDTO.getSetmealDoctores();
        checkup_packageDoctores.forEach(checkup_packageDoctor -> checkup_packageDoctor.setSetmealId(id));

//        3.重新插入套餐和医生的关联关系
        checkup_packageDoctorMapper.insertBatch(checkup_packageDoctores);
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

        Setmeal checkup_package = Setmeal.builder()
                .id(id)
                .status(status)
                .build();
        checkup_packageMapper.update(checkup_package);
    }

    /**
     * 条件查询
     * @param checkup_package
     * @return
     */
    @Override
    public List<Setmeal> list(Setmeal checkup_package) {
        return checkup_packageMapper.list(checkup_package);
    }

    /**
     * 根据id查询医生选项
     * @param id
     * @return
     */
    @Override
    public List<DoctorItemVO> getDoctorItemById(Long id) {
        return checkup_packageMapper.getDoctorItemBySetmealId(id);
    }
}
