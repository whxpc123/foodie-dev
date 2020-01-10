package com.imooc.controller.center;


import com.imooc.pojo.Users;
import com.imooc.service.center.ICenterUserService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "用户中心",tags = {"用户中心相关接口"})
@RestController
@RequestMapping("center")
public class CenterController {

    @Autowired
    private ICenterUserService centerUserService;


    @ApiOperation(value = " 获取用户信息",notes = "获取用户信息",httpMethod = "GET")
    @GetMapping("userInfo")
    public IMOOCJSONResult userInfo(@RequestParam String userId) {
        Users users = centerUserService.queryUserInfo(userId);
        return IMOOCJSONResult.ok(users);

    }



}
