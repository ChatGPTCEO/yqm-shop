package com.yqm.module.admin.service.polymerization;

import com.yqm.common.conversion.TpPagesToDTO;
import com.yqm.common.dto.TpPagesDTO;
import com.yqm.common.dto.TpSiteDTO;
import com.yqm.common.event.SiteCreateEvent;
import com.yqm.common.request.TpPagesRequest;
import com.yqm.common.request.TpSiteRequest;
import com.yqm.common.utils.DateUtils;
import com.yqm.module.admin.service.PagesService;
import com.yqm.module.admin.service.SiteService;
import com.yqm.module.service.PagesCommonService;
import com.yqm.security.User;
import com.yqm.security.UserInfoService;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class SitePolymerization {

    private final PagesService pagesService;
    private final PagesCommonService pagesCommonService;
    private final SiteService siteService;
    private final ApplicationContext applicationContext;

    public SitePolymerization(PagesService pagesService, ApplicationContext applicationContext,
                              SiteService siteService, PagesCommonService pagesCommonService) {
        this.pagesService = pagesService;
        this.applicationContext = applicationContext;
        this.siteService = siteService;
        this.pagesCommonService = pagesCommonService;
    }

    /**
     * 创建站点
     */
    public TpSiteDTO createSite() {
        User user = UserInfoService.getUser();
        TpSiteRequest siteRequest = new TpSiteRequest();
        siteRequest.setUserId(user.getId());
        siteRequest.setSiteName("我的站点");
        siteRequest.setDueTime(DateUtils.addMonth(LocalDateTime.now(), 1));
        TpSiteDTO siteDTO = siteService.saveSite(siteRequest);

        // 默认用户id
        String userId = "-1";

        TpPagesRequest requestPages = new TpPagesRequest();
        requestPages.setUserId(userId);
        List<TpPagesDTO> pagesDTOs = pagesCommonService.baseListPages(requestPages);
        pagesService.saveBachPages(TpPagesToDTO.toTpPagesRequestList(pagesDTOs));

        // 发送站点创建成功事件
        applicationContext.publishEvent(new SiteCreateEvent(siteDTO));
        return siteDTO;
    }

}
