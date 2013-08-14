package com.compuware.oauth2.client1;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class OAuthConfigManager {
	
    private static volatile OAuthConfig config = null;
		
	public static OAuthConfig getConfig () {
		if (config == null) {
			throw new IllegalStateException("The OAuth configuration has not been initialized.");
		}
		return config;
	}
	
	private OAuthConfigManager () {}
	
	public static class Initializer implements ServletContextListener {
		private ServletContext context;

		@Override
		public void contextInitialized(ServletContextEvent evt) {
			this.context = evt.getServletContext();
			String authorizationEndpoint = getInitParameter("authorizationEndpoint");
			String tokenEndpoint = getInitParameter("tokenEndpoint");
			String clientId = getInitParameter("clientId");
			String clientSecret = getInitParameter("clientSecret");
			String redirectUri = getInitParameter("redirectUri");
			
			OAuthConfigManager.config = new OAuthConfig.Builder()
						.setAuthorizationEndpoint(authorizationEndpoint)
						.setTokenEndpoint(tokenEndpoint)
						.setClientId(clientId)
						.setClientSecret(clientSecret)
						.setRedirectUri(redirectUri)
						.build();
			
		}

		@Override
		public void contextDestroyed(ServletContextEvent evt) {
			// No special destruction logic needed
		}
		
		private String getInitParameter(String name) {
			String param = context.getInitParameter(name);
			if (param == null) {
				throw new RuntimeException(String.format("Expected an initialization parameter named '%s'", name));
			}
			return param;
		}
	}

}
