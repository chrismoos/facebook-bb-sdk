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

import javax.microedition.io.InputConnection;

import net.rim.device.api.browser.field2.BrowserField;
import net.rim.device.api.browser.field2.BrowserFieldNavigationRequestHandler;
import net.rim.device.api.browser.field2.BrowserFieldRequest;
import net.rim.device.api.browser.field2.BrowserFieldResourceRequestHandler;
import net.rim.device.api.browser.field2.ProtocolController;
import net.rim.device.api.system.Application;
import net.rim.device.api.ui.UiApplication;
import com.blackberry.facebook.FacebookContext;
import com.blackberry.util.log.Logger;

public class LoginScreen extends BrowserScreen implements ActionListener {

	public static final String ACTION_SUCCESS = "success";
	public static final String ACTION_LOGGED_IN = "loggedIn";
	public static final String ACTION_ERROR = "error";

	protected MyBrowserFieldRequestHandler handler;
	protected Logger log = Logger.getLogger(getClass());
	protected String nextUrl;

	public LoginScreen(FacebookContext pfbc) {

		super("http://www.facebook.com/dialog/oauth?scope=publish_stream,offline_access&redirect_uri=" + pfbc.getNextUrl() + "&response_type=token&display=touch&client_id=" + pfbc.getApplicationId());

		nextUrl = pfbc.getNextUrl().trim();
		handler = new MyBrowserFieldRequestHandler(bf);
		((ProtocolController) bf.getController()).setNavigationRequestHandler("http", handler);
		((ProtocolController) bf.getController()).setResourceRequestHandler("http", handler);

		addActionListener(this);
		browse();
	}

	public boolean checkAccessTokenFromUrl(String url) {
		if ((url == null) || (url.indexOf(nextUrl) == -1)) {
			return false;
		}

		int startIndex = url.indexOf("access_token");

		if (startIndex > -1) {
			int stopIndex = url.length();

			if (url.indexOf('&', startIndex) > -1) {
				stopIndex = url.indexOf('&', startIndex);
			} else if (url.indexOf(';', startIndex) > -1) {
				stopIndex = url.indexOf(';', startIndex);
			}

			final String accessToken = url.substring(url.indexOf('=', startIndex) + 1, stopIndex);

			Application.getApplication().invokeAndWait(new Runnable() {
				public void run() {
					fireAction(ACTION_SUCCESS, accessToken);
				}
			});

			return true;
		}

		return false;
	}

	public void onAction(Action event) {
		if (event.getSource() == this) {
			if (event.getAction().equals(ACTION_SUCCESS) && (event.getData() != null)) {
				String accessToken = (String) event.getData();
				log.debug("access_token = " + accessToken);
				fireAction(ACTION_LOGGED_IN, accessToken);
				UiApplication.getUiApplication().popScreen(this);
			}
		}
	}

	public class MyBrowserFieldRequestHandler implements BrowserFieldNavigationRequestHandler, BrowserFieldResourceRequestHandler {

		protected BrowserField _browserField;

		public MyBrowserFieldRequestHandler(BrowserField browserField) {
			_browserField = browserField;
		}

		public void handleNavigation(BrowserFieldRequest request) throws Exception {
			if (!checkAccessTokenFromUrl(request.getURL())) {
				_browserField.displayContent(handleResource(request), request.getURL());
			}
		}

		public InputConnection handleResource(BrowserFieldRequest request) throws Exception {
			return _browserField.getConnectionManager().makeRequest(request);
		}

	}

}
