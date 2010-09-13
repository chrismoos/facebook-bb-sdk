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
package com.blackberry.facebook;

import java.util.Hashtable;

import com.blackberry.facebook.json.JSONArray;
import com.blackberry.facebook.json.JSONObject;
import com.blackberry.facebook.json.JSONTokener;
import com.blackberry.util.log.Logger;
import com.blackberry.util.network.HttpClient;
import com.blackberry.util.network.HttpConnectionFactory;

public class FacebookContext implements Facebook {

	private ApplicationSettings settings = null;
	private RestClient restClient = null;
	private Graph graph = null;
	private User loggedInUser = null;
	private HttpConnectionFactory factory = null;
	private HttpClient httpClient = null;

	protected Logger log = Logger.getLogger(getClass());

	/**
	 * Create an instance of FacebookContext.
	 * 
	 * @param settings
	 *            the application settings.
	 * @throws FacebookException
	 *             when any error occurs.
	 */
	public FacebookContext(ApplicationSettings settings) throws FacebookException {
		this(settings, new HttpConnectionFactory());
	}

	public FacebookContext(ApplicationSettings pSettings, HttpConnectionFactory pFactory) throws FacebookException {
		settings = pSettings;
		factory = pFactory;

		httpClient = new HttpClient(factory);
		restClient = new RestClient(settings.restUrl, settings.applicationKey, settings.applicationSecret, factory);

		graph = new GraphContext(this);
		graph.setAccessToken(settings.accessToken);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facebook.Facebook#getSession(java.lang.String)
	 */
	public void getSession(String token) throws FacebookException {
		try {
			Hashtable arguments = new Hashtable();
			arguments.put("method", "auth.getSession");
			arguments.put("auth_token", token);
			JSONObject response = restClient.send(arguments);
			settings.sessionKey = response.getString("session_key");
			settings.sessionSecret = response.getString("secret");
			restClient.setSecretKey(settings.sessionSecret);
		} catch (RestException e) {
			throw new FacebookException(e.getMessage());
		} catch (Exception e) {
			throw new FacebookException(e.getMessage());
		}
	}

	public void clearSession() throws FacebookException {
		settings.sessionKey = null;
		settings.sessionSecret = null;
		settings.accessToken = null;
		restClient.setSecretKey(null);
		graph.setAccessToken(null);
		loggedInUser = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facebook.Facebook#upgradeSession()
	 */
	public void upgradeSession() throws FacebookException {
		if (settings.sessionKey == null) {
			throw new FacebookException("Session key invalid");
		}

		try {
			Hashtable data = new Hashtable();
			data.put("type", "client_cred");
			data.put("client_id", settings.applicationId);
			data.put("client_secret", settings.applicationSecret);
			data.put("sessions", settings.sessionKey);

			StringBuffer response = httpClient.doPost(settings.graphUrl + "/oauth/exchange_sessions", data);

			if (response.length() == 0) {
				throw new FacebookException("Empty response");
			}

			JSONArray array = new JSONArray(new JSONTokener(response.toString()));
			settings.accessToken = array.getJSONObject(0).getString("access_token");
			graph.setAccessToken(settings.accessToken);
		} catch (Exception e) {
			throw new FacebookException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facebook.Facebook#hasSession()
	 */
	public boolean hasSession() {
		return (settings.sessionKey != null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facebook.Facebook#getLoggedInUser()
	 */
	public User getLoggedInUser() throws FacebookException {
		if (loggedInUser == null) {
			loggedInUser = new UserImpl(graph, "me");
		}

		return loggedInUser;
	}

	public ApplicationSettings getApplicationSettings() {
		return settings;
	}

	public HttpConnectionFactory getHttpConnectionFactory() {
		return factory;
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

}
