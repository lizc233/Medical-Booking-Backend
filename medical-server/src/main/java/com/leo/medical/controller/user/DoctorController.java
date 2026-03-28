package com.leo.medical.controller.user;

import com.leo.medical.constant.StatusConstant;
import com.leo.medical.entity.Doctor;
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

@RestController("userDoctorController")
@RequestMapping("/user/doctor")
@Slf4j
@Api(tags = "C端-医生浏览接口")
public class DoctorController {
    @Autowired
    private DoctorService doctorService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据科室id查询医生
     *
     * @param departmentId
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("根据科室id查询医生")
    public Result<List<DoctorVO>> list(Long departmentId) {
//        构造redis中的key，规则：doctor_科室Id
        String key = "doctor_" + departmentId;

//        查询redis中是否存在医生数据
        List<DoctorVO> list = (List<DoctorVO>) redisTemplate.opsForValue().get(key);
        if (list != null && list.size() > 0) {
//            如果存在，直接返回，无需查询数据库
            return Result.success(list);
        }


//        如果不存在，查询数据库，将查询到的数据放入redis中
        Doctor doctor = new Doctor();
        doctor.setDepartmentId(departmentId);
        doctor.setStatus(StatusConstant.ENABLE);//查询在岗中的医生

        list = doctorService.listWithFlavor(doctor);

//        放入redis
        redisTemplate.opsForValue().set(key, list);
        return Result.success(list);
    }


}
