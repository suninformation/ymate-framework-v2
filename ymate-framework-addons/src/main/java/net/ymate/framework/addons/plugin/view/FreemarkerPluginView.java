/*
 * Copyright (c) 2007-2107, the original author or authors. All rights reserved.
 *
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
 */
package net.ymate.framework.addons.plugin.view;

import freemarker.cache.FileTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import net.ymate.framework.core.util.ViewPathUtils;
import net.ymate.platform.core.util.RuntimeUtils;
import net.ymate.platform.plugin.IPlugin;
import net.ymate.platform.webmvc.IWebMvc;
import net.ymate.platform.webmvc.WebMVC;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.view.AbstractView;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.Map;

/**
 * 基于插件资源的Freemarker视图实现
 *
 * @author 刘镇 (suninformation@163.com) on 14/7/6 下午4:49
 * @version 1.0
 */
public class FreemarkerPluginView extends AbstractView {

    protected static Configuration __freemarkerConfig;

    private String __path;

    private String __alias;

    public static FreemarkerPluginView bind(IPlugin plugin, String path) {
        return new FreemarkerPluginView(plugin, path);
    }

    public FreemarkerPluginView(IPlugin plugin, String path) {
        __path = path;
        // 以插件别名为视图文件的路径,若别名为空则直接使用插件ID
        __alias = plugin.getPluginContext().getPluginMeta().getAlias();
        if (StringUtils.isBlank(__alias)) {
            __alias = plugin.getPluginContext().getPluginMeta().getId();
        }
        if (__freemarkerConfig == null) {
            __doInitConfiguration(WebMVC.get(plugin.getPluginContext().getPluginFactory().getOwner()));
        }
    }

    protected void __doProcessPath() {
        if (StringUtils.isNotBlank(__contentType)) {
            WebContext.getResponse().setContentType(__contentType);
        }
        for (Map.Entry<String, Object> _entry : __attributes.entrySet()) {
            WebContext.getRequest().setAttribute(_entry.getKey(), _entry.getValue());
        }
        if (StringUtils.isBlank(__path)) {
            String _mapping = WebContext.getRequestContext().getRequestMapping();
            if (_mapping.charAt(0) == '/') {
                _mapping = _mapping.substring(1);
            }
            if (_mapping.endsWith("/")) {
                _mapping = _mapping.substring(0, _mapping.length() - 1);
            }
            __path = __alias.concat("/templates/").concat(_mapping).concat(".ftl");
        } else {
            if (!__path.startsWith("/")) {
                __path = __alias.concat("/templates/").concat(__path);
            } else if (__path.startsWith(ViewPathUtils.pluginViewPath())) {
                __path = StringUtils.substringAfter(__path, ViewPathUtils.pluginViewPath());
            }
            if (!__path.endsWith(".ftl")) {
                __path += ".ftl";
            }
        }
    }

    protected void __doRenderView() throws Exception {
        __doProcessPath();
        __freemarkerConfig.getTemplate(__path, WebContext.getContext().getLocale()).process(__attributes, WebContext.getResponse().getWriter());
    }

    @Override
    public void render(OutputStream output) throws Exception {
        __doProcessPath();
        __freemarkerConfig.getTemplate(__path, WebContext.getContext().getLocale()).process(__attributes, new BufferedWriter(new OutputStreamWriter(output)));
    }

    /**
     * 初始化Freemarker插件配置参数(全局唯一)
     *
     * @param owner 所属WebMVC框架管理器
     */
    @Override
    protected synchronized void __doInitConfiguration(IWebMvc owner) {
        // 初始化Freemarker模板引擎配置
        if (__freemarkerConfig == null) {
            __freemarkerConfig = new Configuration(Configuration.VERSION_2_3_22);
            __freemarkerConfig.setDefaultEncoding(owner.getModuleCfg().getDefaultCharsetEncoding());
            __freemarkerConfig.setTemplateExceptionHandler(TemplateExceptionHandler.HTML_DEBUG_HANDLER);
            //
            try {
                __freemarkerConfig.setTemplateLoader(new FileTemplateLoader(new File(ViewPathUtils.pluginViewPath())));
            } catch (IOException e) {
                throw new Error(RuntimeUtils.unwrapThrow(e));
            }
        }
    }
}
