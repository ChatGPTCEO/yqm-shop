/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.tools.rest;

import com.yqm.dozer.service.IGenerator;
import com.yqm.logging.aop.log.Log;
import com.yqm.tools.domain.Picture;
import com.yqm.tools.service.PictureService;
import com.yqm.tools.service.dto.PictureDto;
import com.yqm.tools.service.dto.PictureQueryCriteria;
import com.yqm.utils.SecurityUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author weiximei
 * @date 2018/09/20 14:13:32
 */
@RestController
@RequestMapping("/api/pictures")
@Api(tags = "工具：免费图床管理")
public class PictureController {
    private final IGenerator generator;
    private final PictureService pictureService;

    public PictureController(IGenerator generator, PictureService pictureService) {
        this.generator = generator;
        this.pictureService = pictureService;
    }

    @Log("查询图片")
    @PreAuthorize("@el.check('pictures:list')")
    @GetMapping
    @ApiOperation("查询图片")
    public ResponseEntity<Object> getRoles(PictureQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(pictureService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('pictures:list')")
    public void download(HttpServletResponse response, PictureQueryCriteria criteria) throws IOException {
        pictureService.download(generator.convert(pictureService.queryAll(criteria), PictureDto.class), response);
    }

    @Log("上传图片")
    @PreAuthorize("@el.check('pictures:add')")
    @PostMapping
    @ApiOperation("上传图片")
    public ResponseEntity<Object> upload(@RequestParam MultipartFile file){
        String userName = SecurityUtils.getUsername();
        Picture picture = pictureService.upload(file,userName);
        return new ResponseEntity<>(picture,HttpStatus.OK);
    }

    @Log("同步图床数据")
    @ApiOperation("同步图床数据")
    @PostMapping(value = "/synchronize")
    public ResponseEntity<Object> synchronize(){
        pictureService.synchronize();
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Log("多选删除图片")
    @ApiOperation("多选删除图片")
    @PreAuthorize("@el.check('pictures:del')")
    @DeleteMapping
    public ResponseEntity<Object> deleteAll(@RequestBody Long[] ids) {
        pictureService.deleteAll(ids);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
