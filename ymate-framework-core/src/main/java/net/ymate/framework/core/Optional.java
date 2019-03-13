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
package net.ymate.framework.core;

/**
 * Framework框架可选参数常量定义
 *
 * @author 刘镇 (suninformation@163.com) on 14-7-20
 * @version 1.0
 */
public class Optional {

    public static final Integer BOOL_TRUE = 1;

    public static final Integer BOOL_FALSE = 0;

    public static final String OBSERVE_SILENCE = "observe_silence";

    /**
     * 会话处理器类
     */
    public static final String SYSTEM_USER_SESSION_HANDLER_CLASS = "webmvc.user_session_handler_class";

    /**
     * 会话数据存储适配器类
     */
    public static final String SYSTEM_USER_SESSION_STORAGE_ADAPTER_CLASS = "webmvc.user_session_storage_adapter_class";

    /**
     * 会话安全确认处理器类
     */
    public static final String SYSTEM_USER_SESSION_CONFIRM_HANDLER_CLASS = "webmvc.user_session_confirm_handler_class";

    /**
     * 会话安全确认重定向URL地址, 默认值: "confirm?redirect_url=${redirect_url}"
     */
    public static final String CONFIRM_REDIRECT_URL = "webmvc.confirm_redirect_url";

    /**
     * 会话安全确认超时时间(分钟), 默认值: 30
     */
    public static final String CONFIRM_TIMEOUT = "webmvc.confirm_timeout";

    /**
     * 请求令牌参数名称, 默认值: Request-Token
     */
    public static final String REQUEST_TOKEN_NAME = "webmvc.request_token_name";

    /**
     * 重定向登录URL地址参数名称, 默认值: "login?redirect_url=${redirect_url}"
     */
    public static final String REDIRECT_LOGIN_URL = "webmvc.redirect_login_url";

    /**
     * 重定向自动跳转时间间隔参数名称
     */
    public static final String REDIRECT_TIME_INTERVAL = "webmvc.redirect_time_interval";

    /**
     * 签名验证时间间隔(毫秒), 即当前时间与签名时间戳差值在此值范围内视为有效, 默认值: 0 表示不开始时间间隔验证
     */
    public static final String SIGNATURE_TIMESTAMP_INTERVAL = "webmvc.signature_timestamp_interval";

    /**
     * 允许访问和重定向的主机名称, 多个主机名称用'|'分隔, 默认值: 空(表示不限制)
     */
    public static final String ALLOW_ACCESS_HOSTS = "webmvc.allow_access_hosts";

    /**
     * 允许上传的文件类型验证参数名称
     */
    public static final String VALIDATION_ALLOW_UPLOAD_CONTENT_TYPES = "webmvc.validation_allow_upload_content_types";

    /**
     * 是否开启跨域拦截
     */
    public static final String ALLOW_CROSS_DOMAIN = "webmvc.allow_cross_domain";

    /**
     * 针对OPTIONS请求是否自动回复, 默认: true
     */
    public static final String ALLOW_OPTIONS_AUTO_REPLY = "webmvc.allow_options_auto_reply";

    /**
     * 允许跨域的原始主机
     */
    public static final String ALLOW_ORIGIN_HOSTS = "webmvc.allow_origin_hosts";

    /**
     * 允许跨域请求的方法
     */
    @Deprecated
    public static final String DEPRECATED_ALLOW_CROSS_METHODS = "webmvc.allow_cross_metods";

    public static final String ALLOW_CROSS_METHODS = "webmvc.allow_cross_methods";

    /**
     * 允许跨域请求携带的请求头
     */
    public static final String ALLOW_CROSS_HEADERS = "webmvc.allow_cross_headers";

    /**
     * 是否允许跨域请求带有验证信息
     */
    public static final String NOT_ALLOW_CREDENTIALS = "not_allow_credentials";
}
