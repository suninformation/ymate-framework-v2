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
		//
		this.__ui = null;
		this.attrKey = null;
		this.attrValue = null;
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
