/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.modules.product.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName FromatDetailDTO
 * @Author weiximei <610796224@qq.com>
 * @Date 2019/10/12
 **/

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FromatDetailDto {
    private  String attrHidden;

    private  String detailValue;

    private List<String> detail;

    private String value;

}
