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

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import blackberry.util.Logger;
import blackberry.util.LoggerFactory;

import facebook.Post;
import facebook.User;
import graph.GraphContext;
import graph.GraphException;
import graph.Parameters;

/**
 * User
 * 
 * Represents a Facebook User.
 * 
 * @author Eki Baskoro
 * @version 0.1
 * 
 */
public class UserImpl implements User
{

	// Logger
	private static final Logger log = LoggerFactory.getLogger(UserImpl.class.getName());
	
	private GraphContext graphContext = null;
	private JSONObject jsonObject = null;
	private String root = "me";
	
	/**
	 * Create a user instance.
	 * 
	 * @param graphContext the Graph context instance.
	 * @param root the Graph object's root path.
	 */
	public UserImpl(GraphContext graphContext, String root) {
		this.graphContext = graphContext;
		this.root = root;
		
		try {
			jsonObject = (JSONObject)graphContext.read(root);
			
			log.debug(jsonObject.toString());
		} catch (GraphException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * Create a user instance.
	 * 
	 * @param graphContext the Graph context instance.
	 * @param root the Graph object's root path.
	 * @param jsonObject the JSON object.
	 */
	public UserImpl(GraphContext graphContext, String root, JSONObject jsonObject) {
		this.graphContext = graphContext;
		this.root = root;
		this.jsonObject = jsonObject;
	}
	
	/*
	 * (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return getFullName();
	}
	
	/*
	 * (non-Javadoc)
	 * @see facebook.User#getId()
	 */
	public String getId() {
		try {
			return jsonObject.getString("id");
		} catch (JSONException e) {
			log.error(e.getMessage());
		}
		
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see facebook.User#getFirstName()
	 */
	public String getFirstName() {
		return jsonObject.optString("first_name");
	}
	
	/*
	 * (non-Javadoc)
	 * @see facebook.User#getLastName()
	 */
	public String getLastName() {
		return jsonObject.optString("last_name");
	}
	
	/*
	 * (non-Javadoc)
	 * @see facebook.User#getFullName()
	 */
	public String getFullName() {
		return jsonObject.optString("name");
	}
	
	/*
	 * (non-Javadoc)
	 * @see facebook.User#getProfileUrl()
	 */
	public String getProfileUrl() {
		return jsonObject.optString("link");
	}
	
	/*
	 * (non-Javadoc)
	 * @see facebook.User#getAboutText()
	 */
	public String getAboutText() {
		return jsonObject.optString("about");
	}
	
	/*
	 * (non-Javadoc)
	 * @see facebook.User#getBirthday()
	 */
	public String getBirthday() {
		return jsonObject.optString("birthday");
	}
	
	/*
	 * (non-Javadoc)
	 * @see facebook.User#getWebsite()
	 */
	public String getWebsite() {
		return jsonObject.optString("website");
	}
	
	/*
	 * (non-Javadoc)
	 * @see facebook.User#getHomeTown()
	 */
	public String getHomeTown() {
		try {
			if (!jsonObject.has("hometown"))
				return null;
			
			return jsonObject.getJSONObject("hometown").getString("hometown");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see facebook.User#getLocation()
	 */
	public String getLocation() {
		try {
			if (!jsonObject.has("location"))
				return null;
			
			return jsonObject.getJSONObject("location").getString("location");
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see facebook.User#getGender()
	 */
	public String getGender() {
		return jsonObject.optString("gender");
	}
	
	/*
	 * (non-Javadoc)
	 * @see facebook.User#getFriends()
	 */
	public User[] getFriends() {
		User[] friends = null;
		
		try {
			JSONArray array = ((JSONObject)graphContext.read(root + "/friends")).getJSONArray("data");
			friends = new User[array.length()];
			
			for (int i = 0; i < array.length(); i ++) {
				JSONObject userObject = array.getJSONObject(i);
				friends[i] = new UserImpl(graphContext, userObject.getString("id"), userObject);
			}
		} catch (JSONException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return friends;
	}
	
	/*
	 * (non-Javadoc)
	 * @see facebook.User#getStream()
	 */
	public Post[] getStream() {
		Post[] posts = null;
		
		try {
			JSONArray array = ((JSONObject)graphContext.read(root + "/home")).getJSONArray("data");
			posts = new Post[array.length()];
			
			for (int i = 0; i < array.length(); i ++) {
				JSONObject postObject = array.getJSONObject(i);
				posts[i] = new PostImpl(graphContext, postObject.getString("id"), postObject);
			}
		} catch (JSONException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return posts;
	}
	
	/*
	 * (non-Javadoc)
	 * @see facebook.User#getStream(int)
	 */
	public Post[] getStream(int limit) {
		Post[] posts = null;
		Parameters params = new Parameters().add("limit", String.valueOf(limit));
		
		try {
			JSONArray array = ((JSONObject)graphContext.read(root + "/home", params)).getJSONArray("data");
			posts = new Post[array.length()];
			
			for (int i = 0; i < array.length(); i ++) {
				JSONObject postObject = array.getJSONObject(i);
				posts[i] = new PostImpl(graphContext, postObject.getString("id"), postObject);
			}
		} catch (JSONException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		
		return posts;
	}
	
	/*
	 * (non-Javadoc)
	 * @see facebook.User#publishStream(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void publishStream(String message, String linkUrl, String linkName, String linkCaption, String linkDescription) {
		try {
			JSONObject object = new JSONObject();
			object.put("message", message);
			object.put("link", linkUrl);
			object.put("name", linkName);
			object.put("caption", linkCaption);
			object.put("description", linkDescription);
			
			JSONObject responseObject = (JSONObject)graphContext.write(root + "/feed", object);
			
			log.debug(responseObject.toString());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see facebook.User#publishStream(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public void publishStream(String message, String pictureUrl, String linkUrl, String linkName, String linkCaption, String linkDescription, String sourceUrl) {
		try {
			JSONObject object = new JSONObject();
			object.put("message", message);
			object.put("picture", pictureUrl);
			object.put("link", linkUrl);
			object.put("name", linkName);
			object.put("caption", linkCaption);
			object.put("description", linkDescription);
			object.put("source", sourceUrl);
			
			JSONObject responseObject = (JSONObject)graphContext.write(root + "/feed", object);
			
			log.debug(responseObject.toString());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see facebook.User#setStatus(java.lang.String)
	 */
	public void setStatus(String status) {
		try {
			JSONObject object = new JSONObject().put("message", status);
			
			JSONObject responseObject = (JSONObject)graphContext.write(root + "/feed", object);
			
			log.debug(responseObject.toString());
		} catch (JSONException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
}
