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
 * Meta设置标签，用于模板脚本代码追加，其ParentTag为UILayoutTag和LayoutTag标签
 *
 * @author 刘镇 (suninformation@163.com) on 14-7-6
 * @version 1.0
 */
public class MetaTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7014634016889539200L;

	private BaseUITag __ui;

	private String attrKey;

	private String attrValue;

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
					this.setAttrValue(_propValue);
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
		StringBuilder _metaTmpl = new StringBuilder("<meta");
		boolean _isEmpty = true;
		if (StringUtils.isNotBlank(this.getAttrKey())) {
			_metaTmpl.append(" ").append(this.getAttrKey());
			_isEmpty = false;
		}
		if (StringUtils.isNotEmpty(this.getAttrValue())) {
			_metaTmpl.append(" ").append(this.getAttrValue());
			_isEmpty = false;
		}
		_metaTmpl.append(">\n");
		if (!_isEmpty) {
			__ui.writerToMetaPart(_metaTmpl.toString());
		}
		return super.doEndTag();
	}

	public String getAttrKey() {
		return attrKey;
	}

	public void setAttrKey(String attrKey) {
		this.attrKey = attrKey;
	}

	public String getAttrValue() {
		return attrValue;
	}

	public void setAttrValue(String attrValue) {
		this.attrValue = attrValue;
	}

}
