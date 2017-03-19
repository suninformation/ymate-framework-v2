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
     * 系统默认国际化错误消息KEY
     */
    public static final String SYSTEM_ERROR_DEFAULT_I18N_KEY = "webmvc.error_default_i18n_key";

    /**
     * 会话过期消息KEY
     */
    public static final String SYSTEM_SESSION_TIMEOUT_KEY = "webmvc.session_timeout_key";

    /**
     * 会话已授权(登录)KEY
     */
    public static final String SYSTEM_SESSION_AUTHORIZED_KEY = "webmvc.session_authorized_key";

    /**
     * 会话处理器类
     */
    public static final String SYSTEM_USER_SESSION_HANDLER_CLASS = "webmvc.user_session_handler_class";

    /**
     * 控制器请求URL后缀参数名称
     */
    public static final String REQUEST_SUFFIX = "webmvc.request_suffix";

    /**
     * 重定向登录URL地址参数名称, 默认值: "login?redirect_url=${redirect_url}"
     */
    public static final String REDIRECT_LOGIN_URL = "webmvc.redirect_login_url";

    /**
     * 重定向主页URL地址参数名称, 默认值: ""
     */
    public static final String REDIRECT_HOME_URL = "webmvc.redirect_home_url";

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
}
