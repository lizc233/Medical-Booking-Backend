package com.leo.medical.dto;

import com.leo.medical.entity.CheckupPackageDoctor;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class CheckupPackageDTO implements Serializable {

    private Long id;

    //科室id
    private Long departmentId;

    //医疗体验套餐名称
    private String name;

    //医疗体验套餐价格
    private BigDecimal price;

    //状态 0:停用 1:启用
    private Integer status;

    //描述信息
    private String description;

    //图片
    private String image;

    //医疗体验套餐医生关系
    private List<CheckupPackageDoctor> checkupPackageDoctors = new ArrayList<>();

}
