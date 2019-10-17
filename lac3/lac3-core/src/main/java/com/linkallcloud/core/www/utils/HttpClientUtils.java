package com.linkallcloud.core.www.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.lang.Strings;

public class HttpClientUtils {
    private static Log log = Logs.get();

    /**
     * 
     * @param url
     * @return
     */
    public static String get(String url) {
        return HttpClientUtils.get(url, null, null, null, 0);
    }

    /**
     * 
     * @param url
     * @param httpHeaders
     * @return
     */
    public static String get(String url, Map<String, String> httpHeaders) {
        return HttpClientUtils.get(url, null, httpHeaders, null, 0);
    }

    /**
     * 
     * @param url
     * @param timeout
     * @param httpHeaders
     * @return
     */
    public static String get(String url, Integer timeout, Map<String, String> httpHeaders) {
        return HttpClientUtils.get(url, timeout, httpHeaders, null, 0);
    }

    /**
     * 
     * @param url
     * @param timeout
     * @param httpHeaders
     * @param proxyHost
     * @param proxyPort
     * @return
     */
    public static String get(String url, Integer timeout, Map<String, String> httpHeaders, String proxyHost,
            int proxyPort) {
        // 获取可关闭的 httpCilent
        CloseableHttpClient httpClient = null;
        HttpClientConnectionManager clientConnectionManager = init();
        if (clientConnectionManager != null) {
            HttpClientBuilder scb = HttpClients.custom().setConnectionManager(clientConnectionManager);
            if (!Strings.isBlank(proxyHost)) {
                scb.setProxy(new HttpHost(proxyHost, proxyPort));
            }
            httpClient = scb.build();
        } else {
            httpClient = HttpClients.createDefault();
        }

        HttpGet httpGet = new HttpGet(url);
        if (timeout != null) {
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout) // 设置连接超时时间
                    .setConnectionRequestTimeout(timeout) // 设置请求超时时间
                    .setSocketTimeout(timeout).setRedirectsEnabled(true)// 默认允许自动重定向
                    .build();
            httpGet.setConfig(requestConfig);
        }

        if (httpHeaders != null && !httpHeaders.isEmpty()) {
            for (String key : httpHeaders.keySet()) {
                httpGet.addHeader(key, httpHeaders.get(key));
            }
        }

        try {
            CloseableHttpResponse chr = httpClient.execute(httpGet);
            try {
                if (chr.getStatusLine().getStatusCode() == 200) {
                    return EntityUtils.toString(chr.getEntity(), "utf-8");// 获得返回的结果
                } else if (chr.getStatusLine().getStatusCode() == 400) {
                    throw new BaseRuntimeException("400", "错误请求");
                } else if (chr.getStatusLine().getStatusCode() == 500) {
                    throw new BaseRuntimeException("500", "服务器遇到错误，无法完成请求。");
                } else {
                    throw new BaseRuntimeException(chr.getStatusLine().getStatusCode() + "", "HTTP请求错误");
                }
            } finally {
                if (chr != null) {
                    chr.close();
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new BaseRuntimeException("499", "HTTP请求错误");
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close(); // 释放资源
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }

    /**
     * 
     * @param url
     * @param message
     * @return
     */
    public static String postJson(String url, String message) {
        return HttpClientUtils.postJson(url, message, null, null, 0);
    }

    /**
     * 
     * @param url
     * @param message
     * @param timeout
     * @return
     */
    public static String postJson(String url, String message, Integer timeout) {
        return HttpClientUtils.postJson(url, message, timeout, null, 0);
    }

    /**
     * 
     * @param url
     * @param message
     * @param timeout
     * @param proxyHost
     * @param proxyPort
     * @return
     */
    public static String postJson(String url, String message, Integer timeout, String proxyHost, int proxyPort) {
        // 获取可关闭的 httpCilent
        CloseableHttpClient httpClient = null;
        HttpClientConnectionManager clientConnectionManager = init();
        if (clientConnectionManager != null) {
            HttpClientBuilder scb = HttpClients.custom().setConnectionManager(clientConnectionManager);
            if (!Strings.isBlank(proxyHost)) {
                scb.setProxy(new HttpHost(proxyHost, proxyPort));
            }
            httpClient = scb.build();
        } else {
            httpClient = HttpClients.createDefault();
        }

        HttpPost httpPost = new HttpPost(url);

        if (timeout != null) {
            // 配置超时时间
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout)
                    .setConnectionRequestTimeout(timeout).setSocketTimeout(timeout).setRedirectsEnabled(true).build();
            // 设置超时时间
            httpPost.setConfig(requestConfig);
        }

        httpPost.addHeader("Content-Type", "application/json; charset=utf-8");
        try {
            InputStream instream = InputStreamUtils.StringTOInputStream(message, "utf-8");
            httpPost.setEntity(new InputStreamEntity(instream));
            CloseableHttpResponse chr = httpClient.execute(httpPost);
            try {
                if (chr != null) {
                    if (chr.getStatusLine().getStatusCode() == 200) {
                        return EntityUtils.toString(chr.getEntity(), "utf-8");
                    } else if (chr.getStatusLine().getStatusCode() == 400) {
                        throw new BaseRuntimeException("400", "错误请求");
                    } else if (chr.getStatusLine().getStatusCode() == 500) {
                        throw new BaseRuntimeException("500", "服务器遇到错误，无法完成请求。");
                    } else {
                        throw new BaseRuntimeException(chr.getStatusLine().getStatusCode() + "", "HTTP请求错误");
                    }
                }
            } finally {
                if (chr != null) {
                    chr.close();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BaseRuntimeException("499", "HTTP请求错误");
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close(); // 释放资源
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    public static String post(String url, List<BasicNameValuePair> parametersList) {
        return HttpClientUtils.post(url, parametersList, null, null, 0);
    }

    public static String post(String url, List<BasicNameValuePair> parametersList, Integer timeout) {
        return HttpClientUtils.post(url, parametersList, timeout, null, 0);
    }

    /**
     * 
     * @param url
     * @param parametersList
     * @return
     */
    public static String post(String url, List<BasicNameValuePair> parametersList, Integer timeout, String proxyHost,
            int proxyPort) {
        // 获取可关闭的 httpCilent
        CloseableHttpClient httpClient = null;
        HttpClientConnectionManager clientConnectionManager = init();
        if (clientConnectionManager != null) {
            HttpClientBuilder scb = HttpClients.custom().setConnectionManager(clientConnectionManager);
            if (!Strings.isBlank(proxyHost)) {
                scb.setProxy(new HttpHost(proxyHost, proxyPort));
            }
            httpClient = scb.build();
        } else {
            httpClient = HttpClients.createDefault();
        }

        HttpPost httpPost = new HttpPost(url);

        if (timeout != null) {
            // 配置超时时间
            RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout)
                    .setConnectionRequestTimeout(timeout).setSocketTimeout(timeout).setRedirectsEnabled(true).build();
            // 设置超时时间
            httpPost.setConfig(requestConfig);
        }

        try {
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parametersList, "UTF-8");
            // 设置post求情参数
            httpPost.setEntity(entity);
            CloseableHttpResponse chr = httpClient.execute(httpPost);
            try {
                if (chr.getStatusLine().getStatusCode() == 200) {
                    return EntityUtils.toString(chr.getEntity(), "utf-8");
                } else if (chr.getStatusLine().getStatusCode() == 400) {
                    throw new BaseRuntimeException("400", "错误请求");
                } else if (chr.getStatusLine().getStatusCode() == 500) {
                    throw new BaseRuntimeException("500", "服务器遇到错误，无法完成请求。");
                } else {
                    throw new BaseRuntimeException(chr.getStatusLine().getStatusCode() + "", "HTTP请求错误");
                }
            } finally {
                if (chr != null) {
                    chr.close();
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close(); // 释放资源
                }
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
        return null;
    }

    /**
     * 
     * @param url
     * @param fileName
     * @return
     */
    public static String upload(String url, String fileName) {
        if (!Strings.isBlank(fileName)) {
            return HttpClientUtils.upload(url, new File(fileName), null);
        }
        return null;
    }

    public static String upload(String url, File file) {
        if (file != null) {
            return HttpClientUtils.upload(url, file, null);
        }
        return null;
    }

    /**
     * 
     * @param url
     * @param file
     * @param stringFileds
     * @return
     */
    public static String upload(String url, File file, Map<String, String> stringFileds) {
        CloseableHttpClient httpClient = null;
        HttpClientConnectionManager clientConnectionManager = init();
        if (clientConnectionManager != null) {
            HttpClientBuilder scb = HttpClients.custom().setConnectionManager(clientConnectionManager);
            httpClient = scb.build();
        } else {
            httpClient = HttpClients.createDefault();
        }

        HttpPost httppost = new HttpPost(url);

        try {
            RequestConfig requestConfig =
                    RequestConfig.custom().setConnectTimeout(200000).setSocketTimeout(200000).build();
            httppost.setConfig(requestConfig);

            FileBody bin = new FileBody(file);
            MultipartEntityBuilder eb = MultipartEntityBuilder.create().addPart("file", bin);

            if (stringFileds != null && !stringFileds.isEmpty()) {
                Iterator<String> itr = stringFileds.keySet().iterator();
                while (itr.hasNext()) {
                    String fieldName = itr.next();
                    String fieldValue = stringFileds.get(fieldName);
                    StringBody field = new StringBody(fieldValue, ContentType.TEXT_PLAIN);
                    eb.addPart(fieldName, field);
                }
            }

            HttpEntity reqEntity = eb.build();
            httppost.setEntity(reqEntity);
            CloseableHttpResponse response = httpClient.execute(httppost);
            try {
                HttpEntity resEntity = response.getEntity();
                if (resEntity != null) {
                    String responseEntityStr = EntityUtils.toString(response.getEntity());
                    log.debug(responseEntityStr);
                    log.debug("Response content length: " + resEntity.getContentLength());
                    return responseEntityStr;
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            log.errorf(e.getMessage(), e);
        } catch (IOException e) {
            log.errorf(e.getMessage(), e);
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                log.errorf(e.getMessage(), e);
            }
        }
        return null;
    }

    public static HttpClientConnectionManager init() {
        try {
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
                    return true;
                }
            }).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1" },
                    null, SSLConnectionSocketFactory.getDefaultHostnameVerifier());
            Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory> create()
                    .register("http", PlainConnectionSocketFactory.INSTANCE).register("https", sslsf).build();
            return new PoolingHttpClientConnectionManager(registry);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

}
