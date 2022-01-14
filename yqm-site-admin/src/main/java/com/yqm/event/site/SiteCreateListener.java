package com.yqm.event.site;

import java.util.List;

import com.yqm.common.conversion.TpPagesToDTO;
import com.yqm.common.conversion.TpSiteBingDomainToDTO;
import com.yqm.common.dto.TpPagesDTO;
import com.yqm.common.dto.TpPhotoAlbumClassifyDTO;
import com.yqm.common.request.TpPagesRequest;
import com.yqm.common.request.TpPhotoAlbumClassifyRequest;
import com.yqm.module.admin.service.PagesService;
import com.yqm.module.admin.service.PhotoAlbumClassifyService;
import com.yqm.security.event.LoginSuccessEvent;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * 站点创建成功 - 初始化页面
 *
 * @Author: weiximei
 * @Date: 2021/10/16 22:03
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Slf4j
@Component
@Transactional(rollbackFor = Exception.class)
public class SiteCreateListener implements ApplicationListener<LoginSuccessEvent> {

    private final PagesService pagesService;

    public SiteCreateListener(PagesService pagesService) {
        this.pagesService = pagesService;
    }

    @Override
    public void onApplicationEvent(LoginSuccessEvent event) {
        log.info("处理站点创建成功事件 -> 初始化页面");

        String userId = "-1";

        TpPagesRequest requestPages = new TpPagesRequest();
        requestPages.setUserId(userId);
        List<TpPagesDTO> pagesDTOs = pagesService.baseListPages(requestPages);
        pagesService.saveBachPages(TpPagesToDTO.toTpPagesRequestList(pagesDTOs));

    }

}
