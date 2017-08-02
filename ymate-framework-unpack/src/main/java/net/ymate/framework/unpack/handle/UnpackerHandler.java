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
package net.ymate.framework.unpack.handle;

import net.ymate.framework.unpack.IUnpacker;
import net.ymate.framework.unpack.IUnpackers;
import net.ymate.platform.core.beans.IBeanHandler;
import net.ymate.platform.core.util.ClassUtils;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/8/2 下午10:33
 * @version 1.0
 */
public class UnpackerHandler implements IBeanHandler {

    private IUnpackers __owner;

    public UnpackerHandler(IUnpackers owner) {
        __owner = owner;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object handle(Class<?> targetClass) throws Exception {
        if (ClassUtils.isInterfaceOf(targetClass, IUnpacker.class)) {
            __owner.registerUnpacker((Class<? extends IUnpacker>) targetClass);
        }
        return null;
    }
}
