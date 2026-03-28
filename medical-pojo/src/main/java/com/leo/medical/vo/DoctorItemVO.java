package com.leo.medical.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorItemVO implements Serializable {

    //医生名称
    private String name;

    //份数
    private Integer copies;

    //医生图片
    private String image;

    //医生描述
    private String description;
}
