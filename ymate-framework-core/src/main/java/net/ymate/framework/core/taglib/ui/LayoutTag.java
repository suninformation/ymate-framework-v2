/*
 * Copyright (c) 2007-2016, the original author or authors. All rights reserved.
 *
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
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
			__ui.writerToBodyPart(WebUtils.replaceRegClear(__tmplContent));
		} catch (Exception e) {
			throw new JspException(RuntimeUtils.unwrapThrow(e));
		}
		return super.doEndTag();
	}

}
