package com.yqm.module.admin;

import com.yqm.common.entity.TpRegion;
import com.yqm.common.request.TpRegionRequest;
import com.yqm.common.service.ITpRegionService;
import io.github.yedaxia.apidocs.Docs;
import io.github.yedaxia.apidocs.DocsConfig;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

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

    @Test
    void encodeSecret() {
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
        // 所有省
        List<TpRegion> list1 = iTpRegionService.getProvinces(0);
        for (TpRegion entity1 : list1) {
            entity1.setLevel(1);
            iTpRegionService.updateById(entity1);

            TpRegionRequest request = new TpRegionRequest();
            request.setPCode(entity1.getCode());
            // 所有市
            List<TpRegion> list2= iTpRegionService.list(iTpRegionService.queryWrapper(request));

            for (TpRegion entity2 : list2) {
                TpRegionRequest request2 = new TpRegionRequest();
                request2.setPCode(entity2.getCode());

                entity2.setLevel(2);
                iTpRegionService.updateById(entity2);

                // 所有区
                List<TpRegion> list3= iTpRegionService.list(iTpRegionService.queryWrapper(request2));

                for (TpRegion entity3 : list3) {
                    entity3.setLevel(3);
                    iTpRegionService.updateById(entity3);

                }
            }
        }
    }

}
