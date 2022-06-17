package com.yqm.modules.user.service.dto;

import lombok.Data;

import java.util.List;

/**
 * @ClassName BillVo
 * @Author weiximei <610796224@qq.com>
 * @Date 2019/11/12
 **/
@Data
public class BillOrderDto {
    private String time;
    private Integer count;
    private List<BillOrderRecordDto> child;
}
