package com.imooc.controller.center;

import com.imooc.pojo.Orders;
import com.imooc.pojo.vo.OrderStatusCountsVO;
import com.imooc.service.center.IMyOrderService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Api(value = "  我的订单",tags = {"我的订单"})
@RestController
@RequestMapping("myorders")
public class MyOrdersController {

    @Autowired
    private IMyOrderService myOrderService;

    @PostMapping("query")
    public IMOOCJSONResult query(@RequestParam("userId")String userId, @RequestParam("orderStatus")Integer orderStatus,
                                 @RequestParam("page")Integer page, @RequestParam("pageSize")Integer pageSize){

        page = page==null?1:page;
        pageSize = pageSize==null?10:pageSize;

        PagedGridResult pagedGridResult = myOrderService.queryMyOrders(userId, orderStatus, page, pageSize);

        return IMOOCJSONResult.ok(pagedGridResult);
    }

    @GetMapping("deliver")
    public IMOOCJSONResult deliver(@RequestParam("orderId")String orderId){

        myOrderService.updateOrderStatus(orderId);

        return IMOOCJSONResult.ok();
    }

    @PostMapping("confirmReceive")
    public IMOOCJSONResult confirmReceive(@RequestParam("orderId")String orderId,@RequestParam("userId")String userId){

        IMOOCJSONResult checkResult = checkUserOrder(userId, orderId);
        if(!checkResult.isOK()){
            return checkResult;
        }

        boolean res = myOrderService.updateReceiveOrderStatus(orderId);
        if(!res){
            return IMOOCJSONResult.errorMsg("订单确认收货失败");
        }

        return IMOOCJSONResult.ok();
    }

    @PostMapping("delete")
    public IMOOCJSONResult delete(@RequestParam("orderId")String orderId,@RequestParam("userId")String userId){

        IMOOCJSONResult checkResult = checkUserOrder(userId, orderId);
        if(!checkResult.isOK()){
            return checkResult;
        }
        boolean res = myOrderService.deleteOrder(userId, orderId);
        if(!res){
            return IMOOCJSONResult.errorMsg("订单删除失败");
        }

        return IMOOCJSONResult.ok();
    }


    private IMOOCJSONResult checkUserOrder(String userId,String orderId){
        Orders orders = myOrderService.queryMyOrder(userId, orderId);
        if(orders==null){
            return IMOOCJSONResult.errorMsg("订单不存在");
        }
        return IMOOCJSONResult.ok();
    }

    @PostMapping("statusCounts")
    public IMOOCJSONResult statusCounts(@RequestParam("userId")String userId){
        if(userId==null){
         return IMOOCJSONResult.errorMsg(null);
        }
        OrderStatusCountsVO myOrderStatusCounts = myOrderService.getMyOrderStatusCounts(userId);
        return IMOOCJSONResult.ok(myOrderStatusCounts);
    }

    @PostMapping("trend")
    public IMOOCJSONResult trend(@RequestParam("userId")String userId,@RequestParam("page")Integer page, @RequestParam("pageSize")Integer pageSize){
        if(userId==null){
            return IMOOCJSONResult.errorMsg(null);
        }
        page = page==null?1:page;
        pageSize = pageSize==null?10:pageSize;
        PagedGridResult myOrderTrend = myOrderService.getMyOrderTrend(userId, page, pageSize);
        return IMOOCJSONResult.ok(myOrderTrend);
    }


}
