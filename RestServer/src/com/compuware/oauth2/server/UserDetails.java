package com.compuware.oauth2.server;

public class UserDetails {
	private String userId;
	private String firstName;
	private String lastName;
	private String fullName;
	private String emailAddress;
	private Long accountId;
	
	public static Builder builder (String userId) {
		return new Builder(userId);
	}
	
	private UserDetails(String userId, String firstName, String lastName,
			String fullName, String emailAddress, Long accountId) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.fullName = fullName;
		this.emailAddress = emailAddress;
		this.accountId = accountId;
	}

	public String getUserId() {
		return userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getFullName() {
		return fullName;
	}

	public String getEmailAddress() {
		return emailAddress;
	}
	
	public Long getAccountId() {
		return accountId;
	}


	@Override
	public String toString() {
		return "UserDetails [userId=" + userId + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", fullName=" + fullName
				+ ", emailAddress=" + emailAddress + ", accountId=" + accountId
				+ "]";
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accountId == null) ? 0 : accountId.hashCode());
		result = prime * result
				+ ((emailAddress == null) ? 0 : emailAddress.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((fullName == null) ? 0 : fullName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
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
		UserDetails other = (UserDetails) obj;
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		if (emailAddress == null) {
			if (other.emailAddress != null)
				return false;
		} else if (!emailAddress.equals(other.emailAddress))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (fullName == null) {
			if (other.fullName != null)
				return false;
		} else if (!fullName.equals(other.fullName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		return true;
	}


	public static class Builder {
		private String userId;
		private String firstName;
		private String lastName;
		private String fullName;
		private String emailAddress;
		private Long accountId;
		
		private Builder (String userId) {
			this.userId = userId;
		}
		
		public Builder setFirstName (String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		public Builder setLastName (String lastName) {
			this.lastName = lastName;
			return this;
		}
		
		public Builder setFullName (String fullName) {
			this.fullName = fullName;
			return this;
		}
		
		public Builder setEmailAddress (String emailAddress) {
			this.emailAddress = emailAddress;
			return this;
		}
		
		public Builder setAccountId (Long accountId) {
			this.accountId = accountId;
			return this;
		}
		
		
		public UserDetails build () {
			return new UserDetails(userId, firstName, lastName, fullName, emailAddress, accountId);
		}
		
	}

}
