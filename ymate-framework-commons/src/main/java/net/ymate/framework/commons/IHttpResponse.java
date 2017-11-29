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

    class NEW implements IHttpResponse {

        private int __statusCode;

        private String __content;

        private String __contentType;

        private String __contentEncoding;

        private long __contentLength;

        private Map<String, String> __headers;

        public NEW(HttpResponse response) throws IOException {
            this(response, HttpClientHelper.DEFAULT_CHARSET);
        }

        public NEW(HttpResponse response, String defaultCharset) throws IOException {
            __statusCode = response.getStatusLine().getStatusCode();
            __content = EntityUtils.toString(response.getEntity(), defaultCharset);
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

    int getStatusCode();

    String getContent();

    String getContentType();

    long getContentLength();

    String getContentEncoding();

    Map<String, String> getHeaders();
}
