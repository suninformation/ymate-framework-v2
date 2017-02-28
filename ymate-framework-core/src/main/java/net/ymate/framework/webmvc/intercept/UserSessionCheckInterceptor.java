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
package net.ymate.framework.webmvc.intercept;

import net.ymate.framework.core.Optional;
import net.ymate.framework.core.util.WebUtils;
import net.ymate.framework.webmvc.ErrorCode;
import net.ymate.framework.webmvc.WebResult;
import net.ymate.framework.webmvc.support.UserSessionBean;
import net.ymate.platform.core.beans.intercept.IInterceptor;
import net.ymate.platform.core.beans.intercept.InterceptContext;
import net.ymate.platform.core.i18n.I18N;
import net.ymate.platform.core.util.ExpressionUtils;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.view.View;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 检查当前用户会话是否有效
 *
 * @author 刘镇 (suninformation@163.com) on 16/3/12 下午9:54
 * @version 1.0
 */
public class UserSessionCheckInterceptor implements IInterceptor {

    private static final String REDIRECT_URL = "redirect_url";

    public Object intercept(InterceptContext context) throws Exception {
        // 判断当前拦截器执行方向
        switch (context.getDirection()) {
            case BEFORE:
                UserSessionBean _sessionBean = UserSessionBean.current();
                if (_sessionBean == null) {
                    if (UserSessionBean.getSessionHandler() != null) {
                        _sessionBean = UserSessionBean.getSessionHandler().handle(context);
                    }
                } else if (!_sessionBean.isVerified()) {
                    _sessionBean.destroy();
                    _sessionBean = null;
                }
                if (_sessionBean == null) {
                    // 拼装跳转登录URL地址
                    HttpServletRequest _request = WebContext.getRequest();
                    StringBuffer _returnUrlBuffer = _request.getRequestURL();
                    String _queryStr = _request.getQueryString();
                    if (StringUtils.isNotBlank(_queryStr)) {
                        _returnUrlBuffer.append("?").append(_queryStr);
                    }
                    String _redirectUrl = StringUtils.defaultIfBlank(context.getOwner().getConfig().getParam(Optional.REDIRECT_LOGIN_URL), "login?redirect_url=${redirect_url}");
                    _redirectUrl = ExpressionUtils.bind(_redirectUrl).set(REDIRECT_URL, WebUtils.encodeURL(_returnUrlBuffer.toString())).getResult();
                    if (!StringUtils.startsWithIgnoreCase(_redirectUrl, "http://") && !StringUtils.startsWithIgnoreCase(_redirectUrl, "https://")) {
                        _redirectUrl = WebUtils.buildURL(_request, _redirectUrl, true);
                    }
                    //
                    if (WebUtils.isAjax(WebContext.getRequest())) {
                        String _resourceName = StringUtils.defaultIfBlank(context.getOwner().getConfig().getParam(Optional.I18N_RESOURCE_NAME), "messages");
                        String _message = StringUtils.defaultIfBlank(I18N.load(_resourceName, Optional.SYSTEM_SESSION_TIMEOUT_KEY), "Not login or session timeout, Login again.");
                        return WebResult
                                .CODE(ErrorCode.USER_SESSION_INVALID_OR_TIMEOUT)
                                .msg(_message)
                                .attr(REDIRECT_URL, _redirectUrl)
                                .toJSON();
                    }
                    return View.redirectView(_redirectUrl);
                } else {
                    // 更新会话最后活动时间
                    _sessionBean.touch();
                }
                break;
        }
        return null;
    }
}
