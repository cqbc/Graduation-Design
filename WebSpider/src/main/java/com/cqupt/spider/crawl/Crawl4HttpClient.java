package com.cqupt.spider.crawl;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.StringEntity;

import com.cqupt.common.enums.HttpRequestMethod;
import com.cqupt.common.statics.StaticValue;
import com.cqupt.common.statics.SystemParasSpider;
import com.cqupt.common.utils.EncodeUtil;
import com.cqupt.common.utils.IOUtil;
import com.cqupt.common.utils.ObjectAndByteArrayConvertUtil;
import com.cqupt.common.utils.StringOperatorUtil;
import com.cqupt.spider.manager.HttpClientPojoManager;
import com.cqupt.spider.manager.HttpClientPojoManager.HttpClientPojo;
import com.cqupt.spider.pojos.ContentPojo;
import com.cqupt.spider.pojos.HttpRequestPojo;

/**
 * 用httpclient实现的下载器
 * 
 */
public class Crawl4HttpClient {
	public static String crawlWebPage(HttpRequestPojo requestPojo) {
		CloseableHttpResponse response = null;
		try {
			RequestBuilder rb = null;
			if (requestPojo.isGetMethod()) {
				rb = RequestBuilder.get().setUri(
						URI.create(requestPojo.getUrl()));
			} else {
				rb = RequestBuilder.post()
						.setUri(new URI(requestPojo.getUrl()));
			}
			Map<String, Object> map = null;
			if ((map = requestPojo.getHeaderMap()) != null) {
				for (Entry<String, Object> entry : map.entrySet()) {
					rb.addHeader(entry.getKey(), entry.getValue().toString());
				}
			}
			if ((map = requestPojo.getParasMap()) != null) {
				for (Entry<String, Object> entry : map.entrySet()) {
					rb.addParameter(entry.getKey(), entry.getValue().toString());
				}
			}
			// 将form data编码完成后放入entity中
			if (requestPojo.getFormEntity() != null) {
				rb.setEntity(requestPojo.getFormEntity());
			}

			// 查看是否设置代理
			HttpUriRequest requestAll = null;
			HttpClientPojo httpClientPojo = HttpClientPojoManager
					.getHttpClientPojo();
			// 执行请求
			if (SystemParasSpider.proxy_open) {
				rb.setConfig(httpClientPojo.getRequestConfig());
				requestAll = rb.build();
				System.out.println("httpClientPojo.getProxyPojo()--"+httpClientPojo.getProxyPojo());
				response = httpClientPojo.getHttpClient().execute(requestAll);
			} else {
				rb.setConfig(HttpClientPojoManager.default_requestConfig);
				requestAll = rb.build();
				response = httpClientPojo.getHttpClient().execute(requestAll);
			}
			// return parserResponse(response);
			return parserResponse_v2(response);
		} catch (SocketTimeoutException timeOutException) {
			// 此种情况将会认为可能是代理异常失效，但暂不处理这种异常对代理替换策略的影响的!
			timeOutException.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static String crawlWebPage4ES(HttpRequestPojo requestPojo,
			String jsonEntity) {
		CloseableHttpResponse response = null;
		try {
			RequestBuilder rb = null;
			if (requestPojo.isGetMethod()) {
				rb = RequestBuilder.get().setUri(
						URI.create(requestPojo.getUrl()));
			} else {
				rb = RequestBuilder.post()
						.setUri(new URI(requestPojo.getUrl()));
			}
			StringEntity stringEntity = new StringEntity(jsonEntity,
					StaticValue.default_encoding);
			// StringEntity stringEntity = new StringEntity(filename);
			stringEntity.setContentEncoding("UTF-8");
			stringEntity.setContentType("application/json");
			rb.setEntity(stringEntity);

			Map<String, Object> map = null;
			if ((map = requestPojo.getHeaderMap()) != null) {
				for (Entry<String, Object> entry : map.entrySet()) {
					rb.addHeader(entry.getKey(), entry.getValue().toString());
				}
			}
			if ((map = requestPojo.getParasMap()) != null) {
				for (Entry<String, Object> entry : map.entrySet()) {
					rb.addParameter(entry.getKey(), entry.getValue().toString());
				}
			}

			// 查看是否设置代理
			// 暂不用
			HttpUriRequest requestAll = null;
			HttpClientPojo httpClientPojo = HttpClientPojoManager
					.getHttpClientPojo();
			// 执行请求
			rb.setConfig(HttpClientPojoManager.default_requestConfig);
			requestAll = rb.build();
			response = httpClientPojo.getHttpClient().execute(requestAll);
			return parserResponse(response);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (response != null) {
				try {
					response.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	public static String parserResponse(CloseableHttpResponse response) {
		try {
			HttpEntity entity = response.getEntity();
			String content_type_encoding = EncodeUtil
					.getCharsetByMetadata(entity.getContentType().toString());
			byte[] byteArray = ObjectAndByteArrayConvertUtil
					.getByteArrayOutputStream(entity.getContent());
			if (StringOperatorUtil.isNotBlank(content_type_encoding)) {
				String htmlSource = new String(byteArray, content_type_encoding);
				return htmlSource;
			} else {
				// 将页面中的meta中的charset拿到作为charset
				String htmlSource = new String(byteArray,
						StaticValue.default_encoding);
				String page_source_charset = EncodeUtil
						.getPageSourceCharset(htmlSource);
				if (StringOperatorUtil.isBlank(page_source_charset)) {
					return htmlSource;
				} else if (!StaticValue.default_encoding
						.equals(page_source_charset)) {
					return new String(byteArray, page_source_charset);
				}
				return htmlSource;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

		}
		return null;
	}

	public static EncodeUtil encodeUtil = new EncodeUtil();

	public static String parserResponse_v2(CloseableHttpResponse response) {
		try {
			HttpEntity entity = response.getEntity();
			InputStream is = null;
			// System.out.println(entity.toString());
			is = entity.getContent();
			byte[] byteArray = ObjectAndByteArrayConvertUtil
					.getByteArrayOutputStream(is);
			// System.out.println("byte_array length---" + byteArray.length);
			ContentPojo contentPojo = encodeUtil.getWebPageCharset(byteArray,
					entity.getContentType().toString());
			// System.out.println("---------" + contentPojo.getCharset());
			if (contentPojo != null) {
				return new String(byteArray, contentPojo.getCharset());
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return null;
	}

	public static void main(String[] args) throws Exception {
		HttpRequestPojo requestPojo = new HttpRequestPojo();
		requestPojo.setRequestMethod(HttpRequestMethod.GET);

		// String url = "http://www.oscca.gov.cn/";
		// String url = "http://www.baidu.com/";
		// String url = "http://news.sina.com.cn/";
		String url = "http://data.weibo.com/index/ajax/getchartdata?month=default&__rnd=1443164017238";
		Map<String, Object> headerMap = new HashMap<String, Object>();
		Map<String, Object> parasMap = new HashMap<String, Object>();
		// Map<String, String> formNameValueMap = new HashMap<String, String>();

		requestPojo.setUrl(url);
		
		String cookie=IOUtil.readDirOrFile("d://test/header_2.txt","utf-8");
//		JsonOperUtil.getJsonObject(header).putAll(headerMap);
//		JsonObject jsonObj=(JsonObject)(GsonOperatorUtil.parse(header));
//		Set<Map.Entry<String, JsonElement>> set=jsonObj.entrySet();
		headerMap.put("Referer","http://data.weibo.com/index/hotword?wid=1061309190000232115&wname=ios");
		headerMap.put("X-Requested-With","XMLHttpRequest");
		headerMap.put("User-Agent","Mozilla/5.0 (Windows NT 6.2; WOW64) AppleWebKit/534.57.2 (KHTML, like Gecko) Version/5.1.7 Safari/534.57.2");
		headerMap.put("Accept","*/*");
		
//		headerMap.put("Cookie","DATA=usrmdinst_5; WBStore=4e40f953589b7b00|undefined; _s_tentry=-; Apache=2458491126905.9375.1443149642549; PHPSESSID=5mn3gd114s7u2vph1m3rrf7j71; open_div=close");
		headerMap.put("Cookie","DATA=usrmdinst_7; WBStore=4e40f953589b7b00|undefined; _s_tentry=-; Apache=2888515270315.111.1443167536733; PHPSESSID=vmlspfg9iulq0l4s4qccd17qh0;");
		
		requestPojo.setHeaderMap(headerMap);
		
//		requestPojo.setParasMap(parasMap);
		// form name value是为非iso-8859-1编码的value pair而添加,当然是指存在中文的情况
		// requestPojo.setFormNameValePairMap(formNameValueMap,
		// CharsetEnum.UTF8);

		String source = crawlWebPage(requestPojo);

		System.out.println(source);

		System.out.println("done!");
	}
}
