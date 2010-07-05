package blackberry.facebook.ui;

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

import blackberry.action.ActionEvent;
import blackberry.action.ActionListener;
import blackberry.facebook.ApplicationSettings;
import blackberry.net.CookieManager;
import blackberry.ui.BrowserScreen;
import blackberry.util.Logger;
import blackberry.util.LoggerFactory;

/**
 * LoginScreen
 * 
 * @author Eki Baskoro
 * @version 0.1
 *
 */
public class LoginScreen extends BrowserScreen implements ActionListener
{

	// Logger
	private static final Logger log = LoggerFactory.getLogger(LoginScreen.class.getName());
	
	// List of actions:
	public static final String ACTION_SUCCESS = "success";
	public static final String ACTION_LOGGED_IN = "loggedIn";
	public static final String ACTION_ERROR = "error";
	
	private ApplicationSettings settings;
	
	/**
	 * Create Login Screen.
	 * 
	 * @param settings Facebook Application settings.
	 * @param cookieManager cookie manager.
	 */
	public LoginScreen(ApplicationSettings settings, CookieManager cookieManager) {
		super(new StringBuffer()
			.append("http://m.facebook.com/tos.php?")
			.append("api_key=").append(settings.applicationKey).append('&')
			.append("v=1.0").append('&')
			.append("next=").append(settings.nextUrl)
			.toString(), cookieManager);
		this.settings = settings;
		
		addActionListener(this);
		
		log.debug("URL: " + getUrl());
	}
	
	/**
	 * Perform login.
	 * 
	 */
	public void login() {
		browse();
	}
	
	/**
	 * Action handler.
	 * 
	 * @param event the action event to handle.
	 */
	public void actioned(ActionEvent event) {
		if (event.getSource() == this) {
			if (event.getAction().equals(ACTION_SUCCESS)
					&& getUrl().startsWith(settings.nextUrl)) {
				String url = getUrl();
				log.debug("URL: " + url);
				int startIndex = url.indexOf("auth_token");
				
				if (startIndex > -1) {
					int stopIndex = url.length();
					
					if (url.indexOf('&', startIndex) > -1) {
						stopIndex = url.indexOf('&', startIndex);
					} else if (url.indexOf(';', startIndex) > -1) {
						stopIndex = url.indexOf(';', startIndex);
					}
					
					String authToken = url.substring(url.indexOf('=', startIndex)+1, stopIndex);
					fireActioned(ACTION_LOGGED_IN, authToken);
				}
			}
		}
	}
	
}
