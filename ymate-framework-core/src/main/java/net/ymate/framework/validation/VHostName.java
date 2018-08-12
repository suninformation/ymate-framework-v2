/*
 * Copyright 2007-2018 the original author or authors.
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
package net.ymate.framework.validation;

import net.ymate.framework.core.support.IHostNameChecker;

import java.lang.annotation.*;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/8/12 上午 02:01
 * @version 1.0
 * @since 2.0.6
 */
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface VHostName {

    /**
     * @return 自定义验证消息
     */
    String msg() default "";

    Class<? extends IHostNameChecker> checker() default IHostNameChecker.class;
}