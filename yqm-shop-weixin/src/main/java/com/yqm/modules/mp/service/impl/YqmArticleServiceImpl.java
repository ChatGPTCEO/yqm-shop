/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.mp.service.impl;

import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.modules.mp.service.mapper.ArticleMapper;
import com.yqm.modules.mp.domain.YqmArticle;
import com.yqm.modules.mp.service.YqmArticleService;
import com.yqm.modules.mp.service.dto.YqmArticleDto;
import com.yqm.modules.mp.service.dto.YqmArticleQueryCriteria;
import com.yqm.modules.mp.vo.YqmArticleQueryVo;
import com.yqm.utils.FileUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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



/**
* @author weiximei
* @date 2020-05-12
*/
@Slf4j
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmArticleServiceImpl extends BaseServiceImpl<ArticleMapper, YqmArticle> implements YqmArticleService {

    private final IGenerator generator;
    private final ArticleMapper articleMapper;
    @Value("${file.path}")
    private String uploadDirStr;

    public YqmArticleServiceImpl(IGenerator generator,ArticleMapper articleMapper) {
        this.generator = generator;
        this.articleMapper = articleMapper;
    }

    /**
     * 获取文章列表
     * @param page 页码
     * @param limit 条数
     * @return List
     */
    @Override
    public List<YqmArticleQueryVo> getList(int page, int limit){
        Page<YqmArticle> pageModel = new Page<>(page, limit);

        IPage<YqmArticle> pageList = articleMapper.selectPage(pageModel, Wrappers.<YqmArticle>lambdaQuery()
                .orderByDesc(YqmArticle::getId));

        return generator.convert(pageList.getRecords(),YqmArticleQueryVo.class);
    }

    /**
     * 获取文章详情
     * @param id id
     * @return YqmArticleQueryVo
     */
    @Override
    public YqmArticleQueryVo getDetail(int id){
        return generator.convert(this.getById(id),YqmArticleQueryVo.class);
    }


    @Override
    public void incVisitNum(int id) {
        articleMapper.incVisitNum(id);
    }

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmArticleQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmArticle> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", generator.convert(page.getList(), YqmArticleDto.class));
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    //@Cacheable
    public List<YqmArticle> queryAll(YqmArticleQueryCriteria criteria){
        return baseMapper.selectList(QueryHelpPlus.getPredicate(YqmArticle.class, criteria));
    }


    @Override
    public void download(List<YqmArticleDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmArticleDto yqmArticle : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("分类id", yqmArticle.getCid());
            map.put("文章标题", yqmArticle.getTitle());
            map.put("文章作者", yqmArticle.getAuthor());
            map.put("文章图片", yqmArticle.getImageInput());
            map.put("文章简介", yqmArticle.getSynopsis());
            map.put(" content",  yqmArticle.getContent());
            map.put("文章分享标题", yqmArticle.getShareTitle());
            map.put("文章分享简介", yqmArticle.getShareSynopsis());
            map.put("浏览次数", yqmArticle.getVisit());
            map.put("排序", yqmArticle.getSort());
            map.put("原文链接", yqmArticle.getUrl());
            map.put("状态", yqmArticle.getStatus());
            map.put("是否隐藏", yqmArticle.getHide());
            map.put("管理员id", yqmArticle.getAdminId());
            map.put("商户id", yqmArticle.getMerId());
            map.put("产品关联id", yqmArticle.getProductId());
            map.put("是否热门(小程序)", yqmArticle.getIsHot());
            map.put("是否轮播图(小程序)", yqmArticle.getIsBanner());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }




}
