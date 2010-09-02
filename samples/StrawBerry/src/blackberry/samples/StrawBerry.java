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
package blackberry.samples;

import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import blackberry.action.ActionEvent;
import blackberry.action.ActionListener;
import blackberry.facebook.ApplicationSettings;
import blackberry.facebook.ExtendedPermission;
import blackberry.facebook.FacebookContextImpl;
import blackberry.facebook.ui.LoginScreen;
import blackberry.facebook.ui.PermissionScreen;
import blackberry.net.CookieManager;
import blackberry.net.HttpConnectionFactory;
import blackberry.util.Logger;
import blackberry.util.LoggerFactory;
import facebook.FacebookContext;
import facebook.FacebookException;

/**
 * StrawBerry
 * 
 * Demonstrate the Facebook BlackBerry SDK.
 * 
 * @author Eki Baskoro
 * @version 0.1
 * 
 */
public class StrawBerry extends UiApplication implements ActionListener {

	// Application settings:
	private static final String REST_URL = "http://api.facebook.com/restserver.php"; // As per Facebook.
	private static final String GRAPH_URL = "https://graph.facebook.com"; // As per Facebook.
	private static final String NEXT_URL = "http://www.facebook.com/connect/login_success.html"; // Your successful URL.
	private static final String APPLICATION_KEY = "f21032d377681e02051e639830b4b678"; // Your Facebook Application Key. 
	private static final String APPLICATION_SECRET = "590906fcfea8e348589cf43f06192c2e"; // Your Facebook Application Secret.
	private static final long APPLICATION_ID = 317175255300L; // Your Facebook Application ID.

	private static PersistentObject store;
	private static ApplicationSettings settings;

	static {
		store = PersistentStore.getPersistentObject(0x200eab09899L);

		synchronized (store) {
			if (store.getContents() == null) {
				store.setContents(new ApplicationSettings(REST_URL, GRAPH_URL, NEXT_URL, APPLICATION_KEY, APPLICATION_SECRET, APPLICATION_ID));
				store.commit();
			}
		}

		settings = (ApplicationSettings) store.getContents();
	}

	// Logger
	private static final Logger log = LoggerFactory.getLogger(StrawBerry.class.getName());

	// HttpConnectionFactory
	private static final HttpConnectionFactory connFactory = new HttpConnectionFactory();

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

	/**
	 * Main
	 * 
	 * @param args
	 *            the application arguments
	 */
	public static void main(String[] args) {
		StrawBerry app = new StrawBerry();
		app.enterEventDispatcher();
	}

	/**
	 * Default constructor.
	 * 
	 */
	public StrawBerry() {
		log.debug("====== START =======");

		try {
			new FacebookContextImpl(settings);
		} catch (FacebookException e) {
			log.error(e.getMessage());
			System.exit(0);
		}

		if (!FacebookContext.getInstance().hasSession()) {
			loginScreen = new LoginScreen(settings, cookieManager);
			loginScreen.addActionListener(this);

			permissionScreen = new PermissionScreen(settings, cookieManager);
			permissionScreen.addActionListener(this);

			loginScreen.login();
			pushScreen(loginScreen);
		} else {
			homeScreen = new HomeScreen();
			homeScreen.addActionListener(this);
			pushScreen(homeScreen);

			getApplication().invokeLater(new Runnable() {

				public void run() {
					try {
						Dialog.inform("Hello " + FacebookContext.getInstance().getLoggedInUser().getFirstName() + "!");
					} catch (FacebookException e) {
						Dialog.alert(e.getMessage());
					}
				}

			});
		}
	}

	public static HttpConnectionFactory getHttpConnectionFactory() {
		return connFactory;
	}

	/**
	 * Handles action events.
	 * 
	 * @param event
	 *            the action event to handle.
	 */
	public void actioned(ActionEvent event) {
		if (event.getSource() == loginScreen) {
			if (event.getAction().equals(LoginScreen.ACTION_LOGGED_IN)) {
				try {
					popScreen(loginScreen);
				} catch (IllegalArgumentException e) {
				}

				try {
					FacebookContext.getInstance().getSession((String) event.getData());
					FacebookContext.getInstance().upgradeSession();

					permissionScreen.requestPermissions(new String[] { ExtendedPermission.OFFLINE_ACCESS, ExtendedPermission.PUBLISH_STREAM });
					pushScreen(permissionScreen);
				} catch (Exception e) {
					Dialog.alert("Error: " + e.getMessage());
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
					synchronized (store) {
						store.setContents(settings);
						store.commit();
					}

					if (homeScreen == null) {
						homeScreen = new HomeScreen();
						homeScreen.addActionListener(this);
					}

					pushScreen(homeScreen);
					Dialog.inform("Hello " + FacebookContext.getInstance().getLoggedInUser().getFirstName() + "!");
				} catch (Exception e) {
					Dialog.alert("Error: " + e.getMessage());
				}
			} else if (event.getAction().equals(PermissionScreen.ACTION_ERROR)) {
				Dialog.alert("Error: " + event.getData());
			}
		} else if (event.getSource() == homeScreen) {
			if (event.getAction().equals(UpdateStatusScreen.ACTION_ENTER)) {
				if (updateStatusScreen == null) {
					updateStatusScreen = new UpdateStatusScreen();
					updateStatusScreen.addActionListener(this);
				}

				pushScreen(updateStatusScreen);
			} else if (event.getAction().equals(RecentUpdatesScreen.ACTION_ENTER)) {
				if (recentUpdatesScreen == null) {
					recentUpdatesScreen = new RecentUpdatesScreen();
					recentUpdatesScreen.addActionListener(this);
				}

				recentUpdatesScreen.loadList();
				pushScreen(recentUpdatesScreen);
			} else if (event.getAction().equals(UploadPhotoScreen.ACTION_ENTER)) {
				if (uploadPhotoScreen == null) {
					uploadPhotoScreen = new UploadPhotoScreen();
					uploadPhotoScreen.addActionListener(this);
				}

				pushScreen(uploadPhotoScreen);
			} else if (event.getAction().equals(FriendsListScreen.ACTION_ENTER)) {
				if (friendsListScreen == null) {
					friendsListScreen = new FriendsListScreen();
					friendsListScreen.addActionListener(this);
				}

				friendsListScreen.loadList();
				pushScreen(friendsListScreen);
			} else if (event.getAction().equals(PokeFriendScreen.ACTION_ENTER)) {
				if (pokeFriendScreen == null) {
					pokeFriendScreen = new PokeFriendScreen();
					pokeFriendScreen.addActionListener(this);
				}

				//				pokeFriendScreen.loadList();
				pushScreen(pokeFriendScreen);
			} else if (event.getAction().equals(PostWallScreen.ACTION_ENTER)) {
				if (postWallScreen == null) {
					postWallScreen = new PostWallScreen();
					postWallScreen.addActionListener(this);
				}

				postWallScreen.loadList();
				pushScreen(postWallScreen);
			} else if (event.getAction().equals(SendMessageScreen.ACTION_ENTER)) {
				if (sendMessageScreen == null) {
					sendMessageScreen = new SendMessageScreen();
					sendMessageScreen.addActionListener(this);
				}

				//sendMessageScreen.loadList();
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