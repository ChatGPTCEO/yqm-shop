/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.user.service.mapper;

import com.yqm.common.mapper.CoreMapper;
import com.yqm.modules.user.domain.YqmUserLevel;
import com.yqm.modules.user.service.dto.UserLevelInfoDto;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * 用户等级记录表 Mapper 接口
 * </p>
 *
 * @author weiximei
 * @since 2019-12-06
 */
@Repository
public interface YqmUserLevelMapper extends CoreMapper<YqmUserLevel> {

    @Select("SELECT l.id,a.add_time as addTime,l.discount,a.level_id as levelId,l.name," +
            "l.icon,l.grade FROM yqm_user_level a INNER JOIN yqm_system_user_level l " +
            "ON l.id=a.level_id WHERE a.status = 1 AND a.is_del = 0 AND a.id = #{id} LIMIT 1")
    UserLevelInfoDto getUserLevelInfo(int id);



}
