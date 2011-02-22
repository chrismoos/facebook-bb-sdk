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
package samples.strawberry;


import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.system.PersistentObject;
import net.rim.device.api.system.PersistentStore;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.sf.blackberry.facebook.FacebookContext;
import net.sf.blackberry.facebook.ui.Action;
import net.sf.blackberry.facebook.ui.ActionListener;
import net.sf.blackberry.facebook.ui.LoginScreen;
import net.sf.blackberry.facebook.ui.LogoutScreen;
import net.sf.blackberry.util.log.AppenderFactory;

public class StrawBerry extends UiApplication implements ActionListener {

	// Constants
	public final static String NEXT_URL = "http://www.facebook.com/connect/login_success.html";
	public final static String APPLICATION_ID = "153555168010272";
	private final static long persistentObjectId = 0x854d1b7fa43e3577L;

	private PersistentObject store;

	private LoginScreen loginScreen;
	private LogoutScreen logoutScreen;
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
		new StrawBerry().enterEventDispatcher();
	}

	public StrawBerry() {

		checkPermissions();
		init();

		if ((fbc != null) && fbc.hasValidAccessToken()) {

			homeScreen = new HomeScreen(fbc);
			homeScreen.addActionListener(this);
			pushScreen(homeScreen);

		} else {

			loginScreen = new LoginScreen(fbc);
			loginScreen.addActionListener(this);
			pushScreen(loginScreen);
		}

	}

	private void init() {
		store = PersistentStore.getPersistentObject(persistentObjectId);
		synchronized (store) {
			if (store.getContents() == null) {
				store.setContents(new FacebookContext(NEXT_URL, APPLICATION_ID));
				store.commit();
			}
		}
		fbc = (FacebookContext) store.getContents();
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

	private void saveContext(FacebookContext pfbc) {
		synchronized (store) {
			store.setContents(pfbc);
			store.commit();
		}
	}

	public void logoutAndExit() {
		saveContext(null);
		logoutScreen = new LogoutScreen(fbc);
		logoutScreen.addActionListener(this);
	}

	public void saveAndExit() {
		saveContext(fbc);
		exit();
	}

	private void exit() {
		AppenderFactory.close();
		System.exit(0);
	}

	public void onAction(Action event) {

		if (event.getSource() == loginScreen) {
			if (event.getAction().equals(LoginScreen.ACTION_LOGGED_IN)) {
				try {
					fbc.setAccessToken((String) event.getData());
					try {
						if (homeScreen == null) {
							homeScreen = new HomeScreen(fbc);
							homeScreen.addActionListener(this);
						}
						pushScreen(homeScreen);

					} catch (Exception e) {
						e.printStackTrace();
						Dialog.alert("Error: " + e.getMessage());
					}

				} catch (Throwable t) {
					t.printStackTrace();
					Dialog.alert("Error: " + t.getMessage());
				}

			} else if (event.getAction().equals(LoginScreen.ACTION_ERROR)) {
				Dialog.alert("Error: " + event.getData());
			}

		} else if (event.getSource() == logoutScreen) {
			if (event.getAction().equals(LogoutScreen.ACTION_LOGGED_OUT)) {
				exit();
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