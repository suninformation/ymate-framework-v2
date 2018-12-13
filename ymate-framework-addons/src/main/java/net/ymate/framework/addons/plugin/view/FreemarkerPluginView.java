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
package net.ymate.framework.addons.plugin.view;

import freemarker.template.Configuration;
import net.ymate.framework.core.util.ViewPathUtils;
import net.ymate.platform.core.support.FreemarkerConfigBuilder;
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

    private static Configuration __freemarkerConfig;

    private String __path;

    private String __alias;

    public static FreemarkerPluginView bind(IPlugin plugin, String path) {
        return new FreemarkerPluginView(plugin, path);
    }

    public FreemarkerPluginView(IPlugin plugin, String path) {
        if (__freemarkerConfig == null) {
            __doViewInit(WebMVC.get(plugin.getPluginContext().getPluginFactory().getOwner()));
        }
        __path = path;
        // 以插件别名为视图文件的路径,若别名为空则直接使用插件ID
        __alias = plugin.getPluginContext().getPluginMeta().getAlias();
        if (StringUtils.isBlank(__alias)) {
            __alias = plugin.getPluginContext().getPluginMeta().getId();
        }
    }

    /**
     * @return 返回当前模板引擎配置对象
     */
    public Configuration getEngineConfig() {
        return __freemarkerConfig;
    }

    private void __doProcessPath() {
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

    @Override
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
    protected synchronized void __doViewInit(IWebMvc owner) {
        super.__doViewInit(owner);
        if (__freemarkerConfig == null) {
            try {
                FreemarkerConfigBuilder _builder = FreemarkerConfigBuilder.create();
                if (__baseViewPath.startsWith("/WEB-INF")) {
                    __freemarkerConfig = _builder.addTemplateFileDir(new File(RuntimeUtils.getRootPath(), StringUtils.substringAfter(__baseViewPath, "/WEB-INF/"))).build();
                } else {
                    __freemarkerConfig = _builder.addTemplateFileDir(new File(__baseViewPath)).build();
                }
            } catch (IOException e) {
                throw new Error(RuntimeUtils.unwrapThrow(e));
            }
        }
    }
}
