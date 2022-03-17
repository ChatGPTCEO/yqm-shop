package com.yqm.common.event.attribute;

import com.yqm.common.dto.YqmStoreAttributeDTO;
import org.springframework.context.ApplicationEvent;

/**
 * 属性新增
 *
 * @Author: weiximei
 * @Date: 2021/10/16 23:38
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
public class AttributeAddEvent  extends ApplicationEvent {

	 private YqmStoreAttributeDTO storeAttributeDTO;

	 public AttributeAddEvent(YqmStoreAttributeDTO source) {
		  super(source);
		  this.storeAttributeDTO = source;
	 }

	 public YqmStoreAttributeDTO getStoreAttributeDTO() {
		  return storeAttributeDTO;
	 }
}
