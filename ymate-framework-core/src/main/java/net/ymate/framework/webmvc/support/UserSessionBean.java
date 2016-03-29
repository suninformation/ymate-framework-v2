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
package net.ymate.framework.webmvc.support;

import net.ymate.platform.webmvc.context.WebContext;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户会话对象
 *
 * @author 刘镇 (suninformation@163.com) on 16/3/13 下午11:55
 * @version 1.0
 */
public class UserSessionBean implements Serializable {

    private String id;

    private long lastActivateTime;

    private Map<String, Serializable> __attributes;

    private UserSessionBean() {
        this.id = WebContext.getRequest().getSession().getId();
        this.lastActivateTime = System.currentTimeMillis();
        this.__attributes = new HashMap<String, Serializable>();
    }

    /**
     * @return 创建UserSessionBean对象并存入当前会话中
     */
    public static UserSessionBean create() {
        UserSessionBean _sessionBean = new UserSessionBean();
        WebContext.getContext().getSession().put(UserSessionBean.class.getName(), _sessionBean);
        return _sessionBean;
    }

    /**
     * @return 获取当前会话中的UserSessionBean对象, 若不存在将返回null值
     */
    public static UserSessionBean current() {
        return (UserSessionBean) WebContext.getContext().getSession().get(UserSessionBean.class.getName());
    }

    /**
     * @return 更新会话最后活动时间(毫秒)
     */
    public UserSessionBean touch() {
        this.lastActivateTime = System.currentTimeMillis();
        return this;
    }

    public String getId() {
        return id;
    }

    public long getLastActivateTime() {
        return lastActivateTime;
    }

    @SuppressWarnings("unchecked")
    public <T extends Serializable> T getAttribute(String name) {
        return (T) __attributes.get(name);
    }

    public <T extends Serializable> UserSessionBean addAttribute(String name, T value) {
        __attributes.put(name, value);
        return this;
    }

    public Map<String, ? extends Serializable> getAttributes() {
        return __attributes;
    }
}
