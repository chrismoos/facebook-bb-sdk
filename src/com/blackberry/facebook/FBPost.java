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

import com.blackberry.facebook.json.JSONArray;
import com.blackberry.facebook.json.JSONException;
import com.blackberry.facebook.json.JSONObject;
import com.blackberry.util.log.Logger;

public class FBPost implements Post {

	private JSONObject jsonObject = null;
	private String root = "";
	private String accessToken;

	protected Logger log = Logger.getLogger(getClass());

	/**
	 * Create a post instance.
	 * 
	 * @param facebook
	 *            the Facebook context instance.
	 * @param root
	 *            the Facebook object's root path.
	 */
	public FBPost(String root, String accessToken) {
		this.root = root;
		this.accessToken = accessToken;

		try {
			jsonObject = (JSONObject) Facebook.read(root, accessToken);
		} catch (FacebookException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}
	}

	/**
	 * Create a post instance.
	 * 
	 * @param facebook
	 *            the Facebook context instance.
	 * @param root
	 *            the Facebook object's root path.
	 * @param jsonObject
	 *            the JSON object.
	 */
	public FBPost(String root, JSONObject jsonObject) {
		this.root = root;
		this.jsonObject = jsonObject;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facebook.Post#getId()
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
	 * 
	 * @see facebook.Post#getType()
	 */
	public String getType() {
		return jsonObject.optString("type");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facebook.Post#getAttribution()
	 */
	public String getAttribution() {
		return jsonObject.optString("attribution");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facebook.Post#getPoster()
	 */
	public User getPoster() {
		try {
			return new FBUser(jsonObject.getJSONObject("from").getString("id"), jsonObject.getJSONObject("from"));
		} catch (JSONException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facebook.Post#getMessage()
	 */
	public String getMessage() {
		return jsonObject.optString("message");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facebook.Post#getPictureUrl()
	 */
	public String getPictureUrl() {
		return jsonObject.optString("picture");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facebook.Post#getLinkUrl()
	 */
	public String getLinkUrl() {
		return jsonObject.optString("link");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facebook.Post#getLinkName()
	 */
	public String getLinkName() {
		return jsonObject.optString("name");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facebook.Post#getLinkCaption()
	 */
	public String getLinkCaption() {
		return jsonObject.optString("caption");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facebook.Post#getLinkDescription()
	 */
	public String getLinkDescription() {
		return jsonObject.optString("description");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facebook.Post#getSourceUrl()
	 */
	public String getSourceUrl() {
		return jsonObject.optString("source");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facebook.Post#getIconUrl()
	 */
	public String getIconUrl() {
		return jsonObject.optString("icon");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facebook.Post#getLikesCount()
	 */
	public int getLikesCount() {
		return jsonObject.optInt("likes");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see facebook.Post#getComments()
	 */
	public Post[] getComments() {
		Post[] comments = null;

		try {
			if (!jsonObject.has("comments")) {
				return null;
			}

			JSONArray array = ((JSONObject) Facebook.read(root + "/comments", accessToken)).getJSONArray("data");
			comments = new Post[array.length()];

			for (int i = 0; i < array.length(); i++) {
				JSONObject postObject = array.getJSONObject(i);
				comments[i] = new FBPost(postObject.getString("id"), postObject);
			}
		} catch (JSONException e) {
			log.error(e.getMessage());
		} catch (Exception e) {
			log.error(e.getMessage());
		}

		return comments;
	}

}
