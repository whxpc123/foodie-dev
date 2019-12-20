package com.imooc.controller;


import com.imooc.pojo.bo.ShopCartBo;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("shopcart")
@Api(value = "购物车controller",tags = {"购物车相关的接口"})
public class ShopCartController {

    @ApiOperation(value = "添加商品到购物车",notes = "添加商品到购物车",httpMethod = "POST")
    @PostMapping("add")
    public IMOOCJSONResult add(@RequestParam String userId, @RequestBody ShopCartBo shopCartBo, HttpServletRequest request, HttpServletResponse response){

        if(StringUtils.isBlank(userId)){
            return IMOOCJSONResult.errorMsg("用户id不能为空");
        }
        //TODO 用户登录将用户信息存储到redis中·
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "从购物车中删除商品",notes = "从购物车中删除商品",httpMethod = "POST")
    @PostMapping("del")
    public IMOOCJSONResult del(@RequestParam String userId, @RequestBody String itemSpecId){

        if(StringUtils.isBlank(userId)||StringUtils.isBlank(itemSpecId)){
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }
        //TODO 删除用户购物车中的商品
        return IMOOCJSONResult.ok();
    }


}
