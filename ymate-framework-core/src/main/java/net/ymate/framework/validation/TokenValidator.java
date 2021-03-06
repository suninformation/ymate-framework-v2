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
package net.ymate.framework.validation;

import net.ymate.framework.core.support.TokenProcessHelper;
import net.ymate.platform.core.beans.annotation.CleanProxy;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.validation.AbstractValidator;
import net.ymate.platform.validation.ValidateContext;
import net.ymate.platform.validation.ValidateResult;
import net.ymate.platform.validation.annotation.Validator;
import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.lang.StringUtils;

/**
 * @author 刘镇 (suninformation@163.com) on 16/11/27 下午12:17
 * @version 1.0
 */
@Validator(VToken.class)
@CleanProxy
public class TokenValidator extends AbstractValidator {

    @Override
    public ValidateResult validate(ValidateContext context) {
        boolean _matched = false;
        VToken _vToken = (VToken) context.getAnnotation();
        if (context.getParamValue() != null) {
            String _token = null;
            if (context.getParamValue().getClass().isArray()) {
                Object[] _objArr = (Object[]) context.getParamValue();
                if (_objArr.length > 0) {
                    _token = BlurObject.bind(_objArr[0]).toStringValue();
                }
            } else {
                _token = BlurObject.bind(context.getParamValue()).toStringValue();
            }
            //
            if (!TokenProcessHelper.getInstance().isTokenValid(WebContext.getRequest(), _vToken.name(), _token, _vToken.reset())) {
                _matched = true;
            }
        }
        if (_matched) {
            String _pName = StringUtils.defaultIfBlank(context.getParamLabel(), context.getParamName());
            _pName = __doGetI18nFormatMessage(context, _pName, _pName);
            String _msg = StringUtils.trimToNull(_vToken.msg());
            if (_msg != null) {
                _msg = __doGetI18nFormatMessage(context, _msg, _msg, _pName);
            } else {
                _msg = __doGetI18nFormatMessage(context, "ymp.validation.token_invalid", "{0} is invalid.", _pName);
            }
            return new ValidateResult(context.getParamName(), _msg);
        }
        return null;
    }
}
