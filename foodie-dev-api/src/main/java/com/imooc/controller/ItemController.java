package com.imooc.controller;

import com.imooc.pojo.Items;
import com.imooc.pojo.ItemsImg;
import com.imooc.pojo.ItemsParam;
import com.imooc.pojo.ItemsSpec;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemInfoVO;
import com.imooc.pojo.vo.ShopCartVO;
import com.imooc.service.IItemService;
import com.imooc.utils.IMOOCJSONResult;
import com.imooc.utils.PagedGridResult;
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

    @ApiOperation(value = "查询商品评价等级",notes = "根据itemId查询商品评价等级",httpMethod = "GET")
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

    @ApiOperation(value = "查询商品评价",notes = "根据itemId查询商品评价",httpMethod = "GET")
    @GetMapping("comments")
    public IMOOCJSONResult comments(@RequestParam("itemId")
                                    @ApiParam(name = "itemId",value = "商品id",required = true)
                                            String itemId,
                                    @RequestParam("level")
                                    @ApiParam(name = "level",value = "评价等级",required = false)
                                            Integer level,
                                    @RequestParam("page")
                                    @ApiParam(name = "page",value = "第几页",required = false)
                                            Integer page,
                                    @RequestParam("pageSize")
                                    @ApiParam(name = "pageSize",value = "每页数据条数",required = false)
                                            Integer pageSize) {
        if(StringUtils.isBlank(itemId)){
            IMOOCJSONResult.errorMsg("商品id不能为空");
        }
        if(page == null){
            page =1;
        }
        if(pageSize == null){
            pageSize =10;
        }
        PagedGridResult pagedGridResult = itemService.queryItemComments(itemId, level, page, pageSize);
        return IMOOCJSONResult.ok(pagedGridResult);
    }

    @ApiOperation(value = "根据关键字搜索商品列表",notes = "根据不同关键字搜索商品列表",httpMethod = "GET")
    @GetMapping("search")
    public IMOOCJSONResult search(@RequestParam("keywords")
                                  @ApiParam(name = "keywords",value = "商品名称",required = false)
                                          String keywords,
                                  @RequestParam("sort")
                                  @ApiParam(name = "sort",value = "排序方式c:销售量排序p:价格排序",required = false)
                                          String sort,
                                  @RequestParam("page")
                                  @ApiParam(name = "page",value = "第几页",required = false)
                                          Integer page,
                                  @RequestParam("pageSize")
                                  @ApiParam(name = "pageSize",value = "每页数据条数",required = false)
                                          Integer pageSize) {
        if(page == null){
            page =1;
        }
        if(pageSize == null){
            pageSize =10;
        }
        PagedGridResult pagedGridResult = itemService.searchItems(keywords, sort, page, pageSize);
        return IMOOCJSONResult.ok(pagedGridResult);
    }

    @ApiOperation(value = " 根据分类搜索商品列表",notes = "根据分类搜索商品列表",httpMethod = "GET")
    @GetMapping("catItems")
    public IMOOCJSONResult catItems(@RequestParam("catId")
                                  @ApiParam(name = "catId",value = "分类id",required = false)
                                          Integer catId,
                                  @RequestParam("sort")
                                  @ApiParam(name = "sort",value = "排序方式c:销售量排序p:价格排序",required = false)
                                          String sort,
                                  @RequestParam("page")
                                  @ApiParam(name = "page",value = "第几页",required = false)
                                          Integer page,
                                  @RequestParam("pageSize")
                                  @ApiParam(name = "pageSize",value = "每页数据条数",required = false)
                                          Integer pageSize) {
        if(page == null){
            page =1;
        }
        if(pageSize == null){
            pageSize =10;
        }
        PagedGridResult pagedGridResult = itemService.searchItems(catId, sort, page, pageSize);
        return IMOOCJSONResult.ok(pagedGridResult);
    }

    @ApiOperation(value = "根据规格ids查询最新的商品信息",notes = "根据规格ids查询最新的商品信息",httpMethod = "GET")
    @GetMapping("refresh")
    public IMOOCJSONResult refresh( @RequestParam("itemSpecIds")
                                    @ApiParam(name = "itemSpecIds",value = "拼接的规格ids",required = true,example = "1001,1002,1003")
                                    String itemSpecIds
                                   ) {
        if(StringUtils.isBlank(itemSpecIds)){
            return IMOOCJSONResult.ok();
        }
        List<ShopCartVO> shopCartVOS = itemService.queryItemsBySpecIds(itemSpecIds);

        return IMOOCJSONResult.ok(shopCartVOS);
    }
}
