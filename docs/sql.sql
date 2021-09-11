DROP TABLE IF EXISTS tp_user;
CREATE TABLE tp_user(
                        ID VARCHAR(32) NOT NULL AUTO_INCREMENT  COMMENT '编号;编号' ,
                        USER_NAME VARCHAR(255) NOT NULL   COMMENT '用户姓名;用户姓名' ,
                        SEX INT   DEFAULT 1 COMMENT '性别;性别: 0 女 1 男' ,
                        PHONE VARCHAR(255) NOT NULL   COMMENT '手机;用户手机号' ,
                        EMAIL VARCHAR(255) NOT NULL   COMMENT '邮箱;邮箱' ,
                        ADDRESS VARCHAR(255) NOT NULL   COMMENT '地址;详细地址' ,
                        POSTAL_CODE VARCHAR(255)    COMMENT '邮编;邮编' ,
                        ACCOUNT VARCHAR(255)    COMMENT '账号;账号' ,
                        PASSWORD VARCHAR(255)    COMMENT '密码;密码' ,
                        PROVINCE_ID VARCHAR(255) NOT NULL   COMMENT '省;省' ,
                        city_id VARCHAR(255) NOT NULL   COMMENT '市;市' ,
                        area_id VARCHAR(255) NOT NULL   COMMENT '区;区' ,
                        enterprise_certification INT    COMMENT '企业认证;企业认证: 0未认证 1 认证 2 认证审核中' ,
                        CREATED_BY VARCHAR(32) NOT NULL   COMMENT '创建人;创建人' ,
                        CREATED_TIME DATETIME NOT NULL  DEFAULT now() COMMENT '创建时间;创建时间' ,
                        UPDATED_BY VARCHAR(32) NOT NULL   COMMENT '更新人;更新人' ,
                        UPDATED_TIME DATETIME NOT NULL  DEFAULT now() COMMENT '更新时间;更新时间' ,
                        PRIMARY KEY (ID)
)  COMMENT = '用户表';


CREATE UNIQUE INDEX user_index ON tp_user(PHONE,PROVINCE_ID,city_id,area_id);

DROP TABLE IF EXISTS tp_company;
CREATE TABLE tp_company(
                           id VARCHAR(32) NOT NULL AUTO_INCREMENT  COMMENT '公司id;公司' ,
                           company VARCHAR(255)    COMMENT '公司名称;公司名称' ,
                           logo VARCHAR(1024)    COMMENT 'LOGO;LOGO' ,
                           legal_representative VARCHAR(255)    COMMENT '公司法人;公司法人' ,
                           legal_representative_phone VARCHAR(255)    COMMENT '法人电话' ,
                           established_time VARCHAR(255)    COMMENT '成立时间;成立时间' ,
                           address VARCHAR(255)    COMMENT '地址;地址' ,
                           POSTAL_CODE VARCHAR(255)    COMMENT '邮编;邮编' ,
                           fax VARCHAR(255)    COMMENT '传真;传真' ,
                           user_id VARCHAR(255)    COMMENT '用户id;用户id' ,
                           business_license VARCHAR(1024)    COMMENT '企业执照;企业执照' ,
                           introduce text    COMMENT '简介;简介' ,
                           CREATED_BY VARCHAR(32) NOT NULL   COMMENT '创建人' ,
                           CREATED_TIME DATETIME NOT NULL  DEFAULT now() COMMENT '创建时间' ,
                           UPDATED_BY VARCHAR(32) NOT NULL   COMMENT '更新人' ,
                           UPDATED_TIME DATETIME NOT NULL  DEFAULT now() COMMENT '更新时间' ,
                           PRIMARY KEY (id)
)  COMMENT = '公司';

DROP TABLE IF EXISTS tp_recruitment;
CREATE TABLE tp_recruitment(
                               id VARCHAR(32) NOT NULL AUTO_INCREMENT  COMMENT '编号;编号' ,
                               title VARCHAR(255)    COMMENT '标题;标题' ,
                               position VARCHAR(255)    COMMENT '职务;职务' ,
                               rec_num VARCHAR(255)    COMMENT '招聘人数;招聘人数' ,
                               status VARCHAR(255)    COMMENT '状态;状态: effective 有效 failure 失效 delete 删除' ,
                               sort INT    COMMENT '排序;排序' ,
                               responsibility text    COMMENT '职责;职责' ,
                               requirements text    COMMENT '要求;要求' ,
                               company_id VARCHAR(255) NOT NULL   COMMENT '公司id;公司id' ,
                               phone VARCHAR(255)    COMMENT '联系方式;联系方式' ,
                               user_name VARCHAR(255)    COMMENT '联系人;联系人' ,
                               CREATED_BY VARCHAR(32) NOT NULL   COMMENT '创建人' ,
                               CREATED_TIME DATETIME NOT NULL  DEFAULT now() COMMENT '创建时间' ,
                               UPDATED_BY VARCHAR(32) NOT NULL   COMMENT '更新人' ,
                               UPDATED_TIME DATETIME NOT NULL  DEFAULT now() COMMENT '更新时间' ,
                               PRIMARY KEY (id)
)  COMMENT = '招聘';

DROP TABLE IF EXISTS tp_link;
CREATE TABLE tp_link(
                        id VARCHAR(32) NOT NULL AUTO_INCREMENT  COMMENT '编号;编号' ,
                        url VARCHAR(1024)    COMMENT '链接地址;链接地址' ,
                        link_name VARCHAR(255)    COMMENT '链接名称;链接名称' ,
                        link_img VARCHAR(255)    COMMENT '链接图片;链接图片' ,
                        sort INT    COMMENT '排序;排序' ,
                        link_classify_id VARCHAR(32)    COMMENT '链接分类;链接分类' ,
                        company_id VARCHAR(32) NOT NULL   COMMENT '公司id;公司id' ,
                        CREATED_BY VARCHAR(32) NOT NULL   COMMENT '创建人' ,
                        CREATED_TIME DATETIME NOT NULL  DEFAULT now() COMMENT '创建时间' ,
                        UPDATED_BY VARCHAR(32) NOT NULL   COMMENT '更新人' ,
                        UPDATED_TIME DATETIME NOT NULL  DEFAULT now() COMMENT '更新时间' ,
                        PRIMARY KEY (id)
)  COMMENT = '友情链接';

DROP TABLE IF EXISTS tp_link_classify;
CREATE TABLE tp_link_classify(
                                 id VARCHAR(32) NOT NULL AUTO_INCREMENT  COMMENT '分类id;编号' ,
                                 link_classify_name VARCHAR(255)    COMMENT '分类名称' ,
                                 company_id VARCHAR(32) NOT NULL   COMMENT '公司id;公司id' ,
                                 CREATED_BY VARCHAR(32) NOT NULL   COMMENT '创建人' ,
                                 CREATED_TIME DATETIME NOT NULL  DEFAULT now() COMMENT '创建时间' ,
                                 UPDATED_BY VARCHAR(32) NOT NULL   COMMENT '更新人' ,
                                 UPDATED_TIME DATETIME NOT NULL  DEFAULT now() COMMENT '更新时间' ,
                                 PRIMARY KEY (id)
)  COMMENT = '链接分类';

DROP TABLE IF EXISTS tp_honor;
CREATE TABLE tp_honor(
                         id VARCHAR(32) NOT NULL AUTO_INCREMENT  COMMENT '编号;编号' ,
                         img VARCHAR(1024)    COMMENT '证书图片;证书图片' ,
                         honor_classify_id VARCHAR(32)    COMMENT '证书分类id;证书分类id' ,
                         sort INT    COMMENT '排序;排序' ,
                         company_id VARCHAR(32) NOT NULL   COMMENT '公司id;公司id' ,
                         CREATED_BY VARCHAR(32) NOT NULL   COMMENT '创建人' ,
                         CREATED_TIME DATETIME NOT NULL   COMMENT '创建时间' ,
                         UPDATED_BY VARCHAR(32) NOT NULL   COMMENT '更新人' ,
                         UPDATED_TIME DATETIME NOT NULL   COMMENT '更新时间' ,
                         PRIMARY KEY (id)
)  COMMENT = '荣誉证书';

DROP TABLE IF EXISTS tp_honor_classify;
CREATE TABLE tp_honor_classify(
                                  id VARCHAR(32)    COMMENT '编号;编号' ,
                                  honor_classify_name VARCHAR(255)    COMMENT '分类名称;分类名称' ,
                                  company_id VARCHAR(32) NOT NULL   COMMENT '公司id;公司id' ,
                                  CREATED_BY VARCHAR(32) NOT NULL   COMMENT '创建人' ,
                                  CREATED_TIME DATETIME NOT NULL  DEFAULT now() COMMENT '创建时间' ,
                                  UPDATED_BY VARCHAR(32) NOT NULL   COMMENT '更新人' ,
                                  UPDATED_TIME DATETIME NOT NULL  DEFAULT now() COMMENT '更新时间'
)  COMMENT = '荣誉分类';

DROP TABLE IF EXISTS tp_partners;
CREATE TABLE tp_partners(
                            id VARCHAR(32)    COMMENT '编辑;编辑' ,
                            partners_name VARCHAR(255)    COMMENT '合作伙伴名称;合作伙伴名称' ,
                            partners_classify_id VARCHAR(32)    COMMENT '合作伙伴分类;合作伙伴分类' ,
                            partners_address VARCHAR(1024)    COMMENT '合作伙伴地址;合作伙伴地址' ,
                            img VARCHAR(1024)    COMMENT '合作伙伴图片;合作伙伴图片' ,
                            sort INT    COMMENT '排序;排序' ,
                            company_id VARCHAR(32) NOT NULL   COMMENT '公司id;公司id' ,
                            CREATED_BY VARCHAR(32) NOT NULL   COMMENT '创建人' ,
                            CREATED_TIME DATETIME NOT NULL  DEFAULT now() COMMENT '创建时间' ,
                            UPDATED_BY VARCHAR(32) NOT NULL   COMMENT '更新人' ,
                            UPDATED_TIME DATETIME NOT NULL  DEFAULT now() COMMENT '更新时间'
)  COMMENT = '合作伙伴';

DROP TABLE IF EXISTS tp_team;
CREATE TABLE tp_team(
                        id VARCHAR(32)    COMMENT '编号;编号' ,
                        img VARCHAR(255)    COMMENT '团队图片;团队图片' ,
                        team_classify_id VARCHAR(255)    COMMENT '团队分类;团队分类' ,
                        sort INT    COMMENT '排序;排序' ,
                        company_id VARCHAR(255)    COMMENT '公司id;公司id' ,
                        CREATED_BY VARCHAR(32) NOT NULL   COMMENT '创建人' ,
                        CREATED_TIME DATETIME NOT NULL  DEFAULT now() COMMENT '创建时间' ,
                        UPDATED_BY VARCHAR(32) NOT NULL   COMMENT '更新人' ,
                        UPDATED_TIME DATETIME NOT NULL  DEFAULT now() COMMENT '更新时间'
)  COMMENT = '团队';
