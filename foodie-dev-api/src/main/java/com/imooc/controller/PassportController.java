package com.imooc.controller;


import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.IUserService;
import com.imooc.utils.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@RestController
@RequestMapping("passport")
@Api(value = "注册登录",tags = {"用于注册登录的接口"})
public class PassportController {

    @Autowired
    private IUserService userService;

    @ApiOperation(value = "用户名是否存在",notes = "用户名是否存在",httpMethod = "GET")
    @GetMapping("usernameIsExist")
    public IMOOCJSONResult usernameIsExist(@RequestParam String username){
        if(StringUtils.isBlank(username)){
            return IMOOCJSONResult.errorMsg("用户名不能为空");
        }
        boolean b = userService.queryUsernameIsExist(username);
        if(b){
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户注册",notes = "注册用户",httpMethod = "POST")
    @PostMapping("regist")
    public IMOOCJSONResult register(@RequestBody UserBO userBO){
        BeanValidator.check(userBO);
        if(!userBO.getPassword().equals(userBO.getConfirmPassword())){
            return IMOOCJSONResult.errorMsg("两次密码输入不一致");
        }
        boolean b = userService.queryUsernameIsExist(userBO.getUsername());
        if(b){
            return IMOOCJSONResult.errorMsg("用户名已存在");
        }
        Users user = userService.createUser(userBO);
        return IMOOCJSONResult.ok(user);
    }

    @ApiOperation(value = "用户登录",notes = "用户登录",httpMethod = "POST")
    @PostMapping("login")
    public IMOOCJSONResult login(@RequestBody UserBO userBO, HttpServletRequest request, HttpServletResponse response) throws Exception{
        BeanValidator.check(userBO);
        Users users = userService.queryUserForLogin(userBO.getUsername(), MD5Utils.getMD5Str(userBO.getPassword()));
        if(Objects.isNull(users)){
            return IMOOCJSONResult.errorMsg("用户名或密码不正确");
        }
        CookieUtils.setCookie(request,response,"user", JsonUtils.objectToJson(users),true);
        return IMOOCJSONResult.ok(users);
    }

    @ApiOperation(value = "用户退出登录",notes = "用户退出登录",httpMethod = "POST")
    @PostMapping("logout")
    public IMOOCJSONResult logout(@RequestParam String userId,HttpServletRequest request,HttpServletResponse response){

        CookieUtils.deleteCookie(request,response,"user");

        //TODO 用户退出登录，需要清空用户购物车信息
        //TODO 分布式会话，需要清空用户数据

        return IMOOCJSONResult.ok();
    }


}
