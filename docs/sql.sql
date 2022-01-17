CREATE TABLE SPRING_SESSION
(
    PRIMARY_ID            CHAR(36) NOT NULL,
    SESSION_ID            CHAR(36) NOT NULL,
    CREATION_TIME         BIGINT   NOT NULL,
    LAST_ACCESS_TIME      BIGINT   NOT NULL,
    MAX_INACTIVE_INTERVAL INT      NOT NULL,
    EXPIRY_TIME           BIGINT   NOT NULL,
    PRINCIPAL_NAME        VARCHAR(100),
    CONSTRAINT SPRING_SESSION_PK PRIMARY KEY (PRIMARY_ID)
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

CREATE
UNIQUE INDEX SPRING_SESSION_IX1 ON SPRING_SESSION (SESSION_ID);
CREATE
INDEX SPRING_SESSION_IX2 ON SPRING_SESSION (EXPIRY_TIME);
CREATE
INDEX SPRING_SESSION_IX3 ON SPRING_SESSION (PRINCIPAL_NAME);

CREATE TABLE SPRING_SESSION_ATTRIBUTES
(
    SESSION_PRIMARY_ID CHAR(36)     NOT NULL,
    ATTRIBUTE_NAME     VARCHAR(200) NOT NULL,
    ATTRIBUTE_BYTES    BLOB         NOT NULL,
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_PK PRIMARY KEY (SESSION_PRIMARY_ID, ATTRIBUTE_NAME),
    CONSTRAINT SPRING_SESSION_ATTRIBUTES_FK FOREIGN KEY (SESSION_PRIMARY_ID) REFERENCES SPRING_SESSION (PRIMARY_ID) ON DELETE CASCADE
) ENGINE=InnoDB ROW_FORMAT=DYNAMIC;

DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token`
(
    `token_id`          varchar(255) DEFAULT NULL COMMENT '加密的access_token的值',
    `token`             longblob COMMENT 'OAuth2AccessToken.java对象序列化后的二进制数据',
    `authentication_id` varchar(255) DEFAULT NULL COMMENT '加密过的username,client_id,scope',
    `user_name`         varchar(255) DEFAULT NULL COMMENT '登录的用户名',
    `client_id`         varchar(255) DEFAULT NULL COMMENT '客户端ID',
    `authentication`    longblob COMMENT 'OAuth2Authentication.java对象序列化后的二进制数据',
    `refresh_token`     varchar(255) DEFAULT NULL COMMENT '加密的refresh_token的值'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '访问令牌';

DROP TABLE IF EXISTS `oauth_approvals`;
CREATE TABLE `oauth_approvals`
(
    `userId`         varchar(255) DEFAULT NULL COMMENT '登录的用户名',
    `clientId`       varchar(255) DEFAULT NULL COMMENT '客户端ID',
    `scope`          varchar(255) DEFAULT NULL COMMENT '申请的权限范围',
    `status`         varchar(10)  DEFAULT NULL COMMENT '状态（Approve或Deny）',
    `expiresAt`      datetime     DEFAULT NULL COMMENT '过期时间',
    `lastModifiedAt` datetime     DEFAULT NULL COMMENT '最终修改时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '授权记录';

DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`
(
    `client_id`               varchar(255) NOT NULL COMMENT '客户端ID',
    `resource_ids`            varchar(255) DEFAULT NULL COMMENT '资源ID集合,多个资源时用逗号(,)分隔',
    `client_secret`           varchar(255) DEFAULT NULL COMMENT '客户端密匙',
    `scope`                   varchar(255) DEFAULT NULL COMMENT '客户端申请的权限范围',
    `authorized_grant_types`  varchar(255) DEFAULT NULL COMMENT '客户端支持的grant_type',
    `web_server_redirect_uri` varchar(255) DEFAULT NULL COMMENT '重定向URI',
    `authorities`             varchar(255) DEFAULT NULL COMMENT '客户端所拥有的Spring Security的权限值，多个用逗号(,)分隔',
    `access_token_validity`   int(11) DEFAULT NULL COMMENT '访问令牌有效时间值(单位:秒)',
    `refresh_token_validity`  int(11) DEFAULT NULL COMMENT '更新令牌有效时间值(单位:秒)',
    `additional_information`  varchar(255) DEFAULT NULL COMMENT '预留字段',
    `autoapprove`             varchar(255) DEFAULT NULL COMMENT '用户是否自动Approval操作'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '客户端用来记录token信息';

DROP TABLE IF EXISTS `oauth_client_token`;
CREATE TABLE `oauth_client_token`
(
    `token_id`          varchar(255) DEFAULT NULL COMMENT '加密的access_token值',
    `token`             longblob COMMENT 'OAuth2AccessToken.java对象序列化后的二进制数据',
    `authentication_id` varchar(255) DEFAULT NULL COMMENT '加密过的username,client_id,scope',
    `user_name`         varchar(255) DEFAULT NULL COMMENT '登录的用户名',
    `client_id`         varchar(255) DEFAULT NULL COMMENT '客户端ID'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '客户端信息';

DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code`
(
    `code`           varchar(255) DEFAULT NULL COMMENT '授权码(未加密)',
    `authentication` varbinary(255) DEFAULT NULL COMMENT 'AuthorizationRequestHolder.java对象序列化后的二进制数据'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT = '授权码';

DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE `oauth_refresh_token`
(
    `token_id`       varchar(255) DEFAULT NULL COMMENT '加密过的refresh_token的值',
    `token`          longblob COMMENT 'OAuth2RefreshToken.java对象序列化后的二进制数据 ',
    `authentication` longblob COMMENT 'OAuth2Authentication.java对象序列化后的二进制数据'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT="更新令牌";



DROP TABLE IF EXISTS tp_user;
CREATE TABLE tp_user
(
    id                       VARCHAR(32)  NOT NULL COMMENT '编号;编号',
    user_name                VARCHAR(255) NOT NULL COMMENT '用户姓名',
    sex                      INT                   DEFAULT 1 COMMENT '性别;0 未知 1 男 2 女',
    phone                    VARCHAR(255) NOT NULL COMMENT '手机',
    email                    VARCHAR(255) NOT NULL COMMENT '邮箱',
    address                  VARCHAR(255) NOT NULL COMMENT '地址;详细地址',
    postal_code              VARCHAR(255) COMMENT '邮编',
    account                  VARCHAR(255) COMMENT '账号',
    password                 VARCHAR(255) COMMENT '密码',
    province_id              VARCHAR(255) NOT NULL COMMENT '省',
    city_id                  VARCHAR(255) NOT NULL COMMENT '市',
    area_id                  VARCHAR(255) NOT NULL COMMENT '区',
    enterprise_certification INT COMMENT '企业认证;0未认证 1 认证 2 认证审核中',
    status                   VARCHAR(255) COMMENT '状态;effective 有效 failure 失效 delete 删除',
    created_by               VARCHAR(32)  NOT NULL COMMENT '创建人',
    created_time             DATETIME     NOT NULL DEFAULT now() COMMENT '创建时间',
    updated_by               VARCHAR(32)  NOT NULL COMMENT '更新人',
    updated_time             DATETIME     NOT NULL DEFAULT now() COMMENT '更新时间',
    `avatar`                 varchar(1024)         DEFAULT NULL COMMENT '头像',
    PRIMARY KEY (id)
) COMMENT = '用户表';

DROP TABLE IF EXISTS tp_company;
CREATE TABLE tp_company
(
    id                         VARCHAR(32) NOT NULL COMMENT '公司id',
    company                    VARCHAR(255) COMMENT '公司名称',
    logo                       VARCHAR(1024) COMMENT 'LOGO',
    legal_representative       VARCHAR(255) COMMENT '公司法人',
    legal_representative_phone VARCHAR(255) COMMENT '法人电话',
    established_time           VARCHAR(255) COMMENT '成立时间',
    address                    VARCHAR(255) COMMENT '地址',
    postal_code                VARCHAR(255) COMMENT '邮编',
    fax                        VARCHAR(255) COMMENT '传真',
    user_id                    VARCHAR(255) COMMENT '用户id',
    business_license           VARCHAR(1024) COMMENT '企业执照',
    introduce                  text COMMENT '简介',
    phone                      VARCHAR(255) COMMENT '公司联系电话',
    created_by                 VARCHAR(32) NOT NULL COMMENT '创建人',
    created_time               DATETIME    NOT NULL DEFAULT now() COMMENT '创建时间',
    updated_by                 VARCHAR(32) NOT NULL COMMENT '更新人',
    updated_time               DATETIME    NOT NULL DEFAULT now() COMMENT '更新时间',
    business_personnel_name    VARCHAR(255) COMMENT '业务人员名称',
    business_personnel_phone   VARCHAR(255) COMMENT '业务人员手机',
    business_personnel_email   VARCHAR(255) COMMENT '业务人员邮件',
    business_personnel_sex     VARCHAR(255) COMMENT '性别',
    business_personnel_post    VARCHAR(255) COMMENT '职务',
    PRIMARY KEY (id)
) COMMENT = '公司';

DROP TABLE IF EXISTS tp_recruitment;
CREATE TABLE tp_recruitment
(
    id             VARCHAR(32) NOT NULL COMMENT '编号',
    title          VARCHAR(255) COMMENT '标题',
    position       VARCHAR(255) COMMENT '职务',
    rec_num        VARCHAR(255) COMMENT '招聘人数',
    status         VARCHAR(255) COMMENT '状态;状态: effective 有效 failure 失效 delete 删除',
    sort           INT                  DEFAULT 1 COMMENT '排序',
    responsibility text COMMENT '职责',
    requirements   text COMMENT '要求',
    company_id     VARCHAR(255) COMMENT '公司id',
    user_id        VARCHAR(32) NOT NULL COMMENT '用户id',
    phone          VARCHAR(255) COMMENT '联系方式',
    user_name      VARCHAR(255) COMMENT '联系人',
    created_by     VARCHAR(32) NOT NULL COMMENT '创建人',
    created_time   DATETIME    NOT NULL DEFAULT now() COMMENT '创建时间',
    updated_by     VARCHAR(32) NOT NULL COMMENT '更新人',
    updated_time   DATETIME    NOT NULL DEFAULT now() COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT = '招聘';

DROP TABLE IF EXISTS tp_link;
CREATE TABLE tp_link
(
    id               VARCHAR(32)  NOT NULL COMMENT '编号',
    url              VARCHAR(1024) COMMENT '链接地址',
    link_name        VARCHAR(255) COMMENT '链接名称',
    link_img         VARCHAR(255) COMMENT '链接图片',
    sort             INT                   DEFAULT 1 COMMENT '排序',
    link_classify_id VARCHAR(32) COMMENT '链接分类',
    company_id       VARCHAR(32) COMMENT '公司id',
    user_id          VARCHAR(32)  NOT NULL COMMENT '用户id',
    status           VARCHAR(255) NOT NULL DEFAULT 'effective' COMMENT '状态;状态: effective 有效 failure 失效 delete 删除',
    created_by       VARCHAR(32)  NOT NULL COMMENT '创建人',
    created_time     DATETIME     NOT NULL DEFAULT now() COMMENT '创建时间',
    updated_by       VARCHAR(32)  NOT NULL COMMENT '更新人',
    updated_time     DATETIME     NOT NULL DEFAULT now() COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT = '友情链接';


DROP TABLE IF EXISTS tp_link_classify;
CREATE TABLE tp_link_classify
(
    id                 VARCHAR(32)  NOT NULL COMMENT '编号',
    link_classify_name VARCHAR(255) COMMENT '分类名称',
    company_id         VARCHAR(32) COMMENT '公司id',
    user_id            VARCHAR(32)  NOT NULL COMMENT '用户id',
    status             VARCHAR(255) NOT NULL DEFAULT 'effective' COMMENT '状态;状态: effective 有效 failure 失效 delete 删除',
    created_by         VARCHAR(32)  NOT NULL COMMENT '创建人',
    created_time       DATETIME     NOT NULL DEFAULT now() COMMENT '创建时间',
    updated_by         VARCHAR(32)  NOT NULL COMMENT '更新人',
    updated_time       DATETIME     NOT NULL DEFAULT now() COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT = '链接分类';

DROP TABLE IF EXISTS tp_honor;
CREATE TABLE tp_honor
(
    id                VARCHAR(32)  NOT NULL COMMENT '编号',
    honor_name        VARCHAR(255) COMMENT '证书名称',
    honor_img         VARCHAR(1024) COMMENT '证书图片',
    honor_classify_id VARCHAR(32) COMMENT '证书分类id',
    sort              INT                   DEFAULT 1 COMMENT '排序',
    company_id        VARCHAR(32) COMMENT '公司id',
    user_id           VARCHAR(32)  NOT NULL COMMENT '用户id',
    status            VARCHAR(255) NOT NULL DEFAULT 'effective' COMMENT '状态;状态: effective 有效 failure 失效 delete 删除',
    created_by        VARCHAR(32)  NOT NULL COMMENT '创建人',
    created_time      DATETIME     NOT NULL COMMENT '创建时间',
    updated_by        VARCHAR(32)  NOT NULL COMMENT '更新人',
    updated_time      DATETIME     NOT NULL COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT = '荣誉证书';

DROP TABLE IF EXISTS tp_honor_classify;
CREATE TABLE tp_honor_classify
(
    id                  VARCHAR(32)  NOT NULL COMMENT '编号',
    honor_classify_name VARCHAR(255) COMMENT '分类名称',
    company_id          VARCHAR(32) COMMENT '公司id',
    user_id             VARCHAR(255) NOT NULL COMMENT '用户id',
    `status`            varchar(255) NOT NULL DEFAULT 'effective' COMMENT '状态;状态: effective 有效 failure 失效 delete 删除',
    created_by          VARCHAR(32)  NOT NULL COMMENT '创建人',
    created_time        DATETIME     NOT NULL DEFAULT now() COMMENT '创建时间',
    updated_by          VARCHAR(32)  NOT NULL COMMENT '更新人',
    updated_time        DATETIME     NOT NULL DEFAULT now() COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT = '荣誉分类';

DROP TABLE IF EXISTS tp_partners;
CREATE TABLE tp_partners
(
    id                   VARCHAR(32) NOT NULL COMMENT '编号',
    partners_name        VARCHAR(255) COMMENT '合作伙伴名称',
    partners_classify_id VARCHAR(32) COMMENT '合作伙伴分类',
    partners_address     VARCHAR(1024) COMMENT '合作伙伴地址',
    partners_img         VARCHAR(1024) COMMENT '合作伙伴图片',
    sort                 INT                  DEFAULT 1 COMMENT '排序',
    company_id           VARCHAR(32) COMMENT '公司id',
    user_id              VARCHAR(32) NOT NULL COMMENT '用户id',
    status               VARCHAR(255)         DEFAULT 'effective' COMMENT '状态;状态: effective 有效 failure 失效 delete 删除',
    created_by           VARCHAR(32) NOT NULL COMMENT '创建人',
    created_time         DATETIME    NOT NULL DEFAULT now() COMMENT '创建时间',
    updated_by           VARCHAR(32) NOT NULL COMMENT '更新人',
    updated_time         DATETIME    NOT NULL DEFAULT now() COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT = '合作伙伴';

DROP TABLE IF EXISTS tp_partners_classify;
CREATE TABLE tp_partners_classify
(
    id                     VARCHAR(32) NOT NULL COMMENT '编号',
    company_id             VARCHAR(32) COMMENT '公司id',
    user_id                VARCHAR(32) NOT NULL COMMENT '用户id',
    status                 VARCHAR(255) DEFAULT 'effective' COMMENT '状态;状态: effective 有效 failure 失效 delete 删除',
    create_by              VARCHAR(32) COMMENT '创建人',
    create_time            DATETIME COMMENT '创建时间',
    updated_by             VARCHAR(32) COMMENT '更新人',
    updated_time           DATETIME COMMENT '更新时间',
    partners_classify_name VARCHAR(255) COMMENT '分类名称',
    PRIMARY KEY (id)
) COMMENT = '合作伙伴分类';

DROP TABLE IF EXISTS tp_team;
CREATE TABLE tp_team
(
    id               VARCHAR(32)  NOT NULL COMMENT '编号',
    team_name        VARCHAR(255) COMMENT '名称',
    team_img         VARCHAR(255) COMMENT '团队图片',
    team_classify_id VARCHAR(255) COMMENT '团队分类',
    sort             INT                   DEFAULT 1 COMMENT '排序',
    company_id       VARCHAR(255) COMMENT '公司id',
    user_id          VARCHAR(32)  NOT NULL COMMENT '用户id',
    status           VARCHAR(255) NOT NULL DEFAULT 'effective' COMMENT '状态;状态: effective 有效 failure 失效 delete 删除',
    address          VARCHAR(255) COMMENT '地址',
    created_by       VARCHAR(32)  NOT NULL COMMENT '创建人',
    created_time     DATETIME     NOT NULL DEFAULT now() COMMENT '创建时间',
    updated_by       VARCHAR(32)  NOT NULL COMMENT '更新人',
    updated_time     DATETIME     NOT NULL DEFAULT now() COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT = '团队';

DROP TABLE IF EXISTS tp_team_classify;
CREATE TABLE tp_team_classify
(
    id                 VARCHAR(32) NOT NULL COMMENT '编号',
    team_classify_name VARCHAR(255) COMMENT '名称',
    company_id         VARCHAR(32) COMMENT '公司id',
    user_id            VARCHAR(32) NOT NULL COMMENT '用户id',
    status             VARCHAR(255) DEFAULT 'effective' COMMENT '状态;状态: effective 有效 failure 失效 delete 删除',
    create_by          VARCHAR(32) COMMENT '创建人',
    create_time        DATETIME COMMENT '创建时间',
    updated_by         VARCHAR(32) COMMENT '更新人',
    updated_time       DATETIME COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT = '团队分类';

DROP TABLE IF EXISTS sys_config;
CREATE TABLE sys_config
(
    id           VARCHAR(255) NOT NULL COMMENT '编号',
    config_name  VARCHAR(255) COMMENT '名称',
    config_value text COMMENT '配置值',
    config_desc  VARCHAR(255) COMMENT '描述',
    create_by    VARCHAR(32) COMMENT '创建人',
    create_time  DATETIME COMMENT '创建时间',
    updated_by   VARCHAR(32) COMMENT '更新人',
    updated_time DATETIME COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT = '站点配置';



DROP TABLE IF EXISTS tp_photo_album_classify;
CREATE TABLE tp_photo_album_classify
(
    id                        VARCHAR(32)  NOT NULL COMMENT '编号',
    photo_album_classify_name VARCHAR(900) NOT NULL COMMENT '名称',
    pid                       VARCHAR(255) NOT NULL DEFAULT 0 COMMENT '父编号',
    pids                      VARCHAR(255) NOT NULL COMMENT '父编号集合',
    level                     INT COMMENT '层级',
    user_id                   VARCHAR(32)  NOT NULL COMMENT '用户id',
    company_id                VARCHAR(32) COMMENT '公司id',
    status                    VARCHAR(255)          DEFAULT 'effective' COMMENT '状态;状态: effective 有效 failure 失效 delete 删除',
    create_by                 VARCHAR(32) COMMENT '创建人',
    create_time               DATETIME COMMENT '创建时间',
    updated_by                VARCHAR(32) COMMENT '更新人',
    updated_time              DATETIME COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT = '相册分类';

INSERT INTO `tp_photo_album_classify` (`id`, `photo_album_classify_name`, `pid`, `pids`, `user_id`, `company_id`,
                                       `create_by`, `create_time`, `updated_by`, `updated_time`, `status`)
VALUES ('0', '全部图片', '-1', '-1', '-1', NULL, '-1', '2021-11-14 17:57:52', '-1', '2021-11-14 17:57:56', 'effective');
INSERT INTO `tp_photo_album_classify` (`id`, `photo_album_classify_name`, `pid`, `pids`, `user_id`, `company_id`,
                                       `create_by`, `create_time`, `updated_by`, `updated_time`, `status`)
VALUES ('1', '默认相册', '0', '0', '-1', NULL, '-1', '2021-11-14 18:00:05', '-1', '2021-11-14 18:00:09', 'effective');
INSERT INTO `tp_photo_album_classify` (`id`, `photo_album_classify_name`, `pid`, `pids`, `user_id`, `company_id`,
                                       `create_by`, `create_time`, `updated_by`, `updated_time`, `status`)
VALUES ('2', '荣誉证书', '0', '0', '-1', NULL, '-1', '2021-11-14 18:00:05', '-1', '2021-11-14 18:00:09', 'effective');
INSERT INTO `tp_photo_album_classify` (`id`, `photo_album_classify_name`, `pid`, `pids`, `user_id`, `company_id`,
                                       `create_by`, `create_time`, `updated_by`, `updated_time`, `status`)
VALUES ('3', '团队介绍', '0', '0', '-1', NULL, '-1', '2021-11-14 18:00:05', '-1', '2021-11-14 18:00:09', 'effective');
INSERT INTO `tp_photo_album_classify` (`id`, `photo_album_classify_name`, `pid`, `pids`, `user_id`, `company_id`,
                                       `create_by`, `create_time`, `updated_by`, `updated_time`, `status`)
VALUES ('4', '合作伙伴', '0', '0', '-1', NULL, '-1', '2021-11-14 18:00:05', '-1', '2021-11-14 18:00:09', 'effective');
INSERT INTO `tp_photo_album_classify` (`id`, `photo_album_classify_name`, `pid`, `pids`, `user_id`, `company_id`,
                                       `create_by`, `create_time`, `updated_by`, `updated_time`, `status`)
VALUES ('5', '友情链接', '0', '0', '-1', NULL, '-1', '2021-11-14 18:00:05', '-1', '2021-11-14 18:00:09', 'effective');



DROP TABLE IF EXISTS tp_photo_album;
CREATE TABLE tp_photo_album
(
    id                      VARCHAR(32)  NOT NULL COMMENT '编号',
    photo_album_name        text COMMENT '名称',
    url                     VARCHAR(900) NOT NULL COMMENT '图片地址',
    user_id                 VARCHAR(32)  NOT NULL COMMENT '用户id',
    company_id              VARCHAR(255) COMMENT '公司id',
    photo_album_classify_id VARCHAR(32)  NOT NULL COMMENT '相册分类id',
    status                  VARCHAR(255) NOT NULL DEFAULT 'effective' COMMENT '状态;状态: effective 有效 failure 失效 delete 删除',
    create_by               VARCHAR(32) COMMENT '创建人',
    create_time             DATETIME              DEFAULT now() COMMENT '创建时间',
    updated_by              VARCHAR(32) COMMENT '更新人',
    updated_time            DATETIME              DEFAULT now() COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT = '相册';

DROP TABLE IF EXISTS tp_news_classify;
CREATE TABLE tp_news_classify
(
    id                 VARCHAR(255) NOT NULL COMMENT '编号',
    user_id            VARCHAR(32)  NOT NULL COMMENT '用户id',
    company_id         VARCHAR(255) COMMENT '公司id',
    pid                VARCHAR(255) DEFAULT -1 COMMENT '父编号',
    pids               VARCHAR(900) COMMENT '父id集合',
    level              INT COMMENT '层级',
    news_classify_name VARCHAR(255) COMMENT '名称',
    news_classify_img  VARCHAR(255) COMMENT '图片',
    content            text COMMENT '描述',
    sort               INT          DEFAULT 1 COMMENT '排序',
    `show`             VARCHAR(1)   DEFAULT 1 COMMENT '是否显示;0 不显示 1显示',
    seo_title          VARCHAR(900) COMMENT 'SEO标题',
    seo_keyword        VARCHAR(900) COMMENT 'SEO关键字',
    seo_content        VARCHAR(900) COMMENT 'SEO描述',
    plug_code          VARCHAR(900) COMMENT '插件代码',
    plug_location      VARCHAR(255) COMMENT '插件代码位置;head 在head结束标签前 body 在body结束标签前',
    status             VARCHAR(255) DEFAULT 'effective' COMMENT '状态;状态: effective 有效 failure 失效 delete 删除',
    create_by          VARCHAR(32) COMMENT '创建人',
    create_time        DATETIME     DEFAULT now() COMMENT '创建时间',
    updated_by         VARCHAR(32) COMMENT '更新人',
    updated_time       DATETIME     DEFAULT now() COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT = '新闻分类';



DROP TABLE IF EXISTS tp_news;
CREATE TABLE tp_news
(
    id               VARCHAR(32) NOT NULL COMMENT '编号',
    user_id          VARCHAR(32) NOT NULL COMMENT '用户id;用户id',
    company_id       VARCHAR(32) COMMENT '公司id;公司id',
    news_title       VARCHAR(1024) COMMENT '新闻标题',
    short_title      VARCHAR(1024) COMMENT '短标题',
    subtitle         VARCHAR(255) COMMENT '副标题',
    abstract         VARCHAR(1024) COMMENT '摘要',
    news_classify_id VARCHAR(255) COMMENT '分类',
    release_time     DATETIME COMMENT '发布时间',
    source           VARCHAR(255) COMMENT '来源',
    author           VARCHAR(255) COMMENT '作者',
    sort             INT         NOT NULL DEFAULT 1 COMMENT '排序',
    news_img         VARCHAR(900) COMMENT '封面',
    show_news_img    VARCHAR(1)           DEFAULT 1 COMMENT '是否显示封面;0 否 1 是',
    url              VARCHAR(900) COMMENT '外链地址',
    english_id       VARCHAR(900) COMMENT '英文标识',
    content          text COMMENT '内容',
    tags             VARCHAR(900) COMMENT '标签',
    seo_title        VARCHAR(900) COMMENT 'SEO标题',
    seo_keyword      VARCHAR(900) COMMENT 'SEO关键字',
    seo_content      VARCHAR(900) COMMENT 'SEO描述',
    plug_code        VARCHAR(900) COMMENT '插件代码',
    plug_location    VARCHAR(255) COMMENT '插件代码位置;head 在head结束标签前 body 在body结束标签前',
    status           VARCHAR(255)         DEFAULT 'effective' COMMENT '状态;状态: effective 有效 failure 失效 delete 删除',
    create_by        VARCHAR(32) COMMENT '创建人',
    create_time      DATETIME             DEFAULT now() COMMENT '创建时间',
    updated_by       VARCHAR(32) COMMENT '更新人',
    updated_time     DATETIME             DEFAULT now() COMMENT '更新时间',
    is_top           VARCHAR(1)           DEFAULT 0 COMMENT '是否置顶;0 否 1 是',
    PRIMARY KEY (id)
) COMMENT = '新闻';


DROP TABLE IF EXISTS customer_sys_config;
CREATE TABLE customer_sys_config
(
    id           VARCHAR(32) NOT NULL COMMENT '编号',
    config_name  VARCHAR(255) COMMENT '配置名称',
    config_value text COMMENT '配置值',
    config_desc  VARCHAR(255) COMMENT '描述',
    sort         INT      DEFAULT 1 COMMENT '排序',
    user_id      VARCHAR(32) NOT NULL COMMENT '用户id',
    company_id   VARCHAR(32) COMMENT '公司id',
    create_by    VARCHAR(32) COMMENT '创建人',
    create_time  DATETIME DEFAULT now() COMMENT '创建时间',
    updated_by   VARCHAR(32) COMMENT '更新人',
    updated_time DATETIME DEFAULT now() COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT = '用户站点配置';

DROP TABLE IF EXISTS tp_site;
CREATE TABLE tp_site
(
    id                 VARCHAR(32)  NOT NULL COMMENT '编号',
    site_name          VARCHAR(255) COMMENT '站点名称',
    `language`         VARCHAR(255) NOT NULL DEFAULT 'zh_cn' COMMENT '语言版本;zh_cn 中文 us_en 英文',
    system_version     VARCHAR(255) COMMENT '系统版本',
    due_time           DATETIME     NOT NULL COMMENT '到期时间',
    status             VARCHAR(255) NOT NULL DEFAULT 'effective' COMMENT '状态;状态: effective 有效 failure 失效 delete 删除',
    sort               INT                   DEFAULT 1 COMMENT '排序',
    user_id            VARCHAR(32)  NOT NULL COMMENT '用户id',
    company_id         VARCHAR(32) COMMENT '公司id',
    domain             VARCHAR(900) COMMENT '用户域名',
    system_domain      VARCHAR(900) COMMENT '系统域名',
    computer_room      VARCHAR(255)          DEFAULT 'china' COMMENT '机房;china 中国大陆  hong_kong 中国香港',
    customize_template VARCHAR(255) COMMENT '模板',
    icp                VARCHAR(255) COMMENT '公信备案',
    security_icp       VARCHAR(255) COMMENT '公安备案',
    security_icp_url   VARCHAR(255) COMMENT '公安备案跳转地址',
    whether_pay        INT                   DEFAULT 0 COMMENT '付费状态;0 未付费 1 已付费',
    create_by          VARCHAR(32) COMMENT '创建人',
    create_time        DATETIME              DEFAULT now() COMMENT '创建时间',
    updated_by         VARCHAR(32) COMMENT '更新人',
    updated_time       DATETIME              DEFAULT now() COMMENT '更新时间',
    PRIMARY KEY (id)
) COMMENT = '站点';


DROP TABLE IF EXISTS tp_pages;
CREATE TABLE tp_pages
(
    id            VARCHAR(32)  NOT NULL COMMENT '编号',
    site_id       bigint(11) NOT NULL COMMENT '站点id',
    page_url      VARCHAR(255) NOT NULL COMMENT '页面路径',
    page_name     VARCHAR(255) NOT NULL COMMENT '页面名称',
    page_type     VARCHAR(255) COMMENT '页面类型;navigation 导航',
    page_belongs  VARCHAR(255) NOT NULL DEFAULT 'user' COMMENT '页面归属;system 系统 user 用户',
    seo_title     VARCHAR(900) COMMENT 'SEO标题',
    seo_keyword   VARCHAR(255) COMMENT 'SEO关键字',
    seo_content   VARCHAR(255) COMMENT 'SEO描述',
    plug_code     VARCHAR(900) COMMENT '插件代码',
    plug_location VARCHAR(255) COMMENT '插件代码位置;head 在head结束标签前 body 在body结束标签前',
    pid           VARCHAR(32)  NOT NULL DEFAULT '-1' COMMENT '父id',
    pids          VARCHAR(255) NOT NULL DEFAULT '-1' COMMENT '父id集合',
    sort          INT                   DEFAULT 1 COMMENT '排序',
    user_id       VARCHAR(32)  NOT NULL COMMENT '用户id',
    company_id    VARCHAR(32) COMMENT '公司id',
    create_by     VARCHAR(32) COMMENT '创建人',
    create_time   DATETIME              DEFAULT now() COMMENT '创建时间',
    updated_by    VARCHAR(32) COMMENT '更新人',
    updated_time  DATETIME              DEFAULT now() COMMENT '更新时间',
    status        VARCHAR(255) NOT NULL DEFAULT 'effective' COMMENT '状态;状态: effective 有效 failure 失效 delete 删除状态: effective 有效 failure 失效 delete 删除',
    page_show     VARCHAR(255)          DEFAULT 'show' COMMENT '是否显示;show 显示 hidden 隐藏',
    link_type     VARCHAR(255)          DEFAULT 'default' COMMENT '链接类型;default 默认 custom 自定义',
    link_url      VARCHAR(1024) COMMENT '链接地址',
    open_type     VARCHAR(255)          DEFAULT 'current' COMMENT '打开类型;new 新窗口 current 当前',
    `level`       INT                   DEFAULT 1 COMMENT '层级',
    PRIMARY KEY (id)
) COMMENT = '页面';
insert into `tp_pages` (`id`, `site_id`, `page_url`, `page_name`, `page_type`, `page_belongs`, `seo_title`, `seo_keyword`, `seo_content`, `plug_code`, `plug_location`, `pid`, `pids`, `sort`, `user_id`, `company_id`, `create_by`, `create_time`, `updated_by`, `updated_time`, `status`, `page_show`, `link_type`, `link_url`, `open_type`, `level`) values('1','-1','index','首页','navigation','system',NULL,NULL,NULL,NULL,NULL,'-1','-1','1','-1',NULL,'1','2022-01-02 12:09:24','1','2022-01-02 12:09:24','effective','show','default',NULL,'current','1');
insert into `tp_pages` (`id`, `site_id`, `page_url`, `page_name`, `page_type`, `page_belongs`, `seo_title`, `seo_keyword`, `seo_content`, `plug_code`, `plug_location`, `pid`, `pids`, `sort`, `user_id`, `company_id`, `create_by`, `create_time`, `updated_by`, `updated_time`, `status`, `page_show`, `link_type`, `link_url`, `open_type`, `level`) values('16','-1','404','404页面',NULL,'system',NULL,NULL,NULL,NULL,NULL,'-1','-1','1','-1',NULL,'1','2022-01-16 04:56:11','1','2022-01-16 04:56:11','effective','show','default',NULL,'current','1');
insert into `tp_pages` (`id`, `site_id`, `page_url`, `page_name`, `page_type`, `page_belongs`, `seo_title`, `seo_keyword`, `seo_content`, `plug_code`, `plug_location`, `pid`, `pids`, `sort`, `user_id`, `company_id`, `create_by`, `create_time`, `updated_by`, `updated_time`, `status`, `page_show`, `link_type`, `link_url`, `open_type`, `level`) values('2','-1','news','新闻资讯','navigation','system',NULL,NULL,NULL,NULL,NULL,'-1','-1','1','-1',NULL,'1','2022-01-02 12:09:24','1','2022-01-02 12:09:24','effective','show','default',NULL,'current','1');
insert into `tp_pages` (`id`, `site_id`, `page_url`, `page_name`, `page_type`, `page_belongs`, `seo_title`, `seo_keyword`, `seo_content`, `plug_code`, `plug_location`, `pid`, `pids`, `sort`, `user_id`, `company_id`, `create_by`, `create_time`, `updated_by`, `updated_time`, `status`, `page_show`, `link_type`, `link_url`, `open_type`, `level`) values('3','-1','company','公司简介','navigation','system',NULL,NULL,NULL,NULL,NULL,'-1','-1','1','-1',NULL,'1','2022-01-12 12:08:10','1','2022-01-12 12:08:10','effective','show','default',NULL,'current','1');
insert into `tp_pages` (`id`, `site_id`, `page_url`, `page_name`, `page_type`, `page_belongs`, `seo_title`, `seo_keyword`, `seo_content`, `plug_code`, `plug_location`, `pid`, `pids`, `sort`, `user_id`, `company_id`, `create_by`, `create_time`, `updated_by`, `updated_time`, `status`, `page_show`, `link_type`, `link_url`, `open_type`, `level`) values('4','-1','product','产品展示','navigation','system',NULL,NULL,NULL,NULL,NULL,'-1','-1','1','-1',NULL,'1','2022-01-12 12:08:10','1','2022-01-12 12:08:10','effective','show','default',NULL,'current','1');
insert into `tp_pages` (`id`, `site_id`, `page_url`, `page_name`, `page_type`, `page_belongs`, `seo_title`, `seo_keyword`, `seo_content`, `plug_code`, `plug_location`, `pid`, `pids`, `sort`, `user_id`, `company_id`, `create_by`, `create_time`, `updated_by`, `updated_time`, `status`, `page_show`, `link_type`, `link_url`, `open_type`, `level`) values('5','-1','cert','荣誉证书','navigation','system',NULL,NULL,NULL,NULL,NULL,'-1','-1','1','-1',NULL,'1','2022-01-12 12:08:10','1','2022-01-12 12:08:10','effective','show','default',NULL,'current','1');
insert into `tp_pages` (`id`, `site_id`, `page_url`, `page_name`, `page_type`, `page_belongs`, `seo_title`, `seo_keyword`, `seo_content`, `plug_code`, `plug_location`, `pid`, `pids`, `sort`, `user_id`, `company_id`, `create_by`, `create_time`, `updated_by`, `updated_time`, `status`, `page_show`, `link_type`, `link_url`, `open_type`, `level`) values('6','-1','message','在线留言','navigation','system',NULL,NULL,NULL,NULL,NULL,'-1','-1','1','-1',NULL,'1','2022-01-12 12:08:10','1','2022-01-12 12:08:10','effective','show','default',NULL,'current','1');
insert into `tp_pages` (`id`, `site_id`, `page_url`, `page_name`, `page_type`, `page_belongs`, `seo_title`, `seo_keyword`, `seo_content`, `plug_code`, `plug_location`, `pid`, `pids`, `sort`, `user_id`, `company_id`, `create_by`, `create_time`, `updated_by`, `updated_time`, `status`, `page_show`, `link_type`, `link_url`, `open_type`, `level`) values('7','-1','contact','联系我们','navigation','system',NULL,NULL,NULL,NULL,NULL,'-1','-1','1','-1',NULL,'1','2022-01-12 12:08:10','1','2022-01-12 12:08:10','effective','show','default',NULL,'current','1');



DROP TABLE IF EXISTS tp_industry;
CREATE TABLE tp_industry
(
    id            VARCHAR(32)  NOT NULL COMMENT '编号',
    industry_name VARCHAR(255) NOT NULL COMMENT '行业名称',
    pid           VARCHAR(255) NOT NULL DEFAULT '-1' COMMENT '父编号',
    pids          VARCHAR(900) COMMENT '父编号集合',
    sort          INT                   DEFAULT 1 COMMENT '排序',
    create_by     VARCHAR(32) COMMENT '创建人',
    create_time   DATETIME              DEFAULT now() COMMENT '创建时间',
    updated_by    VARCHAR(32) COMMENT '更新人',
    updated_time  DATETIME              DEFAULT now() COMMENT '更新时间',
    status        VARCHAR(255) NOT NULL DEFAULT 'effective' COMMENT '状态;状态: effective 有效 failure 失效 delete 删除状态: effective 有效 failure 失效 delete 删除',
    PRIMARY KEY (id)
) COMMENT = '行业类目';

DROP TABLE IF EXISTS tp_template;
CREATE TABLE tp_template
(
    id            VARCHAR(32)  NOT NULL COMMENT '编号',
    template_name VARCHAR(255) COMMENT '模板名称',
    template_img  VARCHAR(900) COMMENT '模板图片',
    pay_type      VARCHAR(255)          DEFAULT 'free' COMMENT '价格类型;free 免费  charge 收费',
    industry_id   VARCHAR(32)  NOT NULL COMMENT '行业id',
    sort          INT                   DEFAULT 1 COMMENT '排序',
    company_id    VARCHAR(32) COMMENT '公司id',
    user_id       VARCHAR(32) COMMENT '用户id',
    create_by     VARCHAR(32) COMMENT '创建人',
    create_time   DATETIME              DEFAULT now() COMMENT '创建时间',
    updated_by    VARCHAR(32) COMMENT '更新人',
    updated_time  DATETIME              DEFAULT now() COMMENT '更新时间',
    status        VARCHAR(255) NOT NULL DEFAULT 'effective' COMMENT '状态;状态: effective 有效 failure 失效 delete 删除状态: effective 有效 failure 失效 delete 删除',
    PRIMARY KEY (id)
) COMMENT = '模板';



DROP TABLE IF EXISTS tp_module;
CREATE TABLE tp_module
(
    id           VARCHAR(32)  NOT NULL COMMENT '编号',
    module_name  VARCHAR(255) NOT NULL COMMENT '模块名称',
    sort         INT                   DEFAULT 1 COMMENT '排序',
    company_id   VARCHAR(32) COMMENT '公司id',
    create_by    VARCHAR(32) COMMENT '创建人',
    create_time  DATETIME              DEFAULT now() COMMENT '创建时间',
    updated_by   VARCHAR(32) COMMENT '更新人',
    updated_time DATETIME              DEFAULT now() COMMENT '更新时间',
    status       VARCHAR(255) NOT NULL DEFAULT 'effective' COMMENT '状态;状态: effective 有效 failure 失效 delete 删除状态: effective 有效 failure 失效 delete 删除',
    PRIMARY KEY (id)
) COMMENT = '模块';


DROP TABLE IF EXISTS tp_component;
CREATE TABLE tp_component
(
    id             VARCHAR(32)  NOT NULL COMMENT '编号',
    module_id      VARCHAR(255) NOT NULL COMMENT '模块id',
    template_id    VARCHAR(255) COMMENT '模板id',
    component_name VARCHAR(255) NOT NULL COMMENT '组件名称',
    content        text COMMENT '内容',
    template_sort  INT                   DEFAULT 1 COMMENT '模块排序',
    sort           INT                   DEFAULT 1 COMMENT '排序',
    user_id        VARCHAR(32) COMMENT '用户id',
    company_id     VARCHAR(32) COMMENT '公司id',
    create_by      VARCHAR(32) COMMENT '创建人',
    create_time    DATETIME              DEFAULT now() COMMENT '创建时间',
    updated_by     VARCHAR(32) COMMENT '更新人',
    updated_time   DATETIME              DEFAULT now() COMMENT '更新时间',
    status         VARCHAR(255) NOT NULL DEFAULT 'effective' COMMENT '状态;状态: effective 有效 failure 失效 delete 删除状态: effective 有效 failure 失效 delete 删除',
    PRIMARY KEY (id)
) COMMENT = '组件';


DROP TABLE IF EXISTS tp_user_component;
CREATE TABLE tp_user_component
(
    id           VARCHAR(32)  NOT NULL COMMENT '编号',
    component_id VARCHAR(255) COMMENT '组件id',
    content      text COMMENT '内容',
    site_id      VARCHAR(32)  NOT NULL COMMENT '站点id',
    sort         INT                   DEFAULT 1 COMMENT '排序',
    user_id      VARCHAR(32)  NOT NULL COMMENT '用户id',
    company_id   VARCHAR(32) COMMENT '公司id',
    create_by    VARCHAR(32) COMMENT '创建人',
    create_time  DATETIME              DEFAULT now() COMMENT '创建时间',
    updated_by   VARCHAR(32) COMMENT '更新人',
    updated_time DATETIME              DEFAULT now() COMMENT '更新时间',
    status       VARCHAR(255) NOT NULL DEFAULT 'effective' COMMENT '状态;状态: effective 有效 failure 失效 delete 删除状态: effective 有效 failure 失效 delete 删除',
    PRIMARY KEY (id)
) COMMENT = '用户组件';







