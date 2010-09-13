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
package com.blackberry.facebook.ui;

import com.blackberry.facebook.ApplicationSettings;
import com.blackberry.facebook.FacebookContext;
import com.blackberry.util.log.Logger;
import com.blackberry.util.network.CookieManager;

public class LoginScreen extends BrowserScreen implements ActionListener {

	public static final String ACTION_SUCCESS = "success";
	public static final String ACTION_LOGGED_IN = "loggedIn";
	public static final String ACTION_ERROR = "error";

	private ApplicationSettings settings;

	protected Logger log = Logger.getLogger(getClass());

	public LoginScreen(FacebookContext pfbc, CookieManager cookieManager) {
		super(new StringBuffer().append("http://m.facebook.com/tos.php?").append("api_key=").append(pfbc.getApplicationSettings().applicationKey).append('&').append("v=1.0").append('&').append("next=").append(pfbc.getApplicationSettings().nextUrl).toString(), pfbc, cookieManager);
		settings = pfbc.getApplicationSettings();
		addActionListener(this);
		log.info("(LoginScreen) URL: " + getUrl());
	}

	public void login() {
		browse();
	}

	public void onAction(Action event) {
		if (event.getSource() == this) {
			if (event.getAction().equals(ACTION_SUCCESS) && getUrl().startsWith(settings.nextUrl)) {
				String url = getUrl();
				log.info("(LoginScreen.ACTION_SUCCESS) URL: " + url);
				int startIndex = url.indexOf("auth_token");

				if (startIndex > -1) {
					int stopIndex = url.length();

					if (url.indexOf('&', startIndex) > -1) {
						stopIndex = url.indexOf('&', startIndex);
					} else if (url.indexOf(';', startIndex) > -1) {
						stopIndex = url.indexOf(';', startIndex);
					}

					String authToken = url.substring(url.indexOf('=', startIndex) + 1, stopIndex);
					log.debug("auth_token = " + authToken);
					fireAction(ACTION_LOGGED_IN, authToken);
				}
			}
		}
	}

}
