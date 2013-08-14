package com.compuware.oauth2.client4;

import org.apache.amber.oauth2.client.OAuthClient;
import org.apache.amber.oauth2.client.URLConnectionClient;
import org.apache.amber.oauth2.client.request.OAuthClientRequest;
import org.apache.amber.oauth2.client.response.OAuthAccessTokenResponse;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.types.GrantType;

public class AccessTokenManager {
	
	public static String retrieveAccessToken (Credentials credentials) throws OAuthSystemException, OAuthProblemException {
		OAuthConfig oConfig = OAuthConfigManager.getConfig();
    	OAuthClientRequest req = OAuthClientRequest
                .tokenLocation(oConfig.getTokenEndpoint())
                .setGrantType(GrantType.PASSWORD)
                .setClientId(oConfig.getClientId())
                .setClientSecret(oConfig.getClientSecret())
                .setUsername(credentials.getUserName())
                .setPassword(credentials.getPassword())
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
