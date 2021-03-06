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

import net.ymate.platform.webmvc.WebMVC;
import net.ymate.platform.webmvc.util.CookieHelper;
import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;

/**
 * @author 刘镇 (suninformation@163.com) on 16/12/30 上午1:40
 * @version 1.0
 */
public class CookiesTag extends AbstractTagSupport {

    private String key;

    @Override
    protected Object doProcessTagData() throws JspException {
        if (StringUtils.isBlank(key)) {
            return "";
        }
        return CookieHelper.bind(WebMVC.get()).getCookie(key).toStringValue();
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
