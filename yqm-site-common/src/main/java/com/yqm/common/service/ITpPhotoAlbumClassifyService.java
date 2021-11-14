package com.yqm.common.service;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.yqm.common.entity.TpPhotoAlbumClassify;
import com.yqm.common.request.TpPhotoAlbumClassifyRequest;

/**
* <p>
    * 相册分类 服务类
    * </p>
*
* @author weiximei
* @since 2021-11-14
*/
public interface ITpPhotoAlbumClassifyService extends IService<TpPhotoAlbumClassify> {

    QueryWrapper<TpPhotoAlbumClassify> queryWrapper(TpPhotoAlbumClassifyRequest request);

}
