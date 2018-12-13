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
import net.ymate.framework.webmvc.ErrorCode;
import net.ymate.platform.core.beans.intercept.AbstractInterceptor;
import net.ymate.platform.core.beans.intercept.InterceptContext;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.context.WebContext;
import net.ymate.platform.webmvc.util.WebResult;
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

    private static final String SIGNATURE_KEY = "signature_key";

    private static final String CLIENT_SECRET_KEY = "client_secret_key";

    private static final String TIMESTAMP_KEY = "timestamp_key";

    private int __timestampInterval;

    private boolean __inited;

    /**
     * 初始化
     *
     * @param context 拦截器上下文对象
     */
    private synchronized void __init(InterceptContext context) {
        if (!__inited) {
            __timestampInterval = BlurObject.bind(context.getOwner().getConfig().getParam(Optional.SIGNATURE_TIMESTAMP_INTERVAL)).toIntValue();
            __inited = true;
        }
    }

    /**
     * 构建参与签名的附加参数字符串
     *
     * @param context 拦截器上下文对象
     * @return 返回拼装后的附加参数字符串
     */
    private String buildExtraParamStr(InterceptContext context) {
        String _secretKey = StringUtils.defaultIfBlank(context.getContextParams().get(CLIENT_SECRET_KEY), "client_secret");
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
     * @param params       请求参数映射
     * @param signatureKey 签名密钥
     * @return 返回参数检查结果true为通过
     */
    private boolean checkParameters(Map<String, Object> params, String signatureKey) {
        return params != null && !params.isEmpty() && params.containsKey(signatureKey);
    }

    /**
     * @param context 拦截器上下文对象
     * @return 获取签名参数名称
     */
    private String getSignatureKey(InterceptContext context) {
        return StringUtils.defaultIfBlank(context.getContextParams().get(SIGNATURE_KEY), "signature");
    }

    private String getTimestampKey(InterceptContext context) {
        return StringUtils.defaultIfBlank(context.getContextParams().get(TIMESTAMP_KEY), "timestamp");
    }

    /**
     * @param context 拦截器环境上下文对象
     * @return 获取当前请求参数中的签名参数值
     */
    private String getSignatureValue(InterceptContext context) {
        return WebContext.getRequest().getParameter(getSignatureKey(context));
    }

    @Override
    protected Object __before(InterceptContext context) throws Exception {
        __init(context);
        // 获取请求参数映射
        Map<String, Object> _params = new HashMap<String, Object>(WebContext.getContext().getParameters());
        String _signKey = getSignatureKey(context);
        boolean _flag = !checkParameters(_params, _signKey);
        if (!_flag) {
            _params.remove(_signKey);
            //
            String _sign = ParamUtils.createSignature(_params, false, buildExtraParamStr(context));
            _flag = !StringUtils.equals(_sign, getSignatureValue(context));
            if (!_flag && __timestampInterval > 0) {
                long _timestamp = BlurObject.bind(_params.get(getTimestampKey(context))).toLongValue();
                _flag = _timestamp <= 0 || System.currentTimeMillis() - _timestamp > __timestampInterval;
            }
        }
        if (_flag) {
            return WebResult.formatView(WebResult.create(ErrorCode.INVALID_PARAMS_SIGNATURE).msg("请求参数签名无效"), Type.Const.FORMAT_JSON);
        }
        return null;
    }

    @Override
    protected Object __after(InterceptContext context) throws Exception {
        return null;
    }
}
