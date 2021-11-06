package com.yqm.common.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* <p>
    * 招聘
    * </p>
*
* @author weiximei
* @since 2021-09-11
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    public class TpRecruitment extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 标题
     */
    private String title;

    /**
     * 职务
     */
    private String position;

    /**
     * 招聘人数
     */
    private String recNum;

    /**
     * 状态;状态: effective 有效 failure 失效 delete 删除
     */
    private String status;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 职责
     */
    private String responsibility;

    /**
     * 要求
     */
    private String requirements;

    /**
     * 公司id
     */
    private String companyId;

    /**
     * 用户id
     */
    private String userId;



    /**
     * 联系方式
     */
    private String phone;

    /**
     * 联系人
     */
    private String userName;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;


}