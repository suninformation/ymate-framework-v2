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
package net.ymate.framework.validation;

import net.ymate.platform.core.YMP;
import net.ymate.platform.core.beans.annotation.CleanProxy;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.core.util.CodecUtils;
import net.ymate.platform.validation.AbstractValidator;
import net.ymate.platform.validation.ValidateContext;
import net.ymate.platform.validation.ValidateResult;
import net.ymate.platform.validation.annotation.Validator;
import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/10/24 下午6:10
 * @version 1.0
 * @since 2.0.6
 */
@Validator(VRSAData.class)
@CleanProxy
public class RSADataValidator extends AbstractValidator {

    public static final String RSA_PUBLIC_KEY = "rsa_public_key";

    public static final String RSA_PRIVATE_KEY = "rsa_private_key";

    /**
     * @param paramName 参数名称
     * @return 获取指定名称参数的原始内容
     */
    public static BlurObject originalValue(String paramName) {
        String _value = WebContext.getContext().getAttribute("original_" + paramName);
        return BlurObject.bind(_value);
    }

    public static String getRSAPublicKey() {
        String _publicKey = YMP.get().getConfig().getParam(RSA_PUBLIC_KEY);
        if (StringUtils.isBlank(_publicKey)) {
            throw new NullArgumentException(RSA_PUBLIC_KEY);
        }
        return _publicKey;
    }

    public static String getRSAPrivateKey() {
        String _privateKey = YMP.get().getConfig().getParam(RSA_PRIVATE_KEY);
        if (StringUtils.isBlank(_privateKey)) {
            throw new NullArgumentException(RSA_PRIVATE_KEY);
        }
        return _privateKey;
    }

    /**
     * 对str字符串内容采用RSA公钥加密
     *
     * @param str 字符串内容
     * @return 加密后的BASE64字符串
     * @throws Exception 可能产生的任何异常
     */
    public static String encryptStr(String str) throws Exception {
        return CodecUtils.RSA.encryptPublicKey(str, getRSAPublicKey());
    }

    public static String encryptPrivateStr(String str) throws Exception {
        return CodecUtils.RSA.encrypt(str, getRSAPrivateKey());
    }

    /**
     * 对str字符串内容采用RSA私钥解密
     *
     * @param str 字符串内容
     * @return 解密后的字符串内容
     * @throws Exception 可能产生的任何异常
     */
    public static String decryptStr(String str) throws Exception {
        return CodecUtils.RSA.decrypt(str, getRSAPrivateKey());
    }

    public static String decryptPublicStr(String str) throws Exception {
        return CodecUtils.RSA.decryptPublicKey(str, getRSAPublicKey());
    }

    @Override
    public ValidateResult validate(ValidateContext context) {
        boolean _matched = false;
        boolean _exceptionFlag = false;
        VRSAData _vData = (VRSAData) context.getAnnotation();
        Object _paramValue = context.getParamValue();
        if (_paramValue != null) {
            if (_paramValue.getClass().isArray()) {
                _matched = true;
            } else {
                String _value = BlurObject.bind(_paramValue).toStringValue();
                if (StringUtils.isNotBlank(_value)) {
                    try {
                        String originalValue = decryptStr(_value);
                        int _length = StringUtils.length(originalValue);
                        if (_vData.minLength() > 0 && _vData.maxLength() == _vData.minLength() && _length != _vData.maxLength()) {
                            _matched = true;
                        } else if (_vData.minLength() > 0 && _length < _vData.minLength()) {
                            _matched = true;
                        } else if (_vData.maxLength() > 0 && _length > _vData.maxLength()) {
                            _matched = true;
                        } else {
                            WebContext.getContext().addAttribute("original_" + StringUtils.defaultIfBlank(_vData.value(), context.getParamName()), originalValue);
                        }
                    } catch (Exception e) {
                        _matched = true;
                        _exceptionFlag = true;
                    }
                } else {
                    _matched = true;
                }
            }
        }
        if (_matched) {
            String _pName = StringUtils.defaultIfBlank(context.getParamLabel(), context.getParamName());
            _pName = __doGetI18nFormatMessage(context, _pName, _pName);
            //
            String _msg = StringUtils.trimToNull(_vData.msg());
            if (_msg != null) {
                _msg = __doGetI18nFormatMessage(context, _msg, _msg, _pName);
            } else if (!_exceptionFlag) {
                if (_vData.maxLength() > 0 && _vData.minLength() > 0) {
                    if (_vData.maxLength() == _vData.minLength()) {
                        _msg = __doGetI18nFormatMessage(context, "ymp.validation.length_eq", "{0} length must be eq {1}.", _pName, _vData.maxLength());
                    } else {
                        _msg = __doGetI18nFormatMessage(context, "ymp.validation.length_between", "{0} length must be between {1} and {2}.", _pName, _vData.minLength(), _vData.maxLength());
                    }
                } else if (_vData.maxLength() > 0) {
                    _msg = __doGetI18nFormatMessage(context, "ymp.validation.length_max", "{0} length must be lt {1}.", _pName, _vData.maxLength());
                } else {
                    _msg = __doGetI18nFormatMessage(context, "ymp.validation.rsa_data_invalid", "{0} is invalid.", _pName);
                }
            } else {
                _msg = __doGetI18nFormatMessage(context, "ymp.validation.rsa_data_invalid", "{0} is invalid.", _pName);
            }
            return new ValidateResult(context.getParamName(), _msg);
        }
        return null;
    }
}
