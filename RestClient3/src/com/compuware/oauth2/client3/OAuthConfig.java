package com.compuware.oauth2.client3;

public class OAuthConfig {


	private String authorizationEndpoint;
	private String tokenEndpoint;
	private String clientId;
	private String clientSecret;
	private String redirectUri;
	
	public OAuthConfig(String authorizationEndpoint, String tokenEndpoint,
			String clientId, String clientSecret, String redirectUri) {
		this.authorizationEndpoint = authorizationEndpoint;
		this.tokenEndpoint = tokenEndpoint;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
		this.redirectUri = redirectUri;
	}
	
	public String getAuthorizationEndpoint() {
		return authorizationEndpoint;
	}

	public String getTokenEndpoint() {
		return tokenEndpoint;
	}

	public String getClientId() {
		return clientId;
	}

	public String getClientSecret() {
		return clientSecret;
	}

	public String getRedirectUri() {
		return redirectUri;
	}

	@Override
	public String toString() {
		return "OAuthConfig [authorizationEndpoint=" + authorizationEndpoint
				+ ", tokenEndpoint=" + tokenEndpoint + ", clientId=" + clientId
				+ ", clientSecret=" + clientSecret + ", redirectUri="
				+ redirectUri + "]";
	}

	public static class Builder {
		private String authorizationEndpoint;
		private String tokenEndpoint;
		private String clientId;
		private String clientSecret;
		private String redirectUri;
		
		public Builder () {}
		
		public Builder setAuthorizationEndpoint (String authorizationEndpoint) {
			this.authorizationEndpoint = authorizationEndpoint;
			return this;
		}
		
		public Builder setTokenEndpoint (String tokenEndpoint) {
			this.tokenEndpoint = tokenEndpoint;
			return this;
		}
		
		public Builder setClientId (String clientId) {
			this.clientId = clientId;
			return this;
		}
		
		public Builder setClientSecret (String clientSecret) {
			this.clientSecret = clientSecret;
			return this;
		}
		
		public Builder setRedirectUri (String redirectUri) {
			this.redirectUri = redirectUri;
			return this;
		}
		
		public OAuthConfig build () {
			if (authorizationEndpoint == null || tokenEndpoint == null
					|| clientId == null || clientSecret == null || redirectUri == null) {
				throw new IllegalStateException(
						"OAuthConfig requires an authorization endpoint, a token endpoint, a client id, a client secret, and a redirect URI.");
			}
			return new OAuthConfig(authorizationEndpoint, tokenEndpoint,
					clientId, clientSecret, redirectUri);
		}
	}

}
