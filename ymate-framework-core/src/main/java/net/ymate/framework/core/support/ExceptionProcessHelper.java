/*
 * Copyright 2007-2017 the original author or authors.
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
package net.ymate.framework.core.support;

import org.apache.commons.lang.NullArgumentException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/10/18 下午3:56
 * @version 1.0
 * @see net.ymate.platform.webmvc.util.ExceptionProcessHelper
 */
@Deprecated
public final class ExceptionProcessHelper {

    public final static ExceptionProcessHelper DEFAULT = new ExceptionProcessHelper();

    private final Map<String, IExceptionProcessor> __processors = new ConcurrentHashMap<String, IExceptionProcessor>();

    public ExceptionProcessHelper registerProcessor(Class<? extends Throwable> target, IExceptionProcessor processor) {
        if (target == null) {
            throw new NullArgumentException("target");
        }
        if (processor == null) {
            throw new NullArgumentException("processor");
        }
        __processors.put(target.getName(), processor);
        //
        return this;
    }

    public IExceptionProcessor bind(Class<? extends Throwable> target) {
        return __processors.get(target.getName());
    }
}
