package com.yqm;

import com.yqm.utils.SpringContextHolder;
import com.binarywang.spring.starter.wxjava.miniapp.config.WxMaAutoConfiguration;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author weiximei
 * @date 2019/10/1 9:20:19
 */
@EnableAsync
@EnableTransactionManagement
@EnableCaching
@MapperScan(basePackages ={"com.yqm.modules.*.service.mapper", "com.yqm.config"})
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class , WxMaAutoConfiguration.class})
public class ApiRun {

    public static void main(String[] args) {
        SpringApplication.run(ApiRun.class, args);

        System.out.println(
                   "                             _\n" +
                           "                            | |\n" +
                           " _   _  __ _ _ __ ___    ___| |__   ___  _ __\n" +
                           "| | | |/ _` | '_ ` _ \\  / __| '_ \\ / _ \\| '_ \\\n" +
                           "| |_| | (_| | | | | | | \\__ \\ | | | (_) | |_) |\n" +
                           " \\__, |\\__, |_| |_| |_| |___/_| |_|\\___/| .__/\n" +
                           "  __/ |   | |                           | |\n" +
                           " |___/    |_|                           |_|\n" +

                    "\n亦秋yqm-shop电商系统移动端API启动成功 \n官网：https://www.yqmshop.com 提供技术支持ﾞ  \n");
    }

    @Bean
    public SpringContextHolder springContextHolder() {
        return new SpringContextHolder();
    }
}
