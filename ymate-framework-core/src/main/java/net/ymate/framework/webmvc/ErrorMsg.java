/*
 * Copyright 2007-2018 the original author or authors.
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
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.i18n.I18N;
import net.ymate.platform.core.lang.BlurObject;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/6/14 上午12:10
 * @version 1.0
 */
public class ErrorMsg {

    public static final ErrorMsg SUCCESSED = new ErrorMsg(ErrorCode.SUCCESSED);

    public static final ErrorMsg DATA_VERSION_NOT_MATCH = new ErrorMsg(ErrorCode.DATA_VERSION_NOT_MATCH, Optional.SYSTEM_DATA_VERSION_NOT_MATCH_KEY, "数据版本不匹配");

    public static final ErrorMsg REQUEST_OPERATION_FORBIDDEN = new ErrorMsg(ErrorCode.REQUEST_OPERATION_FORBIDDEN, Optional.SYSTEM_REQUEST_OPERATION_FORBIDDEN_KEY, "请求的操作被禁止");

    public static final ErrorMsg REQUEST_METHOD_NOT_ALLOWED = new ErrorMsg(ErrorCode.REQUEST_METHOD_NOT_ALLOWED, Optional.SYSTEM_REQUEST_METHOD_NOT_ALLOWED_KEY, "请求方法不支持或不正确");

    public static final ErrorMsg REQUEST_RESOURCE_UNAUTHORIZED = new ErrorMsg(ErrorCode.REQUEST_RESOURCE_UNAUTHORIZED, Optional.SYSTEM_REQUEST_RESOURCE_UNAUTHORIZED_KEY, "请求的资源未授权或无权限");

    public static final ErrorMsg RESOURCE_NOT_FOUND_OR_NOT_EXIST = new ErrorMsg(ErrorCode.RESOURCE_NOT_FOUND_OR_NOT_EXIST, Optional.SYSTEM_RESOURCE_NOT_FOUND_OR_NOT_EXIST_KEY, "访问的资源未找到或不存在");

    private int code;

    private String i18nStr;

    private String msg;

    private Map<String, Object> attributes;

    public ErrorMsg(int code) {
        this(code, null, null, null);
    }

    public ErrorMsg(int code, String msg) {
        this(code, null, msg, null);
    }

    public ErrorMsg(int code, String i18nStr, String msg) {
        this(code, i18nStr, msg, null);
    }

    public ErrorMsg(int code, String msg, Map<String, Object> attributes) {
        this(code, msg, null, attributes);
    }

    public ErrorMsg(int code, String i18nStr, String msg, Map<String, Object> attributes) {
        this.code = code;
        this.i18nStr = i18nStr;
        this.msg = msg;
        //
        this.attributes = attributes != null ? new HashMap<String, Object>(attributes) : new HashMap<String, Object>();
    }

    public int getCode() {
        return code;
    }

    public String getI18nStr() {
        return i18nStr;
    }

    public String getMsg() {
        return msg;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public BlurObject getAttribute(String attrKey) {
        return BlurObject.bind(attributes.get(attrKey));
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttr(String attrKey) {
        return (T) attributes.get(attrKey);
    }

    public ErrorMsg addAttribute(String attrKey, Object attrValue) {
        this.attributes.put(attrKey, attrValue);
        return this;
    }

    public WebResult toResult() {
        return toResult(null);
    }

    public WebResult toResult(String resourceName) {
        if (StringUtils.isNotBlank(i18nStr)) {
            String i18nMsg = null;
            String defaultResName = StringUtils.defaultIfBlank(YMP.get().getConfig().getParam(Optional.I18N_RESOURCE_NAME), "messages");
            // 先加载指定名称资源
            if (StringUtils.isNotBlank(resourceName) && !StringUtils.equalsIgnoreCase(defaultResName, resourceName)) {
                i18nMsg = I18N.formatMessage(resourceName, i18nStr, null);
            }
            // 再尝试加载默认资源
            if (StringUtils.isBlank(i18nMsg)) {
                i18nMsg = I18N.formatMessage(defaultResName, i18nStr, msg);
            }
            return WebResult.CODE(code).msg(StringUtils.trimToNull(i18nMsg));
        }
        return WebResult.CODE(code).msg(StringUtils.trimToNull(msg));
    }

    public ErrorMsg createNew() {
        return new ErrorMsg(code, i18nStr, msg, attributes);
    }
}
