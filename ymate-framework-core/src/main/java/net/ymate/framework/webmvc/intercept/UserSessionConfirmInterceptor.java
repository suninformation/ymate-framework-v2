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
import net.ymate.framework.webmvc.IUserSessionConfirmHandler;
import net.ymate.framework.webmvc.support.UserSessionBean;
import net.ymate.platform.core.beans.intercept.IInterceptor;
import net.ymate.platform.core.beans.intercept.InterceptContext;
import net.ymate.platform.core.util.ClassUtils;
import net.ymate.platform.webmvc.context.WebContext;

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
                if (_sessionBean != null) {
                    IUserSessionConfirmHandler _handler = getSessionConfirmHandler();
                    if (!_handler.handle(context)) {
                        return _handler.onNeedConfirm(context);
                    }
                }
                break;
            default:
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
