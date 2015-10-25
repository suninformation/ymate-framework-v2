/*
 * Copyright (c) 2007-2016, the original author or authors. All rights reserved.
 *
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
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
		boolean _isEmpty = true;
		if (StringUtils.isNotBlank(this.getSrc())) {
			_scriptTmpl.append(" src=\"").append(this.getSrc()).append("\"");
			_isEmpty = false;
		}
		_scriptTmpl.append(" type=\"text/javascript\">");
		if (_isEmpty && StringUtils.isNotEmpty(this.getValue())) {
			_scriptTmpl.append("\n<!--//\n").append(this.getValue()).append("\n").append("\n//-->\n</script>\n");
			_isEmpty = false;
		} else {
			_scriptTmpl.append("</script>\n");
		}
		if (!_isEmpty) {
			__ui.writerToScriptPart(_scriptTmpl.toString());
		}
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

}
