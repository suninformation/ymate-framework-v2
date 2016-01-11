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
 * CSS设置标签，用于模板脚本代码追加，其ParentTag为UILayoutTag和LayoutTag标签
 *
 * @author 刘镇 (suninformation@163.com) on 14-7-6
 * @version 1.0
 */
public class CssTag extends BodyTagSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1975747968925711584L;

	private BaseUITag __ui;

	private String href;

	private String rel;

	private String media;

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
				this.bodyContent.clearBody();
			}
		} catch (Exception e) {
			throw new JspException(RuntimeUtils.unwrapThrow(e));
		}
		return super.doAfterBody();
	}

	@Override
	public int doEndTag() throws JspException {
		StringBuilder _metaTmpl = new StringBuilder("<link");
		boolean _isEmpty = true;
		if (StringUtils.isNotBlank(this.getHref())) {
			_metaTmpl.append(" href=\"").append(this.getHref()).append("\"");
			_isEmpty = false;
		}
		if (!_isEmpty) {
			if (StringUtils.isBlank(this.getRel())) {
				this.setRel("stylesheet");
			}
			_metaTmpl.append(" rel=\"").append(this.getRel()).append("\"");
			//
			if (StringUtils.isNotBlank(this.getMedia())) {
				_metaTmpl.append(" media=\"").append(this.getMedia()).append("\"");
			}
			_metaTmpl.append(">\n");
			__ui.writerToCssPart(_metaTmpl.toString());
		}
		return super.doEndTag();
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public String getMedia() {
		return media;
	}

	public void setMedia(String media) {
		this.media = media;
	}

}
