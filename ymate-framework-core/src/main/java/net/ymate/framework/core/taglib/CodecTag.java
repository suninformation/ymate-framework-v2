/*
 * Copyright (c) 2007-2016, the original author or authors. All rights reserved.
 *
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
 */
package net.ymate.framework.core.taglib;

import net.ymate.platform.core.util.RuntimeUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.jsp.JspException;
import java.io.UnsupportedEncodingException;

/**
 * 字符加解密标签
 *
 * @author 刘镇 (suninformation@163.com) on 14/10/23
 * @version 1.0
 */
public class CodecTag extends AbstractTagSupport {

    /**
     * 加密方式：md5/base64encode/base64decode
     */
    private String method;

    /**
     * 待加解密的数据
     */
    private String data;

    /**
     * 字符串编码，默认UTF-8
     */
    private String charset;

    @Override
    protected Object doProcessTagData() throws JspException {
        String _result = null;
        try {
            if ("base64encode".equalsIgnoreCase(method)) {
                _result = Base64.encodeBase64String(data.getBytes(StringUtils.defaultIfEmpty(charset, "UTF-8")));
            } else if ("base64decode".equalsIgnoreCase(method)) {
                _result = new String(Base64.decodeBase64(data), StringUtils.defaultIfEmpty(charset, "UTF-8"));
            } else {
                _result = DigestUtils.md5Hex(data);
            }
        } catch (UnsupportedEncodingException e) {
            throw new JspException(RuntimeUtils.unwrapThrow(e));
        }
        return _result;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

}
