/*
 * Copyright (c) 2007-2107, the original author or authors. All rights reserved.
 * 
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
 */
package net.ymate.framework.core.taglib;

import net.ymate.platform.core.i18n.I18N;

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
        return I18N.formatMessage(getResourceName(), getKey(), getDefaultValue());
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
