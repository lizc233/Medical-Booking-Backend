package com.leo.medical.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.leo.medical.constant.MessageConstant;
import com.leo.medical.constant.StatusConstant;
import com.leo.medical.dto.CheckupPackageDTO;
import com.leo.medical.dto.CheckupPackagePageQueryDTO;
import com.leo.medical.entity.Doctor;
import com.leo.medical.entity.CheckupPackage;
import com.leo.medical.entity.CheckupPackageDoctor;
import com.leo.medical.exception.DeletionNotAllowedException;
import com.leo.medical.exception.CheckupPackageEnableFailedException;
import com.leo.medical.mapper.DoctorMapper;
import com.leo.medical.mapper.CheckupPackageDoctorMapper;
import com.leo.medical.mapper.CheckupPackageMapper;
import com.leo.medical.result.PageResult;
import com.leo.medical.service.CheckupPackageService;
import com.leo.medical.vo.DoctorItemVO;
import com.leo.medical.vo.CheckupPackageVO;
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
    private CheckupPackageMapper checkup_packageMapper;

    @Autowired
    private CheckupPackageDoctorMapper checkup_packageDoctorMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    /**
     * 新增医疗体验套餐
     * @param checkup_packageDTO
     */
    @Override
    @Transactional
    public void saveWithDoctor(CheckupPackageDTO checkup_packageDTO) {
        CheckupPackage checkup_package = new CheckupPackage();
        BeanUtils.copyProperties(checkup_packageDTO, checkup_package);

//        向医疗体验套餐表插入数据
        checkup_packageMapper.insert(checkup_package);

//        获取生成的医疗体验套餐id
        Long id = checkup_package.getId();

//        设置id
        List<CheckupPackageDoctor> checkup_packageDoctores = checkup_packageDTO.getCheckupPackageDoctors();
        checkup_packageDoctores.forEach(checkup_packageDoctor -> checkup_packageDoctor.setCheckupPackageId(id));

//        保存医疗体验套餐和医生的关联关系
        checkup_packageDoctorMapper.insertBatch(checkup_packageDoctores);
    }

    /**
     * 分页查询
     * @param checkup_packagePageQueryDTO
     * @return
     */
    @Override
    public PageResult pageQuery(CheckupPackagePageQueryDTO checkup_packagePageQueryDTO) {
        PageHelper.startPage(checkup_packagePageQueryDTO.getPage(), checkup_packagePageQueryDTO.getPageSize());

        Page<CheckupPackageVO> page = checkup_packageMapper.pageQuery(checkup_packagePageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 批量删除医疗体验套餐
     * @param ids
     */
    @Override
    public void deleteBatch(List<Long> ids) {
//        在岗中的医疗体验套餐不能删除
        ids.forEach(id->{
            CheckupPackage checkup_package = checkup_packageMapper.getById(id);
            if (StatusConstant.ENABLE == checkup_package.getStatus()) {
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });

        ids.forEach(id->{
//            删除医疗体验套餐表中的数据
            checkup_packageMapper.deleteById(id);

//            删除医疗体验套餐餐品关系表中的数据
            checkup_packageDoctorMapper.deleteBySetmaleId(id);
        });

    }

    /**
     * 根据id查询医疗体验套餐
     * @param id
     * @return
     */
    @Override
    public CheckupPackageVO getByIdWithDoctor(Long id) {
        CheckupPackageVO checkup_packageVO = new CheckupPackageVO();

//        查询医疗体验套餐基本信息
        CheckupPackage checkup_package = checkup_packageMapper.getById(id);
        BeanUtils.copyProperties(checkup_package, checkup_packageVO);

//        根据医疗体验套餐信息查询医生信息
        List<CheckupPackageDoctor> checkup_packageDoctorList = checkup_packageDoctorMapper.getByCheckupPackageId(id);
        checkup_packageVO.setCheckup_packageDoctors(checkup_packageDoctorList);

        return checkup_packageVO;
    }

    /**
     * 修改医疗体验套餐
     * @param checkup_packageDTO
     */
    @Override
    public void update(CheckupPackageDTO checkup_packageDTO) {
        CheckupPackage checkup_package = new CheckupPackage();
        BeanUtils.copyProperties(checkup_packageDTO, checkup_package);

//        1.修改医疗体验套餐表，执行update
        checkup_packageMapper.update(checkup_package);

//        医疗体验套餐id
        Long id = checkup_packageDTO.getId();

//        2.删除医疗体验套餐和医生的关联关系
        checkup_packageDoctorMapper.deleteBySetmaleId(checkup_packageDTO.getId());

        List<CheckupPackageDoctor> checkup_packageDoctores = checkup_packageDTO.getCheckupPackageDoctors();
        checkup_packageDoctores.forEach(checkup_packageDoctor -> checkup_packageDoctor.setCheckupPackageId(id));

//        3.重新插入医疗体验套餐和医生的关联关系
        checkup_packageDoctorMapper.insertBatch(checkup_packageDoctores);
    }

    /**
     * 医疗体验套餐在岗离岗
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
//        在岗医疗体验套餐时，判断医疗体验套餐内是否有离岗医生，有离岗医生提示"医疗体验套餐内包含未启售医生，无法启售"
        if (status == StatusConstant.ENABLE) {
            List<Doctor> doctorList = doctorMapper.getByCheckupPackageId(id);
            if (doctorList != null && doctorList.size() > 0) {
                doctorList.forEach(doctor -> {
                    if (StatusConstant.DISABLE == doctor.getStatus()) { // 有离岗商品
                        throw new CheckupPackageEnableFailedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                    }
                });
            }
        }

        CheckupPackage checkup_package = CheckupPackage.builder()
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
    public List<CheckupPackage> list(CheckupPackage checkup_package) {
        return checkup_packageMapper.list(checkup_package);
    }

    /**
     * 根据id查询医生选项
     * @param id
     * @return
     */
    @Override
    public List<DoctorItemVO> getDoctorItemById(Long id) {
        return checkup_packageMapper.getDoctorItemByCheckupPackageId(id);
    }
}
