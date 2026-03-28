package com.leo.medical.service;

import com.leo.medical.vo.BusinessDataVO;
import com.leo.medical.vo.DoctorOverViewVO;
import com.leo.medical.vo.OrderOverViewVO;
import com.leo.medical.vo.CheckupPackageOverViewVO;

import java.time.LocalDateTime;

public interface WorkspaceService {
    /**
     * 根据时间段统计营业数据
     * @param begin
     * @param end
     * @return
     */
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    /**
     * 查询订单管理数据
     * @return
     */
    OrderOverViewVO getOrderOverView();

    /**
     * 查询医生总览
     * @return
     */
    DoctorOverViewVO getDoctorOverView();

    /**
     * 查询医疗体验套餐总览
     * @return
     */
    CheckupPackageOverViewVO getCheckupPackageOverView();
}
