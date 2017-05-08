package com.caveofprog.rest;

import java.io.File;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

public class HttpsClientHelper_SSLCertificate {
	
	
	public static SSLContext getSSLContext(){
		
		TrustStrategy trust = new TrustStrategy() {
			
			public boolean isTrusted(X509Certificate[] arg0, String arg1)
					throws CertificateException {
				return true;
			}
		};
		
		try {
			return SSLContextBuilder.create().loadTrustMaterial(trust).build();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	public static CloseableHttpClient getHttpClient(SSLContext sslContext){
		return HttpClientBuilder.create().setSSLContext(sslContext).build();
	}
	
	
	public static RestResponse performGetRequestWithSSL(String url, Map<String, String> headers){
		HttpGet get = new HttpGet(url);
		
		if(headers!=null){
			get.setHeaders(getCustomHeaders(headers));
		}
		
		return performRequest(get);
	}
	
	
	public static RestResponse performPostRequestWithSSL(String url, Object content, ContentType type, Map<String, String> headers){
		HttpPost post = new HttpPost(url);
		post.setEntity(getHttpEntity(content, type));
		
		if(headers!=null){
			post.setHeaders(getCustomHeaders(headers));
		}
		
		return performRequest(post);
	}
	
	
	public static RestResponse performPutRequestWithSSL(String url, Object content, ContentType type, Map<String, String> headers){
		HttpPut put = new HttpPut(url);
		put.setEntity(getHttpEntity(content, type));
		
		if(headers!=null){
			put.setHeaders(getCustomHeaders(headers));
		}
		
		return performRequest(put);
	}
	
	
	public static RestResponse performDeleteRequestWithSSL(String url, Map<String, String> headers){
		HttpDelete delete = new HttpDelete(url);
		
		if(headers!=null){
			delete.setHeaders(getCustomHeaders(headers));
		}
		
		return performRequest(delete);
	}
	
	
	private static HttpEntity getHttpEntity(Object content,ContentType type){
		if(content instanceof String){
			return new StringEntity((String)content, type);
		}else if(content instanceof File){
			return new FileEntity((File)content, type);
		}else{
			throw new RuntimeException("Entity Type Not Found");
		}
		
	}
	
	
	private static Header[] getCustomHeaders(Map<String, String> header){
		Header[] customHeaders = new Header[header.size()];
		int i=0;
		for (String key : header.keySet()) {
			customHeaders[i++] = new BasicHeader(key, header.get(key));
		}
		return customHeaders;
	}
	
	private static RestResponse performRequest(HttpUriRequest method){
		
		CloseableHttpClient client = getHttpClient(getSSLContext());
		try {
			CloseableHttpResponse response = client.execute(method);
			ResponseHandler<String> handler = new BasicResponseHandler();
			return new RestResponse(response.getStatusLine().getStatusCode(),handler.handleResponse(response));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}

}
