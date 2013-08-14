package com.compuware.oauth2.server;

import java.io.IOException;
import java.net.URLEncoder;

import javax.net.ssl.SSLContext;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.codehaus.jettison.json.JSONObject;

import com.googlecode.concurrentlinkedhashmap.ConcurrentLinkedHashMap;

public class CORSFilter implements Filter {

	
	private FilterConfig config;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse response = (HttpServletResponse) resp;
		    response.setHeader("Access-Control-Allow-Origin", "*");
			chain.doFilter(req, resp);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
        this.config = config;
	}

	@Override
	public void destroy() {
		// No destruction logic needed
	}
}
