package com.yqm.modules.activity.vo;

import lombok.Data;

import java.util.List;

@Data
public class CombinationQueryVo {

    private List<YqmStoreCombinationQueryVo> storeCombinationQueryVos;

    private Long lastPage;

}
