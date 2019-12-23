package com.imooc.service;

import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.bo.SubmitOrderBo;
import com.imooc.pojo.vo.OrderVO;

public interface IOrderService {

    OrderVO create(SubmitOrderBo submitOrderBo);

    void updateOrderStatus(String orderId,Integer orderStatus);

    OrderStatus queryOrderStatusById(String orderId);

    void closeOrder();
}
