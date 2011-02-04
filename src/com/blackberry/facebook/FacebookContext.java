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

import net.rim.device.api.util.Persistable;

public class FacebookContext implements Persistable {

	protected String nextUrl = null;
	protected String applicationId = null;
	protected String accessToken = null;

	public FacebookContext(String pNextUrl, String pApplicationId) {
		nextUrl = pNextUrl;
		applicationId = pApplicationId;
	}

	public String getNextUrl() {
		return nextUrl;
	}

	public void setNextUrl(String pNextUrl) {
		nextUrl = pNextUrl;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(String pApplicationId) {
		applicationId = pApplicationId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String pAccessToken) {
		accessToken = pAccessToken;
	}

	public boolean hasValidAccessToken() {
		if ((accessToken != null) && !accessToken.equals("")) {
			return true;
		} else {
			return false;
		}
	}

}
