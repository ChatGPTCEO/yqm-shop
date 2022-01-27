package com.yqm.common.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.TpCompany;
import com.yqm.common.mapper.TpCompanyMapper;
import com.yqm.common.request.TpCompanyRequest;
import com.yqm.common.service.ITpCompanyService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* <p>
    * 公司 服务实现类
    * </p>
*
* @author weiximei
* @since 2021-10-16
*/
@Service
public class TpCompanyServiceImpl extends ServiceImpl<TpCompanyMapper, TpCompany> implements ITpCompanyService {

    @Override
    public QueryWrapper<TpCompany> queryWrapper(TpCompanyRequest request) {
        QueryWrapper<TpCompany> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(request.getUserId())) {
            queryWrapper.eq("user_id", request.getUserId());
        }
        return queryWrapper;
    }

    @Override
    public TpCompany getByUserId(String userId) {
        TpCompanyRequest request = new TpCompanyRequest();
        request.setUserId(userId);
        return this.getOne(this.queryWrapper(request));
    }
}
