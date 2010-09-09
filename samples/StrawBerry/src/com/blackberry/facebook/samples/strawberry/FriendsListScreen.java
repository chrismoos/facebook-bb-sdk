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

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import com.blackberry.facebook.FacebookContext;
import com.blackberry.facebook.User;
import com.blackberry.facebook.ui.FacebookScreen;
import com.blackberry.util.log.RichTextLoggable;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Display;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ListField;
import net.rim.device.api.ui.component.ListFieldCallback;

final class FriendsListScreen extends FacebookScreen implements RichTextLoggable {

	// List of actions:
	static final String ACTION_ENTER = "friendsList";
	static final String ACTION_SUCCESS = "success";
	static final String ACTION_ERROR = "error";

	// List of labels:
	private static final String LABEL_TITLE = "Friends List";

	private ListField listField;
	private StreamListCallback streamListCallback = new StreamListCallback();

	/**
	 * Default constructor.
	 * 
	 */
	FriendsListScreen(FacebookContext pfbc) {
		super(pfbc);
		setTitle(new LabelField(LABEL_TITLE, LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));

		listField = new ListField();
		listField.setRowHeight(50);
		listField.setCallback(streamListCallback);
		add(listField);
	}

	/**
	 * Load the list of friends.
	 * 
	 */
	void loadList() {
		while (listField.getSize() > 0) {
			listField.delete(0);
		}

		try {
			//			User[] users = Facebook.getInstance().getLoggedInUser().getFriends();
			User[] users = fbc.getLoggedInUser().getFriends();
			streamListCallback.clear();

			for (int i = 0; i < users.length; i++) {
				listField.insert(listField.getSize());
				streamListCallback.add(users[i]);
			}

			streamListCallback.loadBitmaps();

		} catch (Exception e) {
			fireAction(ACTION_ERROR, e.getMessage());
		}
	}

	public boolean onClose() {
		streamListCallback.stopLoading();
		return super.onClose();
	}

	private class StreamListCallback implements ListFieldCallback {

		private Vector friends = new Vector();
		private Hashtable pictureBitmaps = new Hashtable();
		private boolean runThread = false;

		public StreamListCallback() {
		}

		public void clear() {
			friends.removeAllElements();
		}

		public void add(User user) {
			friends.addElement(user);
		}

		public void insert(User user, int index) {
			friends.insertElementAt(user, index);
		}

		public void loadBitmaps() {
			startLoading();
			(new BitmapThread()).start();
		}

		public void startLoading() {
			runThread = true;
		}

		public void stopLoading() {
			runThread = false;
		}

		public void drawListRow(ListField listField, Graphics g, int index, int y, int width) {
			if (index < friends.size()) {
				int height = listField.getRowHeight();
				User user = (User) friends.elementAt(index);
				Bitmap bitmap = getBitmap(user.getId());

				if (bitmap != null) {
					g.drawBitmap(0, y + ((height - Math.min(bitmap.getHeight(), height)) / 2), 50, height, bitmap, 0, 0);
				}

				Font font = Font.getDefault();
				font.derive(Font.BOLD);
				g.setFont(font);
				g.drawText(user.getFullName(), 52, y, 0, width - 52);
				g.drawText(/* user.getLikesCount() */" ", 52, y + (height / 2), DrawStyle.ELLIPSIS, width - 52);
				g.drawLine(0, y + height - 1, width, y + height - 1);
			}
		}

		public Object get(ListField listField, int index) {
			if (index < friends.size()) {
				return friends.elementAt(index);
			}

			return null;
		}

		public int getPreferredWidth(ListField listField) {
			return Display.getWidth();
		}

		public int indexOfList(ListField listField, String prefix, int start) {
			for (int i = start; i < friends.size(); i++) {
				User user = (User) friends.elementAt(i);

				if (user.getFullName().indexOf(prefix) > -1) {
					return i;
				}
			}

			return -1;
		}

		private Bitmap getBitmap(String id) {
			return (Bitmap) pictureBitmaps.get(id);
		}

		private class BitmapThread extends Thread {

			public void run() {
				Enumeration usersEnum = friends.elements();

				while (runThread && usersEnum.hasMoreElements()) {
					User user = (User) usersEnum.nextElement();
					String id = user.getId();
					String url = "http://graph.facebook.com/" + id + "/picture?type=square";

					try {
						StringBuffer response = (fbc.getHttpClient()).doGet(url);
						byte[] data = response.toString().getBytes();

						if (data.length > 0) {
							Bitmap bitmap = Bitmap.createBitmapFromBytes(data, 0, data.length, 1);
							pictureBitmaps.put(id, bitmap);
							listField.invalidate();
						}
					} catch (Exception e) {
						log.error(e.getMessage());
					}
				}
			}

		}

	}

}
