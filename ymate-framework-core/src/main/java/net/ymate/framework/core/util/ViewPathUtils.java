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
package net.ymate.framework.core.util;

import net.ymate.platform.plugin.Plugins;
import net.ymate.platform.webmvc.IRequestContext;
import net.ymate.platform.webmvc.IWebMvc;
import net.ymate.platform.webmvc.WebMVC;
import net.ymate.platform.webmvc.view.IView;
import net.ymate.platform.webmvc.view.View;
import org.apache.commons.lang.StringUtils;

import java.io.File;

/**
 * @author 刘镇 (suninformation@163.com) on 15/8/15 下午3:26
 * @version 1.0
 */
public class ViewPathUtils {

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

    public static IView convention(IWebMvc owner, IRequestContext requestContext) throws Exception {
        String[] _fileTypes = {".html", ".jsp", ".ftl", ".vm", ".btl"};
        for (String _fileType : _fileTypes) {
            // 判断插件目录下是否存在视图文件
            File _targetFile = new File(ViewPathUtils.pluginViewPath(), requestContext.getRequestMapping() + _fileType);
            if (_targetFile.exists()) {
                if (".html".equals(_fileType)) {
                    return View.htmlView(owner, requestContext.getRequestMapping().substring(1));
                } else if (".jsp".equals(_fileType)) {
                    return View.jspView(owner, requestContext.getRequestMapping().substring(1));
                } else if (".ftl".equals(_fileType)) {
                    return View.freemarkerView(owner, requestContext.getRequestMapping().substring(1));
                } else if (".vm".equals(_fileType)) {
                    return View.velocityView(owner, requestContext.getRequestMapping().substring(1));
                } else if (".btl".equals(_fileType)) {
                    return View.beetlView(owner, requestContext.getRequestMapping().substring(1));
                }
            }
        }
        return null;
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
                File _pFile = Plugins.get().getConfig().getPluginHome();
                String _pHome = _pFile == null ? null : _pFile.getPath();
                if (_pHome != null && (_pHome = _pHome.replaceAll("\\\\", "/")).contains("/WEB-INF/")) {
                    _viewPluginPath = StringUtils.substring(_pHome, _pHome.indexOf("/WEB-INF/"));
                    if (!_viewPluginPath.endsWith("/")) {
                        _viewPluginPath += "/";
                    }
                }
            } catch (Throwable ignored) {
                // 一般出现异常的可能性只有插件包未引用导致的NoClassDefFoundError, 可忽略
            }
            __PLUGIN_VIEW_PATH = _viewPluginPath;
        }
    }
}
