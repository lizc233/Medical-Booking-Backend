package com.leo.medical.vo;

import com.leo.medical.entity.OrderDetail;
import com.leo.medical.entity.Orders;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderVO extends Orders implements Serializable {

    //订单医生信息
    private String orderDoctores;

    //订单详情
    private List<OrderDetail> orderDetailList;

}
