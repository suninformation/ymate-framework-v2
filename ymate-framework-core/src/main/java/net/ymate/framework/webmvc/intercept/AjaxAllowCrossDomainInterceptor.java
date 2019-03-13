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
import net.ymate.platform.core.IConfig;
import net.ymate.platform.core.beans.intercept.IInterceptor;
import net.ymate.platform.core.beans.intercept.InterceptContext;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.view.View;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletResponse;

/**
 * 为允许跨域的请求添加必要的请求头参数
 *
 * @author 刘镇 (suninformation@163.com) on 17/3/23 下午5:01
 * @version 1.0
 */
public class AjaxAllowCrossDomainInterceptor implements IInterceptor {

    @Override
    public Object intercept(InterceptContext context) throws Exception {
        if (Direction.BEFORE.equals(context.getDirection())) {
            IConfig _config = context.getOwner().getConfig();
            boolean _allowCrossDomain = BlurObject.bind(_config.getParam(Optional.ALLOW_CROSS_DOMAIN)).toBooleanValue();
            boolean _allowOptions = BlurObject.bind(_config.getParam(Optional.ALLOW_OPTIONS_AUTO_REPLY, "true")).toBooleanValue();
            if (_allowCrossDomain) {
                String _hosts = _config.getParam(Optional.ALLOW_ORIGIN_HOSTS);
                String _methods = StringUtils.defaultIfBlank(_config.getParam(Optional.ALLOW_CROSS_METHODS), _config.getParam(Optional.DEPRECATED_ALLOW_CROSS_METHODS));
                String _headers = _config.getParam(Optional.ALLOW_CROSS_HEADERS);
                __addHeadersToView(_hosts, _methods, _headers, BlurObject.bind(context.getContextParams().get(Optional.NOT_ALLOW_CREDENTIALS)).toBooleanValue());
                //
                if (_allowOptions && Type.HttpMethod.OPTIONS.equals(WebContext.getRequestContext().getHttpMethod())) {
                    return View.nullView();
                }
            }
        }
        return null;
    }

    private void __addHeadersToView(String hosts, String methods, String headers, boolean notAllowCredentials) {
        HttpServletResponse _response = WebContext.getResponse();
        _response.addHeader("Access-Control-Allow-Origin", StringUtils.defaultIfBlank(hosts, "*"));
        if (StringUtils.isNotBlank(methods)) {
            _response.addHeader("Access-Control-Allow-Methods", StringUtils.upperCase(methods));
        }
        if (StringUtils.isNotBlank(headers)) {
            _response.addHeader("Access-Control-Allow-Headers", headers);
        }
        _response.addHeader("Access-Control-Allow-Credentials", notAllowCredentials ? "false" : "true");
    }
}
