package com.caveofprog.rest;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicHeader;

public class RestApiHelper {
	
	
	public static RestResponse performGetRequest(String endPointURL, Map<String, String> header) {
		RestResponse restResponse = null;
		try {
			restResponse= performGetRequest(new URI(endPointURL),header);
		} catch (URISyntaxException e) {
			throw new RuntimeException(e.getMessage(),e);
		}
		return restResponse;
	}
	
	public static RestResponse performGetRequest(URI endPointURL,Map<String, String> header){
		HttpGet get = new HttpGet(endPointURL);
		//adding header to request
		if(header!=null){
			get.setHeaders(getCustomHeaders(header));
		}
		
		return performRequest(get);
}
	
	
	
	public static RestResponse performPostRequest(String url, Object content,ContentType type, Map<String, String> header){
		
		HttpPost post = new HttpPost(url);
		if(header!=null){
			
			post.setHeaders(getCustomHeaders(header));
		}
		// adding body as string
		post.setEntity(getHttpEntity(content,type));
		
		return performRequest(post);
		
	}
	
	
	public static RestResponse performDeleteRequest(String url, Map<String, String> header){
		HttpUriRequest delete= 	RequestBuilder.delete(url).build();
		if(header!=null){
			delete.setHeaders(getCustomHeaders(header));
		}
		return performRequest(delete);
	}
	
	
	public static RestResponse performPutRequest(String url, Object content,ContentType type, Map<String, String> header){
		HttpUriRequest put =	RequestBuilder.put(url).setEntity(getHttpEntity(content, type)).build();
		if(header!=null){
			put.setHeaders(getCustomHeaders(header));
		}
		return performRequest(put);
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
		CloseableHttpClient client = HttpClientBuilder.create().build();
		CloseableHttpResponse response = null;
		ResponseHandler<String> body = null;
		RestResponse restResponse = null;
		try {
			response = client.execute(method);
			body = new BasicResponseHandler();
			restResponse = new RestResponse(response.getStatusLine().getStatusCode(), body.handleResponse(response));
		} catch (ClientProtocolException e) {
			throw new RuntimeException(e.getMessage(),e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(),e);
		} catch (Exception e) {
			if(e instanceof HttpResponseException){
				return new RestResponse(response.getStatusLine().getStatusCode(), e.getMessage());
			}
		}finally{
			try {
				client.close();
				response.close();
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(),e);
			}
		}
		return restResponse;
	}

}
