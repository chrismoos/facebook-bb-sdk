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
package net.sf.blackberry.facebook.ui;

import org.w3c.dom.Document;

import net.rim.device.api.browser.field2.BrowserField;
import net.rim.device.api.browser.field2.BrowserFieldListener;
import net.rim.device.api.system.Application;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.Ui;
import net.rim.device.api.ui.UiEngine;
import net.rim.device.api.ui.component.Dialog;
import net.sf.blackberry.facebook.FacebookContext;
import net.sf.blackberry.util.log.Logger;


public class LogoutScreen extends BrowserScreen implements ActionListener {

	public static final String ACTION_SUCCESS = "success";
	public static final String ACTION_LOGGED_OUT = "loggedOut";
	public static final String ACTION_ERROR = "error";

	protected Logger log = Logger.getLogger(getClass());
	protected String nextUrl;

	public LogoutScreen(FacebookContext pfbc) {

		super("http://m.facebook.com/logout.php?confirm=1&next=" + pfbc.getNextUrl());

		nextUrl = pfbc.getNextUrl().trim();
		bf.addListener(new MyBrowserFieldListener());

		addActionListener(this);
		browse();
	}

	public boolean checkLogoutStatusFromUrl(String url) {
		if ((url == null) || !url.trim().startsWith(nextUrl.trim())) {
			return false;
		} else {
			fireAction(ACTION_SUCCESS, null);
			return true;
		}
	}

	public void onAction(Action event) {
		if (event.getSource() == this) {
			if (event.getAction().equals(ACTION_SUCCESS)) {
				log.info("Logged out successfully.");
				fireAction(ACTION_LOGGED_OUT, null);
			}
		}
	}

	public class MyBrowserFieldListener extends BrowserFieldListener {

		public void documentLoaded(final BrowserField browserField, Document document) throws Exception {
			if (!checkLogoutStatusFromUrl(document.getDocumentURI())) {
				log.debug("Logout status not found.");
			} else {
				log.debug("Logout status found !!!");
			}
		}

		public void documentAborted(final BrowserField browserField, Document document) throws Exception {
		}

		public void documentCreated(final BrowserField browserField, Document document) throws Exception {
		}

		public void documentError(final BrowserField browserField, Document document) throws Exception {
		}

		public void documentUnloading(final BrowserField browserField, Document document) throws Exception {
		}

		public void downloadProgress(final BrowserField browserField, Document document) throws Exception {
		}

		public void showDialog(String msg) {
			synchronized (Application.getEventLock()) {
				Ui.getUiEngine().pushGlobalScreen(new Dialog(Dialog.D_OK, msg, Dialog.OK, Bitmap.getPredefinedBitmap(Bitmap.EXCLAMATION), Manager.VERTICAL_SCROLL), 1, UiEngine.GLOBAL_QUEUE);
			}
		}

	}

}
