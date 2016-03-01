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
package net.ymate.framework.core.taglib;

import net.ymate.framework.core.Optional;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.i18n.I18N;
import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;

/**
 * 国际化资源加载标签
 *
 * @author 刘镇 (suninformation@163.com) on 14-7-6
 * @version 1.0
 */
public class I18nTag extends AbstractTagSupport {

    /**
     *
     */
    private static final long serialVersionUID = 2418822724624807336L;

    private String resourceName;
    private String key;
    private String defaultValue;

    @Override
    protected Object doProcessTagData() throws JspException {
        String _defaultResourceName = StringUtils.defaultIfBlank(YMP.get().getConfig().getParam(Optional.I18N_RESOURCE_NAME), "messages");
        return I18N.formatMessage(StringUtils.defaultIfBlank(getResourceName(), _defaultResourceName), getKey(), getDefaultValue());
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

}
