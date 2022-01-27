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

import com.baomidou.mybatisplus.annotation.TableField;
import com.yqm.common.base.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: weiximei
 * @Date: 2021/10/16 22:21
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Data
public class TpUserDTO extends BaseEntity implements Serializable {

    private String userName;
    private Integer sex;
    private String phone;
    private String email;
    private String address;
    private String postalCode;
    private String account;
    private String password;
    private String provinceId;
    private String cityId;
    private String areaId;
    private Integer enterpriseCertification;
    private String status;
    /**
     * 头像
     */
    private String avatar;

    private List<String> permissions = new ArrayList<>();

    private List<String> roles = new ArrayList<>();
}
