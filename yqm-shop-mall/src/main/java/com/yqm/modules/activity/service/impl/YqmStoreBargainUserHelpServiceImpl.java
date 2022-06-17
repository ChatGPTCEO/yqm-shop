/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.activity.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.dozer.service.IGenerator;
import com.yqm.modules.activity.domain.YqmStoreBargainUser;
import com.yqm.modules.activity.domain.YqmStoreBargainUserHelp;
import com.yqm.modules.activity.service.YqmStoreBargainUserHelpService;
import com.yqm.modules.activity.service.YqmStoreBargainUserService;
import com.yqm.modules.activity.service.mapper.YqmStoreBargainUserHelpMapper;
import com.yqm.modules.activity.vo.YqmStoreBargainUserHelpQueryVo;
import com.yqm.modules.user.domain.YqmUser;
import com.yqm.modules.user.service.YqmUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;


/**
 * <p>
 * 砍价用户帮助表 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2019-12-21
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class YqmStoreBargainUserHelpServiceImpl extends BaseServiceImpl<YqmStoreBargainUserHelpMapper, YqmStoreBargainUserHelp> implements YqmStoreBargainUserHelpService {

    @Autowired
    private IGenerator generator;

    @Autowired
    private YqmStoreBargainUserHelpMapper yqmStoreBargainUserHelpMapper;

    @Autowired
    private YqmStoreBargainUserService storeBargainUserService;
    @Autowired
    private YqmUserService userService;




    /**
     * 获取砍价帮
     * @param bargainId 砍价商品id
     * @param bargainUserUid 砍价用户id
     * @param page page
     * @param limit limit
     * @return list
     */
    @Override
    public List<YqmStoreBargainUserHelpQueryVo> getList(Long bargainId, Long bargainUserUid,
                                                       int page, int limit) {
        YqmStoreBargainUser storeBargainUser = storeBargainUserService
                .getBargainUserInfo(bargainId,bargainUserUid);
        if(ObjectUtil.isNull(storeBargainUser)) {
            return Collections.emptyList();
        }
        Page<YqmStoreBargainUserHelp> pageModel = new Page<>(page, limit);
        LambdaQueryWrapper<YqmStoreBargainUserHelp> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YqmStoreBargainUserHelp::getBargainUserId,storeBargainUser.getId())
                .orderByDesc(YqmStoreBargainUserHelp::getId);
        List<YqmStoreBargainUserHelpQueryVo> storeBargainUserHelpQueryVos = generator
                .convert(yqmStoreBargainUserHelpMapper.selectPage(pageModel,wrapper).getRecords(),
                        YqmStoreBargainUserHelpQueryVo.class);

        storeBargainUserHelpQueryVos.forEach(item->{
            YqmUser yqmUser = userService.getById(item.getUid());
            item.setAvatar(yqmUser.getAvatar());
            item.setNickname(yqmUser.getNickname());
        });

        return storeBargainUserHelpQueryVos;
    }

    /**
     * 获取砍价帮总人数
     * @param bargainId 砍价产品ID
     * @param bargainUserUid 用户参与砍价表id
     * @return int
     */
    @Override
    public int getBargainUserHelpPeopleCount(Long bargainId, Long bargainUserUid) {
        return this.lambdaQuery()
                .eq(YqmStoreBargainUserHelp::getBargainUserId,bargainUserUid)
                .eq(YqmStoreBargainUserHelp::getBargainId,bargainId)
                .count();
    }





}
