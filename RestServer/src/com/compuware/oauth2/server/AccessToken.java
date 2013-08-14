package com.compuware.oauth2.server;

public class AccessToken {
	private String accessToken;
	private String clientId;
	private UserDetails userDetails;
	private boolean read;
	private boolean write;
	
	public static Builder builder (String accessToken) {
		return new Builder(accessToken);
	}
	
	
	private AccessToken(String accessToken, String clientId,
			UserDetails userDetails, boolean read, boolean write) {
		super();
		this.accessToken = accessToken;
		this.clientId = clientId;
		this.userDetails = userDetails;
		this.read = read;
		this.write = write;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public String getClientId() {
		return clientId;
	}

	public UserDetails getUserDetails() {
		return userDetails;
	}
	
	public boolean isRead() {
		return read;
	}


	public boolean isWrite() {
		return write;
	}

	@Override
	public String toString() {
		return "AccessToken [accessToken=" + accessToken + ", clientId="
				+ clientId + ", userDetails=" + userDetails + ", read=" + read
				+ ", write=" + write + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accessToken == null) ? 0 : accessToken.hashCode());
		result = prime * result
				+ ((clientId == null) ? 0 : clientId.hashCode());
		result = prime * result + (read ? 1231 : 1237);
		result = prime * result
				+ ((userDetails == null) ? 0 : userDetails.hashCode());
		result = prime * result + (write ? 1231 : 1237);
		return result;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AccessToken other = (AccessToken) obj;
		if (accessToken == null) {
			if (other.accessToken != null)
				return false;
		} else if (!accessToken.equals(other.accessToken))
			return false;
		if (clientId == null) {
			if (other.clientId != null)
				return false;
		} else if (!clientId.equals(other.clientId))
			return false;
		if (read != other.read)
			return false;
		if (userDetails == null) {
			if (other.userDetails != null)
				return false;
		} else if (!userDetails.equals(other.userDetails))
			return false;
		if (write != other.write)
			return false;
		return true;
	}

	public static class Builder {
		private String accessToken;
		private String clientId;
		private UserDetails userDetails;
		private boolean read;
		private boolean write;
		
		private Builder (String accessToken) {
			this.accessToken = accessToken;
		}
		
		public Builder setClientId (String clientId) {
			this.clientId = clientId;
			return this;
		}
		
		public Builder setUserDetails (UserDetails userDetails) {
			this.userDetails = userDetails;
			return this;
		}
		
		public Builder setRead(boolean read) {
			this.read = read;
			return this;
		}
		
		public Builder setWrite(boolean write) {
			this.write = write;
			return this;
		}

		public AccessToken build () {
			return new AccessToken(accessToken, clientId, userDetails, read, write);
		}
		
	}

}
