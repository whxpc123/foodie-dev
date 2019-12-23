package com.imooc.config;

import com.imooc.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderTask {

    @Autowired
    private IOrderService orderService;

    @Scheduled(cron = "0 0 0/1 * * ? ")
    public void autoCloseOrder(){

        orderService.closeOrder(); //超过一天自动关闭订单

    }
}
