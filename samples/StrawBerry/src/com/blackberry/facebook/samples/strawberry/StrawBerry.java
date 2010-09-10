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
package com.blackberry.facebook.samples.strawberry;

import com.blackberry.facebook.ApplicationSettings;
import com.blackberry.facebook.ExtendedPermission;
import com.blackberry.facebook.FacebookContext;
import com.blackberry.facebook.FacebookException;
import com.blackberry.facebook.ui.Action;
import com.blackberry.facebook.ui.ActionListener;
import com.blackberry.facebook.ui.LoginScreen;
import com.blackberry.facebook.ui.PermissionScreen;
import com.blackberry.util.log.Logger;
import com.blackberry.util.log.LoggerFactory;
import com.blackberry.util.network.CookieManager;
import com.blackberry.util.network.HttpConnectionFactory;

import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;

public class StrawBerry extends UiApplication implements ActionListener {

	// Constants
	private final String REST_URL = "http://api.facebook.com/restserver.php"; // As per Facebook.
	private final String GRAPH_URL = "https://graph.facebook.com"; // As per Facebook.
	private final String NEXT_URL = "http://www.facebook.com/connect/login_success.html"; // Your successful URL.
	private final String APPLICATION_KEY = "f21032d377681e02051e639830b4b678"; // Your Facebook Application Key. 
	private final String APPLICATION_SECRET = "590906fcfea8e348589cf43f06192c2e"; // Your Facebook Application Secret.
	private final String APPLICATION_ID = "317175255300"; // Your Facebook Application ID.
	private final long persistentObjectId = 0x854d1b7fa43e3577L; // com.blackberry.facebook.samples.strawberry.StrawBerry

	private PersistentObject store;
	private HttpConnectionFactory connFactory;

	private CookieManager cookieManager = new CookieManager();
	private LoginScreen loginScreen;
	private PermissionScreen permissionScreen;

	private HomeScreen homeScreen;
	private UpdateStatusScreen updateStatusScreen;
	private RecentUpdatesScreen recentUpdatesScreen;
	private UploadPhotoScreen uploadPhotoScreen;
	private FriendsListScreen friendsListScreen;
	private PokeFriendScreen pokeFriendScreen;
	private PostWallScreen postWallScreen;
	private SendMessageScreen sendMessageScreen;

	private FacebookContext fbc;

	public static void main(String[] args) {
		StrawBerry app = new StrawBerry();
		app.enterEventDispatcher();
	}

	public StrawBerry() {

		checkPermissions();
		init();

		if ((fbc != null) && fbc.hasSession()) {

			homeScreen = new HomeScreen(fbc);
			homeScreen.addActionListener(this);
			pushScreen(homeScreen);

			getApplication().invokeLater(new Runnable() {
				public void run() {
					try {
						Dialog.inform("Hello " + fbc.getLoggedInUser().getFirstName() + "!");
					} catch (FacebookException e) {
						e.printStackTrace();
						Dialog.alert(e.getMessage());
					}
				}
			});

		} else {

			loginScreen = new LoginScreen(fbc, cookieManager);
			loginScreen.addActionListener(this);
			loginScreen.login();
			pushScreen(loginScreen);
		}

		testLog();

	}

	private void checkPermissions() {

		ApplicationPermissionsManager apm = ApplicationPermissionsManager.getInstance();
		ApplicationPermissions original = apm.getApplicationPermissions();

		if ((original.getPermission(ApplicationPermissions.PERMISSION_INPUT_SIMULATION) == ApplicationPermissions.VALUE_ALLOW) && (original.getPermission(ApplicationPermissions.PERMISSION_DEVICE_SETTINGS) == ApplicationPermissions.VALUE_ALLOW) && (original.getPermission(ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION) == ApplicationPermissions.VALUE_ALLOW) && (original.getPermission(ApplicationPermissions.PERMISSION_INTERNET) == ApplicationPermissions.VALUE_ALLOW) && (original.getPermission(ApplicationPermissions.PERMISSION_SERVER_NETWORK) == ApplicationPermissions.VALUE_ALLOW) && (original.getPermission(ApplicationPermissions.PERMISSION_EMAIL) == ApplicationPermissions.VALUE_ALLOW)) {
			return;
		}

		ApplicationPermissions permRequest = new ApplicationPermissions();
		permRequest.addPermission(ApplicationPermissions.PERMISSION_INPUT_SIMULATION);
		permRequest.addPermission(ApplicationPermissions.PERMISSION_DEVICE_SETTINGS);
		permRequest.addPermission(ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION);
		permRequest.addPermission(ApplicationPermissions.PERMISSION_INTERNET);
		permRequest.addPermission(ApplicationPermissions.PERMISSION_SERVER_NETWORK);
		permRequest.addPermission(ApplicationPermissions.PERMISSION_EMAIL);

		boolean acceptance = ApplicationPermissionsManager.getInstance().invokePermissionsRequest(permRequest);

		if (acceptance) {
			// User has accepted all of the permissions.
			return;
		} else {
		}
	}

	private void init() {

		connFactory = new HttpConnectionFactory();
		store = PersistentStore.getPersistentObject(persistentObjectId);
		synchronized (store) {
			if (store.getContents() == null) {
				store.setContents(new ApplicationSettings(REST_URL, GRAPH_URL, NEXT_URL, APPLICATION_KEY, APPLICATION_SECRET, APPLICATION_ID));
				store.commit();
			}
		}

		try {
			fbc = new FacebookContext((ApplicationSettings) store.getContents(), connFactory);

		} catch (Throwable t) {
			t.printStackTrace();
			exit();
		}
	}

	private void testLog() {

		Logger def = LoggerFactory.getLogger();
		Logger log = LoggerFactory.getLogger("TEXT_FILE");
		Logger rlog = LoggerFactory.getLogger("RICH_TEXT_FILE");

		def.debug("************************** StrawBerry.def.xxx() **********************************");
		def.debug("This is just a testing log message.");
		def.info("This is just a testing log message.");
		def.warn("This is just a testing log message.");
		def.error("This is just a testing log message.");
		def.fatal("This is just a testing log message.");
		def.debug("************************** /StrawBerry.def.xxx() **********************************");

		log.debug("************************** StrawBerry.log.xxx() **********************************");
		log.debug("This is just a testing log message.");
		log.info("This is just a testing log message.");
		log.warn("This is just a testing log message.");
		log.error("This is just a testing log message.");
		log.fatal("This is just a testing log message.");
		log.debug("************************** /StrawBerry.log.xxx() **********************************");

		rlog.debug("************************** StrawBerry.rlog.xxx() **********************************");
		rlog.debug("This is just a testing log message.");
		rlog.info("This is just a testing log message.");
		rlog.warn("This is just a testing log message.");
		rlog.error("This is just a testing log message.");
		rlog.fatal("This is just a testing log message.");
		rlog.debug("************************** /StrawBerry.rlog.xxx() **********************************");
	}

	private void saveSettings(ApplicationSettings settings) {
		synchronized (store) {
			store.setContents(settings);
			store.commit();
		}
	}

	public void logoutAndExit() {
		saveSettings(null);
		exit();
	}

	public void saveAndExit() {
		saveSettings(fbc.getApplicationSettings());
		exit();
	}

	private void exit() {
		LoggerFactory.clear();
		System.exit(0);
	}

	public void onAction(Action event) {

		if (event.getSource() == loginScreen) {
			if (event.getAction().equals(LoginScreen.ACTION_LOGGED_IN)) {
				try {
					popScreen(loginScreen);
				} catch (IllegalArgumentException e) {
				}

				try {
					fbc.getSession((String) event.getData());
					fbc.upgradeSession();

					permissionScreen = new PermissionScreen(fbc, cookieManager);
					permissionScreen.addActionListener(this);
					permissionScreen.requestPermissions(new String[] { ExtendedPermission.OFFLINE_ACCESS, ExtendedPermission.PUBLISH_STREAM });
					pushScreen(permissionScreen);

				} catch (Throwable t) {
					t.printStackTrace();
					Dialog.alert("Error: " + t.getMessage());
				}

			} else if (event.getAction().equals(LoginScreen.ACTION_ERROR)) {
				Dialog.alert("Error: " + event.getData());
			}

		} else if (event.getSource() == permissionScreen) {
			if (event.getAction().equals(PermissionScreen.ACTION_GRANTED)) {
				try {
					popScreen(permissionScreen);
				} catch (IllegalArgumentException e) {
				}

				try {
					if (homeScreen == null) {
						homeScreen = new HomeScreen(fbc);
						homeScreen.addActionListener(this);
					}
					pushScreen(homeScreen);
					Dialog.inform("Hello " + fbc.getLoggedInUser().getFirstName() + "!");

				} catch (Exception e) {
					e.printStackTrace();
					Dialog.alert("Error: " + e.getMessage());
				}
			} else if (event.getAction().equals(PermissionScreen.ACTION_ERROR)) {
				Dialog.alert("Error: " + event.getData());
			}

		} else if (event.getSource() == homeScreen) {
			if (event.getAction().equals(UpdateStatusScreen.ACTION_ENTER)) {
				if (updateStatusScreen == null) {
					updateStatusScreen = new UpdateStatusScreen(fbc);
					updateStatusScreen.addActionListener(this);
				}
				pushScreen(updateStatusScreen);

			} else if (event.getAction().equals(RecentUpdatesScreen.ACTION_ENTER)) {
				if (recentUpdatesScreen == null) {
					recentUpdatesScreen = new RecentUpdatesScreen(fbc);
					recentUpdatesScreen.addActionListener(this);
				}
				recentUpdatesScreen.loadList();
				pushScreen(recentUpdatesScreen);

			} else if (event.getAction().equals(UploadPhotoScreen.ACTION_ENTER)) {
				if (uploadPhotoScreen == null) {
					uploadPhotoScreen = new UploadPhotoScreen(fbc);
					uploadPhotoScreen.addActionListener(this);
				}
				pushScreen(uploadPhotoScreen);

			} else if (event.getAction().equals(FriendsListScreen.ACTION_ENTER)) {
				if (friendsListScreen == null) {
					friendsListScreen = new FriendsListScreen(fbc);
					friendsListScreen.addActionListener(this);
				}
				friendsListScreen.loadList();
				pushScreen(friendsListScreen);

			} else if (event.getAction().equals(PokeFriendScreen.ACTION_ENTER)) {
				if (pokeFriendScreen == null) {
					pokeFriendScreen = new PokeFriendScreen(fbc);
					pokeFriendScreen.addActionListener(this);
				}
				pushScreen(pokeFriendScreen);

			} else if (event.getAction().equals(PostWallScreen.ACTION_ENTER)) {
				if (postWallScreen == null) {
					postWallScreen = new PostWallScreen(fbc);
					postWallScreen.addActionListener(this);
				}
				postWallScreen.loadList();
				pushScreen(postWallScreen);

			} else if (event.getAction().equals(SendMessageScreen.ACTION_ENTER)) {
				if (sendMessageScreen == null) {
					sendMessageScreen = new SendMessageScreen(fbc);
					sendMessageScreen.addActionListener(this);
				}
				pushScreen(sendMessageScreen);
			}

		} else if (event.getSource() == updateStatusScreen) {
			if (event.getAction().equals(UpdateStatusScreen.ACTION_SUCCESS)) {
				Dialog.inform("Status updated");

				try {
					popScreen(updateStatusScreen);
				} catch (IllegalArgumentException e) {
				}

			} else if (event.getAction().equals(UpdateStatusScreen.ACTION_SUCCESS)) {
				Dialog.alert("Error: " + event.getData());
			}

		} else if (event.getSource() == recentUpdatesScreen) {
			if (event.getAction().equals(RecentUpdatesScreen.ACTION_SUCCESS)) {
				try {
					popScreen(recentUpdatesScreen);
				} catch (IllegalArgumentException e) {
				}
			} else if (event.getAction().equals(RecentUpdatesScreen.ACTION_ERROR)) {
				Dialog.alert("Error: " + event.getData());
			}
		} else if (event.getSource() == uploadPhotoScreen) {
			if (event.getAction().equals(UploadPhotoScreen.ACTION_SUCCESS)) {
				try {
					popScreen(uploadPhotoScreen);
				} catch (IllegalArgumentException e) {
				}
			} else if (event.getAction().equals(UploadPhotoScreen.ACTION_ERROR)) {
				Dialog.alert("Error: " + event.getData());
			}
		} else if (event.getSource() == friendsListScreen) {
			if (event.getAction().equals(FriendsListScreen.ACTION_SUCCESS)) {
				try {
					popScreen(friendsListScreen);
				} catch (IllegalArgumentException e) {
				}
			} else if (event.getAction().equals(FriendsListScreen.ACTION_ERROR)) {
				Dialog.alert("Error: " + event.getData());
			}
		} else if (event.getSource() == pokeFriendScreen) {
			if (event.getAction().equals(PokeFriendScreen.ACTION_SUCCESS)) {
				try {
					popScreen(pokeFriendScreen);
				} catch (IllegalArgumentException e) {
				}
			} else if (event.getAction().equals(PokeFriendScreen.ACTION_ERROR)) {
				Dialog.alert("Error: " + event.getData());
			}
		} else if (event.getSource() == postWallScreen) {
			if (event.getAction().equals(PostWallScreen.ACTION_SUCCESS)) {
				Dialog.inform("Wall posted");

				try {
					popScreen(postWallScreen);
				} catch (IllegalArgumentException e) {
				}
			} else if (event.getAction().equals(PostWallScreen.ACTION_ERROR)) {
				Dialog.alert("Error: " + event.getData());
			}
		} else if (event.getSource() == sendMessageScreen) {
			if (event.getAction().equals(SendMessageScreen.ACTION_SUCCESS)) {
				try {
					popScreen(sendMessageScreen);
				} catch (IllegalArgumentException e) {
				}
			} else if (event.getAction().equals(SendMessageScreen.ACTION_ERROR)) {
				Dialog.alert("Error: " + event.getData());
			}
		}
	}

}