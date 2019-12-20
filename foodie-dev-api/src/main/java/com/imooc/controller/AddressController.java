package com.imooc.controller;

import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;
import com.imooc.service.IAddressService;
import com.imooc.utils.BeanValidator;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "地址相关",tags = {"地址相关的相关接口"})
@RestController
@RequestMapping("address")
public class AddressController {

    @Autowired
    private IAddressService addressService;

    @ApiOperation(value = "根据用户id查询用户的地址",notes = "根据用户id查询用户的地址",httpMethod = "POST")
    @PostMapping("list")
    public IMOOCJSONResult list(@RequestParam String userId){
        if(StringUtils.isBlank(userId)){
            IMOOCJSONResult.errorMsg("用户id不能为空");
        }
        List<UserAddress> userAddressList = addressService.queryAll(userId);
        return IMOOCJSONResult.ok(userAddressList);
    }

    @ApiOperation(value = "用户新增地址",notes = "用户新增地址",httpMethod = "POST")
    @PostMapping("add")
    public IMOOCJSONResult add(@RequestBody AddressBO addressBO){
        BeanValidator.check(addressBO);
        addressService.addNewUserAddress(addressBO);
        return IMOOCJSONResult.ok();
    }
    @ApiOperation(value = "用户更新地址",notes = "用户更新地址",httpMethod = "POST")
    @PostMapping("update")
    public IMOOCJSONResult update(@RequestBody AddressBO addressBO){
        if(StringUtils.isBlank(addressBO.getAddressId())){
            return IMOOCJSONResult.errorMsg("地址id不能为空");
        }
        addressService.updateUserAddress(addressBO);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户地址删除",notes = "用户地址删除",httpMethod = "POST")
    @PostMapping("delete")
    public IMOOCJSONResult delete(@RequestParam String userId,@RequestParam String addressId){
       if(StringUtils.isBlank(userId)||StringUtils.isBlank(addressId)){
           return IMOOCJSONResult.errorMsg("参数不能为空");
        }
        addressService.deleteUserAddress(userId,addressId);
        return IMOOCJSONResult.ok();
    }

    @ApiOperation(value = "用户设置默认地址",notes = "用户设置默认地址",httpMethod = "POST")
    @PostMapping("setDefault")
    public IMOOCJSONResult setDefault(@RequestParam String userId,@RequestParam String addressId){
        if(StringUtils.isBlank(userId)||StringUtils.isBlank(addressId)){
            return IMOOCJSONResult.errorMsg("参数不能为空");
        }
        addressService.setDefaultAddress(userId,addressId);
        return IMOOCJSONResult.ok();
    }

}
