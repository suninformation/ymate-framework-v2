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

import net.ymate.framework.core.Optional;
import net.ymate.platform.core.beans.annotation.CleanProxy;
import net.ymate.platform.validation.AbstractValidator;
import net.ymate.platform.validation.ValidateContext;
import net.ymate.platform.validation.ValidateResult;
import net.ymate.platform.validation.annotation.Validator;
import net.ymate.platform.webmvc.IUploadFileWrapper;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 刘镇 (suninformation@163.com) on 16/3/20 上午3:48
 * @version 1.0
 */
@Validator(VUploadFile.class)
@CleanProxy
public class UploadFileValidator extends AbstractValidator {

    public ValidateResult validate(ValidateContext context) {
        // 待验证的参数必须是IUploadFileWrapper类型
        Object _paramValue = context.getParamValue();
        if (_paramValue != null) {
            String _paramName = StringUtils.defaultIfBlank(context.getParamLabel(), context.getParamName());
            _paramName = __doGetI18nFormatMessage(context, _paramName, _paramName);
            //
            VUploadFile _vUploadFile = (VUploadFile) context.getAnnotation();
            //
            List<String> _allowContentTypes = __doGetAllowContentTypes(context, _vUploadFile);
            //
            if (_paramValue.getClass().isArray() && _paramValue instanceof IUploadFileWrapper[]) {
                //
                IUploadFileWrapper[] _values = (IUploadFileWrapper[]) _paramValue;
                long _totalSize = 0;
                for (IUploadFileWrapper _value : _values) {
                    _totalSize += _value.getSize();
                    ValidateResult _result = __doValidateUploadFileWrapper(context, _allowContentTypes, _paramName, _vUploadFile, _value);
                    if (_result != null) {
                        return _result;
                    }
                }
                if (_totalSize > _vUploadFile.totalMax()) {
                    String _msg = StringUtils.trimToNull(_vUploadFile.msg());
                    if (_msg != null) {
                        _msg = __doGetI18nFormatMessage(context, _msg, _msg, _paramName);
                    } else {
                        _msg = __doGetI18nFormatMessage(context, "ymp.validation.upload_file_total_max", "{0} total size must be lt {1}.", _paramName, _vUploadFile.totalMax());
                    }
                    return new ValidateResult(_paramName, _msg);
                }
            } else if (_paramValue instanceof IUploadFileWrapper) {
                IUploadFileWrapper _value = (IUploadFileWrapper) _paramValue;
                ValidateResult _result = __doValidateUploadFileWrapper(context, _allowContentTypes, _paramName, _vUploadFile, _value);
                if (_result != null) {
                    return _result;
                }
            }
        }
        return null;
    }

    private ValidateResult __doValidateUploadFileWrapper(ValidateContext context, List<String> allowContentTypes, String paramName, VUploadFile vUploadFile, IUploadFileWrapper value) {
        boolean _matched = false;
        boolean _isNotAllowContentType = false;
        //
        if (vUploadFile.min() > 0 && value.getSize() < vUploadFile.min()) {
            _matched = true;
        } else if (vUploadFile.max() > 0 && value.getSize() > vUploadFile.max()) {
            _matched = true;
        } else if (allowContentTypes.size() > 0) {
            boolean _flag = false;
            for (String _contentType : allowContentTypes) {
                if (StringUtils.contains(value.getContentType(), _contentType)) {
                    _flag = true;
                    break;
                }
            }
            if (!_flag) {
                _matched = true;
                _isNotAllowContentType = true;
            }
        }
        if (_matched) {
            String _msg = StringUtils.trimToNull(vUploadFile.msg());
            if (_msg != null) {
                _msg = __doGetI18nFormatMessage(context, _msg, _msg, paramName);
            } else {
                if (_isNotAllowContentType) {
                    _msg = __doGetI18nFormatMessage(context, "ymp.validation.upload_file_content_type", "{0} content type must be match {1}.", paramName, StringUtils.join(vUploadFile.contentTypes(), ","));
                } else {
                    if (vUploadFile.max() > 0 && vUploadFile.min() > 0) {
                        _msg = __doGetI18nFormatMessage(context, "ymp.validation.upload_file_between", "{0} size must be between {1} and {2}.", paramName, vUploadFile.min(), vUploadFile.max());
                    } else if (vUploadFile.max() > 0) {
                        _msg = __doGetI18nFormatMessage(context, "ymp.validation.upload_file_max", "{0} size must be lt {1}.", paramName, vUploadFile.max());
                    } else {
                        _msg = __doGetI18nFormatMessage(context, "ymp.validation.upload_file_min", "{0} size must be gt {1}.", paramName, vUploadFile.min());
                    }
                }
            }
            return new ValidateResult(context.getParamName(), _msg);
        }
        return null;
    }

    private List<String> __doGetAllowContentTypes(ValidateContext context, VUploadFile vUploadFile) {
        List<String> _contentTyps = new ArrayList<String>();
        //
        _contentTyps.addAll(Arrays.asList(vUploadFile.contentTypes()));
        //
        String[] _types = StringUtils.split(StringUtils.trimToEmpty(context.getContextParams().get(Optional.VALIDATION_ALLOW_UPLOAD_CONTENT_TYPES)), "|");
        if (_types.length > 0) {
            _contentTyps.addAll(Arrays.asList(_types));
        }
        //
        _types = StringUtils.split(StringUtils.trimToEmpty(context.getOwner().getConfig().getParam(Optional.VALIDATION_ALLOW_UPLOAD_CONTENT_TYPES)), "|");
        if (_types.length > 0) {
            _contentTyps.addAll(Arrays.asList(_types));
        }
        return _contentTyps;
    }
}
