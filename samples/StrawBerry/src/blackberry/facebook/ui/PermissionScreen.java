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
package blackberry.facebook.ui;

import blackberry.action.ActionEvent;
import blackberry.action.ActionListener;
import blackberry.facebook.ApplicationSettings;
import blackberry.net.CookieManager;
import blackberry.ui.BrowserScreen;
import blackberry.util.Logger;
import blackberry.util.LoggerFactory;

/**
 * PermissionScreen
 * 
 * @author Eki Baskoro
 * @version 0.1
 * 
 */
public class PermissionScreen extends BrowserScreen implements ActionListener {

	// Logger
	private static final Logger log = LoggerFactory.getLogger(PermissionScreen.class.getName());

	// List of actions:
	public static final String ACTION_SUCCESS = "success";
	public static final String ACTION_GRANTED = "granted";
	public static final String ACTION_ERROR = "error";

	private ApplicationSettings settings;
	private String baseUrl = null;

	/**
	 * Create Permission Screen.
	 * 
	 * @param settings
	 *            Facebook Application settings.
	 * @param cookieManager
	 *            cookie manager.
	 */
	public PermissionScreen(ApplicationSettings settings, CookieManager cookieManager) {
		super(null, cookieManager);
		this.settings = settings;

		StringBuffer urlBuffer = new StringBuffer().append(settings.graphUrl).append("/oauth/authorize?").append("client_id=").append(String.valueOf(settings.applicationId)).append('&').append("redirect_uri=").append(settings.nextUrl).append('&').append("type=user_agent").append('&').append("display=wap").append('&').append("scope=");
		baseUrl = urlBuffer.toString();

		addActionListener(this);
	}

	/**
	 * Perform permissions request.
	 * 
	 * @param permissions
	 *            array of extended permissions requested.
	 */
	public void requestPermissions(String[] permissions) {
		StringBuffer urlBuffer = new StringBuffer(baseUrl);

		for (int i = 0; i < permissions.length; i++) {
			urlBuffer.append(permissions[i]);

			if (i < permissions.length - 1) {
				urlBuffer.append(',');
			}
		}

		setUrl(urlBuffer.toString());
		browse();
	}

	/**
	 * Action handler.
	 * 
	 * @param event
	 *            the action event to handle.
	 */
	public void actioned(ActionEvent event) {
		if (event.getSource() == this) {
			if (event.getAction().equals(ACTION_SUCCESS) && getUrl().startsWith(settings.nextUrl)) {
				String url = getUrl();
				int startIndex = url.indexOf("access_token");

				if (startIndex > -1) {
					int stopIndex = url.length();

					if (url.indexOf('&', startIndex) > -1) {
						stopIndex = url.indexOf('&', startIndex);
					} else if (url.indexOf(';', startIndex) > -1) {
						stopIndex = url.indexOf(';', startIndex);
					}

					String accessToken = url.substring(url.indexOf('=', startIndex) + 1, stopIndex);
					fireActioned(ACTION_GRANTED, accessToken);
				}
			}
		}
	}

}
