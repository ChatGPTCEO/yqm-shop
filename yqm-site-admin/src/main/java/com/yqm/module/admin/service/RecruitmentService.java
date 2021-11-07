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
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.yqm.common.conversion.TpRecruitmentToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.TpCompanyDTO;
import com.yqm.common.dto.TpRecruitmentDTO;
import com.yqm.common.entity.TpRecruitment;
import com.yqm.common.exception.YqmException;
import com.yqm.common.mapper.TpRecruitmentMapper;
import com.yqm.common.request.TpRecruitmentRequest;
import com.yqm.common.service.ITpRecruitmentService;
import com.yqm.module.service.CommonService;
import com.yqm.security.User;
import com.yqm.security.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 管理端-招聘
 * @Author: weiximei
 * @Date: 2021/11/6 14:19
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Slf4j
@Service
public class RecruitmentService {

    private final CommonService commonService;
    private final ITpRecruitmentService recruitmentService;

    public RecruitmentService(CommonService commonService, ITpRecruitmentService recruitmentService) {
        this.commonService = commonService;
        this.recruitmentService = recruitmentService;
    }

    /**
     * 保存/修改 招聘
     * @param request
     * @return
     */
    public TpRecruitmentDTO saveRecruitment(TpRecruitmentRequest request) {
        User user = UserInfoService.getUser();

        TpRecruitment recruitment = TpRecruitmentToDTO.toTpRecruitment(request);
        recruitment.setUserId(user.getId());
        recruitment.setStatus(YqmDefine.StatusType.effective.getValue());

        if (StringUtils.isEmpty(recruitment.getId())) {
          recruitment.setCreatedBy(user.getId());
          recruitment.setCreatedTime(LocalDateTime.now());

          int maxSort = recruitmentService.getMaxSort(user.getId());
          recruitmentService.updateAllSortGal(maxSort,user.getId());
          recruitment.setSort(1);
        }

        recruitment.setUpdatedBy(user.getId());
        recruitment.setUpdatedTime(LocalDateTime.now());
        recruitmentService.saveOrUpdate(recruitment);

        return TpRecruitmentToDTO.toTpRecruitmentDTO(recruitment);
    }

    /**
     * 分页查询 招聘
     * @param request
     * @return
     */
    public IPage<TpRecruitmentDTO> pageRecruitment(TpRecruitmentRequest request) {
        Page<TpRecruitment> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setIncludeStatus(Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        IPage pageList = recruitmentService.page(page, recruitmentService.queryWrapper(request));

        List list = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(list)) {
            pageList.setRecords(TpRecruitmentToDTO.toTpRecruitmentDTOList(list));
        }
        return pageList;
    }

    /**
     * 根据id查询
     * @param id
     * @return
     */
    public TpRecruitmentDTO getById(String id) {
        TpRecruitment recruitment = recruitmentService.getById(id);
        return TpRecruitmentToDTO.toTpRecruitmentDTO(recruitment);
    }

    /**
     * 删除招聘
     * @param id
     * @return
     */
    public String removeRecruitment(String id) {
        User user = UserInfoService.getUser();

        UpdateWrapper<TpRecruitment> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", YqmDefine.StatusType.delete.getValue());
        updateWrapper.eq("id", id);
        updateWrapper.eq("user_id", user.getId());
        recruitmentService.update(updateWrapper);

        return id;
    }

    /**
     * 停用/启用
     * @return
     */
    public String enableRecruitment(TpRecruitmentRequest request) {
        User user = UserInfoService.getUser();

      if (!YqmDefine.includeStatus.contains(request.getStatus())) {
          log.error("操作异常->停用/启用招聘错误->传入状态不正确！[id={},status={}]", request.getId(), request.getStatus());
          return request.getId();
      }

      TpRecruitment recruitment = recruitmentService.getById(request.getId());
      if (Objects.isNull(recruitment)) {
          log.error("操作异常->停用/启用招聘错误->数据未找到！[id={}]", request.getId());
          return request.getId();
      }
      if (YqmDefine.StatusType.delete.getValue().equals(recruitment.getStatus())) {
          log.error("操作异常->停用/启用招聘错误->该招聘信息已经被删除！[id={}]", request.getId());
          return request.getId();
      }

      UpdateWrapper<TpRecruitment> updateWrapper = new UpdateWrapper<>();
      updateWrapper.set("status", request.getStatus());
      updateWrapper.eq("id", request.getId());
      updateWrapper.eq("user_id", user.getId());
      recruitmentService.update(updateWrapper);

      return request.getId();

    }
}
