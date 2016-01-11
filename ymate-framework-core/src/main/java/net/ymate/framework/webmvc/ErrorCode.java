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
     * 系统内部错误
     */
    public static final int INTERNAL_SYSTEM_ERROR = -50;
}
