package com.blackberry.facebook.samples.strawberry;

import net.rim.device.api.util.Persistable;

import com.blackberry.facebook.ApplicationSettings;

public class FacebookSettings implements Persistable {

	public String restUrl = null;
	public String graphUrl = null;
	public String nextUrl = null;
	public String applicationKey = null;
	public String applicationSecret = null;
	public String applicationId = null;

	public String sessionKey = null;
	public String sessionSecret = null;
	public String accessToken = null;

	public FacebookSettings(String restUrl, String graphUrl, String nextUrl, String applicationKey, String applicationSecret, String applicationId) {
		this.restUrl = restUrl;
		this.graphUrl = graphUrl;
		this.nextUrl = nextUrl;
		this.applicationKey = applicationKey;
		this.applicationSecret = applicationSecret;
		this.applicationId = applicationId;
	}

	public FacebookSettings(ApplicationSettings as) {
		restUrl = as.restUrl;
		graphUrl = as.graphUrl;
		nextUrl = as.nextUrl;
		applicationKey = as.applicationKey;
		applicationSecret = as.applicationSecret;
		applicationId = as.applicationId;

		sessionKey = as.sessionKey;
		sessionSecret = as.sessionSecret;
		accessToken = as.accessToken;
	}

	public ApplicationSettings getApplicationSettings() {
		ApplicationSettings as = new ApplicationSettings(restUrl, graphUrl, nextUrl, applicationKey, applicationSecret, applicationId);
		as.sessionKey = sessionKey;
		as.sessionSecret = sessionSecret;
		as.accessToken = accessToken;

		return as;
	}

}
