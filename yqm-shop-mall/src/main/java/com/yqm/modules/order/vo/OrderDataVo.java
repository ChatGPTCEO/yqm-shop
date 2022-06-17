package com.yqm.modules.order.vo;

import com.yqm.serializer.DoubleSerializer;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName OrderDataVo
 * @Author weiximei <610796224@qq.com>
 * @Date 2019/11/25
 **/
@Data
public class OrderDataVo implements Serializable {
    private Integer count;
    @JsonSerialize(using = DoubleSerializer.class)
    private Double price;
    private String time;
}
