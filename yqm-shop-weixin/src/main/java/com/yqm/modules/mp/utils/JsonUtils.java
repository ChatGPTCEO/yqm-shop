/**
 * Copyright (C) 2018-2022
 * All rights reserved, Designed By www.yqmshop.cn

 */
package com.yqm.modules.mp.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Binary Wang(https://github.com/binarywang)
 */
public class JsonUtils {
    public static String toJson(Object obj) {
        Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
        return gson.toJson(obj);
    }
}
