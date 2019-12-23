package com.imooc.service.impl;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.YesOrNo;
import com.imooc.mapper.OrderItemsMapper;
import com.imooc.mapper.OrderStatusMapper;
import com.imooc.mapper.OrdersMapper;
import com.imooc.pojo.*;
import com.imooc.pojo.bo.SubmitOrderBo;
import com.imooc.pojo.vo.MerchantOrdersVO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.IAddressService;
import com.imooc.service.IItemService;
import com.imooc.service.IOrderService;
import com.imooc.utils.DateUtil;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private Sid sid;
    @Autowired
    private IAddressService addressService;
    @Autowired
    private IItemService itemService;
    @Autowired
    private OrderItemsMapper orderItemsMapper;
    @Autowired
    private OrdersMapper ordersMapper;
    @Autowired
    private OrderStatusMapper orderStatusMapper;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public OrderVO create(SubmitOrderBo submitOrderBo) {

        String orderId = sid.nextShort();
        Integer postAmount= 0;
        UserAddress address = addressService.queryByUserIdAndAddressId(submitOrderBo.getUserId(), submitOrderBo.getAddressId());
        Orders orders = Orders.builder()
                .id(orderId)
                .receiverName(address.getReceiver())
                .receiverMobile(address.getMobile())
                .userId(submitOrderBo.getUserId())
                .receiverAddress(address.getProvince() + " " + address.getCity() + " " + address.getDistrict() + " " + address.getDetail())
                .payMethod(submitOrderBo.getPayMethod())
                .leftMsg(submitOrderBo.getLeftMsg())
                .isComment(YesOrNo.NO.type)
                .isDelete (YesOrNo.NO.type)
                .postAmount(postAmount)
                .createdTime(new Date())
                .updatedTime(new Date())
                .build();
        String[] specIdsArr = submitOrderBo.getItemSpecIds().split(",");
        Integer totalAmount = 0;
        Integer realPayAmount = 0;
        for (String specId : specIdsArr) {
            //TODO 后期购买数量从购物车中拿
            Integer buyCounts = 1;
            ItemsSpec itemsSpec = itemService.queryItemSpecBySpeId(specId);
            totalAmount+=itemsSpec.getPriceNormal()*buyCounts;
            realPayAmount+=itemsSpec.getPriceDiscount()*buyCounts;

            Items items = itemService.queryItemById(itemsSpec.getItemId());
            String imgUrl = itemService.queryMainImgById(itemsSpec.getItemId());

            String subId = sid.nextShort();
            OrderItems subOrder = OrderItems.builder()
                    .id(subId)
                    .orderId(orderId)
                    .itemId(items.getId())
                    .itemName(items.getItemName())
                    .itemImg(imgUrl)
                    .buyCounts(buyCounts)
                    .itemSpecId(specId)
                    .itemSpecName(itemsSpec.getName())
                    .price(itemsSpec.getPriceDiscount())
                    .build();
            orderItemsMapper.insert(subOrder);
            //扣除库存
            itemService.decreaseItemSpecStock(specId,buyCounts);
        }
        orders.setTotalAmount(totalAmount);
        orders.setRealPayAmount(realPayAmount);
        ordersMapper.insert(orders);

        OrderStatus orderStatus = OrderStatus.builder()
                .orderId(orderId)
                .orderStatus(OrderStatusEnum.WAIT_PAY.type)
                .createdTime(new Date())
                .build();
        orderStatusMapper.insert(orderStatus);


        //创建商户订单，用于传给支付中心
        MerchantOrdersVO merchantOrdersVO = MerchantOrdersVO.builder().merchantOrderId(orderId)
                .merchantUserId(submitOrderBo.getUserId())
                .amount(realPayAmount + postAmount)
                .payMethod(submitOrderBo.getPayMethod())
                .returnUrl("http://adgipd.natappfree.cc/orders/notifyMerchantOrderPaid")
                .build();

        OrderVO orderVO = new OrderVO();
        orderVO.setOrderId(orderId);
        orderVO.setMerchantOrdersVO(merchantOrdersVO);

        return orderVO;
    }

    @Override
    public void updateOrderStatus(String orderId, Integer orderStatus) {
        OrderStatus order = OrderStatus.builder().orderId(orderId)
                .orderStatus(orderStatus)
                .payTime(new Date()).build();

        orderStatusMapper.updateByPrimaryKeySelective(order);


    }

    public OrderStatus queryOrderStatusById(String orderId){
        return orderStatusMapper.selectByPrimaryKey(orderId);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void closeOrder() {

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderStatus(OrderStatusEnum.WAIT_PAY.type);
        List<OrderStatus> orderStatusList = orderStatusMapper.select(orderStatus);

        for (OrderStatus status : orderStatusList) {
            Date createdTime = status.getCreatedTime();
            int day = DateUtil.daysBetween(createdTime, new Date());
            if(day>=1){
                doCloseOrder(status.getOrderId());
            }
        }
    }
    @Transactional(propagation = Propagation.REQUIRED)
    void doCloseOrder(String  orderId){

        OrderStatus orderStatus = new OrderStatus();
        orderStatus.setOrderId(orderId);
        orderStatus.setOrderStatus(OrderStatusEnum.CLOSE.type);
        orderStatus.setCloseTime(new Date());

        orderStatusMapper.updateByPrimaryKeySelective(orderStatus);
    }
}
