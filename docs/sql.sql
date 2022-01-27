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





