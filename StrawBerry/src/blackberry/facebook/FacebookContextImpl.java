package blackberry.facebook;

/*
Copyright (c) 2010 E.Y. Baskoro

Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.
*/

import java.util.Hashtable;

import org.json.me.JSONArray;
import org.json.me.JSONObject;
import org.json.me.JSONTokener;

import blackberry.facebook.rest.RestClient;
import blackberry.facebook.rest.RestException;
import blackberry.net.HttpClient;
import blackberry.util.Logger;
import blackberry.util.LoggerFactory;
import facebook.FacebookContext;
import facebook.FacebookException;
import facebook.User;
import graph.GraphContext;
import graph.GraphContextFactory;

/**
 * FacebookContextImpl
 * 
 * Implementation of Facebok context.
 *  
 * @author Eki Baskoro
 * @version 0.1
 *
 */
public class FacebookContextImpl extends FacebookContext
{

	// Logger
	private static final Logger log = LoggerFactory.getLogger(FacebookContextImpl.class.getName());
	
	private ApplicationSettings settings = null;
	private RestClient restClient = null;
	private GraphContext graphContext = null;
	private User loggedInUser = null;
	
	/**
	 * Create an instance of FacebookContextImpl.
	 * 
	 * @param settings the application settings.
	 * @throws FacebookException when any error occurs.
	 */
	public FacebookContextImpl(ApplicationSettings settings) throws FacebookException {
		this.settings = settings;
		this.restClient = new RestClient(settings.restUrl, settings.applicationKey, settings.applicationSecret);
		
		setInstance(this);
		
		Hashtable env = new Hashtable();
		env.put(GraphContext.CONTEXT_FACTORY, "blackberry.facebook.graph.GraphContextFactoryImpl");
		env.put(GraphContext.PROVIDER_URL, settings.graphUrl);
		
		try {
			graphContext = new GraphContextFactory(env);
			graphContext.setAccessToken(settings.accessToken);
		} catch (Exception e) {
			throw new FacebookException(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see facebook.FacebookContext#getSession(java.lang.String)
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
	
	/*
	 * (non-Javadoc)
	 * @see facebook.FacebookContext#upgradeSession()
	 */
	public void upgradeSession() throws FacebookException {
		if (settings.sessionKey == null)
			throw new FacebookException("Session key invalid");
		
		try {
			Hashtable data = new Hashtable();
			data.put("type", "client_cred");
			data.put("client_id", String.valueOf(settings.applicationId));
			data.put("client_secret", settings.applicationSecret);
			data.put("sessions", settings.sessionKey);
			
			StringBuffer response = HttpClient.doPost(settings.graphUrl + "/oauth/exchange_sessions", data);
			
			if (response.length() == 0)
				throw new FacebookException("Empty response");
			
			JSONArray array = new JSONArray(new JSONTokener(response.toString()));
			settings.accessToken = array.getJSONObject(0).getString("access_token");
			graphContext.setAccessToken(settings.accessToken);
		} catch (Exception e) {
			throw new FacebookException(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see facebook.FacebookContext#hasSession()
	 */
	public boolean hasSession() {
		return (settings.sessionKey != null);
	}
	
	/*
	 * (non-Javadoc)
	 * @see facebook.FacebookContext#getLoggedInUser()
	 */
	public User getLoggedInUser() throws FacebookException {
		if (loggedInUser == null) {
			loggedInUser = new UserImpl(graphContext, "me");
		}
		
		return loggedInUser;
	}

}
