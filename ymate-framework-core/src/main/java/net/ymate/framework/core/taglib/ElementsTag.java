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

import net.ymate.platform.core.util.RuntimeUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.DynamicAttributes;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 16/11/12 上午2:48
 * @version 1.0
 */
public class ElementsTag extends BodyTagSupport implements DynamicAttributes {

    private Map<String, Object> __dynamicAttributes = new LinkedHashMap<String, Object>();

    private String _id;

    private String _class;

    private String _style;

    private String _tag;

    private boolean _unclosed;

    public ElementsTag() {
    }

    protected void __doSetTagName() {
        _tag = StringUtils.defaultIfBlank(_tag, "div");
    }

    protected StringBuilder __doTagStart() {
        __doSetTagName();
        //
        StringBuilder _tagSB = new StringBuilder("<").append(_tag);
        if (StringUtils.isNotBlank(_id)) {
            _tagSB.append(" id=\"").append(_id).append("\"");
        }
        if (StringUtils.isNotBlank(_class)) {
            _tagSB.append(" class=\"").append(_class).append("\"");
        }
        if (StringUtils.isNotBlank(_style)) {
            _tagSB.append(" style=\"").append(_style).append("\"");
        }
        for (Map.Entry<String, Object> _entry : __dynamicAttributes.entrySet()) {
            if (null != _entry.getValue()) {
                if (_entry.getValue() instanceof String) {
                    String _v = StringUtils.trimToNull((String) _entry.getValue());
                    if (_v != null) {
                        if (_v.equals(_entry.getKey())) {
                            _tagSB.append(" ").append(_entry.getKey());
                        } else {
                            _tagSB.append(" ").append(_entry.getKey()).append("=\"").append(_v).append("\"");
                        }
                    }
                } else {
                    _tagSB.append(" ").append(_entry.getKey()).append("=\"").append(_entry.getValue()).append("\"");
                }
            }
        }
        return _tagSB.append(">");
    }

    protected StringBuilder __doTagEnd(StringBuilder tagContent) {
        if (!_unclosed) {
            tagContent.append("</").append(_tag).append(">");
        }
        return tagContent;
    }

    protected StringBuilder __doTagContent(StringBuilder tagContent, StringBuilder bodyContent) {
        return __doTagEnd(tagContent.append(bodyContent));
    }

    @Override
    public int doEndTag() throws JspException {
        try {
            StringBuilder _tagContent = __doTagStart();
            if (this.getBodyContent() != null) {
                __doTagContent(_tagContent, new StringBuilder(this.getBodyContent().getString()));
                this.getBodyContent().clearBody();
            } else {
                __doTagContent(_tagContent, new StringBuilder(0));
            }
            if (_tagContent != null && _tagContent.length() > 0) {
                pageContext.getOut().write(_tagContent.toString());
            }
        } catch (IOException e) {
            throw new JspException(RuntimeUtils.unwrapThrow(e));
        }
        this._id = null;
        this._class = null;
        this._style = null;
        this._tag = null;
        this._unclosed = false;
        this.__dynamicAttributes.clear();
        return super.doEndTag();
    }

    public Map<String, Object> getDynamicAttributes() {
        return __dynamicAttributes;
    }

    public Object getDynamicAttribute(String attrName) {
        return __dynamicAttributes.get(attrName);
    }

    public void setDynamicAttribute(String uri, String localName, Object value) throws JspException {
        __dynamicAttributes.put(localName, value);
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

    public String get_style() {
        return _style;
    }

    public void set_style(String _style) {
        this._style = _style;
    }

    public String get_tag() {
        return _tag;
    }

    public void set_tag(String _tag) {
        this._tag = _tag;
    }

    public boolean get_unclosed() {
        return _unclosed;
    }

    public void set_unclosed(boolean _unclosed) {
        this._unclosed = _unclosed;
    }
}
