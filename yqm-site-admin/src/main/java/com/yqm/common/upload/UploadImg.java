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

package com.yqm.common.upload;

import com.yqm.common.SpringContextHelper;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * 上传图片操作类
 *
 * @Author: weiximei
 * @Date: 2021/10/23 21:41
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
@Component
public class UploadImg {

    /**
     * 用于保存接口实现类名及对应的类
     */
    private Map<String, UploadAbstractImg> map = new HashMap<>();


    @PostConstruct
    public void init(){
        //根据接口类型返回相应的所有bean
        Map<String, UploadAbstractImg> classzMap = SpringContextHelper.getApplicationContext().getBeansOfType(UploadAbstractImg.class);

        List<UploadAbstractImg> uploadImgList = classzMap.entrySet().stream().map(Map.Entry::getValue).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(uploadImgList)) {
            return;
        }

        for (UploadAbstractImg classz : uploadImgList) {
            map.put(classz.getValue(), classz);
        }
    }

    public Map<String, UploadAbstractImg> getMap() {
        return map;
    }

    public UploadAbstractImg getUploadImg(String key) {
        return map.get(key);
    }

}
