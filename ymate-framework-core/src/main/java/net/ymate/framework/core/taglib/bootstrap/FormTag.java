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
package net.ymate.framework.core.taglib.bootstrap;

import net.ymate.framework.core.taglib.ElementsTag;
import org.apache.commons.lang.StringUtils;

/**
 * 表单
 *
 * @author 刘镇 (suninformation@163.com) on 16/11/12 下午11:31
 * @version 1.0
 */
public class FormTag extends ElementsTag {

    private boolean left;
    private boolean right;

    private boolean inline;
    private boolean horizontal;

    private boolean disabled;
    private boolean fieldset;
    private String legend;

    private String name;
    private String action;
    private String method;

    private String enctype;

    private boolean multipart;
    private boolean urlencoded;

    public FormTag() {
    }

    @Override
    protected void __doSetTagName() {
        this.set_tag("form");
    }

    @Override
    protected StringBuilder __doTagStart() {
        StringBuilder _classSB = new StringBuilder(StringUtils.trimToEmpty(this.get_class()));
        if (this.getParent() instanceof NavbarTag) {
            _classSB.append(" navbar-form");
            if (left) {
                _classSB.append(" navbar-left");
            } else if (right) {
                _classSB.append(" navbar-right");
            }
        }
        //
        if (inline) {
            _classSB.append(" form-inline");
        } else if (horizontal) {
            _classSB.append(" form-horizontal");
        }
        this.set_class(_classSB.toString());
        //
        if (StringUtils.isNotBlank(name)) {
            this.getDynamicAttributes().put("name", name);
        }
        if (StringUtils.isNotBlank(action)) {
            this.getDynamicAttributes().put("action", action);
        }
        if (StringUtils.isNotBlank(method)) {
            this.getDynamicAttributes().put("method", method);
        }
        if (multipart) {
            enctype = "multipart/form-data";
        } else if (urlencoded) {
            enctype = "application/x-www-form-urlencoded";
        }
        if (StringUtils.isNotBlank(enctype)) {
            this.getDynamicAttributes().put("enctype", enctype);
        }
        //
        return super.__doTagStart();
    }

    @Override
    protected StringBuilder __doTagContent(StringBuilder tagContent, StringBuilder bodyContent) {
        if (fieldset || disabled || StringUtils.isNotBlank(legend)) {
            StringBuilder _tmpSB = new StringBuilder("<fieldset");
            if (disabled) {
                _tmpSB.append(" disabled");
            }
            _tmpSB.append(">");
            if (StringUtils.isNotBlank(legend)) {
                _tmpSB.append("<legend>").append(legend).append("</legend>");
            }
            bodyContent.insert(0, _tmpSB).append("</fieldset>");
        }
        return super.__doTagContent(tagContent, bodyContent);
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isInline() {
        return inline;
    }

    public void setInline(boolean inline) {
        this.inline = inline;
    }

    public boolean isHorizontal() {
        return horizontal;
    }

    public void setHorizontal(boolean horizontal) {
        this.horizontal = horizontal;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public boolean isFieldset() {
        return fieldset;
    }

    public void setFieldset(boolean fieldset) {
        this.fieldset = fieldset;
    }

    public String getLegend() {
        return legend;
    }

    public void setLegend(String legend) {
        this.legend = legend;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getEnctype() {
        return enctype;
    }

    public void setEnctype(String enctype) {
        this.enctype = enctype;
    }

    public boolean isMultipart() {
        return multipart;
    }

    public void setMultipart(boolean multipart) {
        this.multipart = multipart;
    }

    public boolean isUrlencoded() {
        return urlencoded;
    }

    public void setUrlencoded(boolean urlencoded) {
        this.urlencoded = urlencoded;
    }
}
