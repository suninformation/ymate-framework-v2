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

import net.ymate.platform.core.lang.BlurObject;
import org.apache.commons.lang.StringUtils;

import java.util.Collections;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/6/14 上午12:10
 * @version 1.0
 */
public class ErrorMsg {

    public static final ErrorMsg SUCCESSED = new ErrorMsg(ErrorCode.SUCCESSED, "操作成功");

    public static final ErrorMsg DATA_VERSION_NOT_MATCH = new ErrorMsg(ErrorCode.DATA_VERSION_NOT_MATCH, "数据版本不匹配");

    public static final ErrorMsg REQUEST_OPERATION_FORBIDDEN = new ErrorMsg(ErrorCode.REQUEST_OPERATION_FORBIDDEN, "请求的操作被禁止");

    public static final ErrorMsg REQUEST_METHOD_NOT_ALLOWED = new ErrorMsg(ErrorCode.REQUEST_METHOD_NOT_ALLOWED, "请求方法不支持或不正确");

    public static final ErrorMsg REQUEST_RESOURCE_UNAUTHORIZED = new ErrorMsg(ErrorCode.REQUEST_RESOURCE_UNAUTHORIZED, "请求的资源未授权或无权限");

    public static final ErrorMsg RESOURCE_NOT_FOUND_OR_NOT_EXIST = new ErrorMsg(ErrorCode.RESOURCE_NOT_FOUND_OR_NOT_EXIST, "访问的资源未找到或不存在");

    private int code;

    private String msg;

    private Map<String, Object> attributes;

    public ErrorMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ErrorMsg(int code, String msg, Map<String, Object> attributes) {
        this.code = code;
        this.msg = msg;
        //
        this.attributes = attributes;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public Map<String, Object> getAttributes() {
        return attributes != null ? Collections.unmodifiableMap(attributes) : Collections.<String, Object>emptyMap();
    }

    public BlurObject getAttribute(String attrKey) {
        return BlurObject.bind(attributes != null ? attributes.get(attrKey) : null);
    }

    public WebResult toResult() {
        return WebResult.CODE(code).msg(StringUtils.trimToNull(msg));
    }
}
