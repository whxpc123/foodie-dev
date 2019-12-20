package com.imooc.mapper;

import com.imooc.pojo.vo.SearchItemsVo;
import com.imooc.pojo.vo.ShopCartVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsMapperCustom  {

    List<SearchItemsVo>  searchItems(@Param("paramsMap")Map<String,Object> map);

    List<SearchItemsVo>  searchItemsByCatId(@Param("paramsMap")Map<String,Object> map);

    List<ShopCartVO> queryItemsBySpecIds(@Param("paramsList")List<String> specIds);
}