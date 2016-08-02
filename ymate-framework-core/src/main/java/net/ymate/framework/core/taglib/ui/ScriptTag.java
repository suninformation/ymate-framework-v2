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
package net.ymate.framework.core.taglib.ui;

import net.ymate.platform.core.util.RuntimeUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;

/**
 * 脚本设置标签，用于模板脚本代码追加，其ParentTag为UILayoutTag和LayoutTag标签
 *
 * @author 刘镇 (suninformation@163.com) on 14-7-6
 * @version 1.0
 */
public class ScriptTag extends BodyTagSupport {

	/**
	 *
	 */
	private static final long serialVersionUID = -2774067114541501535L;

	private BaseUITag __ui;

	private String src;

	private String value;

	private String type;

	private String wrapper;

	@Override
	public int doStartTag() throws JspException {
		__ui = (BaseUITag) this.getParent();
		if (__ui == null) {
			throw new JspException("Parent UITag or LayoutTag not found.");
		}
		return super.doStartTag();
	}

	@Override
	public int doAfterBody() throws JspException {
		try {
			if (this.bodyContent != null) {
				String _propValue = this.bodyContent.getString();
				if (StringUtils.isNotBlank(_propValue)) {
					this.setValue(_propValue);
				}
				this.bodyContent.clearBody();
			}
		} catch (Exception e) {
			throw new JspException(RuntimeUtils.unwrapThrow(e));
		}
		return super.doAfterBody();
	}

	@Override
	public int doEndTag() throws JspException {
		StringBuilder _scriptTmpl = new StringBuilder("<script");
		if (StringUtils.isNotBlank(this.getId())) {
			_scriptTmpl.append(" id=\"").append(this.getId()).append("\"");
		}
		boolean _isEmpty = true;
		if (StringUtils.isNotBlank(this.getSrc())) {
			_scriptTmpl.append(" src=\"").append(this.getSrc()).append("\"");
			_isEmpty = false;
		}
		_scriptTmpl.append(" type=\"").append(StringUtils.defaultIfBlank(this.getType(), "text/javascript")).append("\">");
		if (_isEmpty && StringUtils.isNotEmpty(this.getValue())) {
			String _wrapper = StringUtils.defaultIfBlank(this.getWrapper(), "script");
			String _content = StringUtils.substringBetween(this.getValue(), "<" + _wrapper + ">", "</" + _wrapper + ">");
			if (StringUtils.isNotBlank(_content)) {
				this.setValue(_content);
			}
			_scriptTmpl.append(this.getValue()).append("\n");
			_isEmpty = false;
		}
		_scriptTmpl.append("</script>\n");
		if (!_isEmpty) {
			__ui.writerToScriptPart(_scriptTmpl.toString());
		}
		//
		this.__ui = null;
		this.src = null;
		this.value = null;
		this.id = null;
		this.type = null;
		this.wrapper = null;
		return super.doEndTag();
	}

	public String getSrc() {
		return src;
	}

	public void setSrc(String src) {
		this.src = src;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWrapper() {
		return wrapper;
	}

	public void setWrapper(String wrapper) {
		this.wrapper = wrapper;
	}
}
