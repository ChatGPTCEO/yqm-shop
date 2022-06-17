package com.yqm.modules.template.service.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName RegionDto
 * @Author weiximei <610796224@qq.com>
 * @Date 2020/5/25
 **/
@Getter
@Setter
public class RegionDto {

    private String name;

    private String city_id;

    List<RegionChildrenDto> children;

}
