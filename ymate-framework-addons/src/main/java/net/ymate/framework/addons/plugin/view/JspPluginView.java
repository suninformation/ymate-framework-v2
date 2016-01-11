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

import net.ymate.framework.core.util.ViewPathUtils;
import net.ymate.platform.plugin.IPlugin;
import net.ymate.platform.webmvc.WebMVC;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.view.impl.JspView;
import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * 基于插件资源的JSP视图实现
 *
 * @author 刘镇 (suninformation@163.com) on 14/7/6 下午1:11
 * @version 1.0
 */
public class JspPluginView extends JspView {

    private String __alias;

    public static JspPluginView bind(IPlugin plugin, String path) {
        return new JspPluginView(plugin, path);
    }

    public JspPluginView(IPlugin plugin, String path) {
        super(WebMVC.get(plugin.getPluginContext().getPluginFactory().getOwner()));
        __path = path;
        // 以插件别名为视图文件的路径,若别名为空则直接使用插件ID
        __alias = plugin.getPluginContext().getPluginMeta().getAlias();
        if (StringUtils.isBlank(__alias)) {
            __alias = plugin.getPluginContext().getPluginMeta().getId();
        }
    }

    @Override
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
            __path = ViewPathUtils.pluginViewPath().concat(__alias).concat("/templates/").concat(_mapping).concat(".jsp");
        } else {
            if (!__path.startsWith("/")) {
                __path = ViewPathUtils.pluginViewPath().concat(__alias).concat("/templates/").concat(__path);
            } else if (!__path.startsWith("/WEB-INF")) {
                __path = ViewPathUtils.pluginViewPath().concat(__alias).concat("/templates").concat(__path);
            }
            if (!__path.contains("?") && !__path.endsWith(".jsp")) {
                __path += ".jsp";
            }
        }
    }
}
