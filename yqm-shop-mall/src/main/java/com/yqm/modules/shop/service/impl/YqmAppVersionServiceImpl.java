/**
* Copyright (C) 2018-2022
* All rights reserved, Designed By www.yqmshop.com
* 注意：
* 本软件为www.yqmshop.com开发研制，未经购买不得使用
* 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
* 一经发现盗用、分享等行为，将追究法律责任，后果自负
*/
package com.yqm.modules.shop.service.impl;

import com.yqm.modules.shop.domain.YqmAppVersion;
import com.yqm.common.service.impl.BaseServiceImpl;
import lombok.AllArgsConstructor;
import com.yqm.dozer.service.IGenerator;
import com.github.pagehelper.PageInfo;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.utils.FileUtil;
import com.yqm.modules.shop.service.YqmAppVersionService;
import com.yqm.modules.shop.service.dto.YqmAppVersionDto;
import com.yqm.modules.shop.service.dto.YqmAppVersionQueryCriteria;
import com.yqm.modules.shop.service.mapper.YqmAppVersionMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
// 默认不使用缓存
//import org.springframework.cache.annotation.CacheConfig;
//import org.springframework.cache.annotation.CacheEvict;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import com.yqm.domain.PageResult;
/**
* @author lioncity
* @date 2020-12-09
*/
@Service
@AllArgsConstructor
//@CacheConfig(cacheNames = "yqmAppVersion")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmAppVersionServiceImpl extends BaseServiceImpl<YqmAppVersionMapper, YqmAppVersion> implements YqmAppVersionService {

    private final IGenerator generator;

    @Override
    //@Cacheable
    public PageResult<YqmAppVersionDto> queryAll(YqmAppVersionQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmAppVersion> page = new PageInfo<>(queryAll(criteria));
        return generator.convertPageInfo(page,YqmAppVersionDto.class);
    }


    @Override
    //@Cacheable
    public List<YqmAppVersion> queryAll(YqmAppVersionQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmAppVersion.class, criteria));
    }


    @Override
    public void download(List<YqmAppVersionDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmAppVersionDto yqmAppVersion : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put(" isDel",  yqmAppVersion.getIsDel());
            map.put("更新时间", yqmAppVersion.getCreateTime());
            map.put(" updateTime",  yqmAppVersion.getUpdateTime());
            map.put("版本code", yqmAppVersion.getVersionCode());
            map.put("版本名称", yqmAppVersion.getVersionName());
            map.put("版本描述", yqmAppVersion.getVersionInfo());
            map.put("安卓下载链接", yqmAppVersion.getAndroidUrl());
            map.put("是否强制升级", yqmAppVersion.getForceUpdate());
            map.put("ios store应用商店链接", yqmAppVersion.getIosUrl());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
