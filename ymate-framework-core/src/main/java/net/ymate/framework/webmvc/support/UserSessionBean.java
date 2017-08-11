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

import net.ymate.framework.core.Optional;
import net.ymate.framework.webmvc.IUserSessionHandler;
import net.ymate.framework.webmvc.intercept.UserSessionCheckInterceptor;
import net.ymate.platform.core.util.ClassUtils;
import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.lang.NullArgumentException;
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

    private static volatile IUserSessionHandler __sessionHandler;

    private static volatile boolean __initedFlag = false;

    private String id;

    private String uid;

    private long createTime;

    private long lastActivateTime;

    private Map<String, Serializable> __attributes;

    /**
     * @return 返回会话处理器接口实现类(至少尝试初始化一次)
     */
    public static IUserSessionHandler getSessionHandler() {
        if (__sessionHandler == null && !__initedFlag) {
            synchronized (UserSessionBean.class) {
                if (__sessionHandler == null) {
                    String _handleClassName = WebContext.getContext().getOwner().getOwner().getConfig().getParam(Optional.SYSTEM_USER_SESSION_HANDLER_CLASS);
                    if (StringUtils.isNotBlank(_handleClassName)) {
                        __sessionHandler = ClassUtils.impl(_handleClassName, IUserSessionHandler.class, UserSessionCheckInterceptor.class);
                    }
                    __initedFlag = true;
                }
            }
        }
        return __sessionHandler;
    }

    private UserSessionBean() {
        this(WebContext.getRequest().getSession().getId());
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
        if (StringUtils.isBlank(id)) {
            throw new NullArgumentException("id");
        }
        return new UserSessionBean(id);
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
     * @param name 属性名称
     * @param <T>  返回值类型
     * @return 获取当前会话中指定名称的属性值, 若会话对象不存在或属性不存在将返回null值
     */
    public static <T extends Serializable> T current(String name) {
        UserSessionBean _sessionBean = current();
        if (_sessionBean != null) {
            return _sessionBean.getAttribute(name);
        }
        return null;
    }

    /**
     * @return 更新会话最后活动时间(毫秒)
     */
    public UserSessionBean touch() {
        this.lastActivateTime = System.currentTimeMillis();
        return this;
    }

    /**
     * @return 验证当前会话是否合法有效(若IUserSessionHandler配置参数为空则该方法返回值永真)
     */
    public boolean isVerified() {
        return getSessionHandler() == null || getSessionHandler().verification(this);
    }

    /**
     * @return 重置(会话ID将保留不变)
     */
    public UserSessionBean reset() {
        this.createTime = System.currentTimeMillis();
        this.lastActivateTime = this.createTime;
        this.__attributes = new HashMap<String, Serializable>();
        //
        return this;
    }

    public UserSessionBean save() {
        WebContext.getRequest().getSession().setAttribute(UserSessionBean.class.getName(), this);
        return this;
    }

    /**
     * @return 若当前会话尚未存储或与当前存储会话Id不一致时替换原对象
     */
    public UserSessionBean saveIfNeed() {
        HttpSession _session = WebContext.getRequest().getSession();
        UserSessionBean _sessionBean = (UserSessionBean) _session.getAttribute(UserSessionBean.class.getName());
        if (_sessionBean == null || !StringUtils.equals(this.getId(), _sessionBean.getId())) {
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

    public String getUid() {
        return uid;
    }

    public UserSessionBean setUid(String uid) {
        this.uid = uid;
        return this;
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
