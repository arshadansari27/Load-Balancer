package com.olivelabs.util;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Set;

import org.simpleframework.http.Response;

import com.ning.http.client.Cookie;
import com.ning.http.client.Headers;
import com.ning.http.url.Url;

public class ResponseConvertor {

	public static void copyResponse(com.ning.http.client.Response response,
			Response serverResponse) throws Throwable {
		String contentType = response.getContentType();
		List<Cookie> cookiesResponse = response.getCookies();
		Headers headers = response.getHeaders();
		String responseBody = response.getResponseBody();
		int statusCode = response.getStatusCode();
		String statusText = response.getStatusText();
		Url responseUrl = response.getUrl();
		
		serverResponse.setCode(statusCode);
		serverResponse.setText(statusText);
		for(Cookie cookie : cookiesResponse){
			String name = cookie.getName();
			String value = cookie.getValue();
			String domain = cookie.getDomain();
			int age = cookie.getMaxAge();
			String path = cookie.getPath(); 
			serverResponse.setCookie(new org.simpleframework.http.Cookie(name, value, path));
		}
		Set<String> headersServerResponse = headers.getHeaderNames();
		for (String header : headersServerResponse){
			serverResponse.add(header, headers.getHeaderValue(header));
		}
		PrintStream body = serverResponse.getPrintStream();
		body.print(responseBody);
		body.close();
		serverResponse.commit();
	}

}
