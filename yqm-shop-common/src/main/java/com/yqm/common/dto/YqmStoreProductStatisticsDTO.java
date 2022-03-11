package com.yqm.common.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 商品统计
 * </p>
 *
 * @author weiximei
 * @since 2022-01-30
 */
@Builder
@Data
@EqualsAndHashCode(callSuper = false)
public class YqmStoreProductStatisticsDTO {

	 /**
	  * 总数
	  */
	 private Integer count;

	 /**
	  * 上架总数
	  */
	 private Integer shelvesCount;

	 /**
	  * 未上架总数
	  */
	 private Integer notShelvesCount;

}
