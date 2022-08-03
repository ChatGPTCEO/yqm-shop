/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.shop.service;


import com.yqm.common.service.BaseService;
import com.yqm.modules.shop.domain.YqmSystemAttachment;

/**
 * <p>
 * 附件管理表 服务类
 * </p>
 *
 * @author weiximei
 * @since 2019-11-11
 */
public interface YqmSystemAttachmentService extends BaseService<YqmSystemAttachment> {

    /**
     *  根据名称获取
     * @param name name
     * @return YqmSystemAttachment
     */
    YqmSystemAttachment getInfo(String name);

    /**
     *  根据code获取
     * @param code code
     * @return YqmSystemAttachment
     */
    YqmSystemAttachment getByCode(String code);

    /**
     * 添加附件记录
     * @param name 名称
     * @param attSize 附件大小
     * @param attDir 路径
     * @param sattDir 路径
     */
    void attachmentAdd(String name,String attSize,String attDir,String sattDir);

    /**
     * 添加附件记录
     * @param name 名称
     * @param attSize 附件大小
     * @param attDir 路径
     * @param sattDir 路径
     * @param uid 用户id
     * @param code 邀请码
     */
    void newAttachmentAdd(String name,String attSize,String attDir,String sattDir,Long uid,String code);


}
