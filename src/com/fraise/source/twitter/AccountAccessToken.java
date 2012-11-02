package com.fraise.source.twitter;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class AccountAccessToken {

	public AccountAccessToken(String accountName, String token,
			String tokenSecret) {
		this.accountName = accountName;
		this.token = token;
		this.tokenSecret = tokenSecret;
	}

	@PrimaryKey
	@Persistent
	private String accountName;
	
	@Persistent
	private String token;
	
	@Persistent
	private String tokenSecret;
	
	public void updateTokenAndSecret(String token,
			String tokenSecret) {
		this.token = token;
		this.tokenSecret = tokenSecret;
	}

	public String getAccountName() {
		return accountName;
	}

	public String getToken() {
		return token;
	}

	public String getTokenSecret() {
		return tokenSecret;
	}
	
}
