/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yqmshop.com
* 注意：
* 本软件为www.yqmshop.com开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.yqm.modules.shop.domain;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.yqm.domain.BaseDomain;

/**
* @author lioncity
* @date 2020-12-09
*/
@Data
@TableName("yqm_app_version")
public class YqmAppVersion extends BaseDomain {
    @TableId
    private Integer id;




    /** 版本code */
    private String versionCode;

    /** 版本名称 */
    private String versionName;

    /** 版本描述 */
    private String versionInfo;

    /** 安卓下载链接 */
    private String androidUrl;

    /** 是否强制升级 */
    private Integer forceUpdate;

    /** ios store应用商店链接 */
    private String iosUrl;


    public void copy(YqmAppVersion source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}
