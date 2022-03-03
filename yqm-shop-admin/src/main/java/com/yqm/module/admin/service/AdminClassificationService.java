package com.yqm.module.admin.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yqm.common.conversion.YqmClassificationToDTO;
import com.yqm.common.define.YqmDefine;
import com.yqm.common.dto.YqmClassificationDTO;
import com.yqm.common.entity.YqmClassification;
import com.yqm.common.request.YqmClassificationRequest;
import com.yqm.common.service.IYqmClassificationService;
import com.yqm.security.User;
import com.yqm.security.UserInfoService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/**
 * 管理端-分类
 *
 * @Author: weiximei
 * @Date: 2022/2/28 20:01
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class AdminClassificationService {

	 private final IYqmClassificationService iYqmClassificationService;

	 public AdminClassificationService(IYqmClassificationService iYqmClassificationService) {
		  this.iYqmClassificationService = iYqmClassificationService;
	 }

	 /**
	  * 分页查询
	  *
	  * @param request
	  * @return
	  */
	 public IPage<YqmClassificationDTO> page(YqmClassificationRequest request) {
		  Page<YqmClassification> page = new Page<>();
		  page.setCurrent(request.getCurrent());
		  page.setSize(request.getPageSize());

		  request.setStatusList(YqmDefine.includeStatus);
		  IPage pageList = iYqmClassificationService.page(page, iYqmClassificationService.getQuery(request));
		  if (CollectionUtils.isNotEmpty(pageList.getRecords())) {
				pageList.setRecords(YqmClassificationToDTO.toYqmClassificationDTOList(pageList.getRecords()));
		  }
		  return pageList;
	 }

	 /**
	  * 详情
	  *
	  * @param id
	  * @return
	  */
	 public YqmClassificationDTO getById(String id) {
		  YqmClassification entity = iYqmClassificationService.getById(id);
		  return YqmClassificationToDTO.toYqmClassificationDTO(entity);
	 }

	 /**
	  * 保存/修改
	  *
	  * @param request
	  * @return
	  */
	 public YqmClassificationDTO save(YqmClassificationRequest request) {
		  User user = UserInfoService.getUser();
		  YqmClassification entity = YqmClassificationToDTO.toYqmClassification(request);
		  if (StringUtils.isEmpty(request.getId())) {
				entity.setCreatedTime(LocalDateTime.now());
				entity.setCreatedBy(user.getId());
		  }

		  entity.setUpdatedBy(user.getId());
		  entity.setUpdatedTime(LocalDateTime.now());
		  iYqmClassificationService.saveOrUpdate(entity);
		  return YqmClassificationToDTO.toYqmClassificationDTO(entity);
	 }

	 /**
	  * 是否显示
	  *
	  * @param request
	  * @return
	  */
	 public YqmClassificationDTO isShow(YqmClassificationRequest request) {
		  YqmClassification brand = iYqmClassificationService.getById(request.getId());
		  brand.setIsShow(request.getIsShow());
		  return this.save(YqmClassificationToDTO.toYqmClassificationRequest(brand));
	 }
}
