package com.leo.medical.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DepartmentDTO implements Serializable {

    //主键
    private Long id;

    //类型 1 医生科室 2 医疗体验套餐科室
    private Integer type;

    //科室名称
    private String name;

    //排序
    private Integer sort;

}
