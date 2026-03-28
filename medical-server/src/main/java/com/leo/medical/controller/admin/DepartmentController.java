package com.leo.medical.controller.admin;

import com.leo.medical.dto.DepartmentDTO;
import com.leo.medical.dto.DepartmentPageQueryDTO;
import com.leo.medical.entity.Department;
import com.leo.medical.result.PageResult;
import com.leo.medical.result.Result;
import com.leo.medical.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 科室管理
 */
@RestController
@RequestMapping("/admin/department")
@Api(tags = "科室相关接口")
@Slf4j
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 新增科室
     * @param departmentDTO
     * @return
     */
    @PostMapping
    @ApiOperation("新增科室")
    public Result<String> save(@RequestBody DepartmentDTO departmentDTO){
        log.info("新增科室：{}", departmentDTO);
        departmentService.save(departmentDTO);
        return Result.success();
    }

    /**
     * 科室分页查询
     * @param departmentPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("科室分页查询")
    public Result<PageResult> page(DepartmentPageQueryDTO departmentPageQueryDTO){
        log.info("分页查询：{}", departmentPageQueryDTO);
        PageResult pageResult = departmentService.pageQuery(departmentPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 删除科室
     * @param id
     * @return
     */
    @DeleteMapping
    @ApiOperation("删除科室")
    public Result<String> deleteById(Long id){
        log.info("删除科室：{}", id);
        departmentService.deleteById(id);
        return Result.success();
    }

    /**
     * 修改科室
     * @param departmentDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改科室")
    public Result<String> update(@RequestBody DepartmentDTO departmentDTO){
        departmentService.update(departmentDTO);
        return Result.success();
    }

    /**
     * 启用、禁用科室
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用科室")
    public Result<String> startOrStop(@PathVariable("status") Integer status, Long id){
        departmentService.startOrStop(status,id);
        return Result.success();
    }

    /**
     * 根据类型查询科室
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据类型查询科室")
    public Result<List<Department>> list(Integer type){
        List<Department> list = departmentService.list(type);
        return Result.success(list);
    }
}
