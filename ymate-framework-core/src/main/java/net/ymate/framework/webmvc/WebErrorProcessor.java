/*
 * Copyright (c) 2007-2107, the original author or authors. All rights reserved.
 *
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
 */
package net.ymate.framework.webmvc;

import net.ymate.framework.core.Optional;
import net.ymate.framework.core.util.ViewPathUtils;
import net.ymate.framework.core.util.WebUtils;
import net.ymate.platform.core.i18n.I18N;
import net.ymate.platform.core.util.RuntimeUtils;
import net.ymate.platform.log.Logs;
import net.ymate.platform.validation.ValidateResult;
import net.ymate.platform.webmvc.IRequestContext;
import net.ymate.platform.webmvc.IWebErrorProcessor;
import net.ymate.platform.webmvc.IWebMvc;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.view.IView;
import net.ymate.platform.webmvc.view.View;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.util.Map;

/**
 * 默认WebMVC框架异常错误处理器接口
 *
 * @author 刘镇 (suninformation@163.com) on 14/7/6 下午1:47
 * @version 1.0
 */
public class WebErrorProcessor implements IWebErrorProcessor {

    private IView __toErrorView(IWebMvc owner, int code, String msg) {
        IView _view = null;
        String _errorViewPath = StringUtils.defaultIfEmpty(owner.getOwner().getConfig().getParam(Optional.ERROR_VIEW), "error.jsp");
        if (".ftl".endsWith(_errorViewPath)) {
            _view = View.freemarkerView(owner, _errorViewPath);
        } else {
            _view = View.jspView(owner, _errorViewPath);
        }
        _view.addAttribute("ret", code);
        _view.addAttribute("msg", msg);
        //
        return _view;
    }

    public void onError(IWebMvc owner, Throwable e) {
        String _resourceName = StringUtils.defaultIfBlank(owner.getOwner().getConfig().getParam(Optional.I18N_RESOURCE_NAME), "messages");
        String _msg = I18N.formatMessage(_resourceName, Optional.SYSTEM_ERROR_DEFAULT_I18N_KEY, "System busy, please try again later!");
        IView _view = null;
        if (WebUtils.isAjax(WebContext.getRequest())) {
            _view = WebResultHelper.CODE(ErrorCode.INTERNAL_SYSTEM_ERROR).msg(_msg).toJSON();
        } else {
            _view = __toErrorView(owner, ErrorCode.INTERNAL_SYSTEM_ERROR, _msg);
        }
        try {
            Logs.get(owner.getOwner()).getLogger().error(RuntimeUtils.unwrapThrow(e));
        } catch (Throwable ignored) {
            e.printStackTrace(System.err);
        } finally {
            try {
                _view.render();
            } catch (Exception e1) {
                e1.printStackTrace(System.err);
            }
        }
    }

    public IView onValidation(IWebMvc owner, Map<String, ValidateResult> results) {
        IView _view = null;
        if (WebUtils.isAjax(WebContext.getRequest())) {
            WebResultHelper _result = WebResultHelper.CODE(ErrorCode.INVALID_PARAMS_VALIDATION);
            try {
                for (ValidateResult _vResult : results.values()) {
                    _result.attr(_vResult.getName(), _vResult.getMsg());
                }
                _view = _result.toJSON();
            } catch (Exception e) {
                Logs.get(owner.getOwner()).getLogger().error(RuntimeUtils.unwrapThrow(e));
            }
        } else {
            StringBuilder _message = new StringBuilder();
            for (ValidateResult _vResult : results.values()) {
                _message.append(_vResult.getMsg()).append("\n");
            }
            _view = __toErrorView(owner, ErrorCode.INVALID_PARAMS_VALIDATION, _message.toString().replace("\n", "<br/>"));
        }
        return _view;
    }

    public IView onConvention(IWebMvc owner, IRequestContext requestContext) throws Exception {
        String[] _fileTypes = {".html", ".jsp", ".ftl"};
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
                }
            }
        }
        return null;
    }
}