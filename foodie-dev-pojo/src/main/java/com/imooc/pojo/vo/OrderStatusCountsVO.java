package com.imooc.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderStatusCountsVO {

    private Integer waitPayCounts;

    private Integer waitDeliverCounts;

    private Integer waitReceiveCounts;

    private Integer waitCommentCounts;
}
