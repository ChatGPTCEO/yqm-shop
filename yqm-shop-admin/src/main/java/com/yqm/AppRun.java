/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn
 * 注意：
 * 本软件为www.yqmshop.cn开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm;

import com.yqm.annotation.AnonymousAccess;
import com.yqm.utils.SpringContextHolder;
import com.binarywang.spring.starter.wxjava.miniapp.config.WxMaAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author weiximei
 * @date 2018/11/15 9:20:19
 */
@EnableAsync
@RestController
@SpringBootApplication(exclude = {WxMaAutoConfiguration.class})
@EnableTransactionManagement
@MapperScan(basePackages ={"com.yqm.modules.*.service.mapper", "com.yqm.config"})
public class AppRun {

    public static void main(String[] args) {
        SpringApplication.run(AppRun.class, args);
        System.out.println(
                    "                             _\n" +
                            "                            | |\n" +
                            " _   _  __ _ _ __ ___    ___| |__   ___  _ __\n" +
                            "| | | |/ _` | '_ ` _ \\  / __| '_ \\ / _ \\| '_ \\\n" +
                            "| |_| | (_| | | | | | | \\__ \\ | | | (_) | |_) |\n" +
                            " \\__, |\\__, |_| |_| |_| |___/_| |_|\\___/| .__/\n" +
                            "  __/ |   | |                           | |\n" +
                            " |___/    |_|                           |_|\n" +

                    "\n亦秋yqm-shop电商系统管理后台启动成功 \n官网：https://www.yqmshop.cn 提供技术支持ﾞ  \n");
    }

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }

    @Bean
    public ServletWebServerFactory webServerFactory() {
        TomcatServletWebServerFactory fa = new TomcatServletWebServerFactory();
        fa.addConnectorCustomizers(connector -> connector.setProperty("relaxedQueryChars", "[]{}"));
        return fa;
    }

    /**
     * 配置地址栏不能识别 // 的情况
     * @return
     */
    @Bean
    public HttpFirewall allowUrlEncodedSlashHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        //此处可添加别的规则,目前只设置 允许双 //
        firewall.setAllowUrlEncodedDoubleSlash(true);
        return firewall;
    }


    /**
     * 访问首页提示
     * @return /
     */
    @GetMapping("/")
    @AnonymousAccess
    public String index() {
        return "Backend service started successfully";
    }
}
