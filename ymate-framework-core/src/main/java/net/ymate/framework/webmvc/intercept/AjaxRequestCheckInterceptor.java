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

import net.ymate.framework.core.util.WebUtils;
import net.ymate.platform.core.beans.intercept.IInterceptor;
import net.ymate.platform.core.beans.intercept.InterceptContext;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.view.impl.HttpStatusView;

/**
 * 检测当前请求是否为AJAX模式
 *
 * @author 刘镇 (suninformation@163.com) on 16/3/30 上午2:21
 * @version 1.0
 */
public class AjaxRequestCheckInterceptor implements IInterceptor {
    public Object intercept(InterceptContext context) throws Exception {
        if (!WebUtils.isAjax(WebContext.getRequest())) {
            return HttpStatusView.METHOD_NOT_ALLOWED;
        }
        return null;
    }
}