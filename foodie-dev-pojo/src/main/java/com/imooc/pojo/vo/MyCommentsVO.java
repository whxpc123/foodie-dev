package com.imooc.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class MyCommentsVO {

    private String commentId;

    private String content;

    private Date createdTime;

    private String itemId;

    private String itemName;

    private String specName;

    private String itemImg;

}
