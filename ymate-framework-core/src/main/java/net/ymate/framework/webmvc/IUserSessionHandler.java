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
package net.ymate.framework.webmvc;

import net.ymate.framework.webmvc.support.UserSessionBean;
import net.ymate.platform.core.beans.intercept.InterceptContext;

/**
 * 会话处理器接口
 *
 * @author 刘镇 (suninformation@163.com) on 16/4/30 下午8:52
 * @version 1.0
 */
public interface IUserSessionHandler {

    /**
     * @param context 当前拦截器环境上下文对象
     * @return 返回自定义获取或创建用户会话实例对象, 若不存在则返回空
     * @throws Exception 抛出任何可能异常
     */
    UserSessionBean handle(InterceptContext context) throws Exception;

    /**
     * @param sessionBean 用户会话对象
     * @return 验证用户会话对象是否合法有效
     */
    boolean verification(UserSessionBean sessionBean);
}
