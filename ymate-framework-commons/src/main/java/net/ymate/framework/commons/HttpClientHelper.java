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

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.NullArgumentException;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;

import javax.net.ssl.SSLContext;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.security.*;
import java.security.cert.CertificateException;
import java.util.ArrayList;
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

    /**
     * 编码方式
     */
    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * 连接超时时间
     */
    private int __connectionTimeout = -1;

    private int __requestTimeout = -1;

    private int __socketTimeout = -1;

    private SSLConnectionSocketFactory __socketFactory;

    private IHttpClientConfigurable __httpClientConfigurable;

    public static HttpClientHelper create() {
        return new HttpClientHelper();
    }

    public static HttpClientHelper create(IHttpClientConfigurable configurable) {
        return new HttpClientHelper(configurable);
    }

    public static SSLConnectionSocketFactory createConnectionSocketFactory(URL certFilePath, char[] passwordChars)
            throws CertificateException, UnrecoverableKeyException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException, IOException {
        return createConnectionSocketFactory("PKCS12", certFilePath, passwordChars);
    }

    public static SSLConnectionSocketFactory createConnectionSocketFactory(String certType, URL certFilePath, char[] passwordChars)
            throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException, UnrecoverableKeyException, KeyManagementException {
        if (StringUtils.isBlank(certType)) {
            throw new NullArgumentException("certType");
        }
        if (certFilePath == null) {
            throw new NullArgumentException("certFilePath");
        }
        if (ArrayUtils.isEmpty(passwordChars)) {
            throw new NullArgumentException("passwordChars");
        }
        KeyStore _keyStore = KeyStore.getInstance(certType);
        InputStream _certFileStream = null;
        try {
            _certFileStream = certFilePath.openStream();
            _keyStore.load(_certFileStream, passwordChars);
        } finally {
            IOUtils.closeQuietly(_certFileStream);
        }
        SSLContext _sslContext = SSLContexts.custom().loadKeyMaterial(_keyStore, passwordChars).build();
        return new SSLConnectionSocketFactory(_sslContext, new String[]{"TLSv1"}, null, new DefaultHostnameVerifier());
    }

    private HttpClientHelper() {
    }

    private HttpClientHelper(IHttpClientConfigurable configurable) {
        __httpClientConfigurable = configurable;
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

    private CloseableHttpClient __doBuildHttpClient() {
        CloseableHttpClient _httpClient = __httpClientConfigurable != null ? __httpClientConfigurable.createHttpClient(__socketFactory, __connectionTimeout, __requestTimeout, __socketTimeout) : null;
        if (_httpClient == null) {
            HttpClientBuilder _builder = HttpClientBuilder.create()
                    .setDefaultRequestConfig(RequestConfig.custom()
                            .setConnectTimeout(__connectionTimeout)
                            .setSocketTimeout(__socketTimeout)
                            .setConnectionRequestTimeout(__requestTimeout).build());
            if (__socketFactory == null) {
                __socketFactory = new SSLConnectionSocketFactory(SSLContexts.createSystemDefault(), NoopHostnameVerifier.INSTANCE);
            }
            _httpClient = _builder.setSSLSocketFactory(__socketFactory).build();
        }
        return _httpClient;
    }

    private RequestBuilder __processRequestHeaders(String url, Header[] headers) {
        return __processRequestHeaders(RequestBuilder.get().setUri(url), headers, null);
    }

    private RequestBuilder __processRequestHeaders(String url, Header[] headers, Map<String, String> params) {
        return __processRequestHeaders(RequestBuilder.get().setUri(url), headers, params);
    }

    private RequestBuilder __processRequestHeaders(RequestBuilder requestBuilder, Header[] headers, Map<String, String> params) {
        if (headers != null && headers.length > 0) {
            for (Header _header : headers) {
                requestBuilder.addHeader(_header);
            }
        }
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                requestBuilder.addParameter(entry.getKey(), entry.getValue());
            }
        }
        return requestBuilder;
    }

    private IHttpResponse __execute(RequestBuilder requestBuilder, final String defaultResponseCharset) throws Exception {
        CloseableHttpClient _httpClient = __doBuildHttpClient();
        try {
            return _httpClient.execute(requestBuilder.build(), new ResponseHandler<IHttpResponse>() {
                @Override
                public IHttpResponse handleResponse(HttpResponse response) throws IOException {
                    if (StringUtils.isNotBlank(defaultResponseCharset)) {
                        return new IHttpResponse.NEW(response, defaultResponseCharset);
                    }
                    return new IHttpResponse.NEW(response);
                }
            });
        } finally {
            if (__httpClientConfigurable != null) {
                __httpClientConfigurable.closeHttpClient(_httpClient);
            } else {
                _httpClient.close();
            }
        }
    }

    public <T> T execute(IHttpRequestExecutor<T> requestExecutor) throws Exception {
        CloseableHttpClient _httpClient = __doBuildHttpClient();
        try {
            return requestExecutor.execute(_httpClient);
        } finally {
            if (__httpClientConfigurable != null) {
                __httpClientConfigurable.closeHttpClient(_httpClient);
            } else {
                _httpClient.close();
            }
        }
    }

    public IHttpResponse get(String url) throws Exception {
        return get(url, new Header[0], null);
    }

    public IHttpResponse get(String url, Header[] headers) throws Exception {
        return get(url, headers, null);
    }

    public IHttpResponse get(String url, Header[] headers, String defaultResponseCharset) throws Exception {
        RequestBuilder _reqBuilder = __processRequestHeaders(url, headers);
        return __execute(_reqBuilder, defaultResponseCharset);
    }

    public IHttpResponse get(String url, Map<String, String> params) throws Exception {
        return get(url, params, null);
    }

    public IHttpResponse get(String url, Map<String, String> params, Header[] headers) throws Exception {
        return get(url, params, headers, null);
    }

    public IHttpResponse get(String url, Map<String, String> params, Header[] headers, String defaultResponseCharset) throws Exception {
        return get(url, params, Charset.forName(DEFAULT_CHARSET), headers, defaultResponseCharset);
    }

    public IHttpResponse get(String url, Map<String, String> params, Charset charset, Header[] headers, String defaultResponseCharset) throws Exception {
        RequestBuilder _request = __processRequestHeaders(url, headers, params);
        if (charset != null) {
            _request.setCharset(charset);
        }
        return __execute(_request, defaultResponseCharset);
    }

    public IHttpResponse post(String url, ContentType contentType, String content, Header[] headers) throws Exception {
        return post(url, contentType, content, headers, null);
    }

    public IHttpResponse post(String url, ContentType contentType, String content, Header[] headers, String defaultResponseCharset) throws Exception {
        RequestBuilder _reqBuilder = RequestBuilder.post()
                .setUri(url)
                .setEntity(EntityBuilder.create()
                        .setContentEncoding(contentType == null || contentType.getCharset() == null ? DEFAULT_CHARSET : contentType.getCharset().name())
                        .setContentType(contentType)
                        .setText(content).build());
        _reqBuilder = __processRequestHeaders(_reqBuilder, headers, null);
        return __execute(_reqBuilder, defaultResponseCharset);
    }

    public IHttpResponse post(String url, ContentType contentType, String content) throws Exception {
        return post(url, contentType, content, null, null);
    }

    public IHttpResponse post(String url, String content) throws Exception {
        return post(url, ContentType.create("text/plain", DEFAULT_CHARSET), content, null, null);
    }

    public IHttpResponse post(String url, ContentType contentType, byte[] content, Header[] headers) throws Exception {
        RequestBuilder _reqBuilder = RequestBuilder.post()
                .setUri(url)
                .setEntity(EntityBuilder.create()
                        .setContentEncoding(contentType == null || contentType.getCharset() == null ? DEFAULT_CHARSET : contentType.getCharset().name())
                        .setContentType(contentType)
                        .setBinary(content).build());
        _reqBuilder = __processRequestHeaders(_reqBuilder, headers, null);
        return __execute(_reqBuilder, null);
    }

    public IHttpResponse post(String url, ContentType contentType, InputStream content, Header[] headers) throws Exception {
        return post(url, contentType, content, headers, null);
    }

    public IHttpResponse post(String url, ContentType contentType, InputStream content, Header[] headers, String defaultResponseCharset) throws Exception {
        RequestBuilder _reqBuilder = RequestBuilder.post()
                .setUri(url)
                .setEntity(EntityBuilder.create()
                        .setContentEncoding(contentType == null || contentType.getCharset() == null ? DEFAULT_CHARSET : contentType.getCharset().name())
                        .setContentType(contentType)
                        .setStream(content).build());
        _reqBuilder = __processRequestHeaders(_reqBuilder, headers, null);
        return __execute(_reqBuilder, defaultResponseCharset);
    }

    public IHttpResponse post(String url, ContentType contentType, byte[] content) throws Exception {
        return post(url, contentType, content, null);
    }

    public IHttpResponse post(String url, byte[] content) throws Exception {
        return post(url, ContentType.create("application/octet-stream", DEFAULT_CHARSET), content, null);
    }

    public IHttpResponse post(String url, Map<String, String> params, Header[] headers) throws Exception {
        return post(url, null, params, headers, null);
    }

    public IHttpResponse post(String url, ContentType contentType, Map<String, String> params, Header[] headers) throws Exception {
        return post(url, contentType, params, headers, null);
    }

    public IHttpResponse post(String url, ContentType contentType, Map<String, String> params, Header[] headers, String defaultResponseCharset) throws Exception {
        RequestBuilder _reqBuilder = RequestBuilder.post()
                .setUri(url)
                .setEntity(EntityBuilder.create()
                        .setContentType(contentType)
                        .setContentEncoding(contentType == null || contentType.getCharset() == null ? DEFAULT_CHARSET : contentType.getCharset().name())
                        .setParameters(__doBuildNameValuePairs(params)).build());
        _reqBuilder = __processRequestHeaders(_reqBuilder, headers, null);
        return __execute(_reqBuilder, defaultResponseCharset);
    }

    public IHttpResponse post(String url, Map<String, String> params) throws Exception {
        return post(url, ContentType.create("application/x-www-form-urlencoded", DEFAULT_CHARSET), params, null, null);
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

    public IHttpResponse upload(String url, String fieldName, ContentBody uploadFile, Header[] headers) throws Exception {
        return upload(url, fieldName, uploadFile, headers, null);
    }

    public IHttpResponse upload(String url, String fieldName, ContentBody uploadFile, Header[] headers, final String defaultResponseCharset) throws Exception {
        RequestBuilder _reqBuilder = RequestBuilder.post().setUri(url).setEntity(
                MultipartEntityBuilder.create().addPart(fieldName, uploadFile).build());
        _reqBuilder = __processRequestHeaders(_reqBuilder, headers, null);
        return __execute(_reqBuilder, defaultResponseCharset);
    }

    public IHttpResponse upload(String url, String fieldName, File uploadFile, Header[] headers) throws Exception {
        return upload(url, fieldName, new FileBody(uploadFile), headers, null);
    }

    public IHttpResponse upload(String url, File uploadFile, Header[] headers) throws Exception {
        return upload(url, "media", uploadFile, headers);
    }

    public IHttpResponse upload(String url, String fieldName, File uploadFile) throws Exception {
        return upload(url, fieldName, uploadFile, null);
    }

    public IHttpResponse upload(String url, File uploadFile) throws Exception {
        return upload(url, uploadFile, null);
    }

    private <T> T __doExecHttpDownload(RequestBuilder requestBuilder, final IFileHandler<T> handler) throws Exception {
        CloseableHttpClient _httpClient = __doBuildHttpClient();
        try {
            return _httpClient.execute(requestBuilder.build(), new ResponseHandler<T>() {
                @Override
                public T handleResponse(HttpResponse response) throws IOException {
                    String _fileName = null;
                    if (response.getStatusLine().getStatusCode() == 200) {
                        if (response.containsHeader("Content-disposition")) {
                            _fileName = StringUtils.substringAfter(response.getFirstHeader("Content-disposition").getValue(), "filename=");
                        }
                    }
                    return handler.handle(response, new IFileWrapper.NEW(_fileName, response.getEntity().getContentType().getValue(), response.getEntity().getContentLength(), new BufferedInputStream(response.getEntity().getContent())));
                }
            });
        } finally {
            if (__httpClientConfigurable != null) {
                __httpClientConfigurable.closeHttpClient(_httpClient);
            } else {
                _httpClient.close();
            }
        }
    }

    public <T> T download(String url, ContentType contentType, String content, Header[] headers, final IFileHandler<T> handler) throws Exception {
        RequestBuilder _reqBuilder = RequestBuilder.post()
                .setUri(url)
                .setEntity(EntityBuilder.create()
                        .setContentEncoding(contentType == null || contentType.getCharset() == null ? DEFAULT_CHARSET : contentType.getCharset().name())
                        .setContentType(contentType)
                        .setText(content).build());
        return __doExecHttpDownload(__processRequestHeaders(_reqBuilder, headers, null), handler);
    }

    public <T> T download(String url, String content, IFileHandler<T> handler) throws Exception {
        return download(url, ContentType.create("application/x-www-form-urlencoded", DEFAULT_CHARSET), content, null, handler);
    }

    public <T> T download(String url, Header[] headers, final IFileHandler<T> handler) throws Exception {
        RequestBuilder _reqBuilder = __processRequestHeaders(url, headers);
        return __doExecHttpDownload(_reqBuilder, handler);
    }

    public <T> T download(String url, IFileHandler<T> handler) throws Exception {
        return download(url, new Header[0], handler);
    }
}
