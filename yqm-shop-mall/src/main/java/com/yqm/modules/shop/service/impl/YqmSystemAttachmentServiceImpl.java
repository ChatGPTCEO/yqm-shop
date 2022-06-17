/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.shop.service.impl;


import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.modules.shop.domain.YqmSystemAttachment;
import com.yqm.modules.shop.service.YqmSystemAttachmentService;
import com.yqm.modules.shop.service.mapper.YqmSystemAttachmentMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * <p>
 * 附件管理表 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2019-11-11
 */
@Slf4j
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class YqmSystemAttachmentServiceImpl extends BaseServiceImpl<YqmSystemAttachmentMapper, YqmSystemAttachment> implements YqmSystemAttachmentService {

    private final YqmSystemAttachmentMapper yqmSystemAttachmentMapper;

    /**
     *  根据名称获取
     * @param name name
     * @return YqmSystemAttachment
     */
    @Override
    public YqmSystemAttachment getInfo(String name) {
       LambdaQueryWrapper<YqmSystemAttachment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YqmSystemAttachment::getName,name)
                .last("limit 1");
        return yqmSystemAttachmentMapper.selectOne(wrapper);
    }

    /**
     *  根据code获取
     * @param code code
     * @return YqmSystemAttachment
     */
    @Override
    public YqmSystemAttachment getByCode(String code) {
       LambdaQueryWrapper<YqmSystemAttachment> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YqmSystemAttachment::getInviteCode,code)
                .last("limit 1");
        return yqmSystemAttachmentMapper.selectOne(wrapper);
    }

    /**
     * 添加附件记录
     * @param name 名称
     * @param attSize 附件大小
     * @param attDir 路径
     * @param sattDir 路径
     */
    @Override
    public void attachmentAdd(String name, String attSize, String attDir,String sattDir) {
        YqmSystemAttachment attachment =  YqmSystemAttachment.builder()
                .name(name)
                .attSize(attSize)
                .attDir(attDir)
                .attType("image/jpeg")
                .sattDir(sattDir)
                .build();
        yqmSystemAttachmentMapper.insert(attachment);
    }

    /**
     * 添加附件记录
     * @param name 名称
     * @param attSize 附件大小
     * @param attDir 路径
     * @param sattDir 路径
     * @param uid 用户id
     * @param code 邀请码
     */
    @Override
    public void newAttachmentAdd(String name, String attSize, String attDir, String sattDir,
                                 Long uid, String code) {

        YqmSystemAttachment attachment =  YqmSystemAttachment.builder()
                .name(name)
                .attSize(attSize)
                .attDir(attDir)
                .attType("image/jpeg")
                .sattDir(sattDir)
                .uid(uid)
                .inviteCode(code)
                .build();
        yqmSystemAttachmentMapper.insert(attachment);
    }



}
