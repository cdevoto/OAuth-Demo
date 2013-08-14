package com.compuware.oauth2.client3;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import javax.servlet.ServletException;

import org.apache.amber.oauth2.client.OAuthClient;
import org.apache.amber.oauth2.client.URLConnectionClient;
import org.apache.amber.oauth2.client.request.OAuthClientRequest;
import org.apache.amber.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.amber.oauth2.common.message.types.GrantType;
import org.codehaus.jettison.json.JSONObject;

/**
 * This class ensures that a single OAuth access token scoped
 * to the entire client application is retrieved and managed
 * in a threadsafe way.
 * 
 * @author Carlos Devoto
 *
 */
public class AccessTokenManager {
	private static volatile FutureTask<String> accessToken;

	public static String getAccessToken () throws ServletException {
		FutureTask<String> tokenInstance = accessToken;
		if (tokenInstance == null) {
			synchronized (AccessTokenManager.class) {
				tokenInstance = accessToken;
				if (tokenInstance == null) {
					Callable<String> eval = new RetrieveAccessTokenJob();
					tokenInstance = accessToken = new FutureTask<String>(eval);
					tokenInstance.run();
				}
			}
		}
		String tokenValue;
		try {
			tokenValue = tokenInstance.get();
		} catch (Exception e) {
			throw new ServletException(e.getCause());
		}
	    boolean validToken = false;
    	// We have an access token, so now check to see if its about to expire or has already expired
    	try {
    		JSONObject tokenObject = new JSONObject(tokenValue);
    		long issued = tokenObject.getLong("issued");
    		long expiresIn = tokenObject.getLong("expiresIn");
    		if (System.currentTimeMillis() < issued + (expiresIn * 1000) - 60000) {
    			validToken = true;
    		}
    	} catch (Exception ex) {
    		// Do nothing
    	}
	    if (!validToken) {
	    	// The current access token is expired or is about to expire, so we need a new one.
	    	synchronized (AccessTokenManager.class) {
	    		if (tokenInstance.equals(accessToken)) {
	    			accessToken = null;
	    		}
	    	}
	    	return getAccessToken();
	    } 
	    return tokenValue;
	}
	
	private AccessTokenManager () {}
	
	private static class RetrieveAccessTokenJob implements Callable<String> {

		@Override
		public String call() throws Exception {
			OAuthConfig oConfig = OAuthConfigManager.getConfig();
	    	OAuthClientRequest req = OAuthClientRequest
	                .tokenLocation(oConfig.getTokenEndpoint())
	                .setGrantType(GrantType.CLIENT_CREDENTIALS)
	                .setClientId(oConfig.getClientId())
	                .setClientSecret(oConfig.getClientSecret())
	                .setScope("read write")
	                .buildBodyMessage();
	
	        OAuthClient oAuthClient = new OAuthClient(new URLConnectionClient());
	
	        OAuthAccessTokenResponse oAuthResponse = oAuthClient.accessToken(req);
	
	        String accessToken = oAuthResponse.getAccessToken();
	        Long expiresIn = oAuthResponse.getExpiresIn();
	        String refreshToken = oAuthResponse.getRefreshToken();
	        String oauthAccessToken = String.format("{\"accessToken\": \"%s\", \"issued\": %s, \"expiresIn\": %s, \"refreshToken\": " + (refreshToken != null ? "\"%s\"" : "%s") + " }", accessToken, System.currentTimeMillis(), expiresIn, refreshToken);
			return oauthAccessToken;
		}
		
	}
}
