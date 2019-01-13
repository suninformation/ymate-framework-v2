/*
 * Copyright 2007-2019 the original author or authors.
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

import net.ymate.framework.commons.DateTimeHelper;
import net.ymate.platform.core.beans.annotation.CleanProxy;
import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.core.util.DateTimeUtils;
import net.ymate.platform.validation.AbstractValidator;
import net.ymate.platform.validation.ValidateContext;
import net.ymate.platform.validation.ValidateResult;
import net.ymate.platform.validation.annotation.Validator;
import net.ymate.platform.webmvc.context.WebContext;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Date;

/**
 * @author 刘镇 (suninformation@163.com) on 2019/01/12 下午 12:30
 * @version 1.0
 * @since 2.0.7
 */
@Validator(VDateTimeRange.class)
@CleanProxy
public class DateTimeRangeValidator extends AbstractValidator {

    public static Long getStartDateTime(String paramName) {
        return WebContext.getContext().getAttribute("start_" + paramName);
    }

    public static Long getEndDateTime(String paramName) {
        return WebContext.getContext().getAttribute("end_" + paramName);
    }

    @Override
    public ValidateResult validate(ValidateContext context) {
        Object _paramValue = context.getParamValue();
        if (_paramValue != null) {
            boolean _matched = false;
            boolean _maxDaysInvalid = false;
            VDateTimeRange _vDateTimeRange = (VDateTimeRange) context.getAnnotation();
            if (!context.getParamValue().getClass().isArray()) {
                String[] _dataTimeArr = StringUtils.split(BlurObject.bind(_paramValue).toStringValue(), _vDateTimeRange.separator());
                if (ArrayUtils.isNotEmpty(_dataTimeArr)) {
                    if (_dataTimeArr.length <= 2) {
                        try {
                            Date _startDateTime = DateTimeUtils.parseDateTime(_dataTimeArr[0], _vDateTimeRange.pattern());
                            if (_vDateTimeRange.single() && _dataTimeArr.length > 1) {
                                Date _endDateTime = DateTimeUtils.parseDateTime(_dataTimeArr[1], _vDateTimeRange.pattern());
                                if (_vDateTimeRange.maxDays() > 0) {
                                    long _days = DateTimeHelper.bind(_endDateTime).subtract(_startDateTime) / DateTimeUtils.DAY;
                                    if (_days < 0 || _days > _vDateTimeRange.maxDays()) {
                                        _matched = true;
                                        _maxDaysInvalid = true;
                                    }
                                }
                                if (!_matched) {
                                    WebContext.getContext().addAttribute("end_" + StringUtils.defaultIfBlank(_vDateTimeRange.value(), context.getParamName()), _endDateTime.getTime());
                                }
                            }
                            if (!_matched) {
                                WebContext.getContext().addAttribute("start_" + StringUtils.defaultIfBlank(_vDateTimeRange.value(), context.getParamName()), _startDateTime.getTime());
                            }
                        } catch (Exception e) {
                            _matched = true;
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
                String _msg = StringUtils.trimToNull(((VDateTimeRange) context.getAnnotation()).msg());
                if (_msg != null) {
                    _msg = __doGetI18nFormatMessage(context, _msg, _msg, _pName);
                } else if (_maxDaysInvalid) {
                    _msg = __doGetI18nFormatMessage(context, "ymp.validation.data_time_range_max_days", "{0} has exceeded the max days.", _pName);
                } else {
                    _msg = __doGetI18nFormatMessage(context, "ymp.validation.data_time_range_invalid", "{0} invalid.", _pName);
                }
                return new ValidateResult(context.getParamName(), _msg);
            }
        }
        return null;
    }
}