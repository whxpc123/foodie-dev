package com.imooc.pojo.bo;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class AddressBO {

    private String userId;

    private String addressId;
    @NotNull(message = "收货人不能为空")
    @Length(max = 12,message = "收货人姓名不能大于12")
    private String receiver;
    @NotNull(message = "手机号不能为空")
    @Length(min = 11,max = 11,message = "手机长度不正确")
    @Pattern(regexp = "^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[013678])|(18[0,5-9]))\\d{8}$",message = "手机格式不正确")
    private String mobile;

    private String province;

    private String city;

    private String district;

    private String detail;
}
