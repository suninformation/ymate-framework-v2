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

import net.ymate.platform.core.beans.annotation.CleanProxy;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.core.util.RuntimeUtils;
import net.ymate.platform.validation.AbstractValidator;
import net.ymate.platform.validation.ValidateContext;
import net.ymate.platform.validation.ValidateResult;
import net.ymate.platform.validation.annotation.Validator;
import org.apache.commons.lang.StringUtils;

/**
 * @author 刘镇 (suninformation@163.com) on 2018/09/05 下午 17:11
 * @version 1.0
 * @since 2.0.6
 */
@Validator(VDataRange.class)
@CleanProxy
public class DataRangeValidator extends AbstractValidator {

    @Override
    public ValidateResult validate(ValidateContext context) {
        Object _paramValue = context.getParamValue();
        if (_paramValue != null) {
            boolean _matched = false;
            VDataRange _anno = (VDataRange) context.getAnnotation();
            try {
                String _rangeValues = StringUtils.join(_anno.value(), "|");
                if (_paramValue.getClass().isArray()) {
                    Object[] _pArray = (Object[]) _paramValue;
                    for (Object _pValue : _pArray) {
                        if (_anno.ignoreCase()) {
                            _matched = !StringUtils.containsIgnoreCase(_rangeValues, BlurObject.bind(_pValue).toStringValue());
                        } else {
                            _matched = !StringUtils.contains(_rangeValues, BlurObject.bind(_pValue).toStringValue());
                        }
                        if (_matched) {
                            break;
                        }
                    }
                } else if (!StringUtils.contains(_rangeValues, BlurObject.bind(_paramValue).toStringValue())) {
                    _matched = true;
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
                    _msg = __doGetI18nFormatMessage(context, "ymp.validation.data_range_invalid", "{0} invalid.", _pName);
                }
                return new ValidateResult(context.getParamName(), _msg);
            }
        }
        return null;
    }
}