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
package net.ymate.framework.core.util;

import net.ymate.framework.core.Optional;
import net.ymate.platform.core.YMP;
import net.ymate.platform.core.i18n.I18N;
import net.ymate.platform.core.util.CodecUtils;
import net.ymate.platform.core.util.ExpressionUtils;
import net.ymate.platform.validation.ValidateResult;
import net.ymate.platform.webmvc.WebMVC;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Collection;

/**
 * Web通用工具类
 *
 * @author 刘镇 (suninformation@163.com) on 14-7-6
 * @version 1.0
 */
public class WebUtils {

    /**
     * @param request      HttpServletRequest对象
     * @param requestPath  控制器路径
     * @param withBasePath 是否采用完整路径（即非相对路径）
     * @return 构建控制器URL访问路径
     */
    public static String buildURL(HttpServletRequest request, String requestPath, boolean withBasePath) {
        requestPath = StringUtils.trimToEmpty(requestPath);
        if (withBasePath && requestPath.equals("") && requestPath.charAt(0) == '/') {
            requestPath = StringUtils.substringAfter(requestPath, "/");
        }
        return (withBasePath ? WebUtils.baseURL(request) + requestPath : requestPath) + StringUtils.defaultIfEmpty(YMP.get().getConfig().getParam(Optional.REQUEST_SUFFIX), "");
    }

    /**
     * @param request HttpServletRequest对象
     * @return 获取当前站点基准URL
     */
    public static String baseURL(HttpServletRequest request) {
        StringBuilder basePath = new StringBuilder(request.getScheme()).append("://").append(request.getServerName());
        if (request.getServerPort() != 80 && request.getServerPort() != 443) {
            basePath.append(":").append(request.getServerPort());
        }
        if (StringUtils.isNotBlank(request.getContextPath())) {
            basePath.append(request.getContextPath());
        }
        if (!basePath.toString().endsWith("/")) {
            basePath.append("/");
        }
        return basePath.toString();
    }

    public static String encodeURL(String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String decodeURL(String url) {
        try {
            return URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return url;
    }

    /**
     * @param request HttpServletRequest对象
     * @return 是否AJAX请求（需要在使用Ajax请求时设置请求头）
     */
    public static boolean isAjax(HttpServletRequest request) {
        // 判断条件: (x-requested-with = XMLHttpRequest)
        String _httpx = request.getHeader("x-requested-with");
        return StringUtils.isNotBlank(_httpx) && "XMLHttpRequest".equalsIgnoreCase(_httpx);
    }

    /**
     * @param request HttpServletRequest对象
     * @return 判断当前请求是否采用POST方式提交
     */
    public static boolean isPost(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod());
    }

    /**
     * @param url 目标URL地址
     * @return 执行JS方式的页面跳转
     */
    public static String doRedirectJavaScript(String url) {
        return "<script type=\"text/javascript\">window.location.href=\"" + url + "\"</script>";
    }

    /**
     * @param response HttpServletResponse对象
     * @param url      目标URL地址
     * @return 通过设置Header的Location属性执行页面跳转
     */
    public static String doRedirectHeaderLocation(HttpServletResponse response, String url) {
        response.setHeader("Location", url);
        return "http:" + HttpServletResponse.SC_MOVED_PERMANENTLY;
    }

    /**
     * @param response    HttpServletResponse对象
     * @param templateUrl JSP等模板文件URL
     * @param time        间隔时间
     * @param url         页面URL地址，空为当前页面
     * @return 通过设置Header的Refresh属性执行页面刷新或跳转，若url参数为空，则仅向Header添加time时间后自动刷新当前页面
     */
    public static String doRedirectHeaderRefresh(HttpServletResponse response, String templateUrl, int time, String url) {
        if (StringUtils.isBlank(url)) {
            response.setIntHeader("REFRESH", time);
        } else {
            String _content = time + ";URL=" + url;
            response.setHeader("REFRESH", _content);
        }
        return templateUrl;
    }

    /**
     * @param request HttpServletRequest对象
     * @return 获取用户IP地址
     */
    public static String getRemoteAddr(HttpServletRequest request) {
        String _ip = request.getHeader("x-forwarded-for");
        if (StringUtils.isBlank(_ip) || "unknown".equalsIgnoreCase(_ip)) {
            _ip = request.getHeader("Proxy-Client-IP");
        }
        if (StringUtils.isBlank(_ip) || "unknown".equalsIgnoreCase(_ip)) {
            _ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (StringUtils.isBlank(_ip) || "unknown".equalsIgnoreCase(_ip)) {
            _ip = request.getRemoteAddr();
        }
        return _ip;
    }

    /**
     * @param source 源字符串
     * @param key    键
     * @param value  值
     * @return 占位符替换
     */
    public static String replaceRegText(String source, String key, String value) {
        String _regex = "\\@\\{" + key + "\\}";
        return source.replaceAll(_regex, value);
    }

    public static String replaceRegClear(String source) {
        return replaceRegText(source, "(.+?)", "").replaceAll("\\\\", "");
    }

    /**
     * @param request         HttpServletRequest对象
     * @param response        HttpServletResponse对象
     * @param jspFile         JSP文件路径
     * @param charsetEncoding 字符编码
     * @return 执行JSP并返回HTML源码
     * @throws ServletException 可能产生的异常
     * @throws IOException      可能产生的异常
     */
    public static String includeJSP(HttpServletRequest request, HttpServletResponse response, String jspFile, String charsetEncoding) throws ServletException, IOException {
        final OutputStream _output = new ByteArrayOutputStream();
        final PrintWriter _writer = new PrintWriter(new OutputStreamWriter(_output));
        final ServletOutputStream _servletOutput = new ServletOutputStream() {
            @Override
            public void write(int b) throws IOException {
                _output.write(b);
            }

            @Override
            public void write(byte[] b, int off, int len) throws IOException {
                _output.write(b, off, len);
            }
        };
        HttpServletResponse _response = new HttpServletResponseWrapper(response) {
            @Override
            public ServletOutputStream getOutputStream() {
                return _servletOutput;
            }

            @Override
            public PrintWriter getWriter() {
                return _writer;
            }
        };
        charsetEncoding = StringUtils.defaultIfEmpty(charsetEncoding, response.getCharacterEncoding());
        _response.setCharacterEncoding(StringUtils.defaultIfEmpty(charsetEncoding, WebMVC.get().getModuleCfg().getDefaultCharsetEncoding()));
        request.getRequestDispatcher(jspFile).include(request, _response);
        _writer.flush();
        return _output.toString();
    }

    /**
     * 内容加密(基于客户端IP和浏览器类型)
     *
     * @param request HttpServletRequest对象
     * @param dataStr 待加密的内容
     * @return 加密后的内容
     * @throws Exception 可能产生的异常
     */
    public static String encryptStr(HttpServletRequest request, String dataStr) throws Exception {
        return new String(Base64.encodeBase64URLSafe(CodecUtils.DES.encrypt(dataStr.getBytes("UTF-8"),
                DigestUtils.md5Hex(request.getRemoteAddr() + request.getHeader("User-Agent")).getBytes())));
    }

    /**
     * 内容加密
     *
     * @param dataStr 待加密的内容
     * @param key     密钥
     * @return 加密后的内容
     * @throws Exception 可能产生的异常
     */
    public static String encryptStr(String dataStr, String key) throws Exception {
        return new String(Base64.encodeBase64URLSafe(CodecUtils.DES.encrypt(dataStr.getBytes("UTF-8"), DigestUtils.md5Hex(key).getBytes())));
    }

    /**
     * 内容解密(基于客户端IP和浏览器类型)
     *
     * @param request HttpServletRequest对象
     * @param dataStr 待解密的内容
     * @return 解密后的内容
     * @throws Exception 可能产生的异常
     */
    public static String decryptStr(HttpServletRequest request, String dataStr) throws Exception {
        return new String(CodecUtils.DES.decrypt(Base64.decodeBase64(dataStr.getBytes("UTF-8")),
                DigestUtils.md5Hex(request.getRemoteAddr() + request.getHeader("User-Agent")).getBytes()));
    }

    /**
     * 内容解密
     *
     * @param dataStr 待解密的内容
     * @param key     密钥
     * @return 解密后的内容
     * @throws Exception 可能产生的异常
     */
    public static String decryptStr(String dataStr, String key) throws Exception {
        return new String(CodecUtils.DES.decrypt(Base64.decodeBase64(dataStr.getBytes("UTF-8")), DigestUtils.md5Hex(key).getBytes()));
    }

    /**
     * 加载i18n资源键值
     *
     * @param owner  所属YMP容器
     * @param resKey 键
     * @return 返回resKey指定的键值
     */
    public static String i18nStr(YMP owner, String resKey) {
        String _resourceName = StringUtils.defaultIfBlank(owner.getConfig().getParam(Optional.I18N_RESOURCE_NAME), "messages");
        return I18N.load(_resourceName, resKey);
    }

    /**
     * 加载i18n资源键值
     *
     * @param owner        所属YMP容器
     * @param resKey       键
     * @param defaultValue 默认值
     * @return 返回resKey指定的键值
     */
    public static String i18nStr(YMP owner, String resKey, String defaultValue) {
        String _resourceName = StringUtils.defaultIfBlank(owner.getConfig().getParam(Optional.I18N_RESOURCE_NAME), "messages");
        return I18N.load(_resourceName, resKey, defaultValue);
    }

    public static String messageWithTemplate(YMP owner, String message) {
        return messageWithTemplate(owner, "", message);
    }

    public static String messageWithTemplate(YMP owner, String name, String message) {
        ExpressionUtils _item = ExpressionUtils.bind(StringUtils.defaultIfEmpty(owner.getConfig().getParam(Optional.VALIDATION_TEMPLATE_ITEM), "${message}<br>"));
        _item.set("name", name);
        _item.set("message", message);
        //
        ExpressionUtils _element = ExpressionUtils.bind(StringUtils.defaultIfEmpty(owner.getConfig().getParam(Optional.VALIDATION_TEMPLATE_ELEMENT), "${items}"));
        return _element.set("items", _item.getResult()).getResult();
    }

    public static String messageWithTemplate(YMP owner, Collection<ValidateResult> messages) {
        StringBuilder _messages = new StringBuilder();
        for (ValidateResult _vResult : messages) {
            ExpressionUtils _item = ExpressionUtils.bind(StringUtils.defaultIfEmpty(owner.getConfig().getParam(Optional.VALIDATION_TEMPLATE_ITEM), "${message}<br>"));
            _item.set("name", _vResult.getName());
            _item.set("message", _vResult.getMsg());
            //
            _messages.append(_item.getResult());
        }
        ExpressionUtils _element = ExpressionUtils.bind(StringUtils.defaultIfEmpty(owner.getConfig().getParam(Optional.VALIDATION_TEMPLATE_ELEMENT), "${items}"));
        return _element.set("items", _messages.toString()).getResult();
    }
}
