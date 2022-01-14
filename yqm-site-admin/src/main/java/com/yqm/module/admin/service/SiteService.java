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

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.TpDomainInfoToDTO;
import com.yqm.common.conversion.TpSiteBingDomainToDTO;
import com.yqm.common.conversion.TpSiteToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.TpDomainDnsDTO;
import com.yqm.common.dto.TpDomainInfoDTO;
import com.yqm.common.dto.TpSiteBingDomainDTO;
import com.yqm.common.dto.TpSiteDTO;
import com.yqm.common.entity.TpSite;
import com.yqm.common.event.SiteCreateEvent;
import com.yqm.common.exception.YqmException;
import com.yqm.common.request.TpSiteRequest;
import com.yqm.common.service.ITpSiteService;
import com.yqm.common.utils.DateUtils;
import com.yqm.module.common.service.SysConfigService;
import com.yqm.security.User;
import com.yqm.security.UserInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 管理端-站点
 *
 * @Author: weiximei
 * @Date: 2021/11/28 16:32
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class SiteService {

    private final ITpSiteService iTpSiteService;
    private final SysConfigService sysConfigService;
    private final ApplicationContext applicationContext;

    public SiteService(ITpSiteService iTpSiteService, SysConfigService sysConfigService,
            ApplicationContext applicationContext) {
        this.iTpSiteService = iTpSiteService;
        this.sysConfigService = sysConfigService;
        this.applicationContext = applicationContext;
    }

    /**
     * 创建站点
     */
    public TpSiteDTO createSite() {
        User user = UserInfoService.getUser();
        TpSiteRequest siteRequest = new TpSiteRequest();
        siteRequest.setUserId(user.getId());
        siteRequest.setSiteName("我的站点");
        siteRequest.setDueTime(DateUtils.addMonth(LocalDateTime.now(), 1));
        TpSiteDTO siteDTO = this.saveSite(siteRequest);

        // 发送站点创建成功事件
        applicationContext.publishEvent(new SiteCreateEvent(siteDTO));
        return siteDTO;
    }

    /**
     * 保存/修改 站点
     *
     * @param request
     * @return
     */
    public TpSiteDTO saveSite(TpSiteRequest request) {
        User user = UserInfoService.getUser();

        TpSite site = TpSiteToDTO.toTpSite(request);
        if (Objects.isNull(request.getId())) {
            LocalDateTime nowDate = LocalDateTime.now();
            site.setCreateBy(user.getId());
            site.setCreateTime(nowDate);
            int maxSort = iTpSiteService.getMaxSort(user.getId());
            iTpSiteService.updateAllSortGal(maxSort, user.getId());
            site.setSort(1);
        }

        site.setUserId(user.getId());
        site.setStatus(YqmDefine.StatusType.effective.getValue());
        site.setUpdatedBy(user.getId());
        site.setUpdatedTime(LocalDateTime.now());
        iTpSiteService.saveOrUpdate(site);

        return TpSiteToDTO.toTpSiteDTO(site);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    public TpSiteDTO getById(String id) {
        TpSite site = iTpSiteService.getById(id);
        return TpSiteToDTO.toTpSiteDTO(site);
    }

    /**
     * 删除站点
     *
     * @param id
     * @return
     */
    public String removeSite(String id) {
        User user = UserInfoService.getUser();

        UpdateWrapper<TpSite> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", YqmDefine.StatusType.delete.getValue());
        updateWrapper.eq("id", id);
        updateWrapper.eq("user_id", user.getId());
        iTpSiteService.update(updateWrapper);

        return id;
    }

    /**
     * 停用/启用
     *
     * @return
     */
    public String enableSite(TpSiteRequest request) {
        User user = UserInfoService.getUser();

        if (!YqmDefine.includeStatus.contains(request.getStatus())) {
            log.error("操作异常->停用/启用站点错误->传入状态不正确！[id={},status={}]", request.getId(), request.getStatus());
            return request.getId();
        }

        TpSite site = iTpSiteService.getById(request.getId());
        if (Objects.isNull(site)) {
            log.error("操作异常->停用/启用站点错误->数据未找到！[id={}]", request.getId());
            return request.getId();
        }
        if (YqmDefine.StatusType.delete.getValue().equals(site.getStatus())) {
            log.error("操作异常->停用/启用站点错误->该信息已经被删除！[id={}]", request.getId());
            return request.getId();
        }

        UpdateWrapper<TpSite> updateWrapper = new UpdateWrapper<>();
        updateWrapper.set("status", request.getStatus());
        updateWrapper.eq("id", request.getId());
        updateWrapper.eq("user_id", user.getId());
        iTpSiteService.update(updateWrapper);

        return request.getId();

    }

    /**
     * 分页查询 站点
     *
     * @param request
     * @return
     */
    public IPage<TpSiteDTO> pageSite(TpSiteRequest request) {
        User user = UserInfoService.getUser();
        Page<TpSite> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setUserId(user.getId());
        request.setIncludeStatus(
                Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        IPage pageList = iTpSiteService.page(page, iTpSiteService.queryWrapper(request));

        List list = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(list)) {
            String sysDomain = sysConfigService.getSysCacheValue(YqmDefine.SysConfigType.domain.getValue());
            String sysPhone = sysConfigService.getSysCacheValue(YqmDefine.SysConfigType.sys_phone.getValue());
            List<TpSiteDTO> dtoList = TpSiteToDTO.toTpSiteDTOList(list);
            if (CollectionUtils.isNotEmpty(dtoList)) {
                dtoList.forEach(e -> {
                    // 系统域名
                    e.setSystemDomain(sysDomain + e.getId() + "/index");
                    e.setSysPhone(sysPhone);
                });
            }
            pageList.setRecords(dtoList);
        }
        return pageList;
    }

    /**
     * 查询 站点
     * 所有
     *
     * @param request
     * @return
     */
    public List<TpSiteBingDomainDTO> listSiteSelect(TpSiteRequest request) {
        User user = UserInfoService.getUser();
        List<TpSiteBingDomainDTO> siteDTOS = new ArrayList<>();

        request.setUserId(user.getId());
        request.setIncludeStatus(
                Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        List<TpSite> classifyList = iTpSiteService.list(iTpSiteService.queryWrapper(request));
        if (CollectionUtils.isNotEmpty(classifyList)) {
            siteDTOS = TpSiteBingDomainToDTO.toTpSiteDTOList(classifyList);
        }
        return siteDTOS;
    }

    /**
     * 置顶
     *
     * @param id
     * @return
     */
    public String top(String id) {
        User user = UserInfoService.getUser();

        TpSite tpSite = iTpSiteService.getById(id);
        if (Objects.nonNull(tpSite)) {
            iTpSiteService.updateAllSortGal(tpSite.getSort(), user.getId());
            iTpSiteService.top(id, user.getId());
        }

        return id;
    }

    /**
     * 查询绑定的域名
     *
     * @param request
     * @return
     */
    public IPage<TpSiteBingDomainDTO> getBingDomainSite(TpSiteRequest request) {
        User user = UserInfoService.getUser();
        Page<TpSite> page = new Page<>();
        page.setCurrent(request.getCurrent());
        page.setSize(request.getPageSize());

        request.setUserId(user.getId());
        request.setIncludeStatus(
                Arrays.asList(YqmDefine.StatusType.effective.getValue(), YqmDefine.StatusType.failure.getValue()));
        request.setIsNullDomain(Boolean.TRUE);
        IPage pageList = iTpSiteService.page(page, iTpSiteService.queryWrapper(request));

        List list = pageList.getRecords();
        if (CollectionUtils.isNotEmpty(list)) {
            List<TpSiteBingDomainDTO> dtoList = TpSiteBingDomainToDTO.toTpSiteDTOList(list);
            pageList.setRecords(dtoList);
        }
        return pageList;
    }

    /**
     * 绑定域名
     *
     * @param request
     * @return
     */
    public String bingDomainSite(TpSiteRequest request) {
        User user = UserInfoService.getUser();
        request.setUserId(user.getId());
        TpSite site = iTpSiteService.getOne(iTpSiteService.queryWrapper(request));
        if (Objects.nonNull(site)) {
            if (StringUtils.isNotBlank(site.getDomain())) {
                throw new YqmException("域名已存在");
            }
        } else {
            throw new YqmException("请选择站点");
        }

        site.setDomain(request.getDomain());
        iTpSiteService.saveOrUpdate(site);
        return request.getId();
    }

    /**
     * 移除域名
     *
     * @param id
     * @return
     */
    public String removeBingDomainSite(String id) {
        User user = UserInfoService.getUser();

        UpdateWrapper<TpSite> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", id);
        updateWrapper.eq("user_id", user.getId());
        updateWrapper.set("domain", null);
        iTpSiteService.update(updateWrapper);
        return id;
    }

    /**
     * 域名详情
     *
     * @param id
     * @return
     */
    public TpDomainInfoDTO domainInfo(String id) {
        if (StringUtils.isEmpty(id)) {
            return new TpDomainInfoDTO();
        }
        User user = UserInfoService.getUser();
        String sysPhone = sysConfigService.getSysCacheValue(YqmDefine.SysConfigType.sys_phone.getValue());

        String dnsValue = sysConfigService.getSysCacheValue(YqmDefine.SysConfigType.dns.getValue());
        TpDomainDnsDTO dnsDTO = JSON.parseObject(dnsValue, TpDomainDnsDTO.class);

        List<TpDomainDnsDTO> dnsDTOS = new ArrayList<>();

        TpSiteRequest request = new TpSiteRequest();
        request.setId(id);
        request.setUserId(user.getId());
        TpSite site = iTpSiteService.getOne(iTpSiteService.queryWrapper(request));
        TpDomainInfoDTO domainInfo = TpDomainInfoToDTO.toTpSiteDTO(site);
        domainInfo.setSysPhone(sysPhone);
        domainInfo.setDomainToDnsList(dnsDTOS);

        if (StringUtils.isNotEmpty(site.getDomain())) {
            if (site.getDomain().startsWith("www.")) {
                TpDomainDnsDTO wwwDnsDTO = new TpDomainDnsDTO();
                BeanUtil.copyProperties(dnsDTO, wwwDnsDTO);
                wwwDnsDTO.setDomain(site.getDomain());

                TpDomainDnsDTO notWwwDnsDTO = new TpDomainDnsDTO();
                BeanUtil.copyProperties(dnsDTO, notWwwDnsDTO);
                notWwwDnsDTO.setDomain(site.getDomain().replace("www.", ""));

                dnsDTOS.add(wwwDnsDTO);
                dnsDTOS.add(notWwwDnsDTO);
            } else {
                TpDomainDnsDTO wwwDnsDTO = new TpDomainDnsDTO();
                BeanUtil.copyProperties(dnsDTO, wwwDnsDTO);
                wwwDnsDTO.setDomain("www." + site.getDomain());

                TpDomainDnsDTO notWwwDnsDTO = new TpDomainDnsDTO();
                BeanUtil.copyProperties(dnsDTO, notWwwDnsDTO);
                notWwwDnsDTO.setDomain(site.getDomain());

                dnsDTOS.add(wwwDnsDTO);
                dnsDTOS.add(notWwwDnsDTO);
            }

        }

        return domainInfo;
    }

    /**
     * 修改域名相关信息
     *
     * @param request
     * @return
     */
    public TpDomainInfoDTO updateDomainInfo(TpSiteRequest request) {
        if (StringUtils.isEmpty(request.getDomain())) {
            throw new YqmException("此处无法删除域名，请返回上一级操作");
        }

        User user = UserInfoService.getUser();
        UpdateWrapper<TpSite> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("id", request.getId());
        updateWrapper.eq("user_id", user.getId());
        updateWrapper.set("icp", request.getIcp());
        updateWrapper.set("domain", request.getDomain());
        updateWrapper.set("security_icp", request.getSecurityIcp());
        updateWrapper.set("security_icp_url", request.getSecurityIcpUrl());

        iTpSiteService.update(updateWrapper);

        return this.domainInfo(request.getId());

    }
}
