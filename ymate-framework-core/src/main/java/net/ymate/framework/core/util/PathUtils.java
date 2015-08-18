/*
 * Copyright (c) 2007-2107, the original author or authors. All rights reserved.
 *
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
 */
package net.ymate.framework.core.util;

import net.ymate.platform.plugin.Plugins;
import net.ymate.platform.webmvc.WebMVC;
import org.apache.commons.lang.StringUtils;

import java.io.File;

/**
 * @author 刘镇 (suninformation@163.com) on 15/8/15 下午3:26
 * @version 1.0
 */
public class PathUtils {

    private static String __BASE_VIEW_PATH;

    private static String __PLUGIN_VIEW_PATH;

    /**
     * @return 模板基准路径并以'/WEB-INF'开始，以'/'结束
     */
    public static String rootViewPath() {
        if (__BASE_VIEW_PATH == null) {
            __doInitViewPath();
        }
        return __BASE_VIEW_PATH;
    }

    /**
     * @return 插件模板基准路径，以'/WEB-INF'开始，以'/'结束
     */
    public static String pluginViewPath() {
        if (__PLUGIN_VIEW_PATH == null) {
            __doInitViewPath();
        }
        return __PLUGIN_VIEW_PATH;
    }

    private synchronized static void __doInitViewPath() {
        if (__BASE_VIEW_PATH == null && __PLUGIN_VIEW_PATH == null) {
            String _viewBasePath = StringUtils.trimToNull(WebMVC.get().getModuleCfg().getBaseViewPath());
            if (_viewBasePath == null || !(_viewBasePath = _viewBasePath.replaceAll("\\\\", "/")).startsWith("/WEB-INF/")) {
                _viewBasePath = "/WEB-INF/templates/";
            } else if (!_viewBasePath.endsWith("/")) {
                _viewBasePath += "/";
            }
            __BASE_VIEW_PATH = _viewBasePath;
            //
            // 为了适应Web环境JSP文件的特殊性(即不能引用工程路径外的JSP文件), 建议采用默认"/WEB-INF/plugins/
            String _viewPluginPath = "/WEB-INF/plugins/";
            try {
                File _pFile = Plugins.get().getPluginFactory().getPluginConfig().getPluginHome();
                String _pHome = _pFile == null ? null : _pFile.getPath();
                if (_pHome != null && (_pHome = _pHome.replaceAll("\\\\", "/")).contains("/WEB-INF/")) {
                    _viewPluginPath = StringUtils.substring(_pHome, _pHome.indexOf("/WEB-INF/"));
                    if (!_viewPluginPath.endsWith("/")) {
                        _viewPluginPath += "/";
                    }
                }
            } catch (Exception e) {
                // 一般出现异常的可能性只有插件包未引用导致的空指针
                // 出现任务异常都忽略,保证插件视图路径可用
                e.printStackTrace();
            }
            __PLUGIN_VIEW_PATH = _viewPluginPath;
        }
    }
}
