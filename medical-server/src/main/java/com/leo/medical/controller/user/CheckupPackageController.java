package com.leo.medical.controller.user;

import com.leo.medical.constant.StatusConstant;
import com.leo.medical.entity.CheckupPackage;
import com.leo.medical.result.Result;
import com.leo.medical.service.CheckupPackageService;
import com.leo.medical.vo.DoctorItemVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userCheckupPackageController")
@RequestMapping("/user/checkup_package")
@Api(tags = "C端-医疗体验套餐浏览接口")
public class CheckupPackageController {
    @Autowired
    private CheckupPackageService checkupPackageService;

    /**
     * 条件查询
     *
     * @param departmentId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据科室id查询医疗体验套餐")
    @Cacheable(cacheNames = "checkup_packageCache",key = "#departmentId")
    public Result<List<CheckupPackage>> list(Long departmentId) {
        CheckupPackage checkup_package = new CheckupPackage();
        checkup_package.setDepartmentId(departmentId);
        checkup_package.setStatus(StatusConstant.ENABLE);

        List<CheckupPackage> list = checkupPackageService.list(checkup_package);
        return Result.success(list);
    }

    /**
     * 根据医疗体验套餐id查询包含的医生列表
     *
     * @param id
     * @return
     */
    @GetMapping("/doctor/{id}")
    @ApiOperation("根据医疗体验套餐id查询包含的医生列表")
    public Result<List<DoctorItemVO>> doctorList(@PathVariable("id") Long id) {
        List<DoctorItemVO> list = checkupPackageService.getDoctorItemById(id);
        return Result.success(list);
    }
}
