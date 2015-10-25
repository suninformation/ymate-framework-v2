/*
 * Copyright (c) 2007-2016, the original author or authors. All rights reserved.
 * 
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
 */
package net.ymate.framework.core.taglib;

import net.ymate.platform.core.YMP;

import javax.servlet.jsp.JspException;

/**
 * 判断当前平台是否运行于开发模式，若为true则执行标签体
 *
 * @author 刘镇 (suninformation@163.com) on 14-7-6
 * @version 1.0
 */
public class IsDevModeTag extends AbstractTagSupport {

    /**
     *
     */
    private static final long serialVersionUID = 8204360518278690940L;

    @Override
    protected Object doProcessTagData() throws JspException {
        return YMP.get().getConfig().isDevelopMode() ? true : null;
    }

}
