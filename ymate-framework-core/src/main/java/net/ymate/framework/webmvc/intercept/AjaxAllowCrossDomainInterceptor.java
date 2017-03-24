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
import net.ymate.platform.core.beans.intercept.IInterceptor;
import net.ymate.platform.core.beans.intercept.InterceptContext;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.view.IView;
import net.ymate.platform.webmvc.view.View;
import org.apache.commons.lang.StringUtils;

/**
 * 为允许跨域的请求添加必要的请求头参数
 *
 * @author 刘镇 (suninformation@163.com) on 17/3/23 下午5:01
 * @version 1.0
 */
public class AjaxAllowCrossDomainInterceptor implements IInterceptor {

    public Object intercept(InterceptContext context) throws Exception {
        boolean _allowCrossDomain = BlurObject.bind(context.getOwner().getConfig().getParam(Optional.ALLOW_CROSS_DOMAIN)).toBooleanValue();
        if (_allowCrossDomain) {
            String _hosts = context.getOwner().getConfig().getParam(Optional.ALLOW_ORIGIN_HOSTS);
            boolean _notAllowCredentials = BlurObject.bind(context.getContextParams().get(Optional.NOT_ALLOW_CREDENTIALS)).toBooleanValue();
            switch (context.getDirection()) {
                case BEFORE:
                    if (Type.HttpMethod.OPTIONS.equals(WebContext.getRequestContext().getHttpMethod())) {
                        return __addHeadersToView(View.nullView(), _hosts, _notAllowCredentials);
                    }
                    break;
                case AFTER:
                    if (!Type.HttpMethod.OPTIONS.equals(WebContext.getRequestContext().getHttpMethod()) && context.getResultObject() instanceof IView) {
                        __addHeadersToView((IView) context.getResultObject(), _hosts, _notAllowCredentials);
                    }
                    break;
            }
        }
        return null;
    }

    private IView __addHeadersToView(IView view, String hosts, boolean notAllowCredentials) {
        view.addHeader("Access-Control-Allow-Origin", StringUtils.defaultIfBlank(hosts, "*"));
        view.addHeader("Access-Control-Allow-Credentials", notAllowCredentials ? "false" : "true");
        return view;
    }
}
