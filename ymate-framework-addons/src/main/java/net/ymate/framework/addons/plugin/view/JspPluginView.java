/*
 * Copyright (c) 2007-2107, the original author or authors. All rights reserved.
 *
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
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
