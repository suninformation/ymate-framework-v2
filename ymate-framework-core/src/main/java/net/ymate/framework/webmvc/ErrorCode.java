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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * ret = 0: 正确返回<br>
 * ret &gt; 0: 调用OpenAPI时发生错误，需要开发者进行相应的处理<br>
 * -50 &lt;= ret &lt;= -1: 接口调用不能通过接口代理机校验，需要开发者进行相应的处理<br>
 * ret &lt;-50: 系统内部错误
 *
 * @author 刘镇 (suninformation@163.com) on 14/7/6 下午6:53
 * @version 1.0
 */
public class ErrorCode {

    public static Map<Integer, String> HTTP_STATUS;

    static {
        Map<Integer, String> _httpStatus = new HashMap<Integer, String>();
        //
        _httpStatus.put(400, "Bad Request");
        _httpStatus.put(401, "Unauthorized");
        _httpStatus.put(402, "Payment Required");
        _httpStatus.put(403, "Forbidden");
        _httpStatus.put(404, "Not Found");
        _httpStatus.put(405, "Method Not Allowed");
        _httpStatus.put(406, "Not Acceptable");
        _httpStatus.put(407, "Proxy Authentication Required");
        _httpStatus.put(408, "Request Timeout");
        _httpStatus.put(409, "Conflict");
        _httpStatus.put(410, "Gone");
        _httpStatus.put(411, "Length Required");
        _httpStatus.put(412, "Precondition Failed");
        _httpStatus.put(413, "Request Entity Too Large");
        _httpStatus.put(414, "Request URI Too Long");
        _httpStatus.put(415, "Unsupported Media Type");
        _httpStatus.put(416, "Requested Range Not Satisfiable");
        _httpStatus.put(417, "Expectation Failed");
        _httpStatus.put(421, "Too Many Connections");
        _httpStatus.put(422, "Unprocessable Entity");
        _httpStatus.put(423, "Locked");
        _httpStatus.put(424, "Failed Dependency");
        _httpStatus.put(425, "Unordered Collection");
        _httpStatus.put(426, "Ungrade Required");
        _httpStatus.put(449, "Retry With");
        _httpStatus.put(451, "Unavailable For Legal Reasons");
        //
        _httpStatus.put(500, "Internal Server Error");
        _httpStatus.put(501, "Not Implemented");
        _httpStatus.put(502, "Bad Gateway");
        _httpStatus.put(503, "Service Unavailable");
        _httpStatus.put(504, "Gateway Timeout");
        _httpStatus.put(505, "HTTP Version Not Supported");
        _httpStatus.put(506, "Variant Also Negotiates");
        _httpStatus.put(507, "Insufficient Storage");
        _httpStatus.put(509, "Bandwith Limit Exceeded");
        _httpStatus.put(510, "Not Extended");
        //
        HTTP_STATUS = Collections.unmodifiableMap(_httpStatus);
    }

    /**
     * 请求成功
     */
    public static final int SUCCESSED = 0;

    /**
     * 参数验证无效
     */
    public static final int INVALID_PARAMS_VALIDATION = -1;

    /**
     * 访问的资源未找到或不存在
     */
    public static final int RESOURCE_NOT_FOUND_OR_NOT_EXIST = -2;

    /**
     * 请求方法不支持或不正确
     */
    public static final int REQUEST_METHOD_NOT_ALLOWED = -3;

    /**
     * 请求的资源未授权或无权限
     */
    public static final int REQUEST_RESOURCE_UNAUTHORIZED = -4;

    /**
     * 用户会话无效或超时
     */
    public static final int USER_SESSION_INVALID_OR_TIMEOUT = -5;

    /**
     * 请求的操作被禁止
     */
    public static final int REQUEST_OPERATION_FORBIDDEN = -6;

    /**
     * 用户会话已授权(登录)
     */
    public static final int USER_SESSION_AUTHORIZED = -7;

    /**
     * 参数签名无效
     */
    public static final int INVALID_PARAMS_SIGNATURE = -8;

    /**
     * 数据版本不匹配
     */
    public static final int DATA_VERSION_NOT_MATCH = -20;

    /**
     * 系统内部错误
     */
    public static final int INTERNAL_SYSTEM_ERROR = -50;
}
