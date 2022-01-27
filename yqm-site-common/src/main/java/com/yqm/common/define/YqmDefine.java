/*
 * Copyright 2021 Wei xi mei
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *
 * you may not use this file except in compliance with the License.
 *
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *
 * distributed under the License is distributed on an "AS IS" BASIS,
 *
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * See the License for the specific language governing permissions and
 *
 * limitations under the License.
 */

package com.yqm.common.define;

import java.util.Arrays;
import java.util.List;

/**
 * @Author: weiximei
 * @Date: 2021/11/6 15:39
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
public class YqmDefine {

    // 有效
    public final static List<String> includeStatus = Arrays.asList(StatusType.failure.getValue(), StatusType.effective.getValue());

    /**
     * 状态类型
     */
    public enum StatusType {
        effective("有效", "effective"),
        failure("失效", "failure"),
        delete("删除", "delete");

        private String desc;
        private String value;

        StatusType(String desc, String value) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * 缓存key类型
     */
    public enum CacheKeyType {
        system("系统", "system_"),
        user("用户", "user_");

        private String desc;
        private String value;

        CacheKeyType(String desc, String value) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * 系统配置
     */
    public enum SysConfigType {
        upload("使用上传配置", "upload"),
        qiniu_upload("七牛云上传配置", "qiniu-upload"),
        domain("域名", "domain"),
        sys_phone("联系方式", "sys_phone"),
        dns("dns服务器", "dns"),
        ;

        private String desc;
        private String value;

        SysConfigType(String desc, String value) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * 系统配置
     */
    public enum CustomerSysConfigType {
        customer("用户配置", "customer");

        private String desc;
        private String value;

        CustomerSysConfigType(String desc, String value) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * 付费状态
     */
    public enum WhetherPayType {
        has_been_paid("已支付", 0),
        not_pay("未支付", 1),
        ;

        private String desc;
        private Integer value;

        WhetherPayType(String desc, Integer value) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public Integer getValue() {
            return value;
        }

        public void setValue(Integer value) {
            this.value = value;
        }
    }

    /**
     * 页面类型
     */
    public enum PageType {
        navigation("导航", "navigation");

        private String desc;
        private String value;

        PageType(String desc, String value) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    /**
     * 页面归属
     */
    public enum PageBelongsType {
        system("系统", "system"),
        user("用户", "user");

        private String desc;
        private String value;

        PageBelongsType(String desc, String value) {
            this.desc = desc;
            this.value = value;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

}
