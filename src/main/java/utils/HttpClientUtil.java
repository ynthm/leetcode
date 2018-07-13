package utils;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.*;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpClientUtil{

        private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtil.class);

        /** 编码 */
        private static final String DEFAULT_CHARSET = "UTF-8";

        /** 默认连接超时10s */
        private static final int DEFAULT_TIMEOUT = 10000;

        /** 连接池 */
        private PoolingHttpClientConnectionManager cm = null;

        private HttpClientUtil() {

            LayeredConnectionSocketFactory sslsf = null;
            try {
                sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault());
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    .register("https", sslsf).register("http", new PlainConnectionSocketFactory()).build();
            cm = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            cm.setMaxTotal(200);
            cm.setDefaultMaxPerRoute(20);
        }

private static class SingletonHolder {
    private static final HttpClientUtil instance = new HttpClientUtil();
}

    public static HttpClientUtil instance() {
        return SingletonHolder.instance;
    }

    public CloseableHttpClient getHttpClient(Integer timeout) {

        // 配置请求参数
        RequestConfig requestConfig = RequestConfig.custom().setConnectionRequestTimeout(timeout)
                .setConnectTimeout(timeout).setSocketTimeout(timeout).build();
        // 配置超时回调机制
        HttpRequestRetryHandler retryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception, int executionCount, HttpContext context) {
                if (executionCount >= 3) {// 如果已经重试了3次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return true;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// ssl握手异常
                    return false;
                }
                HttpClientContext clientContext = HttpClientContext.adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };

        ConnectionKeepAliveStrategy connectionKeepAlive = new DefaultConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
                long keepAlive = super.getKeepAliveDuration(response, context);
                if (keepAlive == -1) {
                    // 如果keep-alive值没有由服务器明确设置，那么保持连接持续5秒。
                    keepAlive = 5000;
                }
                return keepAlive;
            }
        };

        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm)
                .setDefaultRequestConfig(requestConfig).setRetryHandler(retryHandler)
                .setKeepAliveStrategy(connectionKeepAlive).build();
        return httpClient;
    }

    public String post(String uri, Map<String, String> map) {
        String result = null;
        CloseableHttpClient httpclient = getHttpClient(DEFAULT_TIMEOUT);
        HttpPost httppost = new HttpPost(uri);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();

        for (Entry<String, String> nameValuePair : map.entrySet()) {
            formparams.add(new BasicNameValuePair(nameValuePair.getKey(), nameValuePair.getValue()));
        }

        UrlEncodedFormEntity uefEntity = null;
        CloseableHttpResponse response = null;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, "UTF-8");
            httppost.setEntity(uefEntity);

            LOGGER.info("executing request " + httppost.getURI());

            response = httpclient.execute(httppost);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "UTF-8");
                LOGGER.info("Response content: " + result);

            }

        } catch (ClientProtocolException e) {
            LOGGER.error("Client Protocol Exception", e);
        } catch (UnsupportedEncodingException e1) {
            LOGGER.error("Unsupported Encoding Exception", e1);
        } catch (IOException e) {
            LOGGER.error("IO Exception", e);
        } finally {
            HttpClientUtils.closeQuietly(response);
        }

        return result;
    }

    public String httpGet(String url, Map<String, String> params) {
        return httpGet(url, params, null, DEFAULT_CHARSET, DEFAULT_TIMEOUT);
    }

    /**
     * get请求
     *
     * @param url
     *            请求地址
     * @param params
     *            请求参数
     * @param timeOut
     *            超时时间(毫秒):从连接池获取连接的时间,请求时间,响应时间
     * @return 响应信息
     */
    public String httpGet(String url, Map<String, String> params, int timeOut) {
        return httpGet(url, params, null, DEFAULT_CHARSET, timeOut);
    }

    public String httpGet(String url, Map<String, String> params, Map<String, String> headers, int timeOut) {
        return httpGet(url, params, headers, DEFAULT_CHARSET, timeOut);
    }

    /**
     * 发送http get请求
     */
    public String httpGet(String url, Map<String, String> params, Map<String, String> headers, String encode,
                          int timeOut) {
        if (encode == null) {
            encode = DEFAULT_CHARSET;
        }
        String content = null;

        try {
            // 添加请求参数信息
            URIBuilder uriBuilder = new URIBuilder(url);
            if (null != params) {
                uriBuilder.setParameters(covertParams(params));
            }

            HttpGet httpGet = new HttpGet(uriBuilder.build());
            // 设置header
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpGet.setHeader(entry.getKey(), entry.getValue());
                }
            }

            content = getResult(httpGet, timeOut, false);

        } catch (URISyntaxException e) {
            LOGGER.error("URISyntaxException", e);
        }

        return content;
    }

    public String httpPostForm(String url, Map<String, String> params) {
        return httpPostForm(url, params, null, DEFAULT_CHARSET, DEFAULT_TIMEOUT);
    }

    public String httpPostForm(String url, Map<String, String> params, Integer timeOut) {
        return httpPostForm(url, params, null, DEFAULT_CHARSET, timeOut);
    }

    /**
     * 发送 http post 请求，参数以form表单键值对的形式提交。
     */
    public String httpPostForm(String url, Map<String, String> bodys, Map<String, String> headers, String encode,
                               int timeOut) {
        if (encode == null) {
            encode = DEFAULT_CHARSET;
        }

        HttpPost httpPost = new HttpPost(url);

        // 添加请求头信息
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpPost.addHeader(entry.getKey(), entry.getValue());
            }
        }
        // 组织请求参数
        if (bodys != null) {
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(covertParams(bodys), encode));
            } catch (UnsupportedEncodingException e) {
                LOGGER.error("UnsupportedEncodingException" + e);
            }
        }

        return getResult(httpPost, timeOut, false);
    }

    public String httpPostRaw(String url, String stringJson) {
        return httpPostRaw(url, stringJson, null, DEFAULT_CHARSET, DEFAULT_TIMEOUT);
    }

    /**
     * 发送 http post 请求，参数以原生字符串进行提交
     *
     * @param url
     * @param encode
     * @return
     */
    public String httpPostRaw(String url, String stringJson, Map<String, String> headers, String encode, int timeOut) {
        if (encode == null) {
            encode = DEFAULT_CHARSET;
        }

        HttpPost httpost = new HttpPost(url);

        // 设置header
        httpost.setHeader("Content-type", "application/json");
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpost.setHeader(entry.getKey(), entry.getValue());
            }
        }
        // 组织请求参数
        StringEntity stringEntity = new StringEntity(stringJson, encode);
        httpost.setEntity(stringEntity);

        return getResult(httpost, timeOut, false);
    }

    /**
     * 发送 http put 请求，参数以原生字符串进行提交
     *
     * @param url
     * @param encode
     * @return
     */
    public String httpPutRaw(String url, String stringJson, Map<String, String> headers, String encode, int timeOut) {

        if (encode == null) {
            encode = DEFAULT_CHARSET;
        }

        HttpPut httpput = new HttpPut(url);

        // 设置header
        httpput.setHeader("Content-type", "application/json");
        if (headers != null && headers.size() > 0) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpput.setHeader(entry.getKey(), entry.getValue());
            }
        }
        // 组织请求参数
        StringEntity stringEntity = new StringEntity(stringJson, encode);
        httpput.setEntity(stringEntity);

        return getResult(httpput, timeOut, false);
    }

    /**
     * 发送http delete请求
     */
    public String httpDelete(String url, Map<String, String> headers, Map<String, String> querys, String encode, int timeout) {

        if (encode == null) {
            encode = DEFAULT_CHARSET;
        }
        String content = null;

        try {
            // 添加请求参数信息
            URIBuilder uriBuilder = new URIBuilder(url);
            if (null != querys) {
                uriBuilder.setParameters(covertParams(querys));
            }

            HttpDelete httpDelete = new HttpDelete(uriBuilder.build());
            if (headers != null) {
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    httpDelete.setHeader(entry.getKey(), entry.getValue());
                }
            }

            content = getResult(httpDelete, timeout, false);

        } catch (URISyntaxException e) {
            LOGGER.error("URISyntaxException", e);
        }


        return content;
    }

    public void doDownload(String url, String targetPath, Map<String, String> header) {
        HttpPost post = new HttpPost(url);
        post.addHeader(HTTP.CONTENT_ENCODING, "UTF-8");
        CloseableHttpResponse response = null;
        try {
            if (header != null) {
                for (Map.Entry<String, String> entry : header.entrySet()) {
                    post.addHeader(entry.getKey(), entry.getValue());
                }
            }
            CloseableHttpClient httpclient = getHttpClient(DEFAULT_TIMEOUT);

            response = httpclient.execute(post);
            if (response.getStatusLine().getStatusCode() == 200) {
                Path path = Paths.get(targetPath);
                Files.copy(response.getEntity().getContent(), path);
            } else {
                throw new RuntimeException(
                        "System level error, Code=[" + response.getStatusLine().getStatusCode() + "].");
            }
        } catch (IOException e) {
            LOGGER.error("IO Exception---" + e);
        } finally {
            HttpClientUtils.closeQuietly(response);
        }
    }

    private String getResult(HttpRequestBase httpRequest, Integer timeout, boolean isStream) {
        return getResult(httpRequest, timeout, isStream, null);
    }

    private String getResult(HttpRequestBase httpRequest, Integer timeout, boolean isStream,
                             HttpContext clientContext) {
        // 响应结果
        StringBuilder sb = new StringBuilder();

        CloseableHttpResponse response = null;

        try {
            // 获取连接客户端
            CloseableHttpClient httpClient = getHttpClient(timeout);
            // 发起请求
            if (null != clientContext) {
                response = httpClient.execute(httpRequest, clientContext);
            } else {
                response = httpClient.execute(httpRequest);
            }

            StatusLine statusLine = response.getStatusLine();
            // 响应码
            int statusCode = statusLine.getStatusCode();
            // 响应信息
            String reasonPhrase = statusLine.getReasonPhrase();

            // 如果是重定向
            if (HttpStatus.SC_MOVED_TEMPORARILY == statusCode) {
                String locationUrl = response.getLastHeader("Location").getValue();
                return getResult(new HttpPost(locationUrl), timeout, isStream);
            }

            // 正确响应
            if (HttpStatus.SC_OK == statusCode) {
                // 获得响应实体
                HttpEntity entity = response.getEntity();

                // 如果是以流的形式获取
                if (isStream) {
                    try( BufferedReader br = new BufferedReader(new InputStreamReader(entity.getContent(), DEFAULT_CHARSET)))
                    {
                        String len = "";
                        while ((len = br.readLine()) != null) {
                            sb.append(len);
                        }
                    }


                } else {
                    ContentType contentType = ContentType.get(entity);
                    LOGGER.info("MineType：" + contentType.getMimeType());
                    sb.append(EntityUtils.toString(entity, DEFAULT_CHARSET));
                }
            } else {
                sb.append("code[").append(statusCode).append("],desc[").append(reasonPhrase).append("]");
            }
        } catch (ConnectionPoolTimeoutException e) {
            LOGGER.error("从连接池获取连接超时!!!", e);
        } catch (SocketTimeoutException e) {
            LOGGER.error("响应超时", e);
        } catch (ConnectTimeoutException e) {
            LOGGER.error("请求超时", e);
        } catch (ClientProtocolException e) {
            LOGGER.error("http协议错误", e);
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("不支持的字符编码", e);
        } catch (UnsupportedOperationException e) {
            LOGGER.error("不支持的请求操作", e);
        } catch (ParseException e) {
            LOGGER.error("解析错误", e);
        } catch (IOException e) {
            LOGGER.error("IO错误", e);

        } finally {
            HttpClientUtils.closeQuietly(response);
        }

        return sb.toString().replace("\r\n", "").replace("\n", "");
    }

    /**
     * Map转换成NameValuePair List集合
     *
     * @param bodys
     *            map
     * @return NameValuePair List集合
     */
    private List<NameValuePair> covertParams(Map<String, String> bodys) {

        List<NameValuePair> paramList = new LinkedList<>();
        for (Map.Entry<String, String> entry : bodys.entrySet()) {
            paramList.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        return paramList;
    }
}
