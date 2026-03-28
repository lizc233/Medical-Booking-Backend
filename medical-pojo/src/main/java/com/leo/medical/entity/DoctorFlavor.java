package com.leo.medical.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 医生口味
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DoctorFlavor implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    //医生id
    private Long doctorId;

    //口味名称
    private String name;

    //口味数据list
    private String value;

}
