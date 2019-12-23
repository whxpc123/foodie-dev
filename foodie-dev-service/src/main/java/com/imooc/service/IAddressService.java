package com.imooc.service;

import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;

import java.util.List;

public interface IAddressService {

    List<UserAddress> queryAll(String userId);

    void addNewUserAddress(AddressBO addressBO);

    void updateUserAddress(AddressBO addressBO);

    void deleteUserAddress(String userId,String addressId);

    void setDefaultAddress(String userId,String addressId);

    UserAddress queryByUserIdAndAddressId(String userId,String addressId);
}
