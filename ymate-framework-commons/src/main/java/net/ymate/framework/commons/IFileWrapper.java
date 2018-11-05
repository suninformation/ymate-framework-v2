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

import net.ymate.platform.core.util.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author 刘镇 (suninformation@163.com) on 15/8/29 上午9:37
 * @version 1.0
 */
public interface IFileWrapper {

    class NEW implements IFileWrapper {

        private boolean __hasError;

        private String __errMsg;

        private String __fileName;

        private String __name;

        private String __suffix;

        private String __contentType;

        private long __contentLength;

        private InputStream __sourceInputStream;

        public NEW(String fileName, String contentType, long contentLength, InputStream sourceInputStream) {
            __fileName = fileName;
            if (StringUtils.isNotBlank(__fileName)) {
                __name = StringUtils.substringBefore(StringUtils.replace(__fileName, "\"", ""), ".");
                __suffix = FileUtils.getExtName(__fileName);
            }
            //
            __contentType = contentType;
            __contentLength = contentLength;
            __sourceInputStream = sourceInputStream;
        }

        public NEW(String contentType, long contentLength, InputStream sourceInputStream) {
            __contentType = contentType;
            __contentLength = contentLength;
            __sourceInputStream = sourceInputStream;
        }

        public NEW(String errMsg) {
            __hasError = true;
            __errMsg = errMsg;
        }

        @Override
        public boolean hasError() {
            return __hasError;
        }

        @Override
        public String getErrorMsg() {
            return __errMsg;
        }

        @Override
        public String getFileName() {
            return __fileName;
        }

        @Override
        public String getName() {
            return __name;
        }

        @Override
        public String getSuffix() {
            return __suffix;
        }

        @Override
        public long getContentLength() {
            return __contentLength;
        }

        @Override
        public String getContentType() {
            return __contentType;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return __sourceInputStream;
        }

        @Override
        public void writeTo(File distFile) throws IOException {
            org.apache.commons.io.FileUtils.copyInputStreamToFile(getInputStream(), distFile);
        }

        @Override
        public ContentBody toContentBody() throws IOException {
            final long _contentLength = getContentLength();
            return new InputStreamBody(getInputStream(), ContentType.create(getContentType()), getFileName()) {
                @Override
                public long getContentLength() {
                    return _contentLength;
                }
            };
        }
    }

    boolean hasError();

    String getErrorMsg();

    String getFileName();

    String getName();

    String getSuffix();

    long getContentLength();

    String getContentType();

    InputStream getInputStream() throws IOException;

    void writeTo(File distFile) throws IOException;

    ContentBody toContentBody() throws IOException;
}
