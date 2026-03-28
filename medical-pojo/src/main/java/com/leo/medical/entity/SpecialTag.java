package com.leo.medical.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 医生特长标签
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpecialTag implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    //医生id
    private Long doctorId;

    //特长标签名称
    private String name;

    //特长标签数据list
    private String value;

}
