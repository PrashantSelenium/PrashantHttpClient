package com.caveofprog.rest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.Assert;
import org.testng.annotations.Test;

public class PostRequest {
	
	
	@Test(description="data as string", enabled=false)
	public void postTest() {
		HttpPost post = new HttpPost("http://localhost:8080/laptop-bag/webapi/api/add");
		CloseableHttpClient client = HttpClientBuilder.create().build();
		
		//Add headers
		post.addHeader("Content-Type", "application/json");
		post.addHeader("Accept", "application/json");
		
		//Add body
		String jsonBody = "{" +
			    "\"BrandName\": \"Moto\"," +
			    "\"Features\": {" +
			      "\"Feature\": [\"8GB RAM\"," +
			        "\"100TB Hard Drive\"]"+
			    "},"+
			    "\"Id\": 191," +
			    "\"LaptopName\": \"Z Series\"" +
			  "}";
		
		StringEntity data = new StringEntity(jsonBody,ContentType.APPLICATION_JSON);
		post.setEntity(data);
		
		CloseableHttpResponse response = null;
		try {
			response = client.execute(post);
			ResponseHandler<String> body = new BasicResponseHandler();
			RestResponse restResponse = new RestResponse(response.getStatusLine().getStatusCode(), body.handleResponse(response));
			System.out.println(restResponse.toString());
			
		} catch (ClientProtocolException e) {
			Assert.fail(e.toString());
		} catch (IOException e) {
			Assert.fail(e.toString());
		} finally{
			try {
				client.close();
				response.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Test(description="data as file", enabled=false)
	public void postTest_File() {
		HttpPost post = new HttpPost("http://localhost:8080/laptop-bag/webapi/api/add");
		CloseableHttpClient client = HttpClientBuilder.create().build();
		
		//Add headers
		post.addHeader("Content-Type", "application/json");
		post.addHeader("Accept", "application/json");
		
		//Add body -- data from file
		File file = new File("TestDataFile");
		FileEntity fileEntitydata = new FileEntity(file,ContentType.APPLICATION_JSON); 
		post.setEntity(fileEntitydata);
		
		CloseableHttpResponse response = null;
		try {
			response = client.execute(post);
			ResponseHandler<String> body = new BasicResponseHandler();
			RestResponse restResponse = new RestResponse(response.getStatusLine().getStatusCode(), body.handleResponse(response));
			System.out.println(restResponse.toString());
			
		} catch (ClientProtocolException e) {
			Assert.fail(e.toString());
		} catch (IOException e) {
			Assert.fail(e.toString());
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
	public void postModular() {
		String url = "http://localhost:8080/laptop-bag/webapi/api/add";

		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");
		header.put("Accept", "application/json");
		
		//Add body
		String jsonBody = "{" + "\"BrandName\": \"Zoto\"," + "\"Features\": {"
				+ "\"Feature\": [\"8GB RAM\"," + "\"100TB Hard Drive\"]" + "},"
				+ "\"Id\": 301," + "\"LaptopName\": \"Z Series\"" + "}";

		//Add body -- data from file
		File file = new File("TestDataFile");
				
		RestResponse restResponse = RestApiHelper.performPostRequest(url, file,ContentType.APPLICATION_JSON, header);
		System.out.println(restResponse.getStatusCode());
		System.out.println(restResponse.getResponseBody());

	}

}
