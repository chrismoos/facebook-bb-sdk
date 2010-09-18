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

import com.blackberry.facebook.FacebookContext;
import com.blackberry.facebook.User;
import com.blackberry.facebook.ui.FacebookScreen;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.component.SeparatorField;

final class PostWallScreen extends FacebookScreen {

	// List of actions:
	static final String ACTION_ENTER = "postWall";
	static final String ACTION_SUCCESS = "posted";
	static final String ACTION_ERROR = "error";

	// List of labels:
	private static final String LABEL_TITLE = "Post To Wall";
	private static final String LABEL_USERS = "Post wall to";
	private static final String LABEL_NAME = "Title:";
	private static final String LABEL_LINK = "Link:";
	private static final String LABEL_CAPTION = "Caption:";
	private static final String LABEL_CONTENT = "Content:";
	private static final String LABEL_POST = "Post";

	private User[] users = null;
	private ObjectChoiceField objectChoiceField;
	private EditField titleEditField;
	private EditField hrefEditField;
	private EditField captionEditField;
	private EditField descriptionEditField;
	private ButtonField buttonField;

	/**
	 * Default constructor.
	 * 
	 */
	PostWallScreen(FacebookContext pfbc) {
		super(pfbc);
		setTitle(new LabelField(LABEL_TITLE, LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH));

		objectChoiceField = new ObjectChoiceField();
		objectChoiceField.setLabel(LABEL_USERS);
		add(objectChoiceField);

		add(new SeparatorField(SeparatorField.LINE_HORIZONTAL));

		titleEditField = new EditField(LABEL_NAME, "", 80, LabelField.USE_ALL_WIDTH);
		add(titleEditField);

		hrefEditField = new EditField(LABEL_LINK, "", 80, LabelField.USE_ALL_WIDTH);
		add(hrefEditField);

		captionEditField = new EditField(LABEL_CAPTION, "", 80, LabelField.USE_ALL_WIDTH);
		add(captionEditField);

		descriptionEditField = new EditField(LABEL_CONTENT, "", 255, LabelField.USE_ALL_WIDTH);
		add(descriptionEditField);

		buttonField = new ButtonField(LABEL_POST);
		buttonField.setChangeListener(new FieldChangeListener() {

			public void fieldChanged(Field field, int context) {
				if (users == null) {
					return;
				}

				try {
					users[objectChoiceField.getSelectedIndex()].publishStream(descriptionEditField.getText(), hrefEditField.getText(), titleEditField.getText(), descriptionEditField.getText(), captionEditField.getText());
					fireAction(ACTION_SUCCESS);
				} catch (Exception e) {
					fireAction(ACTION_ERROR, e.getMessage());
				}
			}

		});
		add(buttonField);
	}

	/**
	 * Load list of users comprising of friends and self.
	 * 
	 */
	void loadList() {
		try {
			User[] friends = fbc.getLoggedInUser().getFriends();

			if (friends == null) {
				users = new User[1];
			} else {
				users = new User[friends.length + 1];
			}

			users[0] = fbc.getLoggedInUser();

			for (int i = 1; i < (friends.length + 1); i++) {
				users[i] = friends[i - 1];
			}

			objectChoiceField.setChoices(users);
		} catch (Exception e) {
			fireAction(ACTION_ERROR, e.getMessage());
		}
	}

}
