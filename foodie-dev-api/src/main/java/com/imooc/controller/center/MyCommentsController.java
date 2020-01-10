package com.imooc.controller.center;

import com.imooc.enums.YesOrNo;
import com.imooc.pojo.OrderItems;
import com.imooc.pojo.Orders;
import com.imooc.pojo.bo.center.OrderItemsCommentBO;
import com.imooc.service.center.IMyCommentService;
import com.imooc.service.center.IMyOrderService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
import io.swagger.annotations.Api;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "我的评论",tags = {"我的评论相关接口"})
@RestController
@RequestMapping("mycomments")
public class MyCommentsController {

    @Autowired
    private IMyCommentService myCommentService;
    @Autowired
    private IMyOrderService myOrderService;

    @PostMapping("pending")
    public IMOOCJSONResult confirmReceive( @RequestParam("userId")String userId,@RequestParam("orderId")String orderId){

        IMOOCJSONResult checkResult = checkUserOrder(userId, orderId);
        if(!checkResult.isOK()){
            return checkResult;
        }
        Orders myOrder = (Orders) checkResult.getData();
        if(myOrder.getIsComment() == YesOrNo.Yes.type){
            return IMOOCJSONResult.errorMsg("该笔订单已评价");
        }
        List<OrderItems> orderItems = myCommentService.queryPendingComment(orderId);
        return IMOOCJSONResult.ok(orderItems);
    }

    @PostMapping("saveList")
    public IMOOCJSONResult saveList(@RequestParam("userId")String userId, @RequestParam("orderId")String orderId, @RequestBody List<OrderItemsCommentBO> commentList){

        IMOOCJSONResult checkResult = checkUserOrder(userId, orderId);
        if(!checkResult.isOK()){
            return checkResult;
        }
        if(CollectionUtils.isEmpty(commentList)){
            return IMOOCJSONResult.errorMsg("评论内容不能为空");
        }
        myCommentService.saveComments(userId,orderId,commentList);
        return IMOOCJSONResult.ok();
    }

    private IMOOCJSONResult checkUserOrder(String userId,String orderId){
        Orders orders = myOrderService.queryMyOrder(userId, orderId);
        if(orders==null){
            return IMOOCJSONResult.errorMsg("订单不存在");
        }
        return IMOOCJSONResult.ok(orders);
    }

    @PostMapping("query")
    public IMOOCJSONResult query(@RequestParam("userId")String userId,@RequestParam("page")Integer page,@RequestParam("pageSize")Integer pageSize){

        if(userId==null) return IMOOCJSONResult.errorMsg(null);

        page = page==null?1:page;
        pageSize =pageSize ==null?10:pageSize;

        PagedGridResult result = myCommentService.queryMyComments(userId, page, pageSize);

        return IMOOCJSONResult.ok(result);
    }
}
