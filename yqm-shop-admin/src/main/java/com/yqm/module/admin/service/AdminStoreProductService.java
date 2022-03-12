package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.YqmStoreProductToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmStoreProductDTO;
import com.yqm.common.dto.YqmStoreProductStatisticsDTO;
import com.yqm.common.entity.YqmBrand;
import com.yqm.common.entity.YqmStoreProduct;
import com.yqm.common.request.YqmBrandRequest;
import com.yqm.common.request.YqmStoreProductRequest;
import com.yqm.common.service.IYqmBrandService;
import com.yqm.common.service.IYqmStoreProductService;
import com.yqm.security.User;
import com.yqm.security.UserInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

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
    private final IYqmBrandService iYqmBrandService;

    public AdminStoreProductService(IYqmStoreProductService iYqmStoreProductService, IYqmBrandService iYqmBrandService) {
        this.iYqmStoreProductService = iYqmStoreProductService;
        this.iYqmBrandService = iYqmBrandService;
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

        request.setInStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmStoreProductService.page(page, iYqmStoreProductService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
            List<YqmStoreProductDTO> dtoList = YqmStoreProductToDTO.toYqmStoreProductDTOList(pageList.getRecords());
            this.getOther(dtoList);
            pageList.setRecords(dtoList);

        }
        return pageList;
    }

    private List<YqmStoreProductDTO> getOther(List<YqmStoreProductDTO> dtoList) {
        if (CollectionUtils.isEmpty(dtoList)) {
            return null;
        }
        // 获取品牌
        List<String> brandIdList = dtoList.stream().map(e -> e.getBrandId()).collect(Collectors.toList());
        YqmBrandRequest brandRequest = new YqmBrandRequest();
        brandRequest.setInIdList(brandIdList);
        List<YqmBrand> brandList = iYqmBrandService.list(iYqmBrandService.getQuery(brandRequest));
        if (CollectionUtils.isNotEmpty(brandList)) {
            Map<String, YqmBrand> brandMap = brandList.stream().collect(Collectors.toMap(e -> e.getId(), e -> e));
            dtoList.forEach(e -> {
                YqmBrand yqmBrand = brandMap.get(e.getBrandId());
                if (Objects.nonNull(yqmBrand)) {
                    e.setBrandName(yqmBrand.getBrandName());
                }
            });
        }

        return dtoList;
    }

    /**
     * 查询
     *
     * @param request
     * @return
     */
    public List<YqmStoreProductDTO> list(YqmStoreProductRequest request) {
        request.setInStatusList(YqmDefine.includeStatus);
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

    /**
     * 商品-统计
     *
     * @return
     */
    public YqmStoreProductStatisticsDTO getStatistics() {
        User user = UserInfoService.getUser();
        Integer count = iYqmStoreProductService.getCount(user.getId());
        Integer shelvesCount = iYqmStoreProductService.getShelvesCount(user.getId(), YqmDefine.ShelvesType.shelves.getValue());
        Integer notShelvesCount = iYqmStoreProductService.getShelvesCount(user.getId(), YqmDefine.ShelvesType.not_shelves.getValue());

        return YqmStoreProductStatisticsDTO.builder()
                .shelvesCount(shelvesCount)
                .notShelvesCount(notShelvesCount)
                .count(count).build();
    }

}
