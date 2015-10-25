/*
 * Copyright (c) 2007-2016, the original author or authors. All rights reserved.
 *
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
 */
package net.ymate.framework.commons;

import net.ymate.platform.core.util.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author 刘镇 (suninformation@163.com) on 15/8/29 上午9:37
 * @version 1.0
 */
public interface IFileWrapper {

    public static class NEW implements IFileWrapper {

        private boolean __hasError;

        private String __errMsg;

        private String __fileName;

        private String __name;

        private String __suffix;

        private String __contentType;

        private long __contentLength;

        private File __sourceFile;

        public NEW(String fileName, String contentType, long contentLength, File sourceFile) {
            __fileName = fileName;
            if (StringUtils.isNotBlank(__fileName)) {
                __name = StringUtils.substringBefore(StringUtils.replace(__fileName, "\"", ""), ".");
                __suffix = FileUtils.getExtName(__fileName);
            }
            //
            __contentType = contentType;
            __contentLength = contentLength;
            __sourceFile = sourceFile;
        }

        public NEW(String contentType, long contentLength, File sourceFile) {
            __contentType = contentType;
            __contentLength = contentLength;
            __sourceFile = sourceFile;
        }

        public NEW(String errMsg) {
            __hasError = true;
            __errMsg = errMsg;
        }

        public boolean hasError() {
            return __hasError;
        }

        public String getErrorMsg() {
            return __errMsg;
        }

        public String getFileName() {
            return __fileName;
        }

        public String getName() {
            return __name;
        }

        public String getSuffix() {
            return __suffix;
        }

        public long getContentLength() {
            return __contentLength;
        }

        public String getContentType() {
            return __contentType;
        }

        public InputStream getInputStream() throws IOException {
            return org.apache.commons.io.FileUtils.openInputStream(__sourceFile);
        }

        public void writeTo(File distFile) throws IOException {
            if (!__sourceFile.renameTo(distFile)) {
                org.apache.commons.io.FileUtils.copyInputStreamToFile(getInputStream(), distFile);
            } else {
                throw new IOException("Cannot write file!");
            }
        }
    }

    public boolean hasError();

    public String getErrorMsg();

    public String getFileName();

    public String getName();

    public String getSuffix();

    public long getContentLength();

    public String getContentType();

    public InputStream getInputStream() throws IOException;

    public void writeTo(File distFile) throws IOException;
}
