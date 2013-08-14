package com.compuware.oauth2.client1;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.amber.oauth2.client.request.OAuthClientRequest;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.types.ResponseType;
import org.codehaus.jettison.json.JSONObject;

public class OAuthFilter implements Filter {
	
	@SuppressWarnings("unused")
	private FilterConfig config;

	@Override
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}


	@Override
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		
		// If this is a request to the OAuth redirect endpoint, let it go through
		if ("/app".equals(request.getServletPath()) && "/redirect".equals(request.getPathInfo())) {
			chain.doFilter(req, resp);
			return;
		}
		
		// Check the session object to see if we already have an access token
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
		
	    if (validToken) {
	    	// We have a valid access token which has not yet expired, so go ahead and forward to the requested resource
	    	chain.doFilter(req, resp);
	    } else {
	    	// We don't have a valid access token, so we need to initiate a request for one by redirecting to the
	    	// authorization endpoint of the authorization server.
			try {
				StringBuffer url = ((HttpServletRequest)request).getRequestURL();
				String queryString = ((HttpServletRequest)request).getQueryString();
				if (queryString != null) {
					url.append("?").append(queryString);
				}
				OAuthConfig oConfig = OAuthConfigManager.getConfig();
				OAuthClientRequest oauthRequest = OAuthClientRequest
						.authorizationLocation(oConfig.getAuthorizationEndpoint())
						.setClientId(oConfig.getClientId())
						.setRedirectURI(oConfig.getRedirectUri())
						.setState(url.toString())
						.setScope("read")
						.setResponseType(ResponseType.CODE.toString()).buildQueryMessage();
				
				response.sendRedirect(oauthRequest.getLocationUri());
			} catch (OAuthSystemException e) {
				throw new ServletException(e);
			}
	    	
	    }

	}

	@Override
	public void destroy() {
		// No destruction logic needed
	}

}
