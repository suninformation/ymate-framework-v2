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

import net.ymate.framework.core.util.WebUtils;
import net.ymate.platform.core.util.RuntimeUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;

/**
 * 布局标签，用于加载解析Layout定义文件，其上下文环境必须存在有UITag签标
 *
 * @author 刘镇 (suninformation@163.com) on 14-7-6
 * @version 1.0
 */
public class LayoutTag extends BaseUITag {

	/**
	 *
	 */
	private static final long serialVersionUID = 7959201563636313024L;

	private BaseUITag __ui;

	/**
	 * Layout模板文件内容
	 */
	private String __tmplContent;

	/**
	 * 自定义占位符名称, 若未提供则默认为body
	 */
	private String name;

	@Override
	public int doStartTag() throws JspException {
		__ui = (BaseUITag) this.getParent();
		if (__ui == null) {
			throw new JspException("Parent UITag or LayoutTag not found.");
		}
		try {
			if (StringUtils.isNotBlank(this.getSrc())) {
				__tmplContent = WebUtils.includeJSP(
						(HttpServletRequest) this.pageContext.getRequest(),
						(HttpServletResponse) this.pageContext.getResponse(),
						this.buildSrcUrl(), __ui.getCharsetEncoding());
			} else {
				__tmplContent = "";
			}
		} catch (Exception e) {
			throw new JspException(RuntimeUtils.unwrapThrow(e));
		}
		return super.doStartTag();
	}

	@Override
	public int doAfterBody() throws JspException {
		try {
			if (this.bodyContent != null) {
				String _layoutBody = StringUtils.defaultIfEmpty(this.bodyContent.getString(), "");
				if (StringUtils.isNotBlank(__tmplContent)) {
					this.writerToBodyPart(_layoutBody);
				} else {
					__tmplContent = _layoutBody;
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
		try {
			__ui.writerToMetaPart(this.getMetaPartContent());
			__ui.writerToCssPart(this.getCssPartContent());
			__ui.writerToScriptPart(this.getScriptPartContent());
			__tmplContent = this.mergeContent(StringUtils.defaultIfEmpty(__tmplContent, ""));
			//
			if (StringUtils.isNotBlank(name) && !"body".equalsIgnoreCase(name)) {
				__ui.putProperty(name, WebUtils.replaceRegClear(__tmplContent));
			} else {
				__ui.writerToBodyPart(WebUtils.replaceRegClear(__tmplContent));
			}
		} catch (Exception e) {
			throw new JspException(RuntimeUtils.unwrapThrow(e));
		}
		//
		this.__ui = null;
		this.__tmplContent = null;
		this.name = null;
		return super.doEndTag();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
