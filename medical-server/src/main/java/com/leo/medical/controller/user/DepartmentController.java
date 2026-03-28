package com.leo.medical.controller.user;

import com.leo.medical.entity.Department;
import com.leo.medical.result.Result;
import com.leo.medical.service.DepartmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController("userDepartmentController")
@RequestMapping("/user/department")
@Api(tags = "C端-科室接口")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * 查询科室
     * @param type
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查询科室")
    public Result<List<Department>> list(Integer type) {
        List<Department> list = departmentService.list(type);
        return Result.success(list);
    }
}
