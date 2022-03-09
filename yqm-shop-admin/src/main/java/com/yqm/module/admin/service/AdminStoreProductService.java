package com.yqm.module.admin.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.YqmStoreProductToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmStoreProductDTO;
import com.yqm.common.entity.YqmStoreProduct;
import com.yqm.common.request.YqmStoreProductRequest;
import com.yqm.common.service.IYqmStoreProductService;
import com.yqm.security.User;
import com.yqm.security.UserInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * 管理端-商品
 *
 * @Author: weiximei
 * @Date: 2022/2/28 20:01
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminStoreProductService {

    private final IYqmStoreProductService iYqmStoreProductService;

    public AdminStoreProductService(IYqmStoreProductService iYqmStoreProductService) {
        this.iYqmStoreProductService = iYqmStoreProductService;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public IPage<YqmStoreProductDTO> page(YqmStoreProductRequest request) {
        Page<YqmStoreProduct> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmStoreProductService.page(page, iYqmStoreProductService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
            pageList.setRecords(YqmStoreProductToDTO.toYqmStoreProductDTOList(pageList.getRecords()));
        }
        return pageList;
    }

    /**
     * 查询
     *
     * @param request
     * @return
     */
    public List<YqmStoreProductDTO> list(YqmStoreProductRequest request) {
        request.setStatusList(YqmDefine.includeStatus);
        List<YqmStoreProduct> list = iYqmStoreProductService.list(iYqmStoreProductService.getQuery(request));
        return YqmStoreProductToDTO.toYqmStoreProductDTOList(list);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public YqmStoreProductDTO getById(String id) {
        YqmStoreProduct entity = iYqmStoreProductService.getById(id);
        return YqmStoreProductToDTO.toYqmStoreProductDTO(entity);
    }

    /**
     * 保存/修改
     *
     * @param request
     * @return
     */
    public YqmStoreProductDTO save(YqmStoreProductRequest request) {
        User user = UserInfoService.getUser();
        YqmStoreProduct entity = YqmStoreProductToDTO.toYqmStoreProduct(request);
        if (StringUtils.isEmpty(request.getId())) {
            entity.setCreatedTime(LocalDateTime.now());
            entity.setCreatedBy(user.getId());
        }
        if (CollectionUtils.isNotEmpty(request.getProductBannerList())) {
            entity.setProductBanner(JSONObject.toJSONString(request.getProductBannerList()));
        }

        entity.setUpdatedBy(user.getId());
        entity.setUpdatedTime(LocalDateTime.now());
        iYqmStoreProductService.saveOrUpdate(entity);
        return YqmStoreProductToDTO.toYqmStoreProductDTO(entity);
    }

    /**
     * 上下架
     *
     * @param request
     * @return
     */
    public YqmStoreProductDTO isShelves(YqmStoreProductRequest request) {
        YqmStoreProduct entity = iYqmStoreProductService.getById(request.getId());
        entity.setIsShelves(request.getIsShelves());
        return this.save(YqmStoreProductToDTO.toYqmStoreProductRequest(entity));
    }


    /**
     * 删除
     *
     * @param id
     * @return
     */
    public String deleteById(String id) {
        YqmStoreProduct entity = iYqmStoreProductService.getById(id);
        if (Objects.isNull(entity)) {
            return id;
        }
        entity.setStatus(YqmDefine.StatusType.delete.getValue());
        this.save(YqmStoreProductToDTO.toYqmStoreProductRequest(entity));
        return id;
    }


}
