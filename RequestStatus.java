package com.caveofprog.rest;

import org.apache.http.HttpResponse;
import org.apache.http.concurrent.FutureCallback;

public class RequestStatus implements FutureCallback<HttpResponse> {

	public void cancelled() {
		System.out.println("------------------request cancelled----------------------");
	}

	public void completed(HttpResponse result) {
		System.out.println("------------------request completed----------------------" + result.getProtocolVersion());
	}

	public void failed(Exception e) {
		System.out.println("------------------request failed----------------------" + e.getMessage());
	}

}
