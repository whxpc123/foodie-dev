package com.imooc.controller;

import com.imooc.enums.OrderStatusEnum;
import com.imooc.enums.PayMethod;
import com.imooc.pojo.OrderStatus;
import com.imooc.pojo.bo.SubmitOrderBo;
import com.imooc.pojo.vo.MerchantOrdersVO;
import com.imooc.pojo.vo.OrderVO;
import com.imooc.service.IOrderService;
import com.imooc.utils.CookieUtils;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Api(value = "d订单相关",tags = {"订单相关的api接口"})
@RestController
@RequestMapping("orders")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

    @ApiOperation(value = "用户下单",notes = "用户下单",httpMethod = "POST")
    @PostMapping("create")
    public IMOOCJSONResult create(@RequestBody SubmitOrderBo submitOrderBo, HttpServletRequest request, HttpServletResponse response){

        if(!PayMethod.isSupport(submitOrderBo.getPayMethod())){
            return IMOOCJSONResult.errorMsg("不支持该支付方式");
        }

        //创建订单
        OrderVO orderVO = orderService.create(submitOrderBo);

        MerchantOrdersVO merchantOrdersVO = orderVO.getMerchantOrdersVO();
        merchantOrdersVO.setAmount(1); //设置金额为1分钱

        HttpHeaders httpHeaders =new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("imoocUserId", "6473892-381021753");
        httpHeaders.add("password","19ij-0k21-01km-10km");
        HttpEntity<MerchantOrdersVO> httpEntity =new HttpEntity<>(merchantOrdersVO,httpHeaders);

        ResponseEntity<IMOOCJSONResult> responseEntity = restTemplate.postForEntity("http://payment.t.mukewang.com/foodie-payment/payment/createMerchantOrder", httpEntity, IMOOCJSONResult.class);
        IMOOCJSONResult body = responseEntity.getBody();
        if(body.getStatus()!=200){
            return IMOOCJSONResult.errorMsg("订单创建失败");
        }

        // CookieUtils.setCookie(request,response,"shopcart","");

        return IMOOCJSONResult.ok(orderVO.getOrderId());

    }

    @PostMapping("notifyMerchantOrderPaid")
    public Integer notifyMerchantOrderPaid(String merchantOrderId){
        orderService.updateOrderStatus(merchantOrderId, OrderStatusEnum.WAIT_DELIVER.type);
        return HttpStatus.OK.value();
    }

    @PostMapping("getPaidOrderInfo")
    public IMOOCJSONResult getPaidOrderInfo(String orderId){
        OrderStatus orderStatus = orderService.queryOrderStatusById(orderId);

        return IMOOCJSONResult.ok(orderStatus);
    }
}
