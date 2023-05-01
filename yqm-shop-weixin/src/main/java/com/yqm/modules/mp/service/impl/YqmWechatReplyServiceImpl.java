/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.modules.mp.service.impl;

import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.exception.EntityExistException;
import com.yqm.modules.mp.service.mapper.WechatReplyMapper;
import com.yqm.modules.mp.domain.YqmWechatReply;
import com.yqm.modules.mp.service.YqmWechatReplyService;
import com.yqm.modules.mp.service.dto.YqmWechatReplyDto;
import com.yqm.modules.mp.service.dto.YqmWechatReplyQueryCriteria;
import com.yqm.utils.FileUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;

/**
* @author weiximei
* @date 2020-05-12
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yqmWechatReply")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmWechatReplyServiceImpl extends BaseServiceImpl<WechatReplyMapper, YqmWechatReply> implements YqmWechatReplyService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmWechatReplyQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmWechatReply> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmWechatReplyDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmWechatReply> queryAll(YqmWechatReplyQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmWechatReply.class, criteria));
    }


    @Override
    public void download(List<YqmWechatReplyDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmWechatReplyDto yqmWechatReply : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("关键字", yqmWechatReply.getKey());
            map.put("回复类型", yqmWechatReply.getType());
            map.put("回复数据", yqmWechatReply.getData());
            map.put("0=不可用  1 =可用", yqmWechatReply.getStatus());
            map.put("是否隐藏", yqmWechatReply.getHide());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public YqmWechatReply isExist(String key) {
        YqmWechatReply yqmWechatReply = this.getOne(new LambdaQueryWrapper<YqmWechatReply>()
                .eq(YqmWechatReply::getKey,key));
        return yqmWechatReply;
    }

    @Override
    public void create(YqmWechatReply yqmWechatReply) {
        if(this.isExist(yqmWechatReply.getKey()) != null){
            throw new EntityExistException(YqmWechatReply.class,"key",yqmWechatReply.getKey());
        }
        this.save(yqmWechatReply);
    }

    @Override
    public void upDate(YqmWechatReply resources) {
        YqmWechatReply yqmWechatReply = this.getById(resources.getId());
        YqmWechatReply yqmWechatReply1;
        yqmWechatReply1 = this.isExist(resources.getKey());
        if(yqmWechatReply1 != null && !yqmWechatReply1.getId().equals(yqmWechatReply.getId())){
            throw new EntityExistException(YqmWechatReply.class,"key",resources.getKey());
        }
        yqmWechatReply.copy(resources);
        this.saveOrUpdate(yqmWechatReply);
    }
}
