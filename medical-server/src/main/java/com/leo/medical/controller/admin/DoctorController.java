package com.leo.medical.controller.admin;


import com.leo.medical.dto.DoctorDTO;
import com.leo.medical.dto.DoctorPageQueryDTO;
import com.leo.medical.entity.Doctor;
import com.leo.medical.result.PageResult;
import com.leo.medical.result.Result;
import com.leo.medical.service.DoctorService;
import com.leo.medical.vo.DoctorVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

/**
 * 医生管理
 */
@RestController
@RequestMapping("/admin/doctor")
@Api(tags = "医生相关接口")
@Slf4j
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 新增医生
     * @param doctorDTO
     * @return
     */
    @PostMapping()
    @ApiOperation("新增医生")
    public Result save(@RequestBody DoctorDTO doctorDTO) {
        log.info("新增医生：{}", doctorDTO);
        doctorService.saveWithFlavor(doctorDTO);

//        清理缓存数据
        String key = "doctor_" + doctorDTO.getDepartmentId();
        clearCache(key);

        return Result.success();
    }

    /**
     * 医生分页查询
     * @param doctorPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("医生分页查询")
    public Result<PageResult> page(DoctorPageQueryDTO doctorPageQueryDTO) {
        log.info("医生分页查询:{}", doctorPageQueryDTO);
        PageResult pageResult = doctorService.pageQuery(doctorPageQueryDTO);//后绪步骤定义
        return Result.success(pageResult);
    }

    /**
     * 医生批量删除
     *
     * @param ids
     * @return
     */
    @DeleteMapping
    @ApiOperation("医生批量删除")
    public Result delete(@RequestParam List<Long> ids) {
        log.info("医生批量删除：{}", ids);
        doctorService.deleteBatch(ids);

//        将所有的医生缓存数据清理掉，所有以doctor_开头的key
        clearCache("doctor_*");
        return Result.success();
    }

    /**
     * 根据id查询医生
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询医生")
    public Result<DoctorVO> getById(@PathVariable Long id) {
        log.info("根据id查询医生：{}", id);
        DoctorVO doctorVO = doctorService.getByIdWithFlavor(id);
        return Result.success(doctorVO);
    }

    /**
     * 修改医生
     *
     * @param doctorDTO
     * @return
     */
    @PutMapping
    @ApiOperation("修改医生")
    public Result update(@RequestBody DoctorDTO doctorDTO) {
        log.info("修改医生：{}", doctorDTO);
        doctorService.updateWithFlavor(doctorDTO);

        //将所有的医生缓存数据清理掉，所有以doctor_开头的key
        clearCache("doctor_*");

        return Result.success();
    }

    /**
     * 根据科室id查询医生
     * @param departmentId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据科室id查询医生")
    public Result<List<Doctor>> list(Long departmentId) {
        List<Doctor> list = doctorService.list(departmentId);
        return Result.success(list);
    }

    /**
     * 医生在岗离岗
     *
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("医生在岗离岗")
    public Result<String> startOrStop(@PathVariable Integer status, Long id) {
        doctorService.startOrStop(status, id);

        //将所有的医生缓存数据清理掉，所有以doctor_开头的key
        clearCache("doctor_*");

        return Result.success();
    }

    /**
     * 清理缓存数据
     * @param pattern
     */
    private void clearCache(String pattern) {
        Set keys = redisTemplate.keys(pattern);
        redisTemplate.delete(keys);
    }

}
