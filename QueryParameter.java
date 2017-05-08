package com.caveofprog.rest;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.client.utils.URIBuilder;
import org.testng.annotations.Test;

public class QueryParameter {
	
	@Test
	public void testQueryParameter() {
		URI uri = null;
		try {
	 uri = 	new URIBuilder()
			.setScheme("http")
			.setHost("localhost:8080")
			//.setPort(8080)
			.setPath("/laptop-bag/webapi/api/query")
			.setParameter("id", "122")
			.setParameter("laptopName", "Latitude")
			.build();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}

		RestResponse response = RestApiHelper.performGetRequest(uri, null);
		System.out.println(response.getResponseBody());
		
	}

}
