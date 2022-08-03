/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.message.redis.config;

import com.yqm.modules.shop.domain.YqmSystemConfig;
import com.yqm.modules.shop.service.YqmSystemConfigService;
import com.yqm.utils.RedisUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * api服务启动初始化reids
 */
@Slf4j
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RedisKeyInitialization  implements CommandLineRunner {


    private final YqmSystemConfigService systemConfigService;


    private final RedisUtils redisUtils;


    /**
     * 初始化redis
     */
    private void redisKeyInitialization(){
        try {
            List<YqmSystemConfig> systemConfigs = systemConfigService.list();
            for (YqmSystemConfig systemConfig : systemConfigs) {
                redisUtils.set(systemConfig.getMenuName(),systemConfig.getValue());
            }

            log.info("---------------redisKey初始化成功---------------");
        }catch (Exception e){
            log.error("redisKey初始化失败: {}",e.getMessage());
        }

    }

    @Override
    public void run(String... args) throws Exception {
        this.redisKeyInitialization();
    }
}
