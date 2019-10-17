package com.linkallcloud.core.www.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.Map;

import javax.net.ssl.SSLContext;

import com.linkallcloud.core.log.Log;
import com.linkallcloud.core.log.Logs;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.linkallcloud.core.exception.BaseRuntimeException;
import com.linkallcloud.core.lang.Strings;

public class HttpClientFactory {
	private static Log log = Logs.get();

	private static HttpClientFactory singleton;
	private static HttpClientFactory singletonSslHostnameVerify;

	private PoolingHttpClientConnectionManager cm;
	private boolean sslHostnameVerify;

	public void init() {
		try {
			SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, new TrustStrategy() {
				@Override
				public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
					return true;
				}
			}).build();
			SSLConnectionSocketFactory sslsf = null;
			if (sslHostnameVerify) {
				sslsf = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1" }, null,
						SSLConnectionSocketFactory.getDefaultHostnameVerifier());
			} else {
				sslsf = new SSLConnectionSocketFactory(sslContext, new String[] { "TLSv1" }, null,
						new NoopHostnameVerifier());
			}
			Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
					.register("http", PlainConnectionSocketFactory.INSTANCE).register("https", sslsf).build();
			cm = new PoolingHttpClientConnectionManager(registry);
			cm.setMaxTotal(200);
			cm.setDefaultMaxPerRoute(20);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

	private HttpClientFactory(boolean sslHostnameVerify) {
		this.sslHostnameVerify = sslHostnameVerify;
		init();
	}

	public boolean isSslHostnameVerify() {
		return sslHostnameVerify;
	}

	public PoolingHttpClientConnectionManager getConnectionManager() {
		return cm;
	}

	public static HttpClientFactory me(boolean sslHostnameVerify) {
		if (sslHostnameVerify) {
			if (singletonSslHostnameVerify == null) {
				synchronized (HttpClientFactory.class) {
					if (singletonSslHostnameVerify == null) {
						singletonSslHostnameVerify = new HttpClientFactory(true);
					}
				}
			}
			return singletonSslHostnameVerify;
		} else {
			if (singleton == null) {
				synchronized (HttpClientFactory.class) {
					if (singleton == null) {
						singleton = new HttpClientFactory(false);
					}
				}
			}
			return singleton;
		}
	}

	public CloseableHttpClient getHttpClient() {
		// 获取可关闭的 httpCilent
		CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(getConnectionManager()).build();
		/*
		 * CloseableHttpClient httpClient =
		 * HttpClients.createDefault();//如果不采用连接池就是这种方式获取连接
		 */
		return httpClient;
	}

	public CloseableHttpClient getHttpClient(String proxyHost, int proxyPort) {
		// 获取可关闭的 httpCilent
		CloseableHttpClient httpClient = null;
		PoolingHttpClientConnectionManager phccm = getConnectionManager();
		if (phccm != null) {
			HttpClientBuilder scb = HttpClients.custom().setConnectionManager(phccm);
			if (!Strings.isBlank(proxyHost)) {
				scb.setProxy(new HttpHost(proxyHost, proxyPort));
			}
			httpClient = scb.build();
		} else {
			HttpClientBuilder scb = HttpClientBuilder.create();
			if (!Strings.isBlank(proxyHost)) {
				scb.setProxy(new HttpHost(proxyHost, proxyPort));
			}
			httpClient = scb.build();
		}
		// CloseableHttpClient httpClient =
		// HttpClients.custom().setConnectionManager(cm).build();
		/*
		 * CloseableHttpClient httpClient =
		 * HttpClients.createDefault();//如果不采用连接池就是这种方式获取连接
		 */
		return httpClient;
	}

	/**
	 * 取得应答数据流
	 * 
	 * @param response
	 * @return
	 * @throws IOException
	 */
	public String getResponseTxt(CloseableHttpResponse response) throws IOException {
		HttpEntity resEntity = response.getEntity();
		String resStr = EntityUtils.toString(resEntity, Consts.UTF_8);
		EntityUtils.consume(resEntity);
		return resStr;
	}

	/**
	 * 
	 * @param url
	 * @return
	 */
	public String get(String url) {
		return get(url, null, null);
	}

	/**
	 * 
	 * @param url
	 * @param httpHeaders
	 * @return
	 */
	public String get(String url, Map<String, String> httpHeaders) {
		return get(url, null, httpHeaders);
	}

	/**
	 * 
	 * @param url
	 * @param timeout
	 * @param httpHeaders
	 * @return
	 */
	public String get(String url, Integer timeout, Map<String, String> httpHeaders) {
		return get(url, null, httpHeaders, null, 0);
	}

	/**
	 * @param url
	 * @param timeout
	 * @param httpHeaders
	 * @param proxyHost
	 * @param proxyPort
	 * @return
	 */
	public String get(String url, Integer timeout, Map<String, String> httpHeaders, String proxyHost, int proxyPort) {
		HttpGet httpGet = new HttpGet(url);
		CloseableHttpResponse response = null;

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
			response = getHttpClient(proxyHost, proxyPort).execute(httpGet);
//			if (response.getStatusLine().getStatusCode() == 200) {
//				return getResponseTxt(response);// 获得返回的结果
//			}  else if (response.getStatusLine().getStatusCode() == 400) {
//				throw new BaseRuntimeException("400", "错误请求");
//			} else if (response.getStatusLine().getStatusCode() == 500) {
//				throw new BaseRuntimeException("500", "服务器遇到错误，无法完成请求。");
//			} else {
//				throw new BaseRuntimeException(String.valueOf(response.getStatusLine().getStatusCode()), "HTTP请求错误");
//			}
			return getResponseTxt(response);// 获得返回的结果
		} catch (IOException e) {
			log.error(e.getMessage(), e);
			throw new BaseRuntimeException("499", "HTTP请求错误");
		} finally {
			try {
				if (httpGet != null) {
					httpGet.abort();
				}
				if (response != null) {
					response.close();
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
	public String post(String url, String message) {
		return post(url, message, null);
	}

	/**
	 * @param url
	 * @param message
	 * @param timeout
	 * @return
	 */
	public String post(String url, String message, Integer timeout) {
		HttpPost httpPost = new HttpPost(url);
		CloseableHttpResponse response = null;

		if (timeout != null) {
			// 配置超时时间
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout)
					.setConnectionRequestTimeout(timeout).setSocketTimeout(timeout).setRedirectsEnabled(true).build();
			// 设置超时时间
			httpPost.setConfig(requestConfig);
		}

		httpPost.addHeader("Content-Type", "application/json; charset=utf-8");

		try {
			if (!Strings.isBlank(message)) {
				InputStream instream = InputStreamUtils.StringTOInputStream(message, "utf-8");
				httpPost.setEntity(new InputStreamEntity(instream));
			}

			response = getHttpClient().execute(httpPost);
			if (response != null) {
//				if (response.getStatusLine().getStatusCode() == 200) {
//					return getResponseTxt(response);
//				} else if (response.getStatusLine().getStatusCode() == 400) {
//					throw new BaseRuntimeException("400", "错误请求");
//				} else if (response.getStatusLine().getStatusCode() == 500) {
//					throw new BaseRuntimeException("500", "服务器遇到错误，无法完成请求。");
//				} else {
//					throw new BaseRuntimeException(response.getStatusLine().getStatusCode() + "", "HTTP请求错误");
//				}
				return getResponseTxt(response);// 获得返回的结果
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BaseRuntimeException("499", "HTTP请求错误");
		} finally {
			try {
				if (httpPost != null) {
					httpPost.abort();
				}
				if (response != null) {
					response.close();
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
	 * @param parametersList
	 * @return
	 */
	public String post(String url, List<BasicNameValuePair> parametersList) {
		return post(url, parametersList, null);
	}

	/**
	 * @param url
	 * @param parametersList
	 * @param timeout
	 * @return
	 */
	public String post(String url, List<BasicNameValuePair> parametersList, Integer timeout) {
		return post(url, parametersList, timeout, null, 0);
	}

	/**
	 * @param url
	 * @param parametersList
	 * @return
	 */
	public String post(String url, List<BasicNameValuePair> parametersList, Integer timeout, String proxyHost,
			int proxyPort) {
		HttpPost httpPost = new HttpPost(url);
		CloseableHttpResponse response = null;

		if (timeout != null) {
			// 配置超时时间
			RequestConfig requestConfig = RequestConfig.custom().setConnectTimeout(timeout)
					.setConnectionRequestTimeout(timeout).setSocketTimeout(timeout).setRedirectsEnabled(true).build();
			// 设置超时时间
			httpPost.setConfig(requestConfig);
		}

		httpPost.addHeader("Content-Type", "application/json; charset=utf-8");

		try {
			if (parametersList != null) {
				UrlEncodedFormEntity entity = new UrlEncodedFormEntity(parametersList, "UTF-8");
				// 设置post求情参数
				httpPost.setEntity(entity);
			}

			response = getHttpClient(proxyHost, proxyPort).execute(httpPost);
			if (response != null) {
//				if (response.getStatusLine().getStatusCode() == 200) {
//					return getResponseTxt(response);
//				} else if (response.getStatusLine().getStatusCode() == 400) {
//					throw new BaseRuntimeException("400", "错误请求");
//				} else if (response.getStatusLine().getStatusCode() == 500) {
//					throw new BaseRuntimeException("500", "服务器遇到错误，无法完成请求。");
//				} else {
//					throw new BaseRuntimeException(response.getStatusLine().getStatusCode() + "", "HTTP请求错误");
//				}
				return getResponseTxt(response);// 获得返回的结果
			}
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			throw new BaseRuntimeException("499", "HTTP请求错误");
		} finally {
			try {
				if (httpPost != null) {
					httpPost.abort();
				}
				if (response != null) {
					response.close();
				}
			} catch (IOException e) {
				log.error(e.getMessage(), e);
			}
		}
		return null;

	}

}
