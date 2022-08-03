/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.activity.service.mapper;

import com.yqm.common.mapper.CoreMapper;
import com.yqm.modules.activity.domain.YqmStorePink;
import com.yqm.modules.activity.service.dto.PinkDto;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @author weiximei
* @date 2020-05-12
*/
@Repository
public interface YqmStorePinkMapper extends CoreMapper<YqmStorePink> {
    @Select("SELECT p.id,p.uid,p.people,p.price,p.stop_time as stopTime,u.nickname,u.avatar" +
            " FROM yqm_store_pink p INNER JOIN yqm_user u ON u.uid=p.uid" +
            " WHERE stop_time > now() AND p.cid = #{cid} AND p.k_id = 0 " +
            "AND p.is_refund = 0 ORDER BY p.create_time DESC")
    List<PinkDto> getPinks(Long cid);

    //<![CDATA[ >= ]]>
    @Select("SELECT p.id,u.nickname,u.avatar" +
            " FROM yqm_store_pink p RIGHT  JOIN yqm_user u ON u.uid=p.uid" +
            " where p.status= 2 AND p.uid <> ${uid} " +
            "AND p.is_refund = 0")
    List<PinkDto> getPinkOkList(Long uid);

    @Select("SELECT p.id,p.uid,p.people,p.price,p.stop_time as stopTime,u.nickname,u.avatar" +
            " FROM yqm_store_pink p LEFT JOIN yqm_user u ON u.uid=p.uid" +
            " where p.k_id= ${kid} " +
            "AND p.is_refund = 0")
    List<PinkDto> getPinkMember(int kid);

    @Select("SELECT p.id,p.uid,p.people,p.price,p.stop_time as stopTime,u.nickname,u.avatar" +
            " FROM yqm_store_pink p LEFT JOIN yqm_user u ON u.uid=p.uid" +
            " where p.id= ${id} ")
    PinkDto getPinkUserOne(int id);

    @Select("select IFNULL(sum(total_num),0) from yqm_store_pink " +
            "where status=2 and is_refund=0")
    int sumNum();
}
