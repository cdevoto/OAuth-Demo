package com.compuware.oauth2.client4;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.codehaus.jettison.json.JSONObject;

public class OAuthFilter implements Filter {

	@SuppressWarnings("unused")
	private FilterConfig config;

	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		HttpSession session = request.getSession(true); 
		
	    String accessToken = (String) session.getAttribute("access_token");
	    boolean validToken = false;
	    if (accessToken != null) {
	    	// We have an access token, so now check to see if its about to expire or has already expired
	    	try {
	    		JSONObject tokenObject = new JSONObject(accessToken);
	    		long issued = tokenObject.getLong("issued");
	    		long expiresIn = tokenObject.getLong("expiresIn");
	    		if (System.currentTimeMillis() < issued + (expiresIn * 1000) - 60000) {
	    			validToken = true;
	    		}
	    	} catch (Exception ex) {
	    		// Do nothing
	    	}
	    }
	    
	    if (!validToken) {
	    	Credentials credentials = (Credentials) session.getAttribute("credentials");
	    	if (credentials != null) {
	    		try {
					accessToken = AccessTokenManager.retrieveAccessToken(credentials);
					session.setAttribute("access_token", accessToken);
					request.setAttribute("access_token", accessToken);
					chain.doFilter(req, resp);
					return;
				} catch (Exception e) {
					session.removeAttribute("credentials");
					throw new ServletException(e);
				}
	    	} else {
				StringBuffer url = ((HttpServletRequest)request).getRequestURL();
				String queryString = ((HttpServletRequest)request).getQueryString();
				if (queryString != null) {
					url.append("?").append(queryString);
				}
	    		String encodedUrl = URLEncoder.encode(url.toString(), "UTF-8").replace("+", "%20");
	    		String redirectUrl = new StringBuilder()
	    		                     .append(request.getScheme()).append("://")
	    		                     .append(request.getServerName()).append(":")
	    		                     .append(request.getServerPort())
	    		                     .append(request.getContextPath())
	    		                     .append("/login?goto=")
	    		                     .append(encodedUrl).toString();
	    		response.sendRedirect(redirectUrl);
	    		return;
	    	}
	    } else {
	    	request.setAttribute("access_token", accessToken);
	    	chain.doFilter(req, resp);
	    	return;
	    }
		
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
		
	}

	@Override
	public void destroy() {
		// No special destruction logic needed
		
	}

}
