package com.leo.medical.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 医疗体验套餐总览
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckupPackageOverViewVO implements Serializable {
    // 已启售数量
    private Integer sold;

    // 已离岗数量
    private Integer discontinued;
}
