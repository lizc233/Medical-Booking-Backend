package com.leo.medical.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class SetmealPageQueryDTO implements Serializable {

    private int page;

    private int pageSize;

    private String name;

    //科室id
    private Integer departmentId;

    //状态 0表示禁用 1表示启用
    private Integer status;

}
