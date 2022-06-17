/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.category.service.impl;

import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.common.utils.QueryHelpPlus;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.ShopCommonEnum;
import com.yqm.modules.category.domain.YqmStoreCategory;
import com.yqm.modules.category.service.YqmStoreCategoryService;
import com.yqm.modules.category.service.dto.YqmStoreCategoryDto;
import com.yqm.modules.category.service.dto.YqmStoreCategoryQueryCriteria;
import com.yqm.modules.category.service.mapper.StoreCategoryMapper;
import com.yqm.utils.CateDTO;
import com.yqm.utils.FileUtil;
import com.yqm.utils.TreeUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;


/**
* @author weiximei
* @date 2020-05-12
*/
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmStoreCategoryServiceImpl extends BaseServiceImpl<StoreCategoryMapper, YqmStoreCategory> implements YqmStoreCategoryService {

    private final IGenerator generator;

    /**
     * 获取分类列表树形列表
     * @return List
     */
    @Override
    public List<CateDTO> getList() {
       LambdaQueryWrapper<YqmStoreCategory> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YqmStoreCategory::getIsShow, ShopCommonEnum.SHOW_1.getValue())
                .orderByAsc(YqmStoreCategory::getSort);
        List<CateDTO> list = generator.convert(baseMapper.selectList(wrapper),CateDTO.class);
        return TreeUtil.list2TreeConverter(list,0);
    }

    //===============================//

    @Override
    public Map<String, Object> queryAll(YqmStoreCategoryQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmStoreCategoryDto> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", page.getList());
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    public List<YqmStoreCategoryDto> queryAll(YqmStoreCategoryQueryCriteria criteria){
        return generator.convert(this.baseMapper.selectList(QueryHelpPlus.getPredicate(YqmStoreCategory.class, criteria)),
                YqmStoreCategoryDto.class);
    }


    @Override
    public void download(List<YqmStoreCategoryDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmStoreCategoryDto yqmStoreCategory : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("父id", yqmStoreCategory.getPid());
            map.put("分类名称", yqmStoreCategory.getCateName());
            map.put("排序", yqmStoreCategory.getSort());
            map.put("图标", yqmStoreCategory.getPic());
            map.put("是否推荐", yqmStoreCategory.getIsShow());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    /**
     * 构建树形
     * @param categoryDTOS 分类列表
     * @return map
     */
    @Override
    public Map<String, Object> buildTree(List<YqmStoreCategoryDto> categoryDTOS) {
        Set<YqmStoreCategoryDto> trees = new LinkedHashSet<>();
        Set<YqmStoreCategoryDto> cates = new LinkedHashSet<>();
        List<String> deptNames = categoryDTOS.stream().map(YqmStoreCategoryDto::getCateName)
                .collect(Collectors.toList());

        //YqmStoreCategoryDto categoryDTO = new YqmStoreCategoryDto();
        Boolean isChild;
        List<YqmStoreCategory> categories = this.list();
        for (YqmStoreCategoryDto deptDTO : categoryDTOS) {
            isChild = false;
            if ("0".equals(deptDTO.getPid().toString())) {
                trees.add(deptDTO);
            }
            for (YqmStoreCategoryDto it : categoryDTOS) {
                if (it.getPid().equals(deptDTO.getId())) {
                    isChild = true;
                    if (deptDTO.getChildren() == null) {
                        deptDTO.setChildren(new ArrayList<YqmStoreCategoryDto>());
                    }
                    deptDTO.getChildren().add(it);
                }
            }
            if (isChild) {
                cates.add(deptDTO);
            }
            for (YqmStoreCategory category : categories) {
                if (category.getId().equals(deptDTO.getPid()) && !deptNames.contains(category.getCateName())) {
                    cates.add(deptDTO);
                }
            }
        }


        if (CollectionUtils.isEmpty(trees)) {
            trees = cates;
        }


        Integer totalElements = categoryDTOS != null ? categoryDTOS.size() : 0;

        Map<String, Object> map = Maps.newHashMap();
        map.put("totalElements", totalElements);
        map.put("content", CollectionUtils.isEmpty(trees) ? categoryDTOS : trees);
        return map;
    }


    /**
     * 检测分类是否操过二级
     * @param pid 父级id
     * @return boolean
     */
    @Override
    public boolean checkCategory(int pid){
        if(pid == 0) {
            return true;
        }
        YqmStoreCategory yqmStoreCategory =  this.getOne(Wrappers.<YqmStoreCategory>lambdaQuery()
                        .eq(YqmStoreCategory::getId,pid));
        return yqmStoreCategory.getPid() <= 0;
    }

    /**
     * 检测商品分类必选选择二级
     * @param id 分类id
     * @return boolean
     */
    @Override
    public boolean checkProductCategory(int id){
        YqmStoreCategory yqmStoreCategory =  this.getOne(Wrappers.<YqmStoreCategory>lambdaQuery()
                .eq(YqmStoreCategory::getId,id));
        return yqmStoreCategory.getPid() != 0;
    }

}
