package com.leo.medical.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class DepartmentPageQueryDTO implements Serializable {

    //页码
    private int page;

    //每页记录数
    private int pageSize;

    //科室名称
    private String name;

    //科室类型 1医生科室  2套餐科室
    private Integer type;

}
