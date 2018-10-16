/*
 * Copyright 2007-2018 the original author or authors.
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
package net.ymate.framework.webmvc;

import com.alibaba.fastjson.JSON;
import net.ymate.framework.core.Optional;
import net.ymate.framework.core.support.ExceptionProcessHelper;
import net.ymate.framework.core.support.IExceptionProcessor;
import net.ymate.framework.core.util.ViewPathUtils;
import net.ymate.framework.core.util.WebUtils;
import net.ymate.framework.exception.*;
import net.ymate.platform.core.i18n.I18N;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.core.util.DateTimeUtils;
import net.ymate.platform.core.util.RuntimeUtils;
import net.ymate.platform.validation.ValidateResult;
import net.ymate.platform.webmvc.*;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.support.GenericResponseWrapper;
import net.ymate.platform.webmvc.view.IView;
import net.ymate.platform.webmvc.view.View;
import org.apache.commons.fileupload.FileUploadBase;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/2/22 下午10:47
 * @version 1.0
 */
public abstract class AbstractWebErrorProcessor implements IWebErrorProcessor, IWebInitializable {

    private static final Log _LOG = LogFactory.getLog(AbstractWebErrorProcessor.class);

    private boolean __inited;

    private String __resourceName;

    private String __errorDefaultI18nKey;

    private String __errorDefaultViewFormat;

    private boolean __disabledAnalysis;

    @Override
    public void init(WebMVC owner) throws Exception {
        if (!__inited) {
            __resourceName = StringUtils.defaultIfBlank(owner.getOwner().getConfig().getParam(Optional.I18N_RESOURCE_NAME), "messages");
            __errorDefaultI18nKey = StringUtils.defaultIfBlank(owner.getOwner().getConfig().getParam(Optional.SYSTEM_ERROR_DEFAULT_I18N_KEY), Optional.SYSTEM_ERROR_DEFAULT_I18N_KEY);
            __errorDefaultViewFormat = StringUtils.trimToEmpty(owner.getOwner().getConfig().getParam(Optional.ERROR_DEFAULT_VIEW_FORMAT)).toLowerCase();
            __disabledAnalysis = BlurObject.bind(owner.getOwner().getConfig().getParam(Optional.SYSTEM_EXCEPTION_ANALYSIS_DISABLED)).toBooleanValue();
            //
            __doInit(owner);
            //
            __inited = true;
        }
    }

    protected void __doInit(IWebMvc owner) throws Exception {
        getExceptionProcessHelper().registerProcessor(DataVersionMismatchException.class, new IExceptionProcessor() {
            @Override
            public IExceptionProcessor.Result process(Throwable target) throws Exception {
                return new IExceptionProcessor.Result(ErrorCode.DATA_VERSION_NOT_MATCH, i18nMsg(Optional.SYSTEM_DATA_VERSION_NOT_MATCH_KEY, "数据版本不匹配"));
            }
        }).registerProcessor(RequestForbiddenException.class, new IExceptionProcessor() {
            @Override
            public IExceptionProcessor.Result process(Throwable target) throws Exception {
                return new IExceptionProcessor.Result(ErrorCode.REQUEST_OPERATION_FORBIDDEN, i18nMsg(Optional.SYSTEM_REQUEST_OPERATION_FORBIDDEN_KEY, "请求的操作被禁止"));
            }
        }).registerProcessor(RequestMethodNotAllowedException.class, new IExceptionProcessor() {
            @Override
            public IExceptionProcessor.Result process(Throwable target) throws Exception {
                return new IExceptionProcessor.Result(ErrorCode.REQUEST_METHOD_NOT_ALLOWED, i18nMsg(Optional.SYSTEM_REQUEST_METHOD_NOT_ALLOWED_KEY, "请求方法不支持或不正确"));
            }
        }).registerProcessor(RequestUnauthorizedException.class, new IExceptionProcessor() {
            @Override
            public IExceptionProcessor.Result process(Throwable target) throws Exception {
                return new IExceptionProcessor.Result(ErrorCode.REQUEST_RESOURCE_UNAUTHORIZED, i18nMsg(Optional.SYSTEM_REQUEST_RESOURCE_UNAUTHORIZED_KEY, "请求的资源未授权或无权限"));
            }
        }).registerProcessor(ResourceNotFoundException.class, new IExceptionProcessor() {
            @Override
            public IExceptionProcessor.Result process(Throwable target) throws Exception {
                return new IExceptionProcessor.Result(ErrorCode.RESOURCE_NOT_FOUND_OR_NOT_EXIST, i18nMsg(Optional.SYSTEM_RESOURCE_NOT_FOUND_OR_NOT_EXIST_KEY, "访问的资源未找到或不存在"));
            }
        }).registerProcessor(UserSessionInvalidException.class, new IExceptionProcessor() {
            @Override
            public IExceptionProcessor.Result process(Throwable target) throws Exception {
                return new IExceptionProcessor.Result(ErrorCode.USER_SESSION_INVALID_OR_TIMEOUT, i18nMsg(Optional.SYSTEM_SESSION_TIMEOUT_KEY, "用户未授权登录或会话已过期"));
            }
        }).registerProcessor(FileUploadBase.FileSizeLimitExceededException.class, new IExceptionProcessor() {
            @Override
            public Result process(Throwable target) throws Exception {
                return new IExceptionProcessor.Result(ErrorCode.UPLOAD_FILE_SIZE_LIMIT_EXCEEDED, i18nMsg(Optional.UPLOAD_FILE_SIZE_LIMIT_EXCEEDED_KEY, "上传文件大小超出限制"));
            }
        }).registerProcessor(FileUploadBase.SizeLimitExceededException.class, new IExceptionProcessor() {
            @Override
            public Result process(Throwable target) throws Exception {
                return new IExceptionProcessor.Result(ErrorCode.UPLOAD_SIZE_LIMIT_EXCEEDED, i18nMsg(Optional.UPLOAD_SIZE_LIMIT_EXCEEDED_KEY, "上传文件总大小超出限制"));
            }
        }).registerProcessor(FileUploadBase.InvalidContentTypeException.class, new IExceptionProcessor() {
            @Override
            public Result process(Throwable target) throws Exception {
                return new IExceptionProcessor.Result(ErrorCode.UPLOAD_CONTENT_TYPE_INVALID, i18nMsg(Optional.UPLOAD_CONTENT_TYPE_INVALID_KEY, "上传文件类型无效"));
            }
        });
    }

    protected abstract IView __doShowErrorMsg(IWebMvc owner, int code, String msg, Map<String, Object> dataMap);

    @Override
    public void destroy() throws Exception {
    }

    public final ExceptionProcessHelper getExceptionProcessHelper() {
        return ExceptionProcessHelper.DEFAULT;
    }

    public final String getResourceName() {
        return __resourceName;
    }

    public final boolean isDisabledAnalysis() {
        return __disabledAnalysis;
    }

    public final boolean isInited() {
        return __inited;
    }

    public final String getErrorDefaultI18nKey() {
        return __errorDefaultI18nKey;
    }

    public final String getErrorDefaultViewFormat() {
        return __errorDefaultViewFormat;
    }

    public final String i18nMsg(String msgKey, String defaultMsg) {
        return I18N.formatMessage(__resourceName, StringUtils.defaultIfBlank(msgKey, __errorDefaultI18nKey), StringUtils.defaultIfBlank(defaultMsg, "系统繁忙, 请稍后重试!"));
    }

    @Override
    public void onError(IWebMvc owner, Throwable e) {
        try {
            Throwable _unwrapThrow = RuntimeUtils.unwrapThrow(e);
            if (_unwrapThrow instanceof ValidationResultException) {
                ValidationResultException _exception = (ValidationResultException) _unwrapThrow;
                if (_exception.getResultView() != null) {
                    _exception.getResultView().render();
                } else {
                    View.httpStatusView(_exception.getHttpStatus(), _exception.getMessage()).render();
                }
            } else {
                IExceptionProcessor _processor = getExceptionProcessHelper().bind(_unwrapThrow.getClass());
                if (_processor != null) {
                    IExceptionProcessor.Result _result = _processor.process(_unwrapThrow);
                    __doShowErrorMsg(owner, _result.getCode(), i18nMsg(_result.getMessage(), _result.getMessage()), null).render();
                } else {
                    if (!__disabledAnalysis && owner.getOwner().getConfig().isDevelopMode()) {
                        _LOG.error(exceptionDetail(_unwrapThrow));
                    } else {
                        _LOG.error("", _unwrapThrow);
                    }
                    __doShowErrorMsg(owner, ErrorCode.INTERNAL_SYSTEM_ERROR, i18nMsg(null, null), null).render();
                }
            }
        } catch (Throwable e1) {
            _LOG.warn("", RuntimeUtils.unwrapThrow(e1));
        }
    }

    public final String exceptionDetail(Throwable e) {
        IRequestContext _requestCtx = WebContext.getRequestContext();
        HttpServletRequest _request = WebContext.getRequest();
        WebContext _context = WebContext.getContext();
        //
        StringBuilder _errSB = new StringBuilder("An exception occurred at ").append(DateTimeUtils.formatTime(System.currentTimeMillis(), DateTimeUtils.YYYY_MM_DD_HH_MM_SS_SSS)).append(":\n");
        _errSB.append("-------------------------------------------------\n");
        _errSB.append("-- ThreadId: ").append(Thread.currentThread().getId()).append("\n");
        _errSB.append("-- RequestMapping: ").append(_requestCtx.getRequestMapping()).append("\n");
        _errSB.append("-- ResponseStatus: ").append(((GenericResponseWrapper) WebContext.getResponse()).getStatus()).append("\n");
        _errSB.append("-- Method: ").append(_requestCtx.getHttpMethod().name()).append("\n");
        _errSB.append("-- RemoteAddrs: ").append(JSON.toJSONString(WebUtils.getRemoteAddrs(_request))).append("\n");
        RequestMeta _meta = _context.getAttribute(RequestMeta.class.getName());
        if (_meta != null) {
            _errSB.append("-- Controller: ").append(_meta.getTargetClass().getName()).append(":").append(_meta.getMethod().getName()).append("\n");
        }
        _errSB.append("-- ContextAttributes:").append("\n");
        for (Map.Entry<String, Object> _entry : _context.getAttributes().entrySet()) {
            if (!StringUtils.startsWith(_entry.getKey(), "net.ymate.platform.webmvc")) {
                _errSB.append("\t  ").append(_entry.getKey()).append(": ").append(JSON.toJSONString(_entry.getValue())).append("\n");
            }
        }
        _errSB.append("-- Parameters:").append("\n");
        for (Map.Entry<String, Object> _entry : _context.getParameters().entrySet()) {
            _errSB.append("\t  ").append(_entry.getKey()).append(": ").append(JSON.toJSONString(_entry.getValue())).append("\n");
        }
        _errSB.append("-- Attributes:").append("\n");
        Enumeration _enum = _request.getAttributeNames();
        while (_enum.hasMoreElements()) {
            String _attrName = (String) _enum.nextElement();
            _errSB.append("\t  ").append(_attrName).append(": ").append(JSON.toJSONString(_request.getAttribute(_attrName))).append("\n");
        }
        _errSB.append("-- Headers:").append("\n");
        _enum = _request.getHeaderNames();
        while (_enum.hasMoreElements()) {
            String _headName = (String) _enum.nextElement();
            if ("cookie".equalsIgnoreCase(_headName)) {
                continue;
            }
            _errSB.append("\t  ").append(_headName).append(": ").append(JSON.toJSONString(_request.getHeader(_headName))).append("\n");
        }
        _errSB.append("-- Cookies:").append("\n");
        Cookie[] _cookies = _request.getCookies();
        if (_cookies != null) {
            for (Cookie _cookie : _cookies) {
                _errSB.append("\t  ").append(_cookie.getName()).append(": ").append(JSON.toJSONString(_cookie.getValue())).append("\n");
            }
        }
        _errSB.append("-- Session:").append("\n");
        for (Map.Entry<String, Object> _entry : _context.getSession().entrySet()) {
            _errSB.append("\t  ").append(_entry.getKey()).append(": ").append(JSON.toJSONString(_entry.getValue())).append("\n");
        }
        _errSB.append(exceptionToString(e));
        _errSB.append("-------------------------------------------------\n");
        //
        return _errSB.toString();
    }

    public final StringBuilder exceptionToString(Throwable e) {
        StringBuilder _errSB = new StringBuilder();
        if (e != null) {
            _errSB.append("-- Exception: ").append(e.getClass().getName()).append("\n");
            _errSB.append("-- Message: ").append(e.getMessage()).append("\n");
            //
            _errSB.append("-- StackTrace:\n");
            StackTraceElement[] _stacks = e.getStackTrace();
            for (StackTraceElement _stack : _stacks) {
                _errSB.append("\t  at ").append(_stack).append("\n");
            }
        }
        return _errSB;
    }

    @Override
    public IView onValidation(IWebMvc owner, Map<String, ValidateResult> results) {
        String _message = i18nMsg(Optional.SYSTEM_PARAMS_VALIDATION_INVALID_KEY, "请求参数验证无效");
        Map<String, Object> _dataMap = new HashMap<String, Object>();
        for (ValidateResult _vResult : results.values()) {
            _dataMap.put(_vResult.getName(), _vResult.getMsg());
        }
        //
        if (!WebUtils.isAjax(WebContext.getRequest(), true, true) && !"json".equals(getErrorDefaultViewFormat())) {
            // 拼装所有的验证消息
            _message = WebUtils.messageWithTemplate(owner.getOwner(), _message, results.values());
        }
        return __doShowErrorMsg(owner, ErrorCode.INVALID_PARAMS_VALIDATION, _message, _dataMap);
    }

    @Override
    public IView onConvention(IWebMvc owner, IRequestContext requestContext) throws Exception {
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
}
