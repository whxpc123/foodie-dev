package com.imooc.service.center;

import com.imooc.pojo.Orders;
import com.imooc.pojo.vo.OrderStatusCountsVO;
import com.imooc.utils.PagedGridResult;

public interface IMyOrderService {

    PagedGridResult queryMyOrders(String userId,Integer orderStatus,Integer page,Integer pageSize);

    void updateOrderStatus(String orderId);

    Orders queryMyOrder(String userId,String orderId);

    boolean updateReceiveOrderStatus(String orderId);

    boolean deleteOrder(String userId,String orderId);

    OrderStatusCountsVO getMyOrderStatusCounts(String userId);

    PagedGridResult getMyOrderTrend(String userId,Integer page,Integer pageSize);

}
