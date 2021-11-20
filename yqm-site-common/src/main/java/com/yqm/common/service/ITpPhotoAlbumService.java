package com.yqm.common.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.TpPhotoAlbum;
import com.yqm.common.request.TpPhotoAlbumRequest;

/**
* <p>
    * 相册 服务类
    * </p>
*
* @author weiximei
* @since 2021-11-20
*/
public interface ITpPhotoAlbumService extends IService<TpPhotoAlbum> {

    QueryWrapper<TpPhotoAlbum> queryWrapper(TpPhotoAlbumRequest request);

}
