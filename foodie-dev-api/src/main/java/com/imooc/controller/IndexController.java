package com.imooc.controller;

import com.imooc.enums.YesOrNo;
import com.imooc.pojo.Carousel;
import com.imooc.pojo.Category;
import com.imooc.pojo.vo.CategoryVO;
import com.imooc.pojo.vo.NewItemsVO;
import com.imooc.service.ICarouselService;
import com.imooc.service.ICategoryService;
import com.imooc.utils.IMOOCJSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "首页",tags = {"首页展示的相关接口"})
@RestController
@RequestMapping("index")
public class IndexController {

    @Autowired
    private ICarouselService carouselService;

    @Autowired
    private ICategoryService categoryService;

    @ApiOperation(value = "获取首页轮播图",notes = "获取首页轮播图",httpMethod = "GET")
    @GetMapping("carousels")
    public IMOOCJSONResult index(){

        List<Carousel> carousels = carouselService.queryAll(YesOrNo.Yes.type);

        return IMOOCJSONResult.ok(carousels);
    }

    @ApiOperation(value = "查询首页所有一级分类",notes = "查询首页所有一级分类",httpMethod = "GET")
    @GetMapping("cats")
    public IMOOCJSONResult rootCategory(){

        List<Category> categories = categoryService.queryALlRootLevel();

        return IMOOCJSONResult.ok(categories);
    }

    @ApiOperation(value = "查询一级分类下面的子分类",notes = "根据一级分类id查询子分类",httpMethod = "GET")
    @GetMapping("subCat/{rootCatId}")
    public IMOOCJSONResult getSubCatList(@PathVariable("rootCatId")
                                         @ApiParam(name = "rootCatId",value = "一级分类id",required = true)
                                         Integer rootCatId){
        if(rootCatId == null){
            IMOOCJSONResult.errorMsg("一级分类不能为空");
        }

        List<CategoryVO> categoryVOS = categoryService.getSubCatList(rootCatId);

        return IMOOCJSONResult.ok(categoryVOS);
    }

    @ApiOperation(value = "查询一级分类下面的6条最新商品数据",notes = "根据一级分类id查询首页一级分类下的6条最新商品信息",httpMethod = "GET")
    @GetMapping("sixNewItems/{rootCatId}")
    public IMOOCJSONResult getSixNewItemsLazy(@PathVariable("rootCatId")
                                         @ApiParam(name = "rootCatId",value = "一级分类id",required = true)
                                                 Integer rootCatId){
        if(rootCatId == null){
            IMOOCJSONResult.errorMsg("一级分类不能为空");
        }
        List<NewItemsVO> list = categoryService.getSixNewItemsLazy(rootCatId);

        return IMOOCJSONResult.ok(list);
    }
}
