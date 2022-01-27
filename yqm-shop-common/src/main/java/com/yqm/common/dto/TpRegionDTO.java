/*
 * Copyright 2021 Wei xi mei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *
 * distributed under the License is distributed on an "AS IS" BASIS,
 *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 *
 * limitations under the License.
 */

package com.yqm.common.dto;

import com.yqm.common.base.BaseEntity;
import lombok.Data;

import java.util.List;

/**
 * @Author: weiximei
 * @Date: 2021/10/24 22:27
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Data
public class TpRegionDTO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 区域名称
     */
    private String name;

    /**
     * 区域上级标识
     */
    private String pid;

    /**
     * 地名简称
     */
    private String sname;

    /**
     * 区域等级
     */
    private Integer level;

    /**
     * 区域编码
     */
    private String citycode;

    /**
     * 邮政编码
     */
    private String yzcode;

    /**
     * 组合名称
     */
    private String mername;

    /**
     *
     */
    private Float lng;

    /**
     *
     */
    private Float lat;

    /**
     *
     */
    private String pinyin;


    /**
     * 子集
     */
    private List<TpRegionDTO> children;
}
