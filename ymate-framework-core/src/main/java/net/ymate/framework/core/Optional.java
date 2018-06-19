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

    public static final String REDIRECT_URL = "redirect_url";

    public static final String OBSERVE_SILENCE = "observe_silence";

    public static final String CUSTOM_REDIRECT = "custom_redirect";

    /**
     * 国际化资源文件参数名称
     */
    public static final String I18N_RESOURCE_NAME = "webmvc.i18n_resource_name";

    /**
     * 国际化语言切换参数名称
     */
    public static final String I18N_LANGUAGE_KEY = "webmvc.i18n_language_key";

    /**
     * 异常信息视图文件参数名称
     */
    public static final String ERROR_VIEW = "webmvc.error_view";

    /**
     * 系统异常分析是否关闭参数名称
     */
    public static final String SYSTEM_EXCEPTION_ANALYSIS_DISABLED = "webmvc.exception_analysis_disabled";

    /**
     * 系统错误消息是否指定ContentType响应头
     */
    public static final String SYSTEM_ERROR_WITH_CONTENT_TYPE = "webmvc.error_with_content_type";

    /**
     * 系统默认国际化错误消息KEY
     */
    public static final String SYSTEM_ERROR_DEFAULT_I18N_KEY = "webmvc.error_default_i18n_key";

    /**
     * 会话过期消息KEY
     */
    public static final String SYSTEM_SESSION_TIMEOUT_KEY = "webmvc.session_timeout_key";

    /**
     * 请求参数验证无效消息KEY
     */
    public static final String SYSTEM_PARAMS_VALIDATION_INVALID_KEY = "webmvc.params_validation_invalid_key";

    /**
     * 请求参数签名无效消息KEY
     */
    public static final String SYSTEM_PARAMS_SIGNATURE_INVALID_KEY = "webmvc.params_signature_invalid_key";

    /**
     * 会话已授权(登录)KEY
     */
    public static final String SYSTEM_SESSION_AUTHORIZED_KEY = "webmvc.session_authorized_key";

    /**
     * 请求的资源未授权或无权限KEY
     */
    public static final String SYSTEM_REQUEST_RESOURCE_UNAUTHORIZED_KEY = "webmvc.request_resource_unauthorized_key";

    /**
     * 请求的操作被禁止KEY
     */
    public static final String SYSTEM_REQUEST_OPERATION_FORBIDDEN_KEY = "webmvc.request_operation_forbidden_key";

    /**
     * 访问的资源未找到或不存在KEY
     */
    public static final String SYSTEM_RESOURCE_NOT_FOUND_OR_NOT_EXIST_KEY = "webmvc.resource_not_found_or_not_exist_key";

    /**
     * 请求方法不支持或不正确KEY
     */
    public static final String SYSTEM_REQUEST_METHOD_NOT_ALLOWED_KEY = "webmvc.request_method_not_allowed_key";

    /**
     * 数据版本不匹配KEY
     */
    public static final String SYSTEM_DATA_VERSION_NOT_MATCH_KEY = "webmvc.data_version_not_match_key";

    // ----------

    /**
     * 默认异常响应视图格式, 默认值: "", 可选范围: json|xml
     */
    public static final String ERROR_DEFAULT_VIEW_FORMAT = "webmvc.error_default_view_format";

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
     * 控制器请求URL后缀参数名称
     */
    public static final String REQUEST_SUFFIX = "webmvc.request_suffix";

    /**
     * 服务名称参数, 默认值: request.getServerName();
     */
    public static final String SERVER_NAME = "webmvc.server_name";

    /**
     * 请求令牌参数名称, 默认值: Request-Token
     */
    public static final String REQUEST_TOKEN_NAME = "webmvc.request_token_name";

    /**
     * 重定向登录URL地址参数名称, 默认值: "login?redirect_url=${redirect_url}"
     */
    public static final String REDIRECT_LOGIN_URL = "webmvc.redirect_login_url";

    /**
     * 重定向主页URL地址参数名称, 默认值: ""
     */
    public static final String REDIRECT_HOME_URL = "webmvc.redirect_home_url";

    /**
     * 自定义重定向URL地址参数名称
     */
    public static final String REDIRECT_CUSTOM_URL = "webmvc.redirect_custom_url";

    /**
     * 重定向自动跳转时间间隔参数名称
     */
    public static final String REDIRECT_TIME_INTERVAL = "webmvc.redirect_time_interval";

    /**
     * 允许上传的文件类型验证参数名称
     */
    public static final String VALIDATION_ALLOW_UPLOAD_CONTENT_TYPES = "webmvc.validation_allow_upload_content_types";

    /**
     * 验证结果消息模板参数名称, 默认值: "${items}"
     */
    public static final String VALIDATION_TEMPLATE_ELEMENT = "webmvc.validation_template_element";

    /**
     * 验证结果消息项模板参数名称, 默认值: "${message}<br>"
     */
    public static final String VALIDATION_TEMPLATE_ITEM = "webmvc.validation_template_item";

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
    public static final String ALLOW_CROSS_METHODS = "webmvc.allow_cross_metods";

    /**
     * 允许跨域请求携带的请求头
     */
    public static final String ALLOW_CROSS_HEADERS = "webmvc.allow_cross_headers";

    /**
     * 是否允许跨域请求带有验证信息
     */
    public static final String NOT_ALLOW_CREDENTIALS = "not_allow_credentials";
}
