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

import net.ymate.framework.core.Optional;
import net.ymate.framework.webmvc.support.UserSessionBean;
import net.ymate.platform.core.beans.intercept.InterceptContext;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.core.util.DateTimeUtils;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * 会话安全确认处理器接口
 *
 * @author 刘镇 (suninformation@163.com) on 17/4/25 下午8:00
 * @version 1.0
 */
public interface IUserSessionConfirmHandler {

    IUserSessionConfirmHandler DEFAULT = new IUserSessionConfirmHandler() {

        @Override
        public boolean handle(InterceptContext context) throws Exception {
            UserSessionConfirmStatus _confirmStatus = getSessionConfirmStatus();
            int _timeout = BlurObject.bind(StringUtils.defaultIfBlank(context.getOwner().getConfig().getParam(Optional.CONFIRM_TIMEOUT), "30")).toIntValue();
            return _confirmStatus != null && BlurObject.bind(_confirmStatus.getStatus()).toBooleanValue() && System.currentTimeMillis() - _confirmStatus.getLastModifyTime() < DateTimeUtils.MINUTE * _timeout;
        }

        @Override
        public UserSessionConfirmStatus getSessionConfirmStatus() {
            UserSessionBean _sessionBean = UserSessionBean.current();
            String _attrKey = UserSessionConfirmStatus.class.getName();
            if (_sessionBean != null) {
                UserSessionConfirmStatus _confirmStatus = _sessionBean.getAttribute(_attrKey);
                if (_confirmStatus == null) {
                    _confirmStatus = new UserSessionConfirmStatus();
                    _confirmStatus.setUid(_sessionBean.getUid());
                    _confirmStatus.setLastModifyTime(_sessionBean.getLastActivateTime());
                    _sessionBean.addAttribute(_attrKey, _confirmStatus);
                }
                return _confirmStatus;
            }
            return null;
        }

        @Override
        public void updateConfirmStatus(String status) {
            UserSessionConfirmStatus _confirmStatus = getSessionConfirmStatus();
            if (_confirmStatus != null) {
                _confirmStatus.setStatus(status);
                _confirmStatus.setLastModifyTime(System.currentTimeMillis());
            }
        }
    };

    /**
     * @param context 当前拦截器环境上下文对象
     * @return 返回用户是否完成了安全确认动作(需实现具体验证逻辑)
     * @throws Exception 抛出任何可能异常
     */
    boolean handle(InterceptContext context) throws Exception;

    /**
     * @return 获取当前会话安全确认状态对象, 若不存在则创建新的
     */
    UserSessionConfirmStatus getSessionConfirmStatus();

    /**
     * 更新会话Confirm的状态数据
     *
     * @param status 安全确认状态数据
     */
    void updateConfirmStatus(String status);

    /**
     * 会话安全确认状态对象
     */
    class UserSessionConfirmStatus implements Serializable {

        private String uid;

        private String status;

        private long lastModifyTime;

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public long getLastModifyTime() {
            return lastModifyTime;
        }

        public void setLastModifyTime(long lastModifyTime) {
            this.lastModifyTime = lastModifyTime;
        }
    }
}
