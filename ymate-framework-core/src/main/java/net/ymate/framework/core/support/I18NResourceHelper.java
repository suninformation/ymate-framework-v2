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

import net.ymate.platform.core.i18n.I18N;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 国际化资源加载辅助工具类
 *
 * @author 刘镇 (suninformation@163.com) on 2017/7/5 下午9:50
 * @version 1.0
 */
public class I18NResourceHelper {

    private IResourceAdapter __resourceAdapter;

    public static I18NResourceHelper create(String resourceName) {
        return new I18NResourceHelper(resourceName);
    }

    public static I18NResourceHelper create(IResourceAdapter resourceAdapter) {
        return new I18NResourceHelper(resourceAdapter);
    }

    private I18NResourceHelper(final String resourceName) {
        if (StringUtils.isBlank(resourceName)) {
            throw new NullArgumentException("resourceName");
        }
        __resourceAdapter = new IResourceAdapter() {

            @Override
            public String getResourceName() {
                return resourceName;
            }

            @Override
            public String getDefaultValue(String resourceKey) {
                return null;
            }
        };
    }

    private I18NResourceHelper(IResourceAdapter resourceAdapter) {
        if (resourceAdapter == null) {
            throw new NullArgumentException("resourceAdapter");
        }
        if (StringUtils.isBlank(resourceAdapter.getResourceName())) {
            throw new NullArgumentException("resourceName");
        }
        __resourceAdapter = resourceAdapter;
    }

    public String load(String resourceKey) {
        String _value = I18N.load(__resourceAdapter.getResourceName(), resourceKey);
        if (StringUtils.isBlank(_value)) {
            _value = __resourceAdapter.getDefaultValue(resourceKey);
        }
        return StringUtils.trimToEmpty(_value);
    }

    public String load(String resourceKey, String defaultValue) {
        String _value = I18N.load(__resourceAdapter.getResourceName(), resourceKey, defaultValue);
        if (StringUtils.isBlank(_value)) {
            _value = __resourceAdapter.getDefaultValue(resourceKey);
        }
        return StringUtils.trimToEmpty(_value);
    }

    public interface IResourceAdapter {

        /**
         * @return 资源名称
         */
        String getResourceName();

        /**
         * @param resourceKey 资源键名称
         * @return 返回该资源默认值, 若未匹配到则返回null
         */
        String getDefaultValue(String resourceKey);
    }

    public static class DefaultResourceAdapter implements IResourceAdapter {

        private Map<String, String> __defaultValues = new HashMap<String, String>();

        private String __resourceName;

        public DefaultResourceAdapter(String resourceName) {
            __resourceName = resourceName;
        }

        @Override
        public String getResourceName() {
            return __resourceName;
        }

        @Override
        public String getDefaultValue(String resourceKey) {
            return __defaultValues.get(resourceKey);
        }

        public DefaultResourceAdapter putValue(String resourceKey, String resourceValue) {
            __defaultValues.put(resourceKey, resourceValue);
            return this;
        }

        public DefaultResourceAdapter putValue(Map<String, String> resourceValues) {
            __defaultValues.putAll(resourceValues);
            return this;
        }
    }
}
