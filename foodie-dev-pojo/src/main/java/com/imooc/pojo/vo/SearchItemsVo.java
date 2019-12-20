package com.imooc.pojo.vo;

import lombok.Data;

@Data
public class SearchItemsVo {

    private String itemId;

    private String itemName;

    private Integer sellCounts;

    private String imgUrl;

    private Integer price;


}
