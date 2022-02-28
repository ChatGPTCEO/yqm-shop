package com.yqm.common.base;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @Author: weiximei
 * @Date: 2021/9/11 21:44
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Data
public class BaseEntity {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

}
