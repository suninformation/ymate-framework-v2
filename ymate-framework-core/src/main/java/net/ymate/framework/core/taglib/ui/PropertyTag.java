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
 * 属性设置标签，用于模板自定义变量替换或追加，其ParentTag为UILayoutTag和LayoutTag标签
 *
 * @author 刘镇 (suninformation@163.com) on 14-7-6
 * @version 1.0
 */
public class PropertyTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3493408190832765819L;

	private BaseUITag __ui;

	private String name;

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
		if (StringUtils.isNotBlank(this.getName()) && StringUtils.isNotEmpty(this.getValue())) {
			__ui.putProperty(this.getName(), this.getValue());
		}
		return super.doEndTag();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
