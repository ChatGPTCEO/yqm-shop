package com.yqm.event.site;

import com.yqm.security.event.LoginSuccessEvent;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

/**
 * 站点创建成功
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

    @Override
    public void onApplicationEvent(LoginSuccessEvent event) {
        log.info("处理站点创建成功事件...");
    }

}