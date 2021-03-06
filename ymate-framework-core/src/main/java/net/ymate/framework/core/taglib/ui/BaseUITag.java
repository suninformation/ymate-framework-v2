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

import net.ymate.framework.core.util.ViewPathUtils;
import net.ymate.platform.webmvc.util.WebUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

/**
 * @author 刘镇 (suninformation@163.com) on 14-7-6
 * @version 1.0
 */
public abstract class BaseUITag extends BodyTagSupport {

    /**
     *
     */
    private static final long serialVersionUID = 8425802569545340622L;

    /**
     * 模板文件路径
     */
    private String src;

    /**
     * 皮肤主题名称
     */
    private String theme;

    /**
     * 插件名
     */
    private String plugin;

    /**
     * 字符编码
     */
    private String charsetEncoding;

    /**
     * 清理占位符
     */
    private boolean cleanup = true;

    //

    private StringBuilder __tmplBodyPart;

    private StringBuilder __tmplScriptPart;

    private StringBuilder __tmplMetaPart;

    private StringBuilder __tmplCssPart;

    private Map<String, String> __tmplPropertyPart;

    @Override
    public int doStartTag() throws JspException {
        __tmplBodyPart = new StringBuilder();
        __tmplScriptPart = new StringBuilder();
        __tmplMetaPart = new StringBuilder();
        __tmplCssPart = new StringBuilder();
        __tmplPropertyPart = new HashMap<String, String>();
        return super.doStartTag();
    }

    @Override
    public int doEndTag() throws JspException {
        this.src = null;
        this.theme = null;
        this.plugin = null;
        this.charsetEncoding = null;
        this.cleanup = true;
        return super.doEndTag();
    }

    @Override
    public void release() {
        __tmplBodyPart = null;
        __tmplScriptPart = null;
        __tmplMetaPart = null;
        __tmplCssPart = null;
        if (__tmplPropertyPart != null) {
            __tmplPropertyPart.clear();
            __tmplPropertyPart = null;
        }
        super.release();
    }

    public String mergeContent(String tmplContent) throws JspException {
        String _tmplContent = tmplContent;
        if (StringUtils.isNotBlank(_tmplContent)) {
            if (__tmplMetaPart.length() > 0) {
                _tmplContent = WebUtils.replaceRegText(_tmplContent, "meta", __tmplMetaPart.toString());
            }
            if (__tmplCssPart.length() > 0) {
                _tmplContent = WebUtils.replaceRegText(_tmplContent, "css", __tmplCssPart.toString());
            }
            if (__tmplScriptPart.length() > 0) {
                _tmplContent = WebUtils.replaceRegText(_tmplContent, "script", __tmplScriptPart.toString());
            }
            if (__tmplBodyPart.length() > 0) {
                _tmplContent = WebUtils.replaceRegText(_tmplContent, "body", __tmplBodyPart.toString());
            }
            for (Map.Entry<String, String> _entry : __tmplPropertyPart.entrySet()) {
                _tmplContent = WebUtils.replaceRegText(_tmplContent, _entry.getKey(), _entry.getValue());
            }
        }
        return _tmplContent;
    }

    public String buildSrcUrl() {
        if (StringUtils.isNotBlank(this.getSrc())) {
            StringBuilder _url = new StringBuilder();
            if (!this.getSrc().startsWith("/")) {
                if (StringUtils.isNotBlank(this.getPlugin())) {
                    _url.append(ViewPathUtils.pluginViewPath()).append(this.getPlugin()).append("/");
                } else {
                    _url.append(ViewPathUtils.rootViewPath());
                }
                if (StringUtils.isNotBlank(this.getTheme())) {
                    _url.append(this.getTheme()).append("/");
                }
            }
            _url.append(this.getSrc());
            if (!this.getSrc().endsWith(".jsp")) {
                _url.append(".jsp");
            }
            return _url.toString();
        }
        return this.getSrc();
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getPlugin() {
        return plugin;
    }

    public void setPlugin(String plugin) {
        this.plugin = plugin;
    }

    public String getCharsetEncoding() {
        return charsetEncoding;
    }

    public void setCharsetEncoding(String charsetEncoding) {
        this.charsetEncoding = charsetEncoding;
    }

    public boolean isCleanup() {
        return cleanup;
    }

    public void setCleanup(boolean cleanup) {
        this.cleanup = cleanup;
    }

    public String getBodyPartContent() {
        return __tmplBodyPart.toString();
    }

    public void writerToBodyPart(String content) {
        __tmplBodyPart.append(Matcher.quoteReplacement(content));
    }

    public String getMetaPartContent() {
        return __tmplMetaPart.toString();
    }

    public void writerToMetaPart(String content) {
        __tmplMetaPart.append(Matcher.quoteReplacement(content));
    }

    public String getCssPartContent() {
        return __tmplCssPart.toString();
    }

    public void writerToCssPart(String content) {
        __tmplCssPart.append(Matcher.quoteReplacement(content));
    }

    public String getScriptPartContent() {
        return __tmplScriptPart.toString();
    }

    public void writerToScriptPart(String content) {
        __tmplScriptPart.append(Matcher.quoteReplacement(content));
    }

    public void putProperty(String key, String value) {
        __tmplPropertyPart.put(key, Matcher.quoteReplacement(value));
    }

    protected Map<String, String> getPropertyPart() {
        return Collections.unmodifiableMap(this.__tmplPropertyPart);
    }
}
