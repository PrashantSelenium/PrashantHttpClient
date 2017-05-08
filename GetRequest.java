package com.caveofprog.rest;

import java.io.IOException;

import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testng.annotations.Test;

public class GetRequest {
	
	@Test
	public void getTest() {
		HttpGet get = new HttpGet("http://localhost:8080/laptop-bag/webapi/api/ping/Kaveri");
		CloseableHttpClient client = HttpClientBuilder.create().build();
		CloseableHttpResponse response = null;
		try {
			response = client.execute(get);
			StatusLine status = response.getStatusLine();
			System.out.println("Status Code --> " + status.getStatusCode());
			System.out.println("Protocol Veriosn --> " + status.getProtocolVersion());
			
			ResponseHandler<String> body = new BasicResponseHandler();
			String responseBody = body.handleResponse(response);
			System.out.println("*********** RESPONSE BODY ****************");
			System.out.println(responseBody);
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
}
