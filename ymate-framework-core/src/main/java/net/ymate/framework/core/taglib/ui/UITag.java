/*
 * Copyright (c) 2007-2107, the original author or authors. All rights reserved.
 *
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
 */
package net.ymate.framework.core.taglib.ui;

import net.ymate.framework.core.util.WebUtils;
import net.ymate.platform.core.util.RuntimeUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

/**
 * UI页面标签，用于页面模板及环境变量初始化
 *
 * @author 刘镇 (suninformation@163.com) on 14-7-6
 * @version 1.0
 */
public class UITag extends BaseUITag {

    /**
     *
     */
    private static final long serialVersionUID = 584494119933433838L;

    private boolean __isCurrentUI;

    /**
     * @return 返回当前线程中的UITag对象
     */
    public UITag currentUI() {
        return (UITag) this.pageContext.getRequest().getAttribute(UITag.class.getName());
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            if (currentUI() == null) {
                __isCurrentUI = true;
                this.pageContext.getRequest().setAttribute(UITag.class.getName(), this);
            }
        } catch (Exception e) {
            throw new JspException(RuntimeUtils.unwrapThrow(e));
        }
        return super.doStartTag();
    }

    @Override
    public int doAfterBody() throws JspException {
        try {
            if (this.bodyContent != null) {
                this.bodyContent.clearBody();
            }
        } catch (Exception e) {
            throw new JspException(RuntimeUtils.unwrapThrow(e));
        }
        return super.doAfterBody();
    }

    @Override
    public int doEndTag() throws JspException {
        if (__isCurrentUI) {
            try {
                /* UI模板文件内容*/
                String __tmplContent = WebUtils.includeJSP(
                        (HttpServletRequest) this.pageContext.getRequest(),
                        (HttpServletResponse) this.pageContext.getResponse(),
                        this.buildSrcUrl(), this.getCharsetEncoding());
                __tmplContent = this.mergeContent(StringUtils.defaultIfEmpty(__tmplContent, ""));
                this.pageContext.getOut().write(WebUtils.replaceRegClear(__tmplContent));
            } catch (Exception e) {
                throw new JspException(RuntimeUtils.unwrapThrow(e));
            }
        }
        return super.doEndTag();
    }

}
