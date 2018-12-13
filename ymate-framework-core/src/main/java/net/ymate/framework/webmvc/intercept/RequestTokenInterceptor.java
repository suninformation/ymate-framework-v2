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
import net.ymate.framework.core.support.TokenProcessHelper;
import net.ymate.platform.core.beans.intercept.IInterceptor;
import net.ymate.platform.core.beans.intercept.InterceptContext;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.util.CookieHelper;
import net.ymate.platform.webmvc.view.impl.HttpStatusView;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 检测当前请求令牌的合法性并重新生成令牌
 *
 * @author 刘镇 (suninformation@163.com) on 2017/11/22 上午3:05
 * @version 1.0
 */
public class RequestTokenInterceptor implements IInterceptor {

    @Override
    public Object intercept(InterceptContext context) throws Exception {
        if (Direction.BEFORE.equals(context.getDirection())) {
            HttpServletRequest _request = WebContext.getRequest();
            String _tokenName = context.getContextParams().get(Optional.REQUEST_TOKEN_NAME);
            if (StringUtils.isBlank(_tokenName)) {
                _tokenName = StringUtils.defaultIfBlank(context.getOwner().getConfig().getParam(Optional.REQUEST_TOKEN_NAME), "Request-Token");
            }
            // 分别尝试从请求参数、请求头和Cookies中获取令牌
            boolean _headerFlag = false;
            boolean _cookieFlag = false;
            CookieHelper _cookieHelper = null;
            String _tokenStr = _request.getParameter(_tokenName);
            if (StringUtils.isBlank(_tokenStr)) {
                _tokenStr = _request.getHeader(_tokenName);
                if (StringUtils.isBlank(_tokenStr)) {
                    _cookieHelper = CookieHelper.bind();
                    _tokenStr = _cookieHelper.getCookie(_tokenName).toStringValue();
                    _cookieFlag = StringUtils.isNotBlank(_tokenStr);
                } else {
                    _headerFlag = true;
                }
            }
            // 验证请求令牌
            boolean _flag = TokenProcessHelper.getInstance().isTokenValid(_request, _tokenName, _tokenStr, true);
            // 当令牌非请求参数传入时, 重新生成令牌
            if (_headerFlag || _cookieFlag) {
                _tokenStr = TokenProcessHelper.getInstance().saveToken(_request, _tokenName);
                if (_headerFlag) {
                    WebContext.getResponse().addHeader(_tokenName, _tokenStr);
                    CookieHelper.bind().removeCookie(_tokenName);
                } else {
                    _cookieHelper.allowUseHttpOnly().setCookie(_tokenName, _tokenStr);
                }
            }
            // 根据令牌验证结果返回
            if (!_flag) {
                return HttpStatusView.BAD_REQUEST;
            }
        }
        return null;
    }
}
