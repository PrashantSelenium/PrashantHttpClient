package com.caveofprog.rest;

public class RestResponse {
	
	private int statusCode;
	private String responseBody;
	
	
	public int getStatusCode() {
		return statusCode;
	}
	
	public String getResponseBody() {
		return responseBody;
	}
	
	public RestResponse(int statusCode,String responseBody) {
		this.statusCode = statusCode;
		this.responseBody = responseBody;
		
	}
	
	@Override
	public String toString() {
		return String.format("Status Code : %1s Body : %2s", this.statusCode,this.responseBody);
	}

}
