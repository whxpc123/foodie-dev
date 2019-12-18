package com.imooc.service.impl;

import com.imooc.enums.Sex;
import com.imooc.mapper.UsersMapper;
import com.imooc.pojo.Users;
import com.imooc.pojo.bo.UserBO;
import com.imooc.service.IUserService;
import com.imooc.utils.DateUtil;
import com.imooc.utils.MD5Utils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Date;


@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private Sid sid;

    private static final String USER_FACE = "http://122.152.205.72:88/group1/M00/00/05/CpoxxFw_8_qAIlFXAAAcIhVPdSg994.png";

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public boolean queryUsernameIsExist(String username) {
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();

        criteria.andEqualTo("username",username);
        Users users = usersMapper.selectOneByExample(userExample);

        return users==null ? false:true ;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public Users createUser(UserBO userBO) {

        try {
            String id = sid.nextShort();
            Users user = Users.builder().username(userBO.getUsername())
                    .id(id)
                    .password(MD5Utils.getMD5Str(userBO.getPassword()))
                    .birthday(DateUtil.stringToDate("1900-01-01"))
                    .sex(Sex.SECRET.type)
                    .face(USER_FACE)
                    .createdTime(new Date())
                    .updatedTime(new Date())
                    .build();
            usersMapper.insert(user);
            return user;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Users queryUserForLogin(String username,String password){
        Example userExample = new Example(Users.class);
        Example.Criteria criteria = userExample.createCriteria();

        criteria.andEqualTo("username",userExample);
        criteria.andEqualTo("password",password);

        Users users = usersMapper.selectOneByExample(userExample);
        return users;
    }
}
