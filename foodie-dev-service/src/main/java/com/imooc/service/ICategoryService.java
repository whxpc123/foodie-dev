package com.imooc.service;

import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.NewItemsVO;

import java.util.List;

public interface ICategoryService {

    /**
     *  查询所有的一级分类
     */
    List<Category>  queryALlRootLevel();

    /**
     * 根据一级分类id查询下面的子分类
     * @param rootCatId
     * @return
     */
    List<CategoryVO> getSubCatList(Integer rootCatId);

    /**
     * 根据一级分类id查询首页一级分类下的6条最新商品信息
     * @param rootCatId
     * @return
     */
    List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId);


}
