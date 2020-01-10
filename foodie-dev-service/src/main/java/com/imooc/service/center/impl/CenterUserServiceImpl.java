package com.imooc.service.center.impl;

import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.center.CenterUserBO;
import com.imooc.service.center.ICenterUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class CenterUserServiceImpl  implements ICenterUserService {

    @Autowired
    private UsersMapper usersMapper;

    @Override
    public Users queryUserInfo(String userId) {
        Users users = usersMapper.selectByPrimaryKey(userId);
        users.setPassword(null);
        return users;
    }

    @Override
    public Users updateUserInfo(String userId, CenterUserBO centerUserBO) {
        Users users = new Users();
        BeanUtils.copyProperties(centerUserBO,users);
        users.setId(userId);
        usersMapper.updateByPrimaryKeySelective(users);

        return queryUserInfo(userId);
    }

    @Override
    public Users updateUserFace(String userId, String userFaceUrl) {
        Users users = Users.builder().id(userId)
                .face(userFaceUrl)
                .updatedTime(new Date()).build();
        usersMapper.updateByPrimaryKeySelective(users);

        return queryUserInfo(userId);
    }
}
