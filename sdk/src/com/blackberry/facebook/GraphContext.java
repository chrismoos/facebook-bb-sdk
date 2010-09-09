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

import java.util.Enumeration;
import java.util.Hashtable;

import com.blackberry.facebook.json.JSONObject;
import com.blackberry.facebook.json.JSONTokener;
import com.blackberry.util.log.RichTextLoggable;

public class GraphContext implements Graph, RichTextLoggable {

	protected String providerUrl = null;
	protected String accessToken = null;

	protected FacebookContext fbc = null;

	public GraphContext(FacebookContext pfbc) {
		fbc = pfbc;
		setProviderUrl(fbc.getApplicationSettings().graphUrl);
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Object read(String path) throws GraphException {
		return read(path, null);
	}

	public Object read(String path, Parameters params) throws GraphException {
		Hashtable args = new Hashtable();
		args.put("access_token", accessToken);
		args.put("format", "JSON");

		if ((params != null) && (params.getCount() > 0)) {
			Enumeration paramNamesEnum = params.getParameterNames();

			while (paramNamesEnum.hasMoreElements()) {
				String paramName = (String) paramNamesEnum.nextElement();
				String paramValue = params.get(paramName).getValue();
				args.put(paramName, paramValue);
			}
		}

		try {
			StringBuffer responseBuffer = fbc.getHttpClient().doGet(getProviderUrl() + '/' + path, args);

			if (responseBuffer.length() == 0) {
				throw new Exception("Empty response");
			}

			return new JSONObject(new JSONTokener(responseBuffer.toString()));

		} catch (Throwable t) {
			t.printStackTrace();
			throw new GraphException(t.getMessage());
		}
	}

	public Object write(String path, Object object) throws GraphException {
		Hashtable data = new Hashtable();
		data.put("access_token", accessToken);
		data.put("format", "JSON");

		try {
			JSONObject jsonObject = (JSONObject) object;
			Enumeration keysEnum = jsonObject.keys();

			while (keysEnum.hasMoreElements()) {
				String key = (String) keysEnum.nextElement();
				Object val = jsonObject.get(key);

				if (!(val instanceof JSONObject)) {
					data.put(key, val.toString());
				}
			}

			StringBuffer responseBuffer = fbc.getHttpClient().doPost(getProviderUrl() + '/' + path, data);

			if (responseBuffer.length() == 0) {
				throw new GraphException("Empty response");
			}

			return new JSONObject(new JSONTokener(responseBuffer.toString()));
		} catch (Exception e) {
			throw new GraphException(e.getMessage());
		}
	}

	public Object delete(String path) throws GraphException {
		Hashtable data = new Hashtable();
		data.put("access_token", accessToken);
		data.put("format", "JSON");
		data.put("method", "delete");

		try {
			StringBuffer responseBuffer = fbc.getHttpClient().doPost(getProviderUrl() + '/' + path, data);

			if (responseBuffer.length() == 0) {
				throw new GraphException("Empty response");
			}

			return new JSONObject(new JSONTokener(responseBuffer.toString()));
		} catch (Exception e) {
			throw new GraphException(e.getMessage());
		}
	}

	public String getProviderUrl() {
		return providerUrl;
	}

	public void setProviderUrl(String providerUrl) {
		this.providerUrl = providerUrl;
	}

}
