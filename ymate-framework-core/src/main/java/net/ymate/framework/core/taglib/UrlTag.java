/*
 * Copyright (c) 2007-2016, the original author or authors. All rights reserved.
 * 
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
 */
package net.ymate.framework.core.taglib;

import net.ymate.framework.core.util.WebUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;

/**
 * URL标签，可根据BaseUrl基准路径生成请求URL或直接输出BaseUrl
 *
 * @author 刘镇 (suninformation@163.com) on 14-7-6
 * @version 1.0
 */
public class UrlTag extends AbstractTagSupport {

    /**
     *
     */
    private static final long serialVersionUID = 9177172857130896780L;

    private String path;

    private boolean withBase;

    @Override
    protected Object doProcessTagData() throws JspException {
        if (StringUtils.isNotBlank(getPath())) {
            return WebUtils.buildURL((HttpServletRequest) pageContext.getRequest(), getPath(), isWithBase());
        }
        return WebUtils.baseURL((HttpServletRequest) pageContext.getRequest());
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isWithBase() {
        return withBase;
    }

    public void setWithBase(boolean withBase) {
        this.withBase = withBase;
    }

}
