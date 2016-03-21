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

import net.ymate.platform.core.beans.annotation.CleanProxy;
import net.ymate.platform.core.i18n.I18N;
import net.ymate.platform.validation.IValidator;
import net.ymate.platform.validation.ValidateContext;
import net.ymate.platform.validation.ValidateResult;
import net.ymate.platform.validation.annotation.Validator;
import net.ymate.platform.webmvc.IUploadFileWrapper;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;

/**
 * @author 刘镇 (suninformation@163.com) on 16/3/20 上午3:48
 * @version 1.0
 */
@Validator(VUploadFile.class)
@CleanProxy
public class UploadFileValidator implements IValidator {

    public ValidateResult validate(ValidateContext context) {
        // 待验证的参数必须是IUploadFileWrapper类型
        if (context.getParamValue() instanceof IUploadFileWrapper) {
            String _paramName = StringUtils.defaultIfBlank(context.getParamLabel(), context.getParamName());
            _paramName = I18N.formatMessage(VALIDATION_I18N_RESOURCE, _paramName, _paramName);
            //
            VUploadFile _vUploadFile = (VUploadFile) context.getAnnotation();
            if (context.getParamValue().getClass().isArray()) {
                //
                IUploadFileWrapper[] _values = (IUploadFileWrapper[]) context.getParamValue();
                if (_values != null) {
                    long _totalSize = 0;
                    for (IUploadFileWrapper _value : _values) {
                        _totalSize += _value.getSize();
                        ValidateResult _result = __doValidateUploadFileWrapper(context, _paramName, _vUploadFile, _value);
                        if (_result != null) {
                            return _result;
                        }
                    }
                    if (_totalSize > _vUploadFile.totalMax()) {
                        String _msg = StringUtils.trimToNull(_vUploadFile.msg());
                        if (_msg != null) {
                            _msg = I18N.formatMessage(VALIDATION_I18N_RESOURCE, _msg, _msg, _paramName);
                        } else {
                            _msg = I18N.formatMessage(VALIDATION_I18N_RESOURCE, "ymp.validation.upload_file_total_max", "{0} total size must be lt {1}.", _paramName, _vUploadFile.totalMax());
                        }
                        return new ValidateResult(_paramName, _msg);
                    }
                }
            } else {
                IUploadFileWrapper _value = (IUploadFileWrapper) context.getParamValue();
                if (_value != null) {
                    ValidateResult _result = __doValidateUploadFileWrapper(context, _paramName, _vUploadFile, _value);
                    if (_result != null) {
                        return _result;
                    }
                }
            }
        }
        return null;
    }

    private ValidateResult __doValidateUploadFileWrapper(ValidateContext context, String paramName, VUploadFile vUploadFile, IUploadFileWrapper value) {
        boolean _matched = false;
        boolean _isNotAllowContentType = false;
        //
        if (vUploadFile.min() > 0 && value.getSize() < vUploadFile.min()) {
            _matched = true;
        } else if (vUploadFile.max() > 0 && value.getSize() > vUploadFile.max()) {
            _matched = true;
        } else if (vUploadFile.contentTypes().length > 0 && !Arrays.asList(vUploadFile.contentTypes()).contains(value.getContentType())) {
            _matched = true;
            _isNotAllowContentType = true;
        }
        if (_matched) {
            String _msg = StringUtils.trimToNull(vUploadFile.msg());
            if (_msg != null) {
                _msg = I18N.formatMessage(VALIDATION_I18N_RESOURCE, _msg, _msg, paramName);
            } else {
                if (_isNotAllowContentType) {
                    _msg = I18N.formatMessage(VALIDATION_I18N_RESOURCE, "ymp.validation.upload_file_content_type", "{0} content type must be match {1}.", paramName, StringUtils.join(vUploadFile.contentTypes(), ","));
                } else {
                    if (vUploadFile.max() > 0 && vUploadFile.min() > 0) {
                        _msg = I18N.formatMessage(VALIDATION_I18N_RESOURCE, "ymp.validation.upload_file_between", "{0} size must be between {1} and {2}.", paramName, vUploadFile.min(), vUploadFile.max());
                    } else if (vUploadFile.max() > 0) {
                        _msg = I18N.formatMessage(VALIDATION_I18N_RESOURCE, "ymp.validation.upload_file_max", "{0} size must be lt {1}.", paramName, vUploadFile.max());
                    } else {
                        _msg = I18N.formatMessage(VALIDATION_I18N_RESOURCE, "ymp.validation.upload_file_min", "{0} size must be gt {1}.", paramName, vUploadFile.min());
                    }
                }
            }
            return new ValidateResult(context.getParamName(), _msg);
        }
        return null;
    }
}
