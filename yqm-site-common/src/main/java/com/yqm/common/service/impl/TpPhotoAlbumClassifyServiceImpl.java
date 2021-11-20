package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.TpLinkClassify;
import com.yqm.common.entity.TpPhotoAlbumClassify;
import com.yqm.common.mapper.TpPhotoAlbumClassifyMapper;
import com.yqm.common.request.TpPhotoAlbumClassifyRequest;
import com.yqm.common.service.ITpPhotoAlbumClassifyService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
* <p>
    * 相册分类 服务实现类
    * </p>
*
* @author weiximei
* @since 2021-11-14
*/
@Service
public class TpPhotoAlbumClassifyServiceImpl extends ServiceImpl<TpPhotoAlbumClassifyMapper, TpPhotoAlbumClassify> implements ITpPhotoAlbumClassifyService {
    @Override
    public QueryWrapper<TpPhotoAlbumClassify> queryWrapper(TpPhotoAlbumClassifyRequest request) {
        QueryWrapper<TpPhotoAlbumClassify> queryWrapper = new QueryWrapper();
        //queryWrapper.orderByDesc("updated_time");
        if (CollectionUtils.isNotEmpty(request.getIncludeStatus())) {
            queryWrapper.in("status", request.getIncludeStatus());
        }
        if (StringUtils.isNotBlank(request.getUserId())) {
            queryWrapper.eq("user_id", request.getUserId());
        }
        if (StringUtils.isNotBlank(request.getPid())) {
            queryWrapper.eq("pid", request.getPid());
        }
        return queryWrapper;
    }

    @Override
    public TpPhotoAlbumClassify getAllPictures(String userId) {
        TpPhotoAlbumClassifyRequest request = new TpPhotoAlbumClassifyRequest();
        request.setPid("-1");
        request.setUserId(userId);
        request.setPhotoAlbumClassifyName("全部图片");
        return this.getOne(this.queryWrapper(request));
    }
}
