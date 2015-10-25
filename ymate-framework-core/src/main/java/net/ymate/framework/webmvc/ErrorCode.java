/*
 * Copyright (c) 2007-2016, the original author or authors. All rights reserved.
 *
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
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
