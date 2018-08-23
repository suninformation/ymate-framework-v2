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

import net.ymate.framework.webmvc.support.UserSessionBean;
import net.ymate.platform.core.beans.intercept.IInterceptor;
import net.ymate.platform.core.beans.intercept.InterceptContext;

/**
 * 检测并维持当前用户会话状态，若会话不存在则尝试获取，若会话无效或已过期则清理掉
 *
 * @author 刘镇 (suninformation@163.com) on 17/5/11 上午9:26
 * @version 1.0
 */
public class UserSessionStatusInterceptor implements IInterceptor {

    @Override
    public Object intercept(InterceptContext context) throws Exception {
        switch (context.getDirection()) {
            case BEFORE:
                UserSessionBean.current(context);
        }
        return null;
    }
}
