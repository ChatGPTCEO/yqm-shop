package com.yqm.modules.sales;

import com.yqm.dozer.service.IGenerator;
import com.yqm.logging.aop.log.Log;
import com.yqm.modules.mp.service.WeixinPayService;
import com.yqm.modules.sales.domain.StoreAfterSales;
import com.yqm.modules.sales.param.SalesCheckDto;
import com.yqm.modules.sales.param.YqmStoreAfterSalesDto;
import com.yqm.modules.sales.service.StoreAfterSalesService;
import com.yqm.modules.sales.param.YqmStoreAfterSalesQueryCriteria;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Map;

/**
 * @author : gzlv 2021/6/29 3:48
 */
@Api(tags = "商城:售后管理")
@RestController
@RequestMapping("/api/yqmStoreAfterSales")
@Slf4j
@SuppressWarnings("unchecked")
@AllArgsConstructor
public class StoreAfterSalesController {

    private final StoreAfterSalesService storeAfterSalesService;
    private final WeixinPayService weixinPayService;
    private final IGenerator generator;

    /**
     * 审核
     */
    @Log("审核")
    @ApiOperation("审核")
    @PreAuthorize("@el.check('admin','yqmStoreAfterSales:edit')")
    @PostMapping(value = "/salesCheck")
    public ResponseEntity salesCheck(@RequestBody SalesCheckDto salesCheckDto) {
        return new ResponseEntity<>(storeAfterSalesService.salesCheck(salesCheckDto.getSalesId(), salesCheckDto.getOrderCode(), salesCheckDto.getApprovalStatus(), salesCheckDto.getConsignee(), salesCheckDto.getPhoneNumber(), salesCheckDto.getAddress()), HttpStatus.OK);
    }

    /**
     * 打款
     */
    @Log("打款")
    @ApiOperation("打款")
    @PostMapping(value = "/makeMoney")
    @PreAuthorize("@el.check('admin','yqmStoreAfterSales:edit')")
    public ResponseEntity makeMoney(@RequestParam(value = "salesId") Long salesId, @RequestParam("orderCode") String orderCode) {
        StoreAfterSales sales = storeAfterSalesService.makeMoney(salesId, orderCode);
        BigDecimal bigDecimal = new BigDecimal("100");
        int payPrice = bigDecimal.multiply(sales.getRefundAmount()).intValue();
        weixinPayService.refundOrder(orderCode, payPrice);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('admin','yqmStoreAfterSales:list')")
    public void download(HttpServletResponse response, YqmStoreAfterSalesQueryCriteria criteria) throws IOException {
        storeAfterSalesService.download(generator.convert(storeAfterSalesService.queryAll(criteria), YqmStoreAfterSalesDto.class), response);
    }

    @GetMapping(value = "/sales/List")
    @Log("查询售后")
    @ApiOperation("查询售后")
    @PreAuthorize("@el.check('admin','yqmStoreAfterSales:list')")
    public ResponseEntity<Map<String, Object>> getYqmStoreAfterSaless(YqmStoreAfterSalesQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(storeAfterSalesService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增售后")
    @ApiOperation("新增售后")
    @PreAuthorize("@el.check('admin','yqmStoreAfterSales:add')")
    public ResponseEntity<Object> create(@Validated @RequestBody StoreAfterSales resources){
        return new ResponseEntity<>(storeAfterSalesService.save(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改售后")
    @ApiOperation("修改售后")
    @PreAuthorize("@el.check('admin','yqmStoreAfterSales:edit')")
    public ResponseEntity<Object> update(@Validated @RequestBody StoreAfterSales resources){
        storeAfterSalesService.updateById(resources);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Log("删除售后")
    @ApiOperation("删除售后")
        @PreAuthorize("@el.check('admin','yqmStoreAfterSales:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        Arrays.asList(ids).forEach(storeAfterSalesService::removeById);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
