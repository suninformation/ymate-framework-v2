/*
 * Copyright (c) 2007-2107, the original author or authors. All rights reserved.
 *
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
 */
package net.ymate.framework.webmvc;

import com.alibaba.fastjson.JSONObject;
import net.ymate.platform.webmvc.view.IView;
import net.ymate.platform.webmvc.view.View;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 刘镇 (suninformation@163.com) on 15/8/18 下午2:18
 * @version 1.0
 */
public class WebResultHelper {

    private int __code;

    private String __msg;

    private Map<String, Object> __datas;

    private Map<String, Object> __attrs;

    public static WebResultHelper SUCCESS() {
        return new WebResultHelper(ErrorCode.SUCCESSED);
    }

    public static WebResultHelper CODE(int code) {
        return new WebResultHelper(code);
    }

    protected WebResultHelper(int code) {
        __code = code;
        __datas = new HashMap<String, Object>();
        __attrs = new HashMap<String, Object>();
    }

    public int code() {
        return __code;
    }

    public String msg() {
        return StringUtils.trimToEmpty(__msg);
    }

    public WebResultHelper msg(String msg) {
        __msg = msg;
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T dataAttr(String dataKey) {
        return (T) __datas.get(dataKey);
    }

    public WebResultHelper dataAttr(String dataKey, Object dataValue) {
        __datas.put(dataKey, dataValue);
        return this;
    }

    @SuppressWarnings("unchecked")
    public <T> T attr(String attrKey) {
        return (T) __attrs.get(attrKey);
    }

    public WebResultHelper attr(String attrKey, Object attrValue) {
        __attrs.put(attrKey, attrValue);
        return this;
    }

    public IView toJSON() {
        JSONObject _jsonObj = new JSONObject();
        _jsonObj.put("ret", __code);
        if (StringUtils.isNotBlank(__msg)) {
            _jsonObj.put("msg", __msg);
        }
        if (!__datas.isEmpty()) {
            _jsonObj.put("data", __datas);
        }
        if (!__attrs.isEmpty()) {
            _jsonObj.putAll(__attrs);
        }
        //
        return View.jsonView(_jsonObj);
    }

    public IView toXML(boolean cdata) {
        StringBuilder _content = new StringBuilder();
        _content.append("<xml><ret>").append(__code).append("</ret>");
        if (StringUtils.isNotBlank(__msg)) {
            if (cdata) {
                _content.append("<msg><![CDATA[").append(__msg).append("]]></msg>");
            } else {
                _content.append("<msg>").append(__msg).append("</msg>");
            }
        }
        if (!__datas.isEmpty()) {
            _content.append("<data>");
            for (Map.Entry<String, Object> _entry : __datas.entrySet()) {
                __doAppendContent(_content, cdata, _entry.getKey(), _entry.getValue());
            }
            _content.append("</data>");
        }
        if (!__attrs.isEmpty()) {
            for (Map.Entry<String, Object> _entry : __attrs.entrySet()) {
                __doAppendContent(_content, cdata, _entry.getKey(), _entry.getValue());
            }
        }
        _content.append("</xml>");
        return View.textView(_content.toString());
    }

    private void __doAppendContent(StringBuilder content, boolean cdata, String key, Object value) {
        if (value != null && StringUtils.isNotBlank(value.toString())) {
            content.append("<").append(key).append(">");
            if (value instanceof Number) {
                content.append(value);
            } else {
                if (cdata) {
                    content.append("<![CDATA[").append(value).append("]]>");
                } else {
                    content.append(value);
                }
            }
            content.append("</").append(key).append(">");
        }
    }

    public IView toXML() {
        return toXML(false);
    }
}
