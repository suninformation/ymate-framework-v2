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

import javax.servlet.http.HttpServletRequest;
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

    private String name;

    private boolean create;

    /**
     * 构造器
     */
    public TokenTag() {
    }

    @Override
    public int doStartTag() throws JspException {
        StringBuilder _contentSB = new StringBuilder();
        HttpSession session = pageContext.getSession();
        if (session != null) {
            String _tokenKey = TokenProcessHelper.TRANSACTION_TOKEN_KEY;
            String _token = null;
            if (create) {
                if (StringUtils.trimToNull(name) != null) {
                    _token = TokenProcessHelper.getInstance().saveToken((HttpServletRequest) pageContext.getRequest(), name);
                    _tokenKey += "|" + name;
                } else {
                    _token = TokenProcessHelper.getInstance().saveToken((HttpServletRequest) pageContext.getRequest());
                }
            } else {
                if (StringUtils.trimToNull(name) != null) {
                    _tokenKey += "|" + name;
                }
                _token = (String) session.getAttribute(_tokenKey);
            }
            //
            if (_token != null) {
                _contentSB.append("<input type=\"hidden\" name=\"").append(StringUtils.defaultIfBlank(name, TokenProcessHelper.TOKEN_KEY)).append("\" value=\"").append(_token).append("\">");
            }
        }
        try {
            pageContext.getOut().println(_contentSB.toString());
        } catch (IOException e) {
            throw new JspException(e.getMessage(), RuntimeUtils.unwrapThrow(e));
        }
        return SKIP_BODY;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }
}
