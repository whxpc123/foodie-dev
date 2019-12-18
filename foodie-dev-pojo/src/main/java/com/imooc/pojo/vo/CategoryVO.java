package com.imooc.pojo.vo;

import lombok.Data;

import java.util.List;

@Data
public class CategoryVO {

    private Integer id;
    private String name;
    private String type;
    private Integer fatherId;
    private List<SubCategoryVO> subCatList;
}
