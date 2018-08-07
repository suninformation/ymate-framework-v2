/*
 * Copyright 2007-2017 the original author or authors.
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
import net.ymate.framework.webmvc.IUserSessionConfirmHandler;
import net.ymate.framework.webmvc.WebResult;
import net.ymate.framework.webmvc.support.UserSessionBean;
import net.ymate.platform.core.beans.intercept.IInterceptor;
import net.ymate.platform.core.beans.intercept.InterceptContext;
import net.ymate.platform.core.util.ClassUtils;
import net.ymate.platform.core.util.ExpressionUtils;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.view.View;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 在当前用户会话有效的前提下要求再次确认用户密码以保证操作的安全性, 该拦截器用于验证安全确认视图返回的数据的合法性
 *
 * @author 刘镇 (suninformation@163.com) on 17/4/25 下午3:53
 * @version 1.0
 */
public class UserSessionConfirmInterceptor implements IInterceptor {

    private static volatile IUserSessionConfirmHandler __sessionConfirmHandler;

    private static volatile boolean __initedFlag = false;

    @Override
    public Object intercept(InterceptContext context) throws Exception {
        switch (context.getDirection()) {
            case BEFORE:
                UserSessionBean _sessionBean = UserSessionBean.current();
                if (_sessionBean != null && _sessionBean.isVerified()) {
                    IUserSessionConfirmHandler _handler = getSessionConfirmHandler();
                    if (!_handler.handle(context)) {
                        HttpServletRequest _request = WebContext.getRequest();
                        StringBuffer _returnUrlBuffer = _request.getRequestURL();
                        String _queryStr = _request.getQueryString();
                        if (StringUtils.isNotBlank(_queryStr)) {
                            _returnUrlBuffer.append("?").append(_queryStr);
                        }
                        //
                        String _redirectUrl = WebUtils.buildRedirectURL(context, StringUtils.defaultIfBlank(context.getOwner().getConfig().getParam(Optional.CONFIRM_REDIRECT_URL), "confirm?redirect_url=${redirect_url}"), true);
                        _redirectUrl = ExpressionUtils.bind(_redirectUrl).set(Optional.REDIRECT_URL, WebUtils.encodeURL(_returnUrlBuffer.toString())).getResult();
                        //
                        if (WebUtils.isAjax(WebContext.getRequest(), true, true)) {
                            WebResult _result = WebResult.SUCCESS()
                                    .attr(Optional.REDIRECT_URL, _redirectUrl);
                            return WebResult.formatView(_result);
                        }
                        return View.redirectView(_redirectUrl);
                    }
                }
        }
        return null;
    }

    public static IUserSessionConfirmHandler getSessionConfirmHandler() {
        if (__sessionConfirmHandler == null && !__initedFlag) {
            synchronized (UserSessionBean.class) {
                String _handleClassName = WebContext.getContext().getOwner().getOwner().getConfig().getParam(Optional.SYSTEM_USER_SESSION_CONFIRM_HANDLER_CLASS);
                __sessionConfirmHandler = ClassUtils.impl(_handleClassName, IUserSessionConfirmHandler.class, UserSessionCheckInterceptor.class);
                if (__sessionConfirmHandler == null) {
                    __sessionConfirmHandler = IUserSessionConfirmHandler.DEFAULT;
                }
                __initedFlag = true;
            }
        }
        return __sessionConfirmHandler;
    }
}
