package com.caveofprog.rest;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GetRequestModular2 {

	@Test(enabled=false)
	public void getModular2() {
		RestResponse restResponse= RestApiHelper.performGetRequest("http://localhost:8080/laptop-bag/webapi/api/ping/Kaveri",null);
		System.out.println(restResponse.toString());
	}
	
	@Test(enabled=false)
	public void testGetAll(){
		String url ="http://localhost:8080/laptop-bag/webapi/api/all";
		
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Accept", "application/json");
		
		RestResponse restResponse= RestApiHelper.performGetRequest(url,header);
		Assert.assertTrue(HttpStatus.SC_OK==restResponse.getStatusCode() || HttpStatus.SC_NO_CONTENT==restResponse.getStatusCode(),"Expected status code not found");
		//System.out.println(restResponse.getResponseBody());
		
	}
	
	@Test(enabled=false)
	public void testGetFindWithID(){
		String url ="http://localhost:8080/laptop-bag/webapi/api/find/201";
		
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Accept", "application/json");
		
		RestResponse restResponse= RestApiHelper.performGetRequest(url,header);
		Assert.assertTrue(HttpStatus.SC_OK==restResponse.getStatusCode() || HttpStatus.SC_NOT_FOUND==restResponse.getStatusCode(),"Expected status code not found");
		//System.out.println(restResponse.getResponseBody());
		
		
		GsonBuilder gsonBuilder = new GsonBuilder();
		Gson gson = gsonBuilder.serializeNulls().setPrettyPrinting().create();
		ResponseBody responseBody = gson.fromJson(restResponse.getResponseBody(),ResponseBody.class);
		/*System.out.println("****************************************");
		System.out.println(responseBody);*/
		
		Assert.assertEquals("Dell", responseBody.BrandName);
		
	}
	
	@Test(enabled=false)
	public void testSecureGet() {
		String url ="http://localhost:8080/laptop-bag/webapi/secure/find/501";
		
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Accept", "application/json");
		header.put("Authorization", "Basic YWRtaW46d2VsY29tZQ==");
		
		RestResponse restResponse= RestApiHelper.performGetRequest(url,header);
		Assert.assertTrue(HttpStatus.SC_OK==restResponse.getStatusCode() || HttpStatus.SC_NOT_FOUND==restResponse.getStatusCode(),"Expected status code not found");
		
		System.out.println(restResponse.getResponseBody());

	}
	
	@Test(enabled=false)
	public void testSecureGet2() {
		String url ="http://localhost:8080/laptop-bag/webapi/secure/find/501";
		
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Accept", "application/json");
		header.put("Authorization", Base64.encodeBase64String("admin:welcome".getBytes()));
		
		RestResponse restResponse= RestApiHelper.performGetRequest(url,header);
		Assert.assertTrue(HttpStatus.SC_OK==restResponse.getStatusCode() || HttpStatus.SC_NOT_FOUND==restResponse.getStatusCode(),"Expected status code not found");
		
		System.out.println(restResponse.getResponseBody());
	}
	
	
	@Test(enabled=false,description="use this way of authentication when propmpt is disaplayed for uid & pass")
	public void promptAuth() {
		
		//deploy sepearte war file which is in prompt folder
		String url ="http://localhost:8080/laptop-bag/webapi/prompt/all";
		
		//uid & password
		CredentialsProvider provider = new BasicCredentialsProvider();
		provider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("admin","admin"));
		HttpClientContext context = HttpClientContext.create();
		context.setCredentialsProvider(provider);
		
		HttpGet get = new HttpGet(url);
		CloseableHttpClient client = HttpClientBuilder.create().build();
		CloseableHttpResponse response = null;
		try {
			response = client.execute(get,context);
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

	
	@Test(enabled=false,description="ssl certificate get request")
	public void getSSL(){
		
		String url = "https://localhost:8443/laptop-bag/webapi/sslres/all";
		
		RestResponse response = HttpsClientHelper_SSLCertificate.performGetRequestWithSSL(url, null);
		System.out.println(response.toString());
	}
	
	@Test(enabled=true,description="Async get request")
	public void asyncGet() {
		String url ="http://localhost:8080/laptop-bag/webapi/api/all";
		
		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Accept", "application/xml");
		
		RestResponse response = HttpAsyncClientHelper.performGetRequestAsync(url, header);
		System.out.println(response.toString());
	}

}
