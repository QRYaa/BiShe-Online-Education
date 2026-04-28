package cn.tgxy.oledu.config;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

import javax.net.ssl.SSLContext;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.github.binarywang.wxpay.bean.request.WxPayDefaultRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.github.binarywang.wxpay.bean.result.WxPaySandboxSignKeyResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;

public class WxPayServiceImpl2 extends WxPayServiceImpl {

	private static final String APPLICATION_JSON = "application/json";

	@Override
	public String getSandboxSignKey() throws WxPayException {
		WxPayDefaultRequest request = new WxPayDefaultRequest();
		request.checkAndSign(this.getConfig());

		String url = "https://api.mch.weixin.qq.com/xdc/apiv2getsignkey/sign/getsignkey";
		String responseContent = this.post2(url, request.toXML(), false);
		WxPaySandboxSignKeyResult result = BaseWxPayResult.fromXML(responseContent, WxPaySandboxSignKeyResult.class);
		result.checkResult(this, request.getSignType(), true);
		return result.getSandboxSignKey();
	}

	public String post2(String url, String requestStr, boolean useKey) throws WxPayException {
		try {
			HttpClientBuilder httpClientBuilder = this.createHttpClientBuilder(useKey);
			HttpPost httpPost = this.createHttpPost(url, requestStr);
			httpPost.setHeader("Content-Type", "application/xml");
			try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
				try (CloseableHttpResponse response = httpClient.execute(httpPost)) {
					String responseString = EntityUtils.toString(response.getEntity(), StandardCharsets.UTF_8);
					return responseString;
				}
			} finally {
				httpPost.releaseConnection();
			}
		} catch (Exception e) {
			throw new WxPayException(e.getMessage(), e);
		}
	}

	private HttpClientBuilder createHttpClientBuilder(boolean useKey) throws WxPayException {
		HttpClientBuilder httpClientBuilder = HttpClients.custom();
		if (useKey) {
			this.initSSLContext(httpClientBuilder);
		}

		if (StringUtils.isNotBlank(this.getConfig().getHttpProxyHost()) && this.getConfig().getHttpProxyPort() > 0) {
			if (StringUtils.isEmpty(this.getConfig().getHttpProxyUsername())) {
				this.getConfig().setHttpProxyUsername("whatever");
			}

			// 使用代理服务器 需要用户认证的代理服务器
			CredentialsProvider provider = new BasicCredentialsProvider();
			provider.setCredentials(
					new AuthScope(this.getConfig().getHttpProxyHost(), this.getConfig().getHttpProxyPort()),
					new UsernamePasswordCredentials(this.getConfig().getHttpProxyUsername(),
							this.getConfig().getHttpProxyPassword()));
			httpClientBuilder.setDefaultCredentialsProvider(provider)
					.setProxy(new HttpHost(this.getConfig().getHttpProxyHost(), this.getConfig().getHttpProxyPort()));
		}

		// 提供自定义httpClientBuilder的能力
		Optional.ofNullable(getConfig().getHttpClientBuilderCustomizer()).ifPresent(e -> {
			e.customize(httpClientBuilder);
		});

		return httpClientBuilder;
	}

	private void initSSLContext(HttpClientBuilder httpClientBuilder) throws WxPayException {
		SSLContext sslContext = this.getConfig().getSslContext();
		if (null == sslContext) {
			sslContext = this.getConfig().initSSLContext();
		}

		httpClientBuilder
				.setSSLSocketFactory(new SSLConnectionSocketFactory(sslContext, new DefaultHostnameVerifier()));
	}

	private HttpPost createHttpPost(String url, String requestStr) {
		HttpPost httpPost = new HttpPost(url);
		httpPost.setEntity(this.createEntry(requestStr));

		httpPost.setConfig(
				RequestConfig.custom().setConnectionRequestTimeout(this.getConfig().getHttpConnectionTimeout())
						.setConnectTimeout(this.getConfig().getHttpConnectionTimeout())
						.setSocketTimeout(this.getConfig().getHttpTimeout()).build());

		return httpPost;
	}

	private StringEntity createEntry(String requestStr) {
		return new StringEntity(requestStr, ContentType.create(APPLICATION_JSON, "utf-8"));
	}

}
