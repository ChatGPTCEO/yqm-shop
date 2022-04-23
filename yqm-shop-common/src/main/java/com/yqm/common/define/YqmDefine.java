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


    public final static String order_update_address = "";


    // 有效
    public final static List<String> includeStatus = Arrays.asList(StatusType.failure.getValue(), StatusType.effective.getValue());

    /**
     * 状态类型
     */
    public enum StatusType {
        effective("有效", "success"),
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
     * 用户配置
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
     * 字典类型
     */
    public enum DictionaryType {
        priceType("运费计算方式", "price_type");

        private String desc;
        private String value;

        DictionaryType(String desc, String value) {
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
     * 上下架状态
     */
    public enum ShelvesType {
        shelves("上架", "shelves"),
        not_shelves("未支付", "not_shelves"),
        ;

        private String desc;
        private String value;

        ShelvesType(String desc, String value) {
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
     * 规格/属性
     */
    public enum SpecType {
        spec("规格", 1),
        attr("属性", 0),
        ;

        private String desc;
        private Integer value;

        SpecType(String desc, Integer value) {
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
     * 订单状态
     */
    public enum OrderStatus {
        wait_payment("待付款", 0),
        already_payment("已付款", 1),
        wait_delivery("待发货", 2),
        completed("已完成", 3),
        close("已关闭", 4),
        already_delivery("已发货", 5),
        completed_delivery("完成收货", 6),
        completed_evaluation("完成评价", 6),
        ;

        private String desc;
        private Integer value;

        OrderStatus(String desc, Integer value) {
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
     * 用户信息
     */
    public enum UserType {
        system("系统", "system"),
        admin("管理用户", "admin"),
        client("客户端", "client"),
        ;

        private String desc;
        private String value;

        UserType(String desc, String value) {
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
     * 订单日志信息模板
     */
    public enum OrderLogNote {
        update_address("修改收件人信息", "修改收件人信息"),
        update_amount("修改费用信息", "修改费用信息"),
        close("关闭订单", "关闭订单: %s"),
        ;

        private String desc;
        private String value;

        OrderLogNote(String desc, String value) {
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
