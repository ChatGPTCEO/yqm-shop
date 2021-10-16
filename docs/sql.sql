
CREATE TABLE SPRING_SESSION (
                                PRIMARY_ID CHAR(36) NOT NULL,
                                SESSION_ID CHAR(36) NOT NULL,
                                CREATION_TIME BIGINT NOT NULL,
                                LAST_ACCESS_TIME BIGINT NOT NULL,
                                MAX_INACTIVE_INTERVAL INT NOT NULL,
                                EXPIRY_TIME BIGINT NOT NULL,
                                PRINCIPAL_NAME VARCHAR(100),
                                CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

CREATE UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE SPRING_SESSION_ATTRIBUTES (
                                           SESSION_PRIMARY_ID CHAR(36) NOT NULL,
                                           ATTRIBUTE_NAME VARCHAR(200) NOT NULL,
                                           ATTRIBUTE_BYTES BLOB NOT NULL,
                                           CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
                                           CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION(PRIMARY_ID) ON DELETE CASCADE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

DROP TABLE IF EXISTS tp_user;
CREATE TABLE tp_user(
                        id VARCHAR(32) NOT NULL   COMMENT '编号;编号' ,
                        user_name VARCHAR(255) NOT NULL   COMMENT '用户姓名' ,
                        sex INT   DEFAULT 1 COMMENT '性别;0 未知 1 男 2 女' ,
                        phone VARCHAR(255) NOT NULL   COMMENT '手机' ,
                        email VARCHAR(255) NOT NULL   COMMENT '邮箱' ,
                        address VARCHAR(255) NOT NULL   COMMENT '地址;详细地址' ,
                        postal_code VARCHAR(255)    COMMENT '邮编' ,
                        account VARCHAR(255)    COMMENT '账号' ,
                        password VARCHAR(255)    COMMENT '密码' ,
                        province_id VARCHAR(255) NOT NULL   COMMENT '省' ,
                        city_id VARCHAR(255) NOT NULL   COMMENT '市' ,
                        area_id VARCHAR(255) NOT NULL   COMMENT '区' ,
                        enterprise_certification INT    COMMENT '企业认证;0未认证 1 认证 2 认证审核中' ,
                        status VARCHAR(255)    COMMENT '状态;effective 有效 failure 失效 delete 删除' ,
                        created_by VARCHAR(32) NOT NULL   COMMENT '创建人' ,
                        created_time DATETIME NOT NULL  DEFAULT now() COMMENT '创建时间' ,
                        updated_by VARCHAR(32) NOT NULL   COMMENT '更新人' ,
                        updated_time DATETIME NOT NULL  DEFAULT now() COMMENT '更新时间' ,
                        PRIMARY KEY (id)
)  COMMENT = '用户表';

DROP TABLE IF EXISTS tp_company;
CREATE TABLE tp_company(
                           id VARCHAR(32) NOT NULL   COMMENT '公司id' ,
                           company VARCHAR(255)    COMMENT '公司名称' ,
                           logo VARCHAR(1024)    COMMENT 'LOGO' ,
                           legal_representative VARCHAR(255)    COMMENT '公司法人' ,
                           legal_representative_phone VARCHAR(255)    COMMENT '法人电话' ,
                           established_time VARCHAR(255)    COMMENT '成立时间' ,
                           address VARCHAR(255)    COMMENT '地址' ,
                           postal_code VARCHAR(255)    COMMENT '邮编' ,
                           fax VARCHAR(255)    COMMENT '传真' ,
                           user_id VARCHAR(255)    COMMENT '用户id' ,
                           business_license VARCHAR(1024)    COMMENT '企业执照' ,
                           introduce text    COMMENT '简介' ,
                           created_by VARCHAR(32) NOT NULL   COMMENT '创建人' ,
                           created_time DATETIME NOT NULL  DEFAULT now() COMMENT '创建时间' ,
                           updated_by VARCHAR(32) NOT NULL   COMMENT '更新人' ,
                           updated_time DATETIME NOT NULL  DEFAULT now() COMMENT '更新时间' ,
                           PRIMARY KEY (id)
)  COMMENT = '公司';

DROP TABLE IF EXISTS tp_recruitment;
CREATE TABLE tp_recruitment(
                               id VARCHAR(32) NOT NULL   COMMENT '编号' ,
                               title VARCHAR(255)    COMMENT '标题' ,
                               position VARCHAR(255)    COMMENT '职务' ,
                               rec_num VARCHAR(255)    COMMENT '招聘人数' ,
                               status VARCHAR(255)    COMMENT '状态;状态: effective 有效 failure 失效 delete 删除' ,
                               sort INT    COMMENT '排序' ,
                               responsibility text    COMMENT '职责' ,
                               requirements text    COMMENT '要求' ,
                               company_id VARCHAR(255) NOT NULL   COMMENT '公司id' ,
                               phone VARCHAR(255)    COMMENT '联系方式' ,
                               user_name VARCHAR(255)    COMMENT '联系人' ,
                               created_by VARCHAR(32) NOT NULL   COMMENT '创建人' ,
                               created_time DATETIME NOT NULL  DEFAULT now() COMMENT '创建时间' ,
                               updated_by VARCHAR(32) NOT NULL   COMMENT '更新人' ,
                               updated_time DATETIME NOT NULL  DEFAULT now() COMMENT '更新时间' ,
                               PRIMARY KEY (id)
)  COMMENT = '招聘';

DROP TABLE IF EXISTS tp_link;
CREATE TABLE tp_link(
                        id VARCHAR(32) NOT NULL   COMMENT '编号' ,
                        url VARCHAR(1024)    COMMENT '链接地址' ,
                        link_name VARCHAR(255)    COMMENT '链接名称' ,
                        link_img VARCHAR(255)    COMMENT '链接图片' ,
                        sort INT    COMMENT '排序' ,
                        link_classify_id VARCHAR(32)    COMMENT '链接分类' ,
                        company_id VARCHAR(32) NOT NULL   COMMENT '公司id' ,
                        created_by VARCHAR(32) NOT NULL   COMMENT '创建人' ,
                        created_time DATETIME NOT NULL  DEFAULT now() COMMENT '创建时间' ,
                        updated_by VARCHAR(32) NOT NULL   COMMENT '更新人' ,
                        updated_time DATETIME NOT NULL  DEFAULT now() COMMENT '更新时间' ,
                        PRIMARY KEY (id)
)  COMMENT = '友情链接';

DROP TABLE IF EXISTS tp_link_classify;
CREATE TABLE tp_link_classify(
                                 id VARCHAR(32) NOT NULL   COMMENT '编号' ,
                                 link_classify_name VARCHAR(255)    COMMENT '分类名称' ,
                                 company_id VARCHAR(32) NOT NULL   COMMENT '公司id' ,
                                 created_by VARCHAR(32) NOT NULL   COMMENT '创建人' ,
                                 created_time DATETIME NOT NULL  DEFAULT now() COMMENT '创建时间' ,
                                 updated_by VARCHAR(32) NOT NULL   COMMENT '更新人' ,
                                 updated_time DATETIME NOT NULL  DEFAULT now() COMMENT '更新时间' ,
                                 PRIMARY KEY (id)
)  COMMENT = '链接分类';

DROP TABLE IF EXISTS tp_honor;
CREATE TABLE tp_honor(
                         id VARCHAR(32) NOT NULL   COMMENT '编号' ,
                         img VARCHAR(1024)    COMMENT '证书图片' ,
                         honor_classify_id VARCHAR(32)    COMMENT '证书分类id' ,
                         sort INT    COMMENT '排序' ,
                         company_id VARCHAR(32) NOT NULL   COMMENT '公司id' ,
                         created_by VARCHAR(32) NOT NULL   COMMENT '创建人' ,
                         created_time DATETIME NOT NULL   COMMENT '创建时间' ,
                         updated_by VARCHAR(32) NOT NULL   COMMENT '更新人' ,
                         updated_time DATETIME NOT NULL   COMMENT '更新时间' ,
                         PRIMARY KEY (id)
)  COMMENT = '荣誉证书';

DROP TABLE IF EXISTS tp_honor_classify;
CREATE TABLE tp_honor_classify(
                                  id VARCHAR(32) NOT NULL   COMMENT '编号' ,
                                  honor_classify_name VARCHAR(255)    COMMENT '分类名称' ,
                                  company_id VARCHAR(32) NOT NULL   COMMENT '公司id' ,
                                  created_by VARCHAR(32) NOT NULL   COMMENT '创建人' ,
                                  created_time DATETIME NOT NULL  DEFAULT now() COMMENT '创建时间' ,
                                  updated_by VARCHAR(32) NOT NULL   COMMENT '更新人' ,
                                  updated_time DATETIME NOT NULL  DEFAULT now() COMMENT '更新时间' ,
                                  PRIMARY KEY (id)
)  COMMENT = '荣誉分类';

DROP TABLE IF EXISTS tp_partners;
CREATE TABLE tp_partners(
                            id VARCHAR(32) NOT NULL   COMMENT '编号' ,
                            partners_name VARCHAR(255)    COMMENT '合作伙伴名称' ,
                            partners_classify_id VARCHAR(32)    COMMENT '合作伙伴分类' ,
                            partners_address VARCHAR(1024)    COMMENT '合作伙伴地址' ,
                            img VARCHAR(1024)    COMMENT '合作伙伴图片' ,
                            sort INT    COMMENT '排序' ,
                            company_id VARCHAR(32) NOT NULL   COMMENT '公司id' ,
                            created_by VARCHAR(32) NOT NULL   COMMENT '创建人' ,
                            created_time DATETIME NOT NULL  DEFAULT now() COMMENT '创建时间' ,
                            updated_by VARCHAR(32) NOT NULL   COMMENT '更新人' ,
                            updated_time DATETIME NOT NULL  DEFAULT now() COMMENT '更新时间' ,
                            PRIMARY KEY (id)
)  COMMENT = '合作伙伴';

DROP TABLE IF EXISTS tp_team;
CREATE TABLE tp_team(
                        id VARCHAR(32) NOT NULL   COMMENT '编号' ,
                        img VARCHAR(255)    COMMENT '团队图片' ,
                        team_classify_id VARCHAR(255)    COMMENT '团队分类' ,
                        sort INT    COMMENT '排序' ,
                        company_id VARCHAR(255)    COMMENT '公司id' ,
                        created_by VARCHAR(32) NOT NULL   COMMENT '创建人' ,
                        created_time DATETIME NOT NULL  DEFAULT now() COMMENT '创建时间' ,
                        updated_by VARCHAR(32) NOT NULL   COMMENT '更新人' ,
                        updated_time DATETIME NOT NULL  DEFAULT now() COMMENT '更新时间' ,
                        PRIMARY KEY (id)
)  COMMENT = '团队';


