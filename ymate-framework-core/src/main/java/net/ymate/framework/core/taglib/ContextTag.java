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

import net.ymate.platform.core.YMP;
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

    public static String param(String key) {
        return paramIfDefault(key, null);
    }

    public static String paramIfDefault(String key, String defaultValue) {
        if (StringUtils.isNotBlank(key)) {
            WebContext _context = WebContext.getContext();
            if (_context != null) {
                return _context.getOwner().getOwner().getConfig().getParam(key, defaultValue);
            }
            return YMP.get().getConfig().getParam(key, defaultValue);
        }
        return null;
    }

    public static String contextAttribute(String attrKey) {
        if (StringUtils.isNotBlank(attrKey)) {
            WebContext _context = WebContext.getContext();
            if (_context != null) {
                return _context.getAttribute(attrKey);
            }
        }
        return null;
    }

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
