package com.imooc.service;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ShopCartVO;
import com.imooc.utils.PagedGridResult;

import java.util.List;

public interface IItemService {
    /**
     * 根据itemId查询商品信息
     * @param itemId
     * @return
     */
    Items queryItemById(String itemId);

    List<ItemsImg> queryItemImgList(String itemId);

    List<ItemsSpec> queryItemSpecList(String itemId);

    ItemsParam queryItemParam(String itemId);

    CommentLevelCountsVO queryCommentCounts(String itemId);

    PagedGridResult queryItemComments(String itemId,Integer level,Integer page,Integer pageSize);

    PagedGridResult searchItems(String keyword,String sort,Integer page,Integer pageSize);

    PagedGridResult searchItems(Integer catId,String sort,Integer page,Integer pageSize);

    List<ShopCartVO> queryItemsBySpecIds(String specIds);

    ItemsSpec queryItemSpecBySpeId(String specId);

    String queryMainImgById(String itemId);

    void decreaseItemSpecStock(String specId,Integer buyCounts);
}
