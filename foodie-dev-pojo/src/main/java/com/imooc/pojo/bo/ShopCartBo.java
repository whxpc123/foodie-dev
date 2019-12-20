package com.imooc.pojo.bo;

import lombok.Data;

@Data
public class ShopCartBo {

    private String itemId;

    private String itemImgUrl;

    private String itemName;

    private String specId;

    private  Integer buyCount;

    private String priceDiscount;

    private String priceNormal;
}
