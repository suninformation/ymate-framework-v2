/*
 * Copyright (c) 2007-2016, the original author or authors. All rights reserved.
 *
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
 */
package net.ymate.framework.commons;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 15/9/7 下午11:20
 * @version 1.0
 */
public interface IHttpResponse {

    public static class NEW implements IHttpResponse {

        private int __statusCode;

        private String __content;

        private String __contentType;

        private String __contentEncoding;

        private long __contentLength;

        private Map<String, String> __headers;

        public NEW(HttpResponse response) throws IOException {
            __statusCode = response.getStatusLine().getStatusCode();
            __content = EntityUtils.toString(response.getEntity(), HttpClientHelper.DEFAULT_CHARSET);
            Header _header = response.getEntity().getContentEncoding();
            if (_header != null) {
                __contentEncoding = _header.getValue();
            }
            _header = response.getEntity().getContentType();
            if (_header != null) {
                __contentType = _header.getValue();
            }
            __contentLength = response.getEntity().getContentLength();
            //
            __headers = new HashMap<String, String>();
            Header[] _headersArr = response.getAllHeaders();
            if (_headersArr != null) {
                for (Header _element : _headersArr) {
                    __headers.put(_element.getName(), _element.getValue());
                }
            }
        }

        public int getStatusCode() {
            return __statusCode;
        }

        public String getContent() {
            return __content;
        }

        public String getContentType() {
            return __contentType;
        }

        public long getContentLength() {
            return __contentLength;
        }

        public String getContentEncoding() {
            return __contentEncoding;
        }

        public Map<String, String> getHeaders() {
            return Collections.unmodifiableMap(__headers);
        }

        @Override
        public String toString() {
            return "{" +
                    "__statusCode=" + __statusCode +
                    ", __content='" + __content + '\'' +
                    ", __contentType='" + __contentType + '\'' +
                    ", __contentEncoding='" + __contentEncoding + '\'' +
                    ", __contentLength=" + __contentLength +
                    ", __headers=" + __headers +
                    '}';
        }
    }

    public int getStatusCode();

    public String getContent();

    public String getContentType();

    public long getContentLength();

    public String getContentEncoding();

    public Map<String, String> getHeaders();
}
