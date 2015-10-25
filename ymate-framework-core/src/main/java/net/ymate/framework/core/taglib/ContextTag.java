/*
 * Copyright (c) 2007-2016, the original author or authors. All rights reserved.
 * 
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
 */
package net.ymate.framework.core.taglib;

import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;

/**
 * YMP框架环境的Web应用上下文对象属性存取标签
 *
 * @author 刘镇 (suninformation@163.com) on 14-7-6
 * @version 1.0
 */
public class ContextTag extends AbstractTagSupport {

    /**
     *
     */
    private static final long serialVersionUID = 5502642633702679976L;

    private String key;

    private String value;

    @Override
    protected Object doProcessTagData() throws JspException {
        if (StringUtils.isBlank(getValue())) {
            return WebContext.getContext().getAttribute(getKey());
        } else {
            WebContext.getContext().addAttribute(getKey(), getValue());
        }
        return null;
    }

    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }

    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }

    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }

}
