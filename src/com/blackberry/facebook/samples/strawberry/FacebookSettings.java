/**
 * Copyright (c) E.Y. Baskoro, Research In Motion Limited.
 * 
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without 
 * restriction, including without limitation the rights to use, 
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the 
 * Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, 
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES 
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND 
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT 
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING 
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR 
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 * This License shall be included in all copies or substantial 
 * portions of the Software.
 * 
 * The name(s) of the above copyright holders shall not be used 
 * in advertising or otherwise to promote the sale, use or other 
 * dealings in this Software without prior written authorization.
 * 
 */
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
