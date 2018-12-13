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
import net.ymate.framework.webmvc.support.UserSessionBean;
import net.ymate.platform.core.beans.intercept.IInterceptor;
import net.ymate.platform.core.beans.intercept.InterceptContext;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.core.util.ExpressionUtils;
import net.ymate.platform.webmvc.WebMVC;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.util.WebResult;
import net.ymate.platform.webmvc.util.WebUtils;
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

    @Override
    public Object intercept(InterceptContext context) throws Exception {
        // 判断当前拦截器执行方向
        if (Direction.BEFORE.equals(context.getDirection())) {
            UserSessionBean _sessionBean = UserSessionBean.current(context);
            if (_sessionBean == null) {
                HttpServletRequest _request = WebContext.getRequest();
                //
                String _redirectUrl = context.getOwner().getConfig().getParam(Optional.REDIRECT_LOGIN_URL);
                if (StringUtils.isBlank(_redirectUrl) || !StringUtils.startsWithIgnoreCase(_redirectUrl, "http://") && !StringUtils.startsWithIgnoreCase(_redirectUrl, "https://")) {
                    _redirectUrl = WebUtils.buildRedirectURL(context, _request, StringUtils.defaultIfBlank(WebUtils.buildRedirectURL(context, null), "login?redirect_url=${redirect_url}"), true);
                }
                _redirectUrl = ExpressionUtils.bind(_redirectUrl).set(Optional.REDIRECT_URL, WebUtils.appendQueryStr(_request, true)).getResult();
                //
                String _message = WebUtils.errorCodeI18n(WebMVC.get(context.getOwner()), WebResult.ErrorCode.USER_SESSION_INVALID_OR_TIMEOUT, "用户未授权登录或会话已过期，请重新登录");
                //
                if (WebUtils.isAjax(WebContext.getRequest(), true, true)) {
                    WebResult _result = WebResult
                            .create(WebResult.ErrorCode.USER_SESSION_INVALID_OR_TIMEOUT)
                            .msg(_message)
                            .attr(Optional.REDIRECT_URL, _redirectUrl);
                    return WebResult.formatView(_result, Type.Const.FORMAT_JSON);
                }
                if (context.getContextParams().containsKey(Optional.OBSERVE_SILENCE)) {
                    return View.redirectView(_redirectUrl);
                }
                return WebUtils.buildErrorView(WebContext.getContext().getOwner(), WebResult.ErrorCode.USER_SESSION_INVALID_OR_TIMEOUT, _message, _redirectUrl, BlurObject.bind(context.getOwner().getConfig().getParam(Optional.REDIRECT_TIME_INTERVAL)).toIntValue());
            } else {
                // 更新会话最后活动时间
                _sessionBean.touch();
            }
        }
        return null;
    }
}
