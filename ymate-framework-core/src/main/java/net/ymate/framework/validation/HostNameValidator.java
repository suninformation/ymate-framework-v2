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

import net.ymate.framework.core.support.IHostNameChecker;
import net.ymate.framework.exception.ValidationResultException;
import net.ymate.platform.core.beans.annotation.CleanProxy;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.core.util.ClassUtils;
import net.ymate.platform.core.util.RuntimeUtils;
import net.ymate.platform.validation.AbstractValidator;
import net.ymate.platform.validation.ValidateContext;
import net.ymate.platform.validation.ValidateResult;
import net.ymate.platform.validation.annotation.Validator;
import net.ymate.platform.webmvc.IRequestContext;
import net.ymate.platform.webmvc.base.Type;
import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.lang.StringUtils;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/8/12 上午 02:01
 * @version 1.0
 * @since 2.0.6
 */
@Validator(VHostName.class)
@CleanProxy
public class HostNameValidator extends AbstractValidator {

    @Override
    public ValidateResult validate(ValidateContext context) {
        Object _paramValue = context.getParamValue();
        if (_paramValue != null) {
            boolean _matched;
            VHostName _anno = (VHostName) context.getAnnotation();
            try {
                if (_paramValue.getClass().isArray()) {
                    _matched = true;
                } else {
                    String _url = BlurObject.bind(_paramValue).toStringValue();
                    //
                    IRequestContext _requestContext = WebContext.getContext().getAttribute(Type.Context.WEB_REQUEST_CONTEXT);
                    if (_requestContext != null && StringUtils.containsIgnoreCase(StringUtils.substringBefore(_url, "?"), _requestContext.getOriginalUrl())) {
                        _matched = true;
                    } else if (_anno.checker().equals(IHostNameChecker.class)) {
                        _matched = !IHostNameChecker.DEFAULT.check(context, _url);
                    } else {
                        _matched = !ClassUtils.impl(_anno.checker(), IHostNameChecker.class).check(context, _url);
                    }
                }
            } catch (Exception e) {
                throw new Error(RuntimeUtils.unwrapThrow(e));
            }
            if (_matched) {
                String _pName = StringUtils.defaultIfBlank(context.getParamLabel(), context.getParamName());
                _pName = __doGetI18nFormatMessage(context, _pName, _pName);
                String _msg = StringUtils.trimToNull(_anno.msg());
                if (_msg != null) {
                    _msg = __doGetI18nFormatMessage(context, _msg, _msg, _pName);
                } else {
                    _msg = __doGetI18nFormatMessage(context, "ymp.validation.hostname_invalid", "{0} is invalid.", _pName);
                }
                if (_anno.httpStatus() > 0) {
                    throw new ValidationResultException(_msg, _anno.httpStatus());
                }
                return new ValidateResult(context.getParamName(), _msg);
            }
        }
        return null;
    }
}