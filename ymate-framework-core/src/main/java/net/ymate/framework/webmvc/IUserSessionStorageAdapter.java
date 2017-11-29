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
package net.ymate.framework.webmvc;

import net.ymate.framework.webmvc.support.UserSessionBean;
import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.lang.StringUtils;

/**
 * 会话数据存储适配器接口
 *
 * @author 刘镇 (suninformation@163.com) on 2017/11/29 下午11:21
 * @version 1.0
 */
public interface IUserSessionStorageAdapter {

    /**
     * 默认会话数据存储适配器接口实现
     */
    IUserSessionStorageAdapter DEFAULT = new IUserSessionStorageAdapter() {

        @Override
        public String createSessionId() {
            return WebContext.getRequest().getSession().getId();
        }

        @Override
        public UserSessionBean currentUserSessionBean() {
            return (UserSessionBean) WebContext.getRequest().getSession().getAttribute(UserSessionBean.class.getName());
        }

        @Override
        public void saveOrUpdate(UserSessionBean sessionBean) {
            WebContext.getRequest().getSession().setAttribute(UserSessionBean.class.getName(), sessionBean);
        }

        @Override
        public void saveIfNeed(UserSessionBean sessionBean) {
            UserSessionBean _sessionBean = currentUserSessionBean();
            if (_sessionBean == null || !StringUtils.equals(sessionBean.getId(), _sessionBean.getId())) {
                saveOrUpdate(sessionBean);
            }
        }

        @Override
        public void remove(UserSessionBean sessionBean) {
            WebContext.getRequest().getSession().removeAttribute(UserSessionBean.class.getName());
        }
    };

    /**
     * @return 构建会话唯一主键
     */
    String createSessionId();

    /**
     * @return 获取当前用户存在的UserSessionBean对象, 若不存在将返回null值
     */
    UserSessionBean currentUserSessionBean();

    /**
     * 存储或更新当前会话对象
     *
     * @param sessionBean 目标会话对象
     */
    void saveOrUpdate(UserSessionBean sessionBean);

    /**
     * 如果有必要则存储当前会话对象
     *
     * @param sessionBean 目标会话对象
     */
    void saveIfNeed(UserSessionBean sessionBean);

    /**
     * 删除当前会话对象
     *
     * @param sessionBean 目标会话对象
     */
    void remove(UserSessionBean sessionBean);
}
