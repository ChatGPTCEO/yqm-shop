package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.YqmProjectToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmProjectDTO;
import com.yqm.common.entity.YqmProject;
import com.yqm.common.entity.YqmProjectGoods;
import com.yqm.common.request.BaseRequest;
import com.yqm.common.request.YqmProjectGoodsRequest;
import com.yqm.common.request.YqmProjectRequest;
import com.yqm.common.service.IYqmProjectGoodsService;
import com.yqm.common.service.IYqmProjectService;
import com.yqm.security.User;
import com.yqm.security.UserInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 管理端-专题
 *
 * @Author: weiximei
 * @Date: 2022/2/28 20:01
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminProjectService {

    private final IYqmProjectService iYqmProjectService;

    private final IYqmProjectGoodsService iYqmProjectGoodsService;


    public AdminProjectService(IYqmProjectService iYqmProjectService, IYqmProjectGoodsService iYqmProjectGoodsService) {
        this.iYqmProjectService = iYqmProjectService;
        this.iYqmProjectGoodsService = iYqmProjectGoodsService;
    }

    /**
     * 分页查询
     *
     * @param request
     * @return
     */
    public IPage<YqmProjectDTO> page(YqmProjectRequest request) {
        Page<YqmProject> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setInStatusList(YqmDefine.includeStatus);
        IPage pageList = iYqmProjectService.page(page, iYqmProjectService.getQuery(request));
        if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
            List<YqmProjectDTO> projectDTOS = YqmProjectToDTO.toYqmProjectDTOList(pageList.getRecords());
            pageList.setRecords(projectDTOS);
        }
        return pageList;
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    public YqmProjectDTO getById(String id) {
        YqmProject entity = iYqmProjectService.getById(id);
        return YqmProjectToDTO.toYqmProjectDTO(entity);
    }

    /**
     * 保存/修改
     *
     * @param request
     * @return
     */
    public YqmProjectDTO save(YqmProjectRequest request) {
        User user = UserInfoService.getUser();
        YqmProject entity = YqmProjectToDTO.toYqmProject(request);
        if (StringUtils.isEmpty(request.getId())) {
            entity.setCreatedTime(LocalDateTime.now());
            entity.setCreatedBy(user.getId());
        }

        entity.setUpdatedBy(user.getId());
        entity.setUpdatedTime(LocalDateTime.now());

        if (CollectionUtils.isNotEmpty(request.getProductList())) {
            YqmProjectGoodsRequest goodsRequest = new YqmProjectGoodsRequest();
            goodsRequest.setProjectId(entity.getId());
            List<YqmProjectGoods> projectGoods = iYqmProjectGoodsService.list(iYqmProjectGoodsService.getQuery(goodsRequest));

            List<String> productIdList = request.getProductList().stream().map(BaseRequest::getId).collect(Collectors.toList());

            List<YqmProjectGoods> notProjectGoodsList = projectGoods.stream().filter(e -> !productIdList.contains(e.getId())).map(e -> {
                e.setStatus(YqmDefine.StatusType.delete.getValue());
                return e;
            }).collect(Collectors.toList());
            iYqmProjectGoodsService.saveOrUpdateBatch(notProjectGoodsList);
            
        } else {
            YqmProjectGoodsRequest goodsRequest = new YqmProjectGoodsRequest();
            goodsRequest.setProjectId(entity.getId());
            List<YqmProjectGoods> projectGoods = iYqmProjectGoodsService.list(iYqmProjectGoodsService.getQuery(goodsRequest));
            iYqmProjectGoodsService.saveOrUpdateBatch(projectGoods);
        }

        iYqmProjectService.saveOrUpdate(entity);
        return YqmProjectToDTO.toYqmProjectDTO(entity);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    public String deleteById(String id) {
        YqmProject entity = iYqmProjectService.getById(id);
        if (Objects.isNull(entity)) {
            return id;
        }
        entity.setStatus(YqmDefine.StatusType.delete.getValue());
        this.save(YqmProjectToDTO.toYqmProjectRequest(entity));
        return id;
    }

    /**
     * 集合
     *
     * @param request
     * @return
     */
    public List<YqmProjectDTO> list(YqmProjectRequest request) {
        request.setInStatusList(YqmDefine.includeStatus);
        List<YqmProject> list = iYqmProjectService.list(iYqmProjectService.getQuery(request));
        return YqmProjectToDTO.toYqmProjectDTOList(list);
    }
}
