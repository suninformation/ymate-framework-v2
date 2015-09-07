/*
 * Copyright (c) 2007-2107, the original author or authors. All rights reserved.
 *
 * This program licensed under the terms of the GNU Lesser General Public License version 3.0
 * as published by the Free Software Foundation.
 */
package net.ymate.framework.commons;

import net.ymate.platform.core.util.UUIDUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 基于HttpClient工具封装
 *
 * @author 刘镇 (suninformation@163.com) on 14/3/15 下午5:15:32
 * @version 1.0
 */
public class HttpClientHelper {

    private static final Log _LOG = LogFactory.getLog(HttpClientHelper.class);

    // 编码方式
    public static final String DEFAULT_CHARSET = "UTF-8";

    // 连接超时时间
    private int __connectionTimeout = -1;

    private int __requestTimeout = -1;

    private int __socketTimeout = -1;

    private SSLConnectionSocketFactory __socketFactory;

    public static HttpClientHelper create() {
        return new HttpClientHelper();
    }

    private HttpClientHelper() {
    }

    public HttpClientHelper customSSL(SSLConnectionSocketFactory socketFactory) {
        __socketFactory = socketFactory;
        return this;
    }

    public HttpClientHelper connectionTimeout(int connectionTimeout) {
        __connectionTimeout = connectionTimeout;
        return this;
    }

    public HttpClientHelper requestTimeout(int requestTimeout) {
        __requestTimeout = requestTimeout;
        return this;
    }

    public HttpClientHelper socketTimeout(int socketTimeout) {
        __socketTimeout = socketTimeout;
        return this;
    }

    private CloseableHttpClient __doBuildHttpClient() throws KeyManagementException, NoSuchAlgorithmException {
        HttpClientBuilder _builder = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(__connectionTimeout)
                        .setSocketTimeout(__socketTimeout)
                        .setConnectionRequestTimeout(__requestTimeout).build());
        if (__socketFactory == null) {
            __socketFactory = new SSLConnectionSocketFactory(
                    SSLContexts.custom().useSSL().build(),
                    SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
        }
        return _builder.setSSLSocketFactory(__socketFactory).build();
    }

    public IHttpResponse get(String url) throws Exception {
        CloseableHttpClient _httpClient = __doBuildHttpClient();
        try {
            _LOG.debug("Request URL ['" + url + "']");
            IHttpResponse _result = _httpClient.execute(RequestBuilder.get().setUri(url).build(), new ResponseHandler<IHttpResponse>() {

                public IHttpResponse handleResponse(HttpResponse response) throws IOException {
                    return new IHttpResponse.NEW(response);
                }

            });
            _LOG.debug("Request URL ['" + url + "'] Response ['" + _result + "']");
            return _result;
        } finally {
            _httpClient.close();
        }
    }

    public IHttpResponse get(String url, Map<String, String> params) throws Exception {
        RequestBuilder _request = RequestBuilder.get().setUri(url);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            _request.addParameter(entry.getKey(), entry.getValue());
        }
        CloseableHttpClient _httpClient = __doBuildHttpClient();
        try {
            _LOG.debug("Request URL ['" + url + "'], Param ['" + params + "']");
            IHttpResponse _result = _httpClient.execute(_request.build(), new ResponseHandler<IHttpResponse>() {

                public IHttpResponse handleResponse(HttpResponse response) throws IOException {
                    return new IHttpResponse.NEW(response);
                }

            });
            _LOG.debug("Request URL ['" + url + "'] Response ['" + _result + "']");
            return _result;
        } finally {
            _httpClient.close();
        }
    }

    public IHttpResponse post(String url, ContentType contentType, String content) throws Exception {
        CloseableHttpClient _httpClient = __doBuildHttpClient();
        try {
            _LOG.debug("Request URL ['" + url + "'] PostBody ['" + content + "']");
            IHttpResponse _result = _httpClient.execute(RequestBuilder.post()
                    .setUri(url)
                    .setEntity(EntityBuilder.create()
                            .setContentEncoding(DEFAULT_CHARSET)
                            .setContentType(contentType)
                            .setText(content).build()).build(), new ResponseHandler<IHttpResponse>() {

                public IHttpResponse handleResponse(HttpResponse response) throws IOException {
                    return new IHttpResponse.NEW(response);
                }

            });
            _LOG.debug("Request URL ['" + url + "'] Response ['" + _result + "']");
            return _result;
        } finally {
            _httpClient.close();
        }
    }

    public IHttpResponse post(String url, String content) throws Exception {
        return post(url, ContentType.create("text/plain", DEFAULT_CHARSET), content);
    }

    public IHttpResponse post(String url, ContentType contentType, byte[] content) throws Exception {
        CloseableHttpClient _httpClient = __doBuildHttpClient();
        try {
            _LOG.debug("Request URL ['" + url + "'] PostBody ['" + Arrays.toString(content) + "']");
            IHttpResponse _result = _httpClient.execute(RequestBuilder.post()
                    .setUri(url)
                    .setEntity(EntityBuilder.create()
                            .setContentEncoding(DEFAULT_CHARSET)
                            .setContentType(contentType)
                            .setBinary(content).build())
                    .build(), new ResponseHandler<IHttpResponse>() {

                public IHttpResponse handleResponse(HttpResponse response) throws IOException {
                    return new IHttpResponse.NEW(response);
                }

            });
            _LOG.debug("Request URL ['" + url + "'] Response ['" + _result + "']");
            return _result;
        } finally {
            _httpClient.close();
        }
    }

    public IHttpResponse post(String url, byte[] content) throws Exception {
        return post(url, ContentType.create("application/octet-stream", DEFAULT_CHARSET), content);
    }

    public IHttpResponse post(String url, Map<String, String> params) throws Exception {
        CloseableHttpClient _httpClient = __doBuildHttpClient();
        try {
            _LOG.debug("Request URL ['" + url + "'] PostBody ['" + params + "']");
            IHttpResponse _result = _httpClient.execute(RequestBuilder.post()
                    .setUri(url)
                    .setEntity(EntityBuilder.create()
                            .setContentEncoding(DEFAULT_CHARSET)
                            .setParameters(__doBuildNameValuePairs(params)).build()).build(), new ResponseHandler<IHttpResponse>() {

                public IHttpResponse handleResponse(HttpResponse response) throws IOException {
                    return new IHttpResponse.NEW(response);
                }

            });
            _LOG.debug("Request URL ['" + url + "'] Response ['" + _result + "']");
            return _result;
        } finally {
            _httpClient.close();
        }
    }

    private static List<NameValuePair> __doBuildNameValuePairs(Map<String, String> params) {
        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (StringUtils.isNotBlank(entry.getValue())) {
                nameValuePair.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
        }
        return nameValuePair;
    }

    public String upload(String url, File uploadFile) throws Exception {
        CloseableHttpClient _httpClient = __doBuildHttpClient();
        try {
            _LOG.debug("Upload File ['" + uploadFile + "']");
            String _result = EntityUtils.toString(_httpClient.execute(RequestBuilder.post().setUri(url)
                    .setEntity(MultipartEntityBuilder.create().addPart("media", new FileBody(uploadFile)).build()).build()).getEntity(), DEFAULT_CHARSET);
            _LOG.debug("Upload File ['" + uploadFile + "'] Response ['" + _result + "']");
            return _result;
        } finally {
            _httpClient.close();
        }
    }

    public IFileWrapper download(String url, String content) throws Exception {
        CloseableHttpClient _httpClient = __doBuildHttpClient();
        try {
            return _httpClient.execute(RequestBuilder.post()
                    .setUri(url)
                    .setEntity(EntityBuilder.create()
                            .setContentEncoding(DEFAULT_CHARSET)
                            .setContentType(ContentType.create("application/x-www-form-urlencoded", DEFAULT_CHARSET))
                            .setText(content).build()).build(), new ResponseHandler<IFileWrapper>() {

                public IFileWrapper handleResponse(HttpResponse response) throws IOException {
                    //
                    File _tmpFile = File.createTempFile(UUIDUtils.UUID(), null);
                    FileUtils.copyInputStreamToFile(response.getEntity().getContent(), _tmpFile);
                    //
                    return new IFileWrapper.NEW(response.getEntity().getContentType().getValue(), response.getEntity().getContentLength(), _tmpFile);
                }
            });
        } finally {
            _httpClient.close();
        }
    }

    public IFileWrapper download(String url) throws Exception {
        CloseableHttpClient _httpClient = __doBuildHttpClient();
        try {
            return _httpClient.execute(RequestBuilder.get().setUri(url).build(), new ResponseHandler<IFileWrapper>() {

                public IFileWrapper handleResponse(HttpResponse response) throws IOException {
                    String _cType = response.getEntity().getContentType().getValue().toLowerCase();
                    if (_cType.equals("text/plain") || _cType.equals("text/html")) {
                        final String _errMsg = EntityUtils.toString(response.getEntity(), DEFAULT_CHARSET);
                        //
                        return new IFileWrapper.NEW(_errMsg);
                    } else {
                        String _fileName = StringUtils.substringAfter(response.getFirstHeader("Content-disposition").getValue(), "filename=");
                        //
                        File _tmpFile = File.createTempFile(_fileName, null);
                        FileUtils.copyInputStreamToFile(response.getEntity().getContent(), _tmpFile);
                        //
                        return new IFileWrapper.NEW(_fileName, response.getEntity().getContentType().getValue(),
                                response.getEntity().getContentLength(), _tmpFile);
                    }
                }
            });
        } finally {
            _httpClient.close();
        }
    }
}
