package com.yqm.common.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yqm.common.entity.TpPhotoAlbum;
import com.yqm.common.mapper.TpPhotoAlbumMapper;
import com.yqm.common.request.TpPhotoAlbumRequest;
import com.yqm.common.service.ITpPhotoAlbumService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 相册 服务实现类
 * </p>
 *
 * @author weiximei
 * @since 2021-11-20
 */
@Service
public class TpPhotoAlbumServiceImpl extends ServiceImpl<TpPhotoAlbumMapper, TpPhotoAlbum> implements ITpPhotoAlbumService {

    @Override
    public QueryWrapper<TpPhotoAlbum> queryWrapper(TpPhotoAlbumRequest request) {
        QueryWrapper<TpPhotoAlbum> queryWrapper = new QueryWrapper();
        //queryWrapper.orderByDesc("updated_time");
        if (CollectionUtils.isNotEmpty(request.getIncludeStatus())) {
            queryWrapper.in("status", request.getIncludeStatus());
        }
        if (StringUtils.isNotBlank(request.getUserId())) {
            queryWrapper.eq("user_id", request.getUserId());
        }
        if (StringUtils.isNotBlank(request.getPhotoAlbumName())) {
            queryWrapper.like("photo_album_name", request.getPhotoAlbumName());
        }
        return queryWrapper;
    }
}
