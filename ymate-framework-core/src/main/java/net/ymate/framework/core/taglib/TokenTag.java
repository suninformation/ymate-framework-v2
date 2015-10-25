/*
 * Copyright (c) 2007-2016, the original author or authors. All rights reserved.
 * 
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
 */
package net.ymate.framework.core.taglib;

import net.ymate.framework.core.support.TokenProcessHelper;
import net.ymate.platform.core.util.RuntimeUtils;

import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;

/**
 * Token标签
 *
 * @author 刘镇 (suninformation@163.com) on 14-7-6
 * @version 1.0
 */
public class TokenTag extends TagSupport {

    /**
     *
     */
    private static final long serialVersionUID = 3543848202628493208L;

    private boolean xhtml = true;

    /**
     * 构造器
     */
    public TokenTag() {
    }

    @Override
    public int doStartTag() throws JspException {
        StringBuilder results = new StringBuilder();
        HttpSession session = pageContext.getSession();

        if (session != null) {
            String token =
                    (String) session.getAttribute(TokenProcessHelper.TRANSACTION_TOKEN_KEY);

            if (token != null) {
                results.append("<div><input type=\"hidden\" name=\"");
                results.append(TokenProcessHelper.TOKEN_KEY);
                results.append("\" value=\"");
                results.append(token);
                if (this.isXhtml()) {
                    results.append("\" />");
                } else {
                    results.append("\" >");
                }
                results.append("</div>");
            }
        }
        try {
            pageContext.getOut().println(results.toString());
        } catch (IOException e) {
            throw new JspException(e.getMessage(), RuntimeUtils.unwrapThrow(e));
        }
        return SKIP_BODY;
    }

    /**
     * @return the xhtml
     */
    public boolean isXhtml() {
        return xhtml;
    }

    /**
     * @param xhtml the xhtml to set
     */
    public void setXhtml(boolean xhtml) {
        this.xhtml = xhtml;
    }

}
