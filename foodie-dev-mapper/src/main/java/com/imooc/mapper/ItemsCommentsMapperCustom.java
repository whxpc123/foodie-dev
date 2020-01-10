package com.imooc.mapper;

import com.imooc.pojo.vo.ItemCommentVo;
import com.imooc.pojo.vo.MyCommentsVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ItemsCommentsMapperCustom  {

    List<ItemCommentVo> queryItemComments(@Param("paramsMap")Map<String,Object> map);

    void saveComments(Map<String,Object> map);

    List<MyCommentsVO> queryMyComments(@Param("paramsMap") Map<String,Object> map);
}