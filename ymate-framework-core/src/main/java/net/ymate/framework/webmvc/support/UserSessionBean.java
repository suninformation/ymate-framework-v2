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

import net.ymate.framework.core.util.WebUtils;
import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpSession;
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

    private String remoteAddr;

    private String userAgent;

    private long createTime;

    private long lastActivateTime;

    private Map<String, Serializable> __attributes;

    private UserSessionBean() {
        HttpSession _session = WebContext.getRequest().getSession();
        //
        this.id = _session.getId();
        this.createTime = System.currentTimeMillis();
        this.lastActivateTime = this.createTime;
        this.__attributes = new HashMap<String, Serializable>();
        //
        this.remoteAddr = WebUtils.getRemoteAddr(WebContext.getRequest());
        this.userAgent = WebContext.getRequest().getHeader("user-agent");
        //
        _session.setAttribute(UserSessionBean.class.getName(), this);
    }

    private UserSessionBean(String id) {
        this.id = id;
        this.createTime = System.currentTimeMillis();
        this.lastActivateTime = this.createTime;
        this.__attributes = new HashMap<String, Serializable>();
    }

    /**
     * @return 创建UserSessionBean对象并存入当前会话中
     */
    public static UserSessionBean create() {
        return new UserSessionBean();
    }

    public static UserSessionBean create(String id) {
        if (StringUtils.isNotBlank(id)) {
            return new UserSessionBean(id);
        }
        return null;
    }

    public static UserSessionBean createIfNeed() {
        UserSessionBean _sessionBean = current();
        if (_sessionBean == null) {
            _sessionBean = new UserSessionBean();
        }
        return _sessionBean;
    }

    public static UserSessionBean createIfNeed(String id) {
        UserSessionBean _sessionBean = current();
        if (_sessionBean == null) {
            _sessionBean = new UserSessionBean(id);
        }
        return _sessionBean;
    }

    /**
     * @return 获取当前会话中的UserSessionBean对象, 若不存在将返回null值
     */
    public static UserSessionBean current() {
        return (UserSessionBean) WebContext.getRequest().getSession().getAttribute(UserSessionBean.class.getName());
    }

    /**
     * @return 更新会话最后活动时间(毫秒)
     */
    public UserSessionBean touch() {
        this.lastActivateTime = System.currentTimeMillis();
        return this;
    }

    /**
     * @return 若当前会话尚未存储或与当前存储会话Id不一致时替换原对象
     */
    public UserSessionBean saveIfNeed() {
        HttpSession _session = WebContext.getRequest().getSession();
        UserSessionBean _sessionBean = (UserSessionBean) _session.getAttribute(UserSessionBean.class.getName());
        if (_sessionBean == null || !StringUtils.equals(this.getId(), _sessionBean.getId())) {
            if (StringUtils.isBlank(this.remoteAddr)) {
                this.remoteAddr = WebUtils.getRemoteAddr(WebContext.getRequest());
            }
            if (StringUtils.isBlank(this.userAgent)) {
                this.userAgent = WebContext.getRequest().getHeader("user-agent");
            }
            _session.setAttribute(UserSessionBean.class.getName(), this);
        }
        return this;
    }

    public void destroy() {
        WebContext.getRequest().getSession().removeAttribute(UserSessionBean.class.getName());
    }

    public String getId() {
        return id;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getLastActivateTime() {
        return lastActivateTime;
    }

    public boolean hasAttribute(String name) {
        return __attributes.containsKey(name);
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
