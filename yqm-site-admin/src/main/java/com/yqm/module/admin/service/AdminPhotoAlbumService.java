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
import com.yqm.common.conversion.TpPhotoAlbumToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.TpPhotoAlbumDTO;
import com.yqm.common.entity.TpPhotoAlbum;
import com.yqm.common.entity.TpPhotoAlbumClassify;
import com.yqm.common.request.TpPhotoAlbumRequest;
import com.yqm.common.service.ITpPhotoAlbumClassifyService;
import com.yqm.common.service.ITpPhotoAlbumService;
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
 * @Author: weiximei
 * @Date: 2021/11/20 18:54
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class AdminPhotoAlbumService {


    private ITpPhotoAlbumService iTpPhotoAlbumService;
    private ITpPhotoAlbumClassifyService iTpPhotoAlbumClassifyService;


    public AdminPhotoAlbumService(ITpPhotoAlbumService iTpPhotoAlbumService, ITpPhotoAlbumClassifyService iTpPhotoAlbumClassifyService) {
        this.iTpPhotoAlbumService = iTpPhotoAlbumService;
        this.iTpPhotoAlbumClassifyService = iTpPhotoAlbumClassifyService;
    }


    /**
     * 保存/修改 相册
     *
     * @param request
     * @return
     */
    public TpPhotoAlbumDTO savePhotoAlbum(TpPhotoAlbumRequest request) {
        User user = UserInfoService.getUser();

        TpPhotoAlbum photoAlbum = TpPhotoAlbumToDTO.toTpPhotoAlbum(request);
        if (StringUtils.isEmpty(request.getId())) {
            photoAlbum.setCreateBy(user.getId());
            photoAlbum.setCreateTime(LocalDateTime.now());
        }
        TpPhotoAlbumClassify photoAlbumClassify = iTpPhotoAlbumClassifyService.getById(request.getPhotoAlbumClassifyId());
        if (Objects.isNull(photoAlbumClassify)) {
            TpPhotoAlbumClassify photoAlbumClassifyId = iTpPhotoAlbumClassifyService.getAllPictures(user.getId());
            photoAlbum.setPhotoAlbumClassifyId(photoAlbumClassifyId.getId());
        }

        photoAlbum.setUserId(user.getId());
        photoAlbum.setStatus(YqmDefine.StatusType.effective.getValue());
        photoAlbum.setUpdatedBy(user.getId());
        photoAlbum.setUpdatedTime(LocalDateTime.now());
        iTpPhotoAlbumService.saveOrUpdate(photoAlbum);

        return TpPhotoAlbumToDTO.toTpPhotoAlbumDTO(photoAlbum);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public TpPhotoAlbumDTO getById(String id) {
        TpPhotoAlbum photoAlbum = iTpPhotoAlbumService.getById(id);
        return TpPhotoAlbumToDTO.toTpPhotoAlbumDTO(photoAlbum);
    }

    /**
     * 删除相册
     *
     * @param id
     * @return
     */
    public String removePhotoAlbum(String id) {
        User user = UserInfoService.getUser();

        UpdateWrapper<TpPhotoAlbum> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", YqmDefine.StatusType.delete.getValue());
        updateWrapper.eq("id", id);
        updateWrapper.eq("user_id", user.getId());
        iTpPhotoAlbumService.update(updateWrapper);

        return id;
    }

    /**
     * 停用/启用
     *
     * @return
     */
    public String enablePhotoAlbum(TpPhotoAlbumRequest request) {
        User user = UserInfoService.getUser();

        if (!YqmDefine.includeStatus.contains(request.getStatus())) {
            log.error("操作异常->停用/启用相册错误->传入状态不正确！[id={},status={}]", request.getId(), request.getStatus());
            return request.getId();
        }

        TpPhotoAlbum photoAlbum = iTpPhotoAlbumService.getById(request.getId());
        if (Objects.isNull(photoAlbum)) {
            log.error("操作异常->停用/启用相册错误->数据未找到！[id={}]", request.getId());
            return request.getId();
        }
        if (YqmDefine.StatusType.delete.getValue().equals(photoAlbum.getStatus())) {
            log.error("操作异常->停用/启用相册错误->该信息已经被删除！[id={}]", request.getId());
            return request.getId();
        }

        UpdateWrapper<TpPhotoAlbum> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", request.getStatus());
        updateWrapper.eq("id", request.getId());
        updateWrapper.eq("user_id", user.getId());
        iTpPhotoAlbumService.update(updateWrapper);

        return request.getId();

    }

    /**
     * 分页查询 相册
     *
     * @param request
     * @return
     */
    public IPage<TpPhotoAlbumDTO> pagePhotoAlbum(TpPhotoAlbumRequest request) {
        User user = UserInfoService.getUser();
        Page<TpPhotoAlbum> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setUserId(user.getId());
        request.setIncludeStatus(Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        IPage pageList = iTpPhotoAlbumService.page(page, iTpPhotoAlbumService.queryWrapper(request));

        List list = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(list)) {
            pageList.setRecords(TpPhotoAlbumToDTO.toTpPhotoAlbumDTOList(list));
        }
        return pageList;
    }

    /**
     * 查询 相册
     *
     * @param request
     * @return
     */
    public List<TpPhotoAlbumDTO> listPhotoAlbum(TpPhotoAlbumRequest request) {
        User user = UserInfoService.getUser();
        List<TpPhotoAlbumDTO> photoAlbumDTOS = new ArrayList<>();

        request.setUserId(user.getId());
        request.setIncludeStatus(Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        List<TpPhotoAlbum> classifyList = iTpPhotoAlbumService.list(iTpPhotoAlbumService.queryWrapper(request));
        if (CollectionUtils.isNotEmpty(classifyList)) {
            photoAlbumDTOS = TpPhotoAlbumToDTO.toTpPhotoAlbumDTOList(classifyList);
        }
        return photoAlbumDTOS;
    }

}
