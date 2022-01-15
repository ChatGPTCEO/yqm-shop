/*
 * Copyright 2021 Wei xi mei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *
 * distributed under the License is distributed on an "AS IS" BASIS,
 *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 *
 * limitations under the License.
 */

package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.TpPhotoAlbumClassifyToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.TpPhotoAlbumClassifyDTO;
import com.yqm.common.entity.TpPhotoAlbumClassify;
import com.yqm.common.request.TpPhotoAlbumClassifyRequest;
import com.yqm.common.service.ITpPhotoAlbumClassifyService;
import com.yqm.security.User;
import com.yqm.security.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 管理端-相册分类
 *
 * @Author: weiximei
 * @Date: 2021/11/7 17:30
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class AdminPhotoAlbumClassifyService {

    private ITpPhotoAlbumClassifyService iTpPhotoAlbumClassifyService;

    public AdminPhotoAlbumClassifyService(ITpPhotoAlbumClassifyService iTpPhotoAlbumClassifyService) {
        this.iTpPhotoAlbumClassifyService = iTpPhotoAlbumClassifyService;
    }

    /**
     * 保存/修改 相册分类
     *
     * @param request
     * @return
     */
    public TpPhotoAlbumClassifyDTO savePhotoAlbumClassify(TpPhotoAlbumClassifyRequest request) {
        User user = UserInfoService.getUser();

        TpPhotoAlbumClassify linkClassify = TpPhotoAlbumClassifyToDTO.toTpPhotoAlbumClassify(request);
        if (StringUtils.isEmpty(request.getId())) {
            linkClassify.setCreateBy(user.getId());
            linkClassify.setCreateTime(LocalDateTime.now());
        }

        if (CollectionUtils.isNotEmpty(request.getPidsList())) {
            List<String> pidsList = request.getPidsList();
            String pid = pidsList.get(pidsList.size() - 1);

            linkClassify.setPid(pid);
            linkClassify.setPids(String.join(",", pidsList));
        } else {
            List<TpPhotoAlbumClassifyDTO> dtoList = listPhotoAlbumClassify("-1");
            if (CollectionUtils.isNotEmpty(dtoList)) {
                linkClassify.setPid(dtoList.get(0).getId());
                linkClassify.setPids(dtoList.get(0).getId());
            }

        }

        linkClassify.setUserId(user.getId());
        linkClassify.setStatus(YqmDefine.StatusType.effective.getValue());
        linkClassify.setUpdatedBy(user.getId());
        linkClassify.setUpdatedTime(LocalDateTime.now());
        iTpPhotoAlbumClassifyService.saveOrUpdate(linkClassify);

        return TpPhotoAlbumClassifyToDTO.toTpPhotoAlbumClassifyDTO(linkClassify);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public TpPhotoAlbumClassifyDTO getById(String id) {
        TpPhotoAlbumClassify linkClassify = iTpPhotoAlbumClassifyService.getById(id);
        return TpPhotoAlbumClassifyToDTO.toTpPhotoAlbumClassifyDTO(linkClassify);
    }

    /**
     * 删除相册分类
     *
     * @param id
     * @return
     */
    public String removePhotoAlbumClassify(String id) {
        User user = UserInfoService.getUser();

        UpdateWrapper<TpPhotoAlbumClassify> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", YqmDefine.StatusType.delete.getValue());
        updateWrapper.eq("id", id);
        updateWrapper.eq("user_id", user.getId());
        iTpPhotoAlbumClassifyService.update(updateWrapper);

        return id;
    }

    /**
     * 停用/启用
     *
     * @return
     */
    public String enablePhotoAlbumClassify(TpPhotoAlbumClassifyRequest request) {
        User user = UserInfoService.getUser();

        if (!YqmDefine.includeStatus.contains(request.getStatus())) {
            log.error("操作异常->停用/启用相册分类错误->传入状态不正确！[id={},status={}]", request.getId(), request.getStatus());
            return request.getId();
        }

        TpPhotoAlbumClassify linkClassify = iTpPhotoAlbumClassifyService.getById(request.getId());
        if (Objects.isNull(linkClassify)) {
            log.error("操作异常->停用/启用相册分类错误->数据未找到！[id={}]", request.getId());
            return request.getId();
        }
        if (YqmDefine.StatusType.delete.getValue().equals(linkClassify.getStatus())) {
            log.error("操作异常->停用/启用相册分类错误->该信息已经被删除！[id={}]", request.getId());
            return request.getId();
        }

        UpdateWrapper<TpPhotoAlbumClassify> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", request.getStatus());
        updateWrapper.eq("id", request.getId());
        updateWrapper.eq("user_id", user.getId());
        iTpPhotoAlbumClassifyService.update(updateWrapper);

        return request.getId();

    }

    /**
     * 分页查询 相册分类
     *
     * @param request
     * @return
     */
    public IPage<TpPhotoAlbumClassifyDTO> pagePhotoAlbumClassify(TpPhotoAlbumClassifyRequest request) {
        User user = UserInfoService.getUser();
        Page<TpPhotoAlbumClassify> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setUserId(user.getId());
        request.setIncludeStatus(Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        IPage pageList = iTpPhotoAlbumClassifyService.page(page, iTpPhotoAlbumClassifyService.queryWrapper(request));

        List list = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(list)) {
            pageList.setRecords(TpPhotoAlbumClassifyToDTO.toTpPhotoAlbumClassifyDTOList(list));
        }
        return pageList;
    }

    /**
     * 查询 相册分类
     *
     * @param request
     * @return
     */
    public List<TpPhotoAlbumClassifyDTO> listPhotoAlbumClassify(TpPhotoAlbumClassifyRequest request) {
        User user = UserInfoService.getUser();
        List<TpPhotoAlbumClassifyDTO> linkClassifyDTOS = new ArrayList<>();

        request.setUserId(user.getId());
        request.setIncludeStatus(Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        List<TpPhotoAlbumClassify> classifyList = iTpPhotoAlbumClassifyService.list(iTpPhotoAlbumClassifyService.queryWrapper(request));
        if (CollectionUtils.isNotEmpty(classifyList)) {
            linkClassifyDTOS = TpPhotoAlbumClassifyToDTO.toTpPhotoAlbumClassifyDTOList(classifyList);
        }
        return linkClassifyDTOS;
    }

    /**
     * 根据pid查询 相册分类
     *
     * @param pid
     * @return
     */
    public List<TpPhotoAlbumClassifyDTO> listPhotoAlbumClassify(String pid) {
        User user = UserInfoService.getUser();
        TpPhotoAlbumClassifyRequest request = new TpPhotoAlbumClassifyRequest();
        request.setPid(pid);
        request.setUserId(user.getId());
        return TpPhotoAlbumClassifyToDTO.toTpPhotoAlbumClassifyDTOList(TpPhotoAlbumClassifyToDTO.toTpPhotoAlbumClassifyList(this.listPhotoAlbumClassify(request)));
    }

    /**
     * 查询 相册分类
     *
     * @return
     */
    public List<TpPhotoAlbumClassifyDTO> getPhotoAlbumClassify() {
        List<TpPhotoAlbumClassifyDTO> dtoList = this.listPhotoAlbumClassify("-1");
        for (TpPhotoAlbumClassifyDTO dto : dtoList) {
            List<TpPhotoAlbumClassifyDTO> DtoList2 = this.listPhotoAlbumClassify(dto.getId());
            dto.setChildren(DtoList2);
            if (CollectionUtils.isEmpty(DtoList2)) {
                continue;
            }
            for (TpPhotoAlbumClassifyDTO dto3 : DtoList2) {
                List<TpPhotoAlbumClassifyDTO> areaDtoList = this.listPhotoAlbumClassify(dto3.getId());
                dto3.setChildren(areaDtoList);
            }

        }
        return dtoList;
    }

}
