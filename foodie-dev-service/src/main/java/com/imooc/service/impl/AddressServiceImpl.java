package com.imooc.service.impl;

import com.imooc.enums.YesOrNo;
import com.imooc.mapper.UserAddressMapper;
import com.imooc.pojo.UserAddress;
import com.imooc.pojo.bo.AddressBO;
import com.imooc.service.IAddressService;
import org.apache.commons.collections.CollectionUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl  implements IAddressService {
    @Autowired
    private UserAddressMapper userAddressMapper;
    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<UserAddress> queryAll(String userId) {

        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        return userAddressMapper.select(userAddress);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void addNewUserAddress(AddressBO addressBO) {

        Integer iaDefault = 0; //0代表不是默认地址
        List<UserAddress> userAddresses = queryAll(addressBO.getUserId());
        if(CollectionUtils.isEmpty(userAddresses)){
            iaDefault = 1;//结果为空，说明用户还没有地址，将新增地址设置为默认地址
        }
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO,userAddress);
        String id = sid.nextShort();
        userAddress.setId(id);
        userAddress.setIsDefault(iaDefault);
        userAddress.setCreatedTime(new Date());
        userAddress.setUpdatedTime(new Date());
        userAddressMapper.insert(userAddress);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void updateUserAddress(AddressBO addressBO) {
        String addressId = addressBO.getAddressId();
        UserAddress userAddress = new UserAddress();
        BeanUtils.copyProperties(addressBO,userAddress);
        userAddress.setId(addressId);
        userAddress.setUpdatedTime(new Date());
        userAddressMapper.updateByPrimaryKeySelective(userAddress);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void deleteUserAddress(String userId,String addressId){
        UserAddress userAddress = new UserAddress();
        userAddress.setId(addressId);
        userAddress.setUserId(userId);
        userAddressMapper.delete(userAddress);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void setDefaultAddress(String userId,String addressId){
        //查询用户的默认地址
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(userId);
        userAddress.setIsDefault(YesOrNo.Yes.type);
        List<UserAddress> userAddressList = userAddressMapper.select(userAddress);
        for (UserAddress address : userAddressList) {
            address.setIsDefault(YesOrNo.NO.type);   //将默认地址设置为不默认
            userAddressMapper.updateByPrimaryKeySelective(address);
        }
        //将传来的addressId设置为默认地址
        UserAddress defaultAddress = new UserAddress();
        defaultAddress.setIsDefault(YesOrNo.Yes.type);
        defaultAddress.setUserId(userId);
        defaultAddress.setId(addressId);
        userAddressMapper.updateByPrimaryKeySelective(defaultAddress);

    }

    @Override
    public UserAddress queryByUserIdAndAddressId(String userId, String addressId) {
        UserAddress userAddress = new UserAddress();
        userAddress.setId(userId);
        userAddress.setId(addressId);

        return userAddressMapper.selectOne(userAddress);
    }

}
