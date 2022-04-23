/*
 *  Copyright  2022 Wei xi mei
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package com.yqm.common.utils;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * @Author: weiximei
 * @Date: 2022/4/23 14:35
 * @微信: wxm907147608
 * @QQ: 907147608
 * @Email: 907147608@qq.com
 */
public class BigDecimalUtils {
    

    public static BigDecimal subtraction(BigDecimal b1, BigDecimal b2) {
        if (Objects.isNull(b1)) {
            return b2;
        }
        if (Objects.isNull(b2)) {
            return b1;
        }
        return b1.subtract(b2);
    }

    /**
     * 四舍五入
     *
     * @param b1
     * @param b2
     * @return
     */
    public static BigDecimal subtractionRoundHalfUp(BigDecimal b1, BigDecimal b2) {
        return subtraction(b1, b2).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    public static BigDecimal add(BigDecimal b1, BigDecimal b2) {
        if (Objects.isNull(b1)) {
            return b2;
        }
        if (Objects.isNull(b2)) {
            return b1;
        }
        return b1.add(b2);
    }

    /**
     * 四舍五入
     *
     * @param b1
     * @param b2
     * @return
     */
    public static BigDecimal addRoundHalfUp(BigDecimal b1, BigDecimal b2) {
        return add(b1, b2).setScale(2, BigDecimal.ROUND_HALF_UP);
    }

}
