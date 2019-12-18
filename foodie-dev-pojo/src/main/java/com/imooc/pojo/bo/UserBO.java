package com.imooc.pojo.bo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "用户对象BO",description = "用于注册用户创建使用")
public class UserBO {

    @ApiModelProperty(value = "用户名",name = "username",example = "test",required = true)
    @NotNull(message = "用户名不能为空")
    private String username;

    @Length(min = 6,message = "密码长度不能小于6位")
    @NotNull(message = "密码不能为空")
    private String password;

    @Length(min = 6,message = "密码长度不能小于6位")
    @NotNull(message = "确认密码不能为空")
    private String confirmPassword;
}
