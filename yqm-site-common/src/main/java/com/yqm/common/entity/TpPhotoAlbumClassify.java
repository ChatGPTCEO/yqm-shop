package com.yqm.common.entity;

import com.yqm.common.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* <p>
    * 相册分类
    * </p>
*
* @author weiximei
* @since 2021-11-14
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    public class TpPhotoAlbumClassify  extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 名称
     */
    private String photoAlbumClassifyName;

    /**
     * 父编号
     */
    private String pid;

    /**
     * 父编号集合
     */
    private String pids;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 公司id
     */
    private String companyId;

    /**
     * 状态;状态: effective 有效 failure 失效 delete 删除
     */
    private String status;

    /**
     * 创建人
     */
    private String createBy;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updatedBy;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;


}