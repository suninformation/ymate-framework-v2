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
import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.lang.StringUtils;

/**
 * 已登录用户拦截器
 *
 * @author 刘镇 (suninformation@163.com) on 16/12/2 上午1:39
 * @version 1.0
 */
public class UserSessionAlreadyInterceptor implements IInterceptor {

    public Object intercept(InterceptContext context) throws Exception {
        switch (context.getDirection()) {
            case BEFORE:
                UserSessionBean _sessionBean = UserSessionBean.current();
                if (_sessionBean != null && !_sessionBean.isVerified()) {
                    _sessionBean.destroy();
                    _sessionBean = null;
                }
                if (_sessionBean != null) {
                    String _redirectUrl = StringUtils.defaultIfBlank(WebContext.getRequest().getParameter(Optional.REDIRECT_URL), context.getContextParams().get(Optional.REDIRECT_URL));
                    if (StringUtils.isBlank(_redirectUrl)) {
                        _redirectUrl = context.getOwner().getConfig().getParam(Optional.REDIRECT_HOME_URL);
                        if (StringUtils.isBlank(_redirectUrl)) {
                            _redirectUrl = StringUtils.defaultIfBlank(_redirectUrl, WebUtils.baseURL(WebContext.getRequest()));
                        }
                    }
                    if (!StringUtils.startsWithIgnoreCase(_redirectUrl, "http://") && !StringUtils.startsWithIgnoreCase(_redirectUrl, "https://")) {
                        _redirectUrl = WebUtils.buildURL(WebContext.getRequest(), _redirectUrl, true);
                    }
                    //
                    String _resourceName = StringUtils.defaultIfBlank(context.getOwner().getConfig().getParam(Optional.I18N_RESOURCE_NAME), "messages");
                    String _message = StringUtils.defaultIfBlank(I18N.load(_resourceName, Optional.SYSTEM_SESSION_AUTHORIZED_KEY), "用户已经授权登录");
                    if (WebUtils.isAjax(WebContext.getRequest())) {
                        return WebResult
                                .CODE(ErrorCode.USER_SESSION_AUTHORIZED)
                                .msg(_message)
                                .attr(Optional.REDIRECT_URL, _redirectUrl)
                                .toJSON();
                    }
                    return WebUtils.buildErrorView(WebContext.getContext().getOwner(), ErrorCode.USER_SESSION_AUTHORIZED, _message, _redirectUrl, 3);
                }
                break;
        }
        return null;
    }
}
