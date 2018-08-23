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
package net.ymate.framework.unpack.impl;

import net.ymate.framework.unpack.IUnpackers;
import net.ymate.framework.unpack.IUnpackersModuleCfg;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.lang.BlurObject;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2017/08/02 下午 22:27
 * @version 1.0
 */
public class DefaultModuleCfg implements IUnpackersModuleCfg {

    private boolean __isDisabled;

    private String[] __disabledUnpackers;

    public DefaultModuleCfg(YMP owner) {
        Map<String, String> _moduleCfgs = owner.getConfig().getModuleConfigs(IUnpackers.MODULE_NAME);
        //
        __isDisabled = BlurObject.bind(_moduleCfgs.get("disabled")).toBooleanValue();
        __disabledUnpackers = StringUtils.split(_moduleCfgs.get("disabled_unpacker_list"));
    }

    @Override
    public boolean isDisabled() {
        return __isDisabled;
    }

    @Override
    public String[] getDisabledUnpackers() {
        return __disabledUnpackers;
    }
}