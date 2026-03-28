package com.leo.medical.controller.user;

import com.leo.medical.constant.StatusConstant;
import com.leo.medical.entity.Setmeal;
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

@RestController("userSetmealController")
@RequestMapping("/user/checkup_package")
@Api(tags = "C端-套餐浏览接口")
public class SetmealController {
    @Autowired
    private CheckupPackageService checkupPackageService;

    /**
     * 条件查询
     *
     * @param departmentId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据科室id查询套餐")
    @Cacheable(cacheNames = "checkup_packageCache",key = "#departmentId")
    public Result<List<Setmeal>> list(Long departmentId) {
        Setmeal checkup_package = new Setmeal();
        checkup_package.setDepartmentId(departmentId);
        checkup_package.setStatus(StatusConstant.ENABLE);

        List<Setmeal> list = checkupPackageService.list(checkup_package);
        return Result.success(list);
    }

    /**
     * 根据套餐id查询包含的医生列表
     *
     * @param id
     * @return
     */
    @GetMapping("/doctor/{id}")
    @ApiOperation("根据套餐id查询包含的医生列表")
    public Result<List<DoctorItemVO>> doctorList(@PathVariable("id") Long id) {
        List<DoctorItemVO> list = checkupPackageService.getDoctorItemById(id);
        return Result.success(list);
    }
}
