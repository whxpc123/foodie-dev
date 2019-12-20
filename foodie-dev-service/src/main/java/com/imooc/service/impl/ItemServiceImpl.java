package com.imooc.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import com.imooc.enums.CommentLevel;
import com.imooc.mapper.*;
import com.imooc.pojo.*;
import com.imooc.pojo.vo.CommentLevelCountsVO;
import com.imooc.pojo.vo.ItemCommentVo;
import com.imooc.pojo.vo.SearchItemsVo;
import com.imooc.pojo.vo.ShopCartVO;
import com.imooc.service.IItemService;
import com.imooc.utils.DesensitizationUtil;
import com.imooc.utils.PagedGridResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class ItemServiceImpl  implements IItemService {

    @Autowired
    private ItemsMapper itemsMapper;

    @Autowired
    private ItemsImgMapper itemsImgMapper;

    @Autowired
    private ItemsSpecMapper itemsSpecMapper;

    @Autowired
    private ItemsParamMapper itemsParamMapper;

    @Autowired
    private ItemsCommentsMapper itemsCommentsMapper;

    @Autowired
    private ItemsCommentsMapperCustom itemsCommentsMapperCustom;

    @Autowired
    private ItemsMapperCustom itemsMapperCustom;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Items queryItemById(String itemId) {

        return itemsMapper.selectByPrimaryKey(itemId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsImg> queryItemImgList(String itemId) {

        Example example = new Example(ItemsImg.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("itemId",itemId);
        List<ItemsImg> itemsImgs = itemsImgMapper.selectByExample(example);

        return itemsImgs;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public List<ItemsSpec> queryItemSpecList(String itemId) {
        Example example = new Example(ItemsSpec.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("itemId",itemId);
        List<ItemsSpec> itemsSpecs = itemsSpecMapper.selectByExample(example);

        return itemsSpecs;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public ItemsParam queryItemParam(String itemId) {
        Example example = new Example(ItemsParam.class);
        Example.Criteria criteria = example.createCriteria();

        criteria.andEqualTo("itemId",itemId);
        ItemsParam itemsParam = itemsParamMapper.selectOneByExample(example);

        return itemsParam;
    }
    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public CommentLevelCountsVO queryCommentCounts(String itemId) {
        Integer goodCounts = getCommentCounts(itemId, CommentLevel.GOOD.type);
        Integer normalCounts = getCommentCounts(itemId, CommentLevel.NORMAL.type);
        Integer badCounts = getCommentCounts(itemId, CommentLevel.BAD.type);
        Integer totalCounts = goodCounts+normalCounts+badCounts;
        CommentLevelCountsVO countsVO = CommentLevelCountsVO.builder()
                .goodCounts(goodCounts).normalCounts(normalCounts)
                .badCounts(badCounts).totalCounts(totalCounts).build();
        return countsVO;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult queryItemComments(String itemId, Integer level, Integer page, Integer pageSize) {

        Map<String,Object> map =Maps.newHashMap();
        map.put("itemId",itemId);
        map.put("level",level);

        PageHelper.startPage(page, pageSize);

        List<ItemCommentVo> itemCommentVos = itemsCommentsMapperCustom.queryItemComments(map);
        itemCommentVos.stream().forEach(e-> e.setNickname(DesensitizationUtil.commonDisplay(e.getNickname())));//用户信息脱敏
        PagedGridResult pagedGridResult = setPagedGrid(itemCommentVos, page);
        return pagedGridResult;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItems(String keyword, String sort, Integer page, Integer pageSize) {

        Map<String,Object> map =Maps.newHashMap();
        map.put("keyword",keyword);
        map.put("sort",sort);

        PageHelper.startPage(page, pageSize);

        List<SearchItemsVo> searchItemsVos = itemsMapperCustom.searchItems(map);

        return  setPagedGrid(searchItemsVos,page);


    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public PagedGridResult searchItems(Integer catId, String sort, Integer page, Integer pageSize) {

        Map<String,Object> map =Maps.newHashMap();
        map.put("catId",catId);
        map.put("sort",sort);

        PageHelper.startPage(page, pageSize);

        List<SearchItemsVo> searchItemsVos = itemsMapperCustom.searchItemsByCatId(map);

        return  setPagedGrid(searchItemsVos,page);


    }

    @Override
    public List<ShopCartVO> queryItemsBySpecIds(String specIds) {

        String[] specIdArray = specIds.split(",");
        List<String> specIdList = Arrays.asList(specIdArray);
        return itemsMapperCustom.queryItemsBySpecIds(specIdList);
    }

    private PagedGridResult setPagedGrid(List<?> list,Integer page){
        PageInfo<?> pageList = new PageInfo<>(list);
        PagedGridResult grid = new PagedGridResult();
        grid.setPage(page);
        grid.setRows(list);
        grid.setTotal(pageList.getPages());
        grid.setRecords(pageList.getTotal());
        return grid;
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    Integer getCommentCounts(String itemId,Integer level){
        ItemsComments itemsComments = new ItemsComments();
        itemsComments.setItemId(itemId);
        if(level != null){
            itemsComments.setCommentLevel(level);
        }
        int count = itemsCommentsMapper.selectCount(itemsComments);
        return count;
    }

}
