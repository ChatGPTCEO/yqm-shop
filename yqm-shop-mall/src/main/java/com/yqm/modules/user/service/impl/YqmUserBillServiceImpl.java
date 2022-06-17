/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.com
 * 注意：
 * 本软件为www.yqmshop.com开发研制，未经购买不得使用
 * 购买后可获得全部源代码（禁止转卖、分享、上传到码云、github等开源平台）
 * 一经发现盗用、分享等行为，将追究法律责任，后果自负
 */
package com.yqm.modules.user.service.impl;

import com.yqm.common.service.impl.BaseServiceImpl;
import com.yqm.dozer.service.IGenerator;
import com.yqm.enums.BillDetailEnum;
import com.yqm.enums.BillEnum;
import com.yqm.enums.BillInfoEnum;
import com.yqm.enums.ShopCommonEnum;
import com.yqm.modules.user.domain.YqmUserBill;
import com.yqm.modules.user.service.YqmUserBillService;
import com.yqm.modules.user.service.dto.BillOrderDto;
import com.yqm.modules.user.service.dto.BillOrderRecordDto;
import com.yqm.modules.user.service.dto.YqmUserBillDto;
import com.yqm.modules.user.service.dto.YqmUserBillQueryCriteria;
import com.yqm.modules.user.service.mapper.UserBillMapper;
import com.yqm.modules.user.vo.BillVo;
import com.yqm.modules.user.vo.YqmUserBillQueryVo;
import com.yqm.utils.FileUtil;
import com.yqm.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;


/**
* @author weiximei
* @date 2020-05-12
*/
@Service
@AllArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class YqmUserBillServiceImpl extends BaseServiceImpl<UserBillMapper, YqmUserBill> implements YqmUserBillService {

    private final IGenerator generator;
    private final UserBillMapper yqmUserBillMapper;


    /**
     * 增加支出流水
     * @param uid uid
     * @param title 账单标题
     * @param category 明细种类
     * @param type 明细类型
     * @param number 明细数字
     * @param balance 剩余
     * @param mark 备注
     */
    @Override
    public void expend(Long uid,String title,String category,String type,double number,double balance,String mark){
        YqmUserBill userBill = YqmUserBill.builder()
                .uid(uid)
                .title(title)
                .category(category)
                .type(type)
                .number(BigDecimal.valueOf(number))
                .balance(BigDecimal.valueOf(balance))
                .mark(mark)
                .pm(BillEnum.PM_0.getValue())
                .build();

        yqmUserBillMapper.insert(userBill);
    }

    /**
     * 增加收入/支入流水
     * @param uid uid
     * @param title 账单标题
     * @param category 明细种类
     * @param type 明细类型
     * @param number 明细数字
     * @param balance 剩余
     * @param mark 备注
     * @param linkid 关联id
     */
    @Override
    public void income(Long uid,String title,String category,String type,double number,
                       double balance,String mark,String linkid){
        YqmUserBill userBill = YqmUserBill.builder()
                .uid(uid)
                .title(title)
                .category(category)
                .type(type)
                .number(BigDecimal.valueOf(number))
                .balance(BigDecimal.valueOf(balance))
                .mark(mark)
                .pm(BillEnum.PM_1.getValue())
                .linkId(linkid)
                .build();

        yqmUserBillMapper.insert(userBill);
    }

    /**
     * 签到了多少次
     * @param uid
     * @return
     */
    @Override
    public int cumulativeAttendance(Long uid) {
       LambdaQueryWrapper<YqmUserBill> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(YqmUserBill::getUid,uid).eq(YqmUserBill::getCategory,"integral")
                .eq(YqmUserBill::getType,"sign").eq(YqmUserBill::getPm,1);
        return yqmUserBillMapper.selectCount(wrapper);
    }

    /**
     * 获取推广订单列表
     * @param uid   uid
     * @param page  page
     * @param limit limit
     * @return Map
     */
    @Override
    public Map<String, Object> spreadOrder(Long uid, int page, int limit) {
       QueryWrapper<YqmUserBill> wrapper = new QueryWrapper<>();
        wrapper.lambda().in(YqmUserBill::getUid, uid)
                .eq(YqmUserBill::getType, BillDetailEnum.TYPE_2.getValue())
                .eq(YqmUserBill::getCategory, BillDetailEnum.CATEGORY_1.getValue());
        wrapper.orderByDesc("time").groupBy("time");
        Page<YqmUserBill> pageModel = new Page<>(page, limit);
        List<String> list = yqmUserBillMapper.getBillOrderList(wrapper, pageModel);

        int count = yqmUserBillMapper.selectCount(Wrappers.<YqmUserBill>lambdaQuery()
                .eq(YqmUserBill::getUid, uid)
                .eq(YqmUserBill::getType, BillDetailEnum.TYPE_2.getValue())
                .eq(YqmUserBill::getCategory, BillDetailEnum.CATEGORY_1.getValue()));
        List<BillOrderDto> listT = new ArrayList<>();
        for (String str : list) {
            BillOrderDto billOrderDTO = new BillOrderDto();
            List<BillOrderRecordDto> orderRecordDTOS = yqmUserBillMapper
                    .getBillOrderRList(str, uid);
            billOrderDTO.setChild(orderRecordDTOS);
            billOrderDTO.setCount(orderRecordDTOS.size());
            billOrderDTO.setTime(str);

            listT.add(billOrderDTO);
        }

        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("list", listT);
        map.put("count", count);

        return map;
    }

    /**
     * 获取用户账单记录
     * @param page page
     * @param limit limit
     * @param uid uid
     * @param type BillDetailEnum
     * @return map
     */
    @Override
    public Map<String,Object> getUserBillList(int page, int limit, long uid, int type) {
       QueryWrapper<YqmUserBill> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(YqmUserBill::getUid,uid).orderByDesc(YqmUserBill::getCreateTime)
                .orderByAsc(YqmUserBill::getId);
        wrapper.groupBy("time");
        switch (BillInfoEnum.toType(type)){
            case PAY_PRODUCT:
                wrapper.lambda().eq(YqmUserBill::getCategory,BillDetailEnum.CATEGORY_1.getValue());
                wrapper.lambda().eq(YqmUserBill::getType,BillDetailEnum.TYPE_3.getValue());
                break;
            case RECHAREGE:
                wrapper.lambda().eq(YqmUserBill::getCategory,BillDetailEnum.CATEGORY_1.getValue());
                wrapper.lambda().eq(YqmUserBill::getType,BillDetailEnum.TYPE_1.getValue());
                break;
            case BROKERAGE:
                wrapper.lambda().eq(YqmUserBill::getCategory,BillDetailEnum.CATEGORY_1.getValue());
                wrapper.lambda().eq(YqmUserBill::getType,BillDetailEnum.TYPE_2.getValue());
                break;
            case EXTRACT:
                wrapper.lambda().eq(YqmUserBill::getCategory,BillDetailEnum.CATEGORY_1.getValue());
                wrapper.lambda().eq(YqmUserBill::getType,BillDetailEnum.TYPE_4.getValue());
                break;
            case SIGN_INTEGRAL:
                wrapper.lambda().eq(YqmUserBill::getCategory,BillDetailEnum.CATEGORY_2.getValue());
                wrapper.lambda().eq(YqmUserBill::getType,BillDetailEnum.TYPE_10.getValue());
                break;
            default:
                wrapper.lambda().eq(YqmUserBill::getCategory,BillDetailEnum.CATEGORY_1.getValue());

        }
        Page<YqmUserBill> pageModel = new Page<>(page, limit);
        List<BillVo> billDTOList = yqmUserBillMapper.getBillList(wrapper,pageModel);
        for (BillVo billDTO : billDTOList) {
           LambdaQueryWrapper<YqmUserBill> wrapperT = new LambdaQueryWrapper<>();
            wrapperT.in(YqmUserBill::getId,Arrays.asList(billDTO.getIds().split(",")));
            wrapperT.orderByDesc(YqmUserBill::getCreateTime);
            billDTO.setList(yqmUserBillMapper.getUserBillList(wrapperT));

        }
        Map<String,Object> map = new HashMap<>();
        map.put("list",billDTOList);
        map.put("total",pageModel.getTotal());
        map.put("totalPage",pageModel.getPages());
        return map;
       // return billDTOList;
    }

    @Override
    public double getBrokerage(int uid) {
        return yqmUserBillMapper.sumPrice(uid);
    }

    /**
     * 统计昨天的佣金
     * @param uid uid
     * @return double
     */
    @Override
    public double yesterdayCommissionSum(Long uid) {
        return yqmUserBillMapper.sumYesterdayPrice(uid);
    }

    /**
     * 根据类别获取账单记录
     * @param uid uid
     * @param category  BillDetailEnum
     * @param page page
     * @param limit limit
     * @return List
     */
    @Override
    public List<YqmUserBillQueryVo> userBillList(Long uid,String category,int page,int limit) {
       LambdaQueryWrapper<YqmUserBill> wrapper = new LambdaQueryWrapper<>();
        wrapper
                .eq(YqmUserBill::getStatus, ShopCommonEnum.IS_STATUS_1.getValue())
                .eq(YqmUserBill::getUid,uid)
                .eq(YqmUserBill::getCategory,category)
                .orderByDesc(YqmUserBill::getId);
        Page<YqmUserBill> pageModel = new Page<>(page, limit);
        IPage<YqmUserBill> pageList = yqmUserBillMapper.selectPage(pageModel,wrapper);
        return generator.convert(pageList.getRecords(),YqmUserBillQueryVo.class);
    }


    //============================================//

    @Override
    //@Cacheable
    public Map<String, Object> queryAll(YqmUserBillQueryCriteria criteria, Pageable pageable) {
        getPage(pageable);
        PageInfo<YqmUserBillDto> page = new PageInfo<>(queryAll(criteria));
        Map<String, Object> map = new LinkedHashMap<>(2);
        map.put("content", page.getList());
        map.put("totalElements", page.getTotal());
        return map;
    }


    @Override
    public List<YqmUserBillDto> queryAll(YqmUserBillQueryCriteria criteria){
        String date =null;
        String date1 = null;
        if(StringUtils.isNotEmpty(criteria.getStartTime())){
            date =  criteria.getStartTime()+ " 00:00:00";
            if(StringUtils.isNotEmpty(criteria.getEndTime())){
                date1 =criteria.getEndTime()+ " 23:59:59";
            }
        }

        return baseMapper.findAllByQueryCriteria(criteria.getCategory(),criteria.getType(),criteria.getNickname(),criteria.getPm(),date,date1,criteria.getTitle());
    }


    @Override
    public void download(List<YqmUserBillDto> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (YqmUserBillDto yqmUserBill : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("用户uid", yqmUserBill.getUid());
            map.put("关联id", yqmUserBill.getLinkId());
            map.put("0 = 支出 1 = 获得", yqmUserBill.getPm());
            map.put("账单标题", yqmUserBill.getTitle());
            map.put("明细种类", yqmUserBill.getCategory());
            map.put("明细类型", yqmUserBill.getType());
            map.put("明细数字", yqmUserBill.getNumber());
            map.put("剩余", yqmUserBill.getBalance());
            map.put("备注", yqmUserBill.getMark());
            map.put("0 = 带确定 1 = 有效 -1 = 无效", yqmUserBill.getStatus());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}
