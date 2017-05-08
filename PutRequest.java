package com.caveofprog.rest;

import java.io.IOException;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testng.annotations.Test;

public class PutRequest {
	
	@Test(enabled=false)
	public void putRequest() {
		
		
		/*
		 * you can use HttpPutclass also
		 */
		RequestBuilder requestBuilder=	RequestBuilder.put("http://localhost:8080/laptop-bag/webapi/api/update")
							            .setHeader("Content-Type", "application/xml")
							            .setHeader("Accept", "application/xml");
	//Add body
	String xmlBody = "<Laptop>"+
    				"<BrandName>HP201</BrandName>"+
    				"<Features>"+
        				"<Feature>5GB RAM</Feature>"+
        				"<Feature>10TB Hard Drive</Feature>" +
    					"</Features>"+
    				"<Id>123</Id>"+
    				"<LaptopName>Latitude</LaptopName>"+
					"</Laptop>";
	
	
	HttpUriRequest put = requestBuilder.setEntity(new StringEntity(xmlBody,ContentType.APPLICATION_XML)).build();
	
	CloseableHttpClient client = HttpClientBuilder.create().build();
	CloseableHttpResponse response = null;
	try {
		response = client.execute(put);
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
	private void putRequest_Cutomized() {
		String url = "http://localhost:8080/laptop-bag/webapi/api/update";

		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/xml");
		header.put("Accept", "application/xml");
		
		String xmlBody = "<Laptop>"+
				"<BrandName>HP301</BrandName>"+
				"<Features>"+
    				"<Feature>15GB RAM</Feature>"+
    				"<Feature>100TB Hard Drive</Feature>" +
					"</Features>"+
				"<Id>123</Id>"+
				"<LaptopName>Latitude</LaptopName>"+
				"</Laptop>";
		
		RestResponse restResponse = RestApiHelper.performPutRequest(url, xmlBody, ContentType.APPLICATION_XML,header);
		System.out.println(restResponse.getResponseBody());
	}
	

}
