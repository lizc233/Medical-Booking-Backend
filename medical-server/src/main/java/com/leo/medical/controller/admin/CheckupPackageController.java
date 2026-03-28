package com.leo.medical.controller.admin;

import com.leo.medical.dto.CheckupPackageDTO;
import com.leo.medical.dto.CheckupPackagePageQueryDTO;
import com.leo.medical.result.PageResult;
import com.leo.medical.result.Result;
import com.leo.medical.service.CheckupPackageService;
import com.leo.medical.vo.CheckupPackageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 医疗体验套餐管理
 */
@RestController
@RequestMapping("/admin/checkup_package")
@Api(tags = "医疗体验套餐相关接口")
@Slf4j
public class CheckupPackageController {

    @Autowired
    private CheckupPackageService checkupPackageService;

    /**
     * 新增医疗体验套餐
     * @param checkup_packageDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增医疗体验套餐")
    @CacheEvict(cacheNames = "checkup_packageCache",key = "#checkup_packageDTO.departmentId")
    public Result save(@RequestBody CheckupPackageDTO checkup_packageDTO) {
        checkupPackageService.saveWithDoctor(checkup_packageDTO);
        return Result.success();
    }

    /**
     * 分页查询
     * @param checkup_packagePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("分页查询")
    public Result<PageResult> page(CheckupPackagePageQueryDTO checkup_packagePageQueryDTO) {
        PageResult pageResult = checkupPackageService.pageQuery(checkup_packagePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除医疗体验套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("批量删除医疗体验套餐")
    @CacheEvict(cacheNames = "checkup_packageCache",allEntries = true)
    public Result delete(@RequestParam List<Long> ids) {
        checkupPackageService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据id查询医疗体验套餐
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询医疗体验套餐")
    public Result<CheckupPackageVO> getById(@PathVariable Long id) {
        CheckupPackageVO checkup_packageVO = checkupPackageService.getByIdWithDoctor(id);
        return Result.success(checkup_packageVO);
    }

    /**
     * 修改医疗体验套餐
     * @param checkup_packageDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改医疗体验套餐")
    @CacheEvict(cacheNames = "checkup_packageCache",allEntries = true)
    public Result update(@RequestBody CheckupPackageDTO checkup_packageDTO) {
        checkupPackageService.update(checkup_packageDTO);
        return Result.success();
    }

    /**
     * 医疗体验套餐在岗离岗
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("医疗体验套餐在岗离岗")
    @CacheEvict(cacheNames = "checkup_packageCache",allEntries = true)
    public Result startOrStop(@PathVariable Integer status, Long id) {
        checkupPackageService.startOrStop(status, id);
        return Result.success();
    }
}
