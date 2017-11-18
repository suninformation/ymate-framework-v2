/*
 * Copyright 2007-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.ymate.framework.commons;

import net.ymate.platform.core.lang.BlurObject;

import java.math.BigDecimal;

/**
 * 精确的数学计算工具类
 *
 * @author 刘镇 (suninformation@163.com) on 15/8/18 上午10:55
 * @version 1.0
 */
public class MathCalcHelper {

    /**
     * 默认除法运算精度
     */
    private static final int DEFAULT_DIV_SCALE = 10;

    private BigDecimal __value;

    private int __scale = -1;

    private int __roundingMode = -1;

    public static MathCalcHelper bind(double value) {
        return new MathCalcHelper(value);
    }

    public static MathCalcHelper bind(String value) {
        return new MathCalcHelper(value);
    }

    public static MathCalcHelper bind(BigDecimal value) {
        return new MathCalcHelper(value);
    }

    private MathCalcHelper(double value) {
        __value = new BigDecimal(Double.toString(value));
    }

    private MathCalcHelper(String value) {
        __value = new BigDecimal(value);
    }

    private MathCalcHelper(BigDecimal value) {
        __value = value;
    }

    //

    public MathCalcHelper scale(int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException("the scale must be a positive integer or zero.");
        }
        __scale = scale;
        return this;
    }

    public MathCalcHelper roundUp() {
        __roundingMode = BigDecimal.ROUND_UP;
        return this;
    }

    public MathCalcHelper roundDown() {
        __roundingMode = BigDecimal.ROUND_DOWN;
        return this;
    }

    public MathCalcHelper roundCeiling() {
        __roundingMode = BigDecimal.ROUND_CEILING;
        return this;
    }

    public MathCalcHelper roundFloor() {
        __roundingMode = BigDecimal.ROUND_FLOOR;
        return this;
    }

    public MathCalcHelper roundHalfUp() {
        __roundingMode = BigDecimal.ROUND_HALF_UP;
        return this;
    }

    public MathCalcHelper roundHalfDown() {
        __roundingMode = BigDecimal.ROUND_HALF_DOWN;
        return this;
    }

    public MathCalcHelper roundHalfEven() {
        __roundingMode = BigDecimal.ROUND_HALF_EVEN;
        return this;
    }

    public MathCalcHelper roundUnnecessary() {
        __roundingMode = BigDecimal.ROUND_UNNECESSARY;
        return this;
    }

    //

    public MathCalcHelper add(double value) {
        __value = __value.add(new BigDecimal(Double.toString(value)));
        return this;
    }

    public MathCalcHelper add(String value) {
        __value = __value.add(new BigDecimal(value));
        return this;
    }

    public MathCalcHelper add(BigDecimal value) {
        __value = __value.add(value);
        return this;
    }

    //

    public MathCalcHelper subtract(double value) {
        __value = __value.subtract(new BigDecimal(Double.toString(value)));
        return this;
    }

    public MathCalcHelper subtract(String value) {
        __value = __value.subtract(new BigDecimal(value));
        return this;
    }

    public MathCalcHelper subtract(BigDecimal value) {
        __value = __value.subtract(value);
        return this;
    }

    //

    public MathCalcHelper multiply(double value) {
        __value = __value.multiply(new BigDecimal(Double.toString(value)));
        return this;
    }

    public MathCalcHelper multiply(String value) {
        __value = __value.multiply(new BigDecimal(value));
        return this;
    }

    public MathCalcHelper multiply(BigDecimal value) {
        __value = __value.multiply(value);
        return this;
    }

    //

    public MathCalcHelper divide(double value) {
        return divide(new BigDecimal(Double.toString(value)));
    }

    public MathCalcHelper divide(String value) {
        return divide(new BigDecimal(value));
    }

    public MathCalcHelper divide(BigDecimal value) {
        __value = __value.divide(value,
                (__scale >= 0 ? __scale : DEFAULT_DIV_SCALE),
                (__roundingMode >= 0 ? __roundingMode : BigDecimal.ROUND_HALF_EVEN));
        return this;
    }

    //

    public MathCalcHelper round() {
        __value = __value.setScale((__scale >= 0 ? __scale : DEFAULT_DIV_SCALE),
                (__roundingMode >= 0 ? __roundingMode : BigDecimal.ROUND_HALF_EVEN));
        return this;
    }

    //

    public BlurObject toBlurObject() {
        return BlurObject.bind(__value);
    }
}
