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
package net.ymate.framework.filter;

import net.ymate.platform.core.lang.BlurObject;
import net.ymate.platform.core.lang.PairObject;
import net.ymate.platform.webmvc.base.Type;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 通用基础过滤器, 目前支持:
 * <p>1.自定义响应头</p>
 *
 * @author 刘镇 (suninformation@163.com) on 2018/10/10 下午5:38
 * @version 1.0
 * @since 2.0.6
 */
public class GeneralBasicFilter implements Filter {

    private Map<String, PairObject<Type.HeaderType, Object>> __responseHeaders;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String[] _responseHeaders = StringUtils.split(filterConfig.getInitParameter("response_headers"), "|");
        if (ArrayUtils.isNotEmpty(_responseHeaders)) {
            __responseHeaders = new HashMap<String, PairObject<Type.HeaderType, Object>>(_responseHeaders.length);
            for (String _header : _responseHeaders) {
                String[] _headerArr = StringUtils.split(_header, "=");
                if (_headerArr != null && _headerArr.length == 2) {
                    __responseHeaders.put(_headerArr[0], new PairObject<Type.HeaderType, Object>(NumberUtils.isDigits(_headerArr[1]) ? Type.HeaderType.INT : Type.HeaderType.STRING, _headerArr[1]));
                }
            }
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse _response = (HttpServletResponse) response;
        if (__responseHeaders != null && !__responseHeaders.isEmpty()) {
            for (Map.Entry<String, PairObject<Type.HeaderType, Object>> _entry : __responseHeaders.entrySet()) {
                if (_entry.getValue().getKey() == Type.HeaderType.INT) {
                    _response.addIntHeader(_entry.getKey(), BlurObject.bind(_entry.getValue().getValue()).toIntValue());
                } else {
                    _response.addHeader(_entry.getKey(), BlurObject.bind(_entry.getValue().getValue()).toStringValue());
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
