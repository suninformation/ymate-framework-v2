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

import net.ymate.framework.core.support.TokenProcessHelper;
import net.ymate.platform.core.util.RuntimeUtils;
import org.apache.commons.lang.StringUtils;

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

    private String name;

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
            String _tokenKey = TokenProcessHelper.TRANSACTION_TOKEN_KEY;
            if (StringUtils.isNotBlank(name)) {
                _tokenKey += "|" + name;
            }
            String token = (String) session.getAttribute(_tokenKey);

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
