package com.caveofprog.rest;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import junit.framework.Assert;

import org.apache.http.HttpStatus;
import org.apache.http.entity.ContentType;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

public class GetPostTogether {
	
	@Test(description="send body in json format & accept response in XML format")
	public void testPostWithXml() {
		
		String posturl = "http://localhost:8080/laptop-bag/webapi/api/add";
		String getURL = "http://localhost:8080/laptop-bag/webapi/api/find/203";

		HashMap<String, String> header = new HashMap<String, String>();
		header.put("Content-Type", "application/json");
		header.put("Accept", "application/xml"); // accept the response in xml format
		
		//Add body -- data from file
		File file = new File("TestDataFile");
				
		RestResponse restResponse = RestApiHelper.performPostRequest(posturl, file,ContentType.APPLICATION_JSON, header);
		Assert.assertEquals(HttpStatus.SC_OK, restResponse.getStatusCode());
		restResponse = RestApiHelper.performGetRequest(getURL, header);
		
		//Deserialize xml response
		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true); // do this step as we have "Features" as list
		
		ResponseBody responseBody = null;
		try {
			responseBody = xmlMapper.readValue(restResponse.getResponseBody(), ResponseBody.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		Assert.assertEquals("Dell", responseBody.BrandName);
		Assert.assertEquals("D Series", responseBody.LaptopName);

	}

}
