package com.imooc.pojo.vo;

import lombok.Data;

@Data
public class OrderVO {

    private MerchantOrdersVO merchantOrdersVO;

    private String orderId;
}
