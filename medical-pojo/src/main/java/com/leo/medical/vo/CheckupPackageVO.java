package com.leo.medical.vo;

import com.leo.medical.entity.CheckupPackageDoctor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckupPackageVO implements Serializable {

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

    //更新时间
    private LocalDateTime updateTime;

    //科室名称
    private String departmentName;

    //医疗体验套餐和医生的关联关系
    private List<CheckupPackageDoctor> checkup_packageDoctors = new ArrayList<>();
}
