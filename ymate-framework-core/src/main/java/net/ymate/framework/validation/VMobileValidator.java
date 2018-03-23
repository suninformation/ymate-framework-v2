/*
 * Copyright 2007-2017 the original author or authors.
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
import net.ymate.platform.validation.AbstractValidator;
import net.ymate.platform.validation.ValidateContext;
import net.ymate.platform.validation.ValidateResult;
import net.ymate.platform.validation.annotation.Validator;
import org.apache.commons.lang.StringUtils;

/**
 * @author 刘镇 (suninformation@163.com) on 17/4/6 下午1:57
 * @version 1.0
 */
@Validator(VMobile.class)
@CleanProxy
public class VMobileValidator extends AbstractValidator {

    public ValidateResult validate(ValidateContext context) {
        Object _paramValue = context.getParamValue();
        if (_paramValue != null) {
            if (!context.getParamValue().getClass().isArray()) {
                String _value = BlurObject.bind(_paramValue).toStringValue();
                if (StringUtils.isNotBlank(_value)) {
                    /*
                    目前匹配号段:
                    - 中国电信号段: 133、149、153、173、177、180、181、189、199
                    - 中国联通号段: 130、131、132、145、155、156、166、175、176、185、186
                    - 中国移动号段: 134(0-8)、135、136、137、138、139、147、150、151、152、157、158、159、178、182、183、184、187、188、198
                    - 其他号段: 14号段以前为上网卡专属号段，如中国联通的是145，中国移动的是147等等。
                    - 虚拟运营商
                        + 电信：1700、1701、1702
                        + 移动：1703、1705、1706
                        + 联通：1704、1707、1708、1709、171
                     */
                    String _regex = StringUtils.defaultIfBlank(((VMobile) context.getAnnotation()).regex(), "^1([358][0-9]|4[579]|66|7[0135678]|9[89])[0-9]{8}$");
                    if (!_value.matches(_regex)) {
                        String _pName = StringUtils.defaultIfBlank(context.getParamLabel(), context.getParamName());
                        _pName = __doGetI18nFormatMessage(context, _pName, _pName);
                        //
                        String _msg = StringUtils.trimToNull(((VMobile) context.getAnnotation()).msg());
                        if (_msg != null) {
                            _msg = __doGetI18nFormatMessage(context, _msg, _msg, _pName);
                        } else {
                            _msg = __doGetI18nFormatMessage(context, "ymp.validation.mobile", "{0} not a valid mobile phone number.", _pName);
                        }
                        return new ValidateResult(context.getParamName(), _msg);
                    }
                }
            }
        }
        return null;
    }
}
