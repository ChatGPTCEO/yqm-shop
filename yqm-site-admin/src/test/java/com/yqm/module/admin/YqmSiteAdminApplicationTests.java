package com.yqm.module.admin;

import com.yqm.common.service.ITpRegionService;
import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
@SpringBootTest
class YqmSiteAdminApplicationTests {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ITpRegionService iTpRegionService;

    @Test
    void contextLoads() {
        log.info("生成密码:{}", passwordEncoder.encode("123456"));
    }

    /**
     * 密匙加密
     */
    @Test
    void secretEncodeSecret() {
        log.info("生成密匙:{}", passwordEncoder.encode("29f5446f-b867-4417-9be4-5cae62ee3fc1"));
    }


    @Test
    void genDocs() {
        String rootSrc = System.getProperty("user.dir");
        log.info(rootSrc);

        String root = rootSrc.replace("\\yqm-site-admin", "");
        log.info(root);
        DocsConfig config = new DocsConfig();
        config.setProjectPath(rootSrc); // 项目根目录
        config.setProjectName("yqm-docs"); // 项目名称
        config.setApiVersion("V1.0");       // 声明该API的版本
        config.setDocsPath(root + "\\docs\\api"); // 生成API 文档所在目录
        config.setAutoGenerate(Boolean.TRUE);  // 配置自动生成
        Docs.buildHtmlDocs(config); // 执行生成文档
    }


    @Test
    public void region() {

    }

}
