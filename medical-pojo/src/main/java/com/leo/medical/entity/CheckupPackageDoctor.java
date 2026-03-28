package com.leo.medical.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 医疗体验套餐医生关系
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckupPackageDoctor implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //医疗体验套餐id
    private Long checkupPackageId;

    //医生id
    private Long doctorId;

    //医生名称 （冗余字段）
    private String name;

    //医生原价
    private BigDecimal price;

    //份数
    private Integer copies;
}
