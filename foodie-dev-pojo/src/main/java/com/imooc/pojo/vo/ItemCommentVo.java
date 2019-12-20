package com.imooc.pojo.vo;

import lombok.Data;

import java.util.Date;

@Data
public class ItemCommentVo {

    private Integer commentLevel;

    private String content;

    private String specName;

    private Date createdTime;

    private String userFace;

    private String nickname;

}
