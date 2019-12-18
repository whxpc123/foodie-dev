package com.imooc.controller;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemInfoVO;
import com.imooc.service.IItemService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(value = "商品详情",tags = {"商品详情展示的相关接口"})
@RestController
@RequestMapping("items")
public class ItemController {

    @Autowired
    private IItemService itemService;

    @ApiOperation(value = "查询商品详情",notes = "根据itemId查询商品详情",httpMethod = "GET")
    @GetMapping("info/{itemId}")
    public IMOOCJSONResult getSixNewItemsLazy(@PathVariable("itemId")
                                         @ApiParam(name = "itemId",value = "商品id",required = true)
                                                 String itemId){
        if(StringUtils.isBlank(itemId)){
            IMOOCJSONResult.errorMsg(null);
        }

        Items items = itemService.queryItemById(itemId);
        List<ItemsImg> itemsImgs = itemService.queryItemImgList(itemId);
        List<ItemsSpec> itemsSpecs = itemService.queryItemSpecList(itemId);
        ItemsParam itemsParam = itemService.queryItemParam(itemId);

        ItemInfoVO itemInfoVO = ItemInfoVO.builder().item(items).itemImgList(itemsImgs)
                .itemSpecList(itemsSpecs).itemParams(itemsParam).build();

        return IMOOCJSONResult.ok(itemInfoVO);
    }

    @ApiOperation(value = "查询商品评价",notes = "根据itemId查询商品评价",httpMethod = "GET")
    @GetMapping("commentLevel")
    public IMOOCJSONResult commentLevel(@RequestParam("itemId")
                                              @ApiParam(name = "itemId",value = "商品id",required = true)
                                                      String itemId){
        if(StringUtils.isBlank(itemId)){
            IMOOCJSONResult.errorMsg("商品id不能为空");
        }

        CommentLevelCountsVO commentLevelCountsVO = itemService.queryCommentCounts(itemId);
        return IMOOCJSONResult.ok(commentLevelCountsVO);
    }
}
