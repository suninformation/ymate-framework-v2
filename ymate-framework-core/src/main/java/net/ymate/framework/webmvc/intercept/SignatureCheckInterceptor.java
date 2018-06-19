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
package net.ymate.framework.webmvc.intercept;

import net.ymate.framework.commons.ParamUtils;
import net.ymate.framework.core.Optional;
import net.ymate.framework.core.util.WebUtils;
import net.ymate.framework.webmvc.ErrorCode;
import net.ymate.framework.webmvc.ErrorMsg;
import net.ymate.framework.webmvc.WebResult;
import net.ymate.platform.core.beans.intercept.AbstractInterceptor;
import net.ymate.platform.core.beans.intercept.InterceptContext;
import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 请求参数签名验证拦截器
 *
 * @author 刘镇 (suninformation@163.com) on 2018/6/13 下午5:31
 * @version 1.0
 */
public class SignatureCheckInterceptor extends AbstractInterceptor {

    private static final Log _LOG = LogFactory.getLog(SignatureCheckInterceptor.class);

    public static final String SIGNATURE_KEY = "signature_key";

    public static final String CLIENT_SECRET_KEY = "client_secret_key";

    private ErrorMsg __errorMsg;

    private boolean __inited;

    /**
     * 初始化
     *
     * @param context 拦截器上下文对象
     */
    private synchronized void __init(InterceptContext context) {
        if (!__inited) {
            __errorMsg = new ErrorMsg(ErrorCode.INVALID_PARAMS_SIGNATURE, WebUtils.i18nStr(context.getOwner(), Optional.SYSTEM_PARAMS_SIGNATURE_INVALID_KEY, "请求参数签名无效"));
            __inited = true;
        }
    }

    /**
     * 构建参与签名的附加参数字符串
     *
     * @param context 拦截器上下文对象
     * @return 返回拼装后的附加参数字符串
     */
    protected String buildExtraParamStr(InterceptContext context) {
        String _secretKey = getClientSecretKey(context);
        String clientSecretValue = StringUtils.trimToNull(context.getContextParams().get(_secretKey));
        if (clientSecretValue == null) {
            clientSecretValue = StringUtils.trimToNull(context.getOwner().getConfig().getParam(_secretKey));
            if (clientSecretValue == null) {
                throw new NullArgumentException(_secretKey);
            }
        }
        return _secretKey + "=" + clientSecretValue;
    }

    /**
     * @param params 请求参数映射
     * @return 返回参数检查结果true为通过
     */
    protected boolean checkParameters(Map<String, Object> params, String signatureKey) {
        return params != null && !params.isEmpty() && params.containsKey(signatureKey);
    }

    /**
     * 获取请求参数映射
     *
     * @return 返回待参与签名的参数映射
     */
    protected Map<String, Object> getRequestParameters() {
        return new HashMap<String, Object>(WebContext.getContext().getParameters());
    }

    /**
     * @return 获取当前请求参数中的签名参数值
     */
    protected String getSignatureValue(InterceptContext context) {
        return WebContext.getRequest().getParameter(getSignatureKey(context));
    }

    @Override
    protected Object __before(InterceptContext context) throws Exception {
        __init(context);
        //
        Map<String, Object> _params = getRequestParameters();
        String _signKey = getSignatureKey(context);
        boolean _flag = !checkParameters(_params, _signKey);
        if (!_flag) {
            _params.remove(_signKey);
            //
            String _sign = ParamUtils.createSignature(_params, false, buildExtraParamStr(context));
            _flag = !StringUtils.equals(_sign, getSignatureValue(context));
        }
        if (_flag) {
            return WebResult.formatView(__errorMsg.toResult(), "json");
        }
        return null;
    }

    @Override
    protected Object __after(InterceptContext context) throws Exception {
        return null;
    }

    /**
     * @param context 拦截器上下文对象
     * @return 获取签名参数名称
     */
    public String getSignatureKey(InterceptContext context) {
        return StringUtils.defaultIfBlank(context.getContextParams().get(SIGNATURE_KEY), "signature");
    }

    /**
     * @param context 拦截器上下文对象
     * @return 获取用于签名的密钥参数值
     */
    public String getClientSecretKey(InterceptContext context) {
        return StringUtils.defaultIfBlank(context.getContextParams().get(CLIENT_SECRET_KEY), "client_secret");
    }
}
