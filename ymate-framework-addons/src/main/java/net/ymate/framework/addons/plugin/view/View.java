/*
 * Copyright (c) 2007-2107, the original author or authors. All rights reserved.
 *
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
 */
package net.ymate.framework.addons.plugin.view;

import net.ymate.platform.plugin.IPlugin;
import net.ymate.platform.webmvc.view.impl.JspView;

/**
 * @author 刘镇 (suninformation@163.com) on 15/8/17 上午9:09
 * @version 1.0
 */
public class View extends net.ymate.platform.webmvc.view.View {

    public static JspView jspPluginView(IPlugin plugin, String path) {
        return JspPluginView.bind(plugin, path);
    }

    public static FreemarkerPluginView freemarkerPluginView(IPlugin plugin, String path) {
        return FreemarkerPluginView.bind(plugin, path);
    }
}
