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
package net.ymate.framework.commons;

import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.core.util.RuntimeUtils;
import net.ymate.platform.core.util.UUIDUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 15-1-12 下午4:50
 * @version 1.0
 */
public class ParamUtils {

    private static final Log _LOG = LogFactory.getLog(ParamUtils.class);

    /**
     * @param params  请求参数映射
     * @param encode  是否对参数进行编码
     * @param charset Encode编码字符集，默认UTF-8
     * @return 对参数进行ASCII正序排列并生成请求参数串
     */
    public static String buildQueryParamStr(Map<String, ?> params, boolean encode, String charset) {
        String[] _keys = params.keySet().toArray(new String[params.size()]);
        Arrays.sort(_keys);
        StringBuilder _paramSB = new StringBuilder();
        for (String _key : _keys) {
            Object _value = params.get(_key);
            if (_value != null) {
                if (_value.getClass().isArray()) {
                    for (Object _v : (Object[]) _value) {
                        __doAppendParamValue(_key, _v, _paramSB, encode, charset);
                    }
                } else {
                    __doAppendParamValue(_key, _value, _paramSB, encode, charset);
                }
            }
        }
        return _paramSB.toString();
    }

    private static void __doAppendParamValue(String _key, Object _v, StringBuilder _paramSB, boolean encode, String charset) {
        if (_paramSB.length() > 0) {
            _paramSB.append("&");
        }
        String _valueStr = _v.toString();
        if (encode) {
            try {
                _paramSB.append(_key).append("=").append(URLEncoder.encode(_valueStr, StringUtils.defaultIfBlank(charset, "UTF-8")));
            } catch (UnsupportedEncodingException e) {
                _LOG.warn("", RuntimeUtils.unwrapThrow(e));
            }
        } else {
            _paramSB.append(_key).append("=").append(_valueStr);
        }
    }

    public static Map<String, String> convertParamMap(Map<String, Object> sourceMap) {
        Map<String, String> _returnValue = new HashMap<String, String>(sourceMap.size());
        for (Map.Entry<String, Object> _ent : sourceMap.entrySet()) {
            if (_ent.getValue() != null) {
                _returnValue.put(_ent.getKey(), BlurObject.bind(_ent.getValue()).toStringValue());
            }
        }
        return _returnValue;
    }

    /**
     * 解析远程模拟提交后返回的信息, 并将参数串转换成Map映射
     *
     * @param paramStr 要解析的字符串
     * @return 解析结果
     */
    public static Map<String, String> parseQueryParamStr(String paramStr) {
        return parseQueryParamStr(paramStr, false, null);
    }

    public static Map<String, String> parseQueryParamStr(String paramStr, boolean decode, String charset) {
        // 以“&”字符切割字符串
        String[] _paramArr = StringUtils.split(paramStr, '&');
        // 把切割后的字符串数组变成变量与数值组合的字典数组
        Map<String, String> _returnValue = new HashMap<String, String>(_paramArr.length);
        for (String _param : _paramArr) {
            //获得第一个=字符的位置
            int nPos = _param.indexOf('=');
            //获得字符串长度
            int nLen = _param.length();
            //获得变量名
            String strKey = _param.substring(0, nPos);
            //获得数值
            String strValue = _param.substring(nPos + 1, nLen);
            if (decode) {
                try {
                    strValue = URLDecoder.decode(strValue, StringUtils.defaultIfBlank(charset, "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    _LOG.warn("", RuntimeUtils.unwrapThrow(e));
                }
            }
            //放入MAP类中
            _returnValue.put(strKey, strValue);
        }
        return _returnValue;
    }

    public static String buildActionForm(String actionUrl, boolean usePost, Map<String, String> params) {
        StringBuilder _payHtml = new StringBuilder();
        _payHtml.append("<form id=\"_payment_submit\" name=\"_payment_submit\" action=\"")
                .append(actionUrl).append("\" method=\"")
                .append(usePost ? "POST" : "GET")
                .append("\">");
        //
        for (Map.Entry<String, String> _entry : params.entrySet()) {
            if (_entry.getKey() != null) {
                _payHtml.append("<input type=\"hidden\" name=\"")
                        .append(_entry.getKey())
                        .append("\" value=\"")
                        .append(_entry.getValue())
                        .append("\"/>");
            }
        }
        // submit按钮控件请不要含有name属性
        _payHtml.append("<input type=\"submit\" value=\"doSubmit\" style=\"display:none;\"></form>");
        _payHtml.append("<script>document.forms['_payment_submit'].submit();</script>");
        return _payHtml.toString();
    }

    /**
     * @return 产生随机字符串，长度为6到32位不等
     */
    public static String createNonceStr() {
        return UUIDUtils.randomStr(UUIDUtils.randomInt(6, 32), false).toLowerCase();
    }

    /**
     * @param queryParamMap 请求协议参数对象映射
     * @param encode        是否进行编码
     * @param extraParams   扩展参数
     * @return 返回最终生成的签名
     */
    public static String createSignature(Map<String, String> queryParamMap, boolean encode, String... extraParams) {
        StringBuilder _queryParamStr = new StringBuilder(buildQueryParamStr(queryParamMap, encode, null));
        if (extraParams != null && extraParams.length > 0) {
            for (String _extraParam : extraParams) {
                _queryParamStr.append("&").append(_extraParam);
            }
        }
        return DigestUtils.md5Hex(_queryParamStr.toString()).toUpperCase();
    }
}
