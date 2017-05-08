package com.caveofprog.rest;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testng.annotations.Test;

public class DeleteRequest {
	

	@Test(enabled=false)
	public void deleteTest() {
		
		/*
		 * you can use HttpDelete class also
		 */
	HttpUriRequest delete= 	RequestBuilder.delete("http://localhost:8080/laptop-bag/webapi/api/delete/203").build();
	
	CloseableHttpClient client = HttpClientBuilder.create().build();
	CloseableHttpResponse response = null;
	try {
		response = client.execute(delete);
		ResponseHandler<String> body = new BasicResponseHandler();
		RestResponse restResponse = new RestResponse(response.getStatusLine().getStatusCode(), body.handleResponse(response));
		System.out.println(restResponse.getResponseBody());
		
	} catch (ClientProtocolException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	} finally{
		try {
			client.close();
			response.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	}
	
	
	@Test(enabled=true)
	public void deletRequest_Customized(){
		RestResponse restResponse = RestApiHelper.performDeleteRequest("http://localhost:8080/laptop-bag/webapi/api/delete/201", null);
		System.out.println(restResponse.getResponseBody());
	}
	
}

