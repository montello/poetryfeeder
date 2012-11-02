package com.fraise.source.twitter;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable
public class ApplicationToken {

	@PrimaryKey
	@Persistent
	private String applicationName;

	@Persistent
	private String consumerKey;

	@Persistent
	private String consumerSecret;

	public ApplicationToken(String applicationName, String consumerKey,
			String consumerSecret) {
		this.applicationName = applicationName;
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
	}

	public void updateKeyAndSecret(String consumerKey, String consumerSecret) {
		this.consumerKey = consumerKey;
		this.consumerSecret = consumerSecret;
	}

	public String getApplicationName() {
		return applicationName;
	}

	public String getConsumerKey() {
		return consumerKey;
	}

	public String getConsumerSecret() {
		return consumerSecret;
	}
}
