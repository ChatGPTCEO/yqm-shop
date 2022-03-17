package com.yqm.event.attribute;

import com.yqm.common.dto.YqmStoreAttributeDTO;
import com.yqm.common.event.attribute.AttributeDeleteEvent;
import com.yqm.common.service.IYqmStoreTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 监听属性删除
 * @Author: weiximei
 * @Date: 2021/10/16 22:05
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Slf4j
@Component
public class AttributeDeleteListener  implements ApplicationListener<AttributeDeleteEvent> {


	 private final IYqmStoreTypeService storeTypeService;

	 public AttributeDeleteListener(IYqmStoreTypeService storeTypeService) {
		  this.storeTypeService = storeTypeService;
	 }

	 @Override
	 public void onApplicationEvent(AttributeDeleteEvent event) {
		  log.info("处理属性删除...");
	 	 YqmStoreAttributeDTO storeAttributeDTO = event.getStoreAttributeDTO();
	 	 if (Objects.nonNull(storeAttributeDTO)) {
			  storeTypeService.subtractionOneAttributeNum(storeAttributeDTO.getStoreTypeId());
		 }
	 }
}
