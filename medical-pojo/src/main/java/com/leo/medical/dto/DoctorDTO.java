package com.leo.medical.dto;

import com.leo.medical.entity.SpecialTag;
import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class DoctorDTO implements Serializable {

    private Long id;
    //医生名称
    private String name;
    //医生科室id
    private Long departmentId;
    //医生价格
    private BigDecimal price;
    //图片
    private String image;
    //描述信息
    private String description;
    //0 停售 1 起售
    private Integer status;
    //特长标签
    private List<SpecialTag> flavors = new ArrayList<>();

}
