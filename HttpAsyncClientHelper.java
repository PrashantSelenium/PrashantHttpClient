package com.caveofprog.rest;

import java.io.File;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.apache.http.message.BasicHeader;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

public class HttpAsyncClientHelper {

	
	public static RestResponse performGetRequestAsync(String url, Map<String, String> headers) {
		HttpGet get = new HttpGet(url);
		if(headers != null){
			get.setHeaders(getCustomHeaders(headers));
		}
		
		try {
			return performRequest(get, null);
		} catch (InterruptedException  e) {
			throw new RuntimeException(e.getMessage());
		} catch (ExecutionException e) {
			throw new RuntimeException(e.getMessage());
		} 
	}
	
	
	public static RestResponse performGetRequestSLLAsync(String url, Map<String, String> headers) {
		HttpGet get = new HttpGet(url);
		if(headers != null){
			get.setHeaders(getCustomHeaders(headers));
		}
		
		try {
			return performRequest(get, getSSLContext());
		} catch (InterruptedException  e) {
			throw new RuntimeException(e.getMessage());
		} catch (ExecutionException e) {
			throw new RuntimeException(e.getMessage());
		} 
	}
	
	
	public static RestResponse performPostRequestAsync(String url, Object content, ContentType type, Map<String, String> headers){
		HttpPost post = new HttpPost(url);
		post.setEntity(getHttpEntity(content, type));
		
		if(headers!=null){
			post.setHeaders(getCustomHeaders(headers));
		}
		
		try {
			return performRequest(post, null);
		} catch (InterruptedException  e) {
			throw new RuntimeException(e.getMessage());
		} catch (ExecutionException e) {
			throw new RuntimeException(e.getMessage());
		} 
	}
	
	public static RestResponse performPostRequestSSLAsync(String url, Object content, ContentType type, Map<String, String> headers){
		HttpPost post = new HttpPost(url);
		post.setEntity(getHttpEntity(content, type));
		
		if(headers!=null){
			post.setHeaders(getCustomHeaders(headers));
		}
		
		try {
			return performRequest(post, getSSLContext());
		} catch (InterruptedException  e) {
			throw new RuntimeException(e.getMessage());
		} catch (ExecutionException e) {
			throw new RuntimeException(e.getMessage());
		} 
	}
	
	public static RestResponse performPutRequestAsync(String url, Object content, ContentType type, Map<String, String> headers){
		HttpPut put = new HttpPut(url);
		put.setEntity(getHttpEntity(content, type));
		
		if(headers!=null){
			put.setHeaders(getCustomHeaders(headers));
		}
		
		try {
			return performRequest(put, null);
		} catch (InterruptedException  e) {
			throw new RuntimeException(e.getMessage());
		} catch (ExecutionException e) {
			throw new RuntimeException(e.getMessage());
		} 
	}
	
	public static RestResponse performPutRequestSSLAsync(String url, Object content, ContentType type, Map<String, String> headers){
		HttpPut put = new HttpPut(url);
		put.setEntity(getHttpEntity(content, type));
		
		if(headers!=null){
			put.setHeaders(getCustomHeaders(headers));
		}
		
		try {
			return performRequest(put, getSSLContext());
		} catch (InterruptedException  e) {
			throw new RuntimeException(e.getMessage());
		} catch (ExecutionException e) {
			throw new RuntimeException(e.getMessage());
		} 
	}
	
	public static RestResponse performDeleteRequestAsync(String url, Map<String, String> headers) {
		HttpDelete delete = new HttpDelete(url);
		if(headers != null){
			delete.setHeaders(getCustomHeaders(headers));
		}
		
		try {
			return performRequest(delete, null);
		} catch (InterruptedException  e) {
			throw new RuntimeException(e.getMessage());
		} catch (ExecutionException e) {
			throw new RuntimeException(e.getMessage());
		} 
	}
	
	
	public static RestResponse performDeleteRequestSSLAsync(String url, Map<String, String> headers) {
		HttpDelete delete = new HttpDelete(url);
		if(headers != null){
			delete.setHeaders(getCustomHeaders(headers));
		}
		
		try {
			return performRequest(delete, getSSLContext());
		} catch (InterruptedException  e) {
			throw new RuntimeException(e.getMessage());
		} catch (ExecutionException e) {
			throw new RuntimeException(e.getMessage());
		} 
	}
	
	private static CloseableHttpAsyncClient getHttpAsyncClient(SSLContext context) {
		if(context == null){
			return HttpAsyncClientBuilder.create().build();
		}else{
			return HttpAsyncClientBuilder.create().setSSLContext(context).build();
		}
	}
	
	private static SSLContext getSSLContext(){
		
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
	
	
	private static RestResponse performRequest(HttpUriRequest method, SSLContext context) throws InterruptedException, ExecutionException{
		Future<HttpResponse> response = null;
		try{
			CloseableHttpAsyncClient client = getHttpAsyncClient(context);
			client.start();// important for asyn calls
			response = client.execute(method, new RequestStatus()); // see RequestStatus implementation
			ResponseHandler<String> handler = new BasicResponseHandler();
			return new RestResponse(response.get().getStatusLine().getStatusCode(), handler.handleResponse(response.get()));
		} catch (Exception e) {
			if(e instanceof HttpResponseException)
				return new RestResponse(response.get().getStatusLine().getStatusCode(), e.getMessage());
			throw new RuntimeException(e.getMessage());
		}
		
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
	
}
