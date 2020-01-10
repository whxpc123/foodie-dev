package com.imooc.service.center;

import com.imooc.pojo.OrderItems;
import com.imooc.pojo.bo.center.OrderItemsCommentBO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

public interface IMyCommentService {

    List<OrderItems> queryPendingComment(String orderId);

    void saveComments(String userId, String orderId, List<OrderItemsCommentBO> commentList);

    PagedGridResult queryMyComments(String userId,Integer page,Integer pageSize);
}
