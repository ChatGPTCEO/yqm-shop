package com.yqm.modules.mp.vo;

import com.yqm.modules.mp.service.dto.YqmWechatLiveDto;
import lombok.Data;

import java.util.List;

@Data
public class WechatLiveVo {

    private List<YqmWechatLiveDto> content;

    private Long totalElements;

    private Integer pageNumber;

    private Integer lastPage;


}
