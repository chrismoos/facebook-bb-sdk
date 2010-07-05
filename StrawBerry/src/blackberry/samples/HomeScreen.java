package blackberry.samples;

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

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import blackberry.ui.AbstractScreen;

/**
 * HomeScreen
 * 
 * @author Eki Baskoro
 * @version 0.1
 *
 */
final class HomeScreen extends AbstractScreen
{

	// List of labels:
	private static final String LABEL_TITLE = "StrawBerry for BlackBery";
	private static final String LABEL_UPDATE_STATUS = "Update Status";
	private static final String LABEL_RECENT_UPDATES = "Recent Updates";
	private static final String LABEL_UPLOAD_PHOTO = "Upload Photos";
	private static final String LABEL_FRIENDS_LIST = "Friends List";
	private static final String LABEL_POKE_FRIEND = "Poke a Friend";
	private static final String LABEL_WRITE_WALL = "Write on a Wall";
	private static final String LABEL_SEND_MESSAGE = "Send a Message";
	
	private ButtonField updateStatusButton;
	private ButtonField recentUpdatesButton;
	private ButtonField uploadPhotoButton;
	private ButtonField friendListButton;
	private ButtonField pokeButton;
	private ButtonField wallButton;
	private ButtonField sendMessageButton;
	
	/**
	 * Default constructor.
	 * 
	 */
	HomeScreen() {
		LabelField titleLabel = new LabelField(LABEL_TITLE, LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH);
		setTitle(titleLabel);
		
		HorizontalFieldManager topManager = new HorizontalFieldManager(Manager.HORIZONTAL_SCROLL);
		add(topManager);
		
		updateStatusButton = new ButtonField(LABEL_UPDATE_STATUS);
		updateStatusButton.setChangeListener(new FieldChangeListener()
		{
			
			public void fieldChanged(Field field, int context) {
				fireActioned(UpdateStatusScreen.ACTION_ENTER);
			}
			
		});
		topManager.add(updateStatusButton);
		
		recentUpdatesButton = new ButtonField(LABEL_RECENT_UPDATES);
		recentUpdatesButton.setChangeListener(new FieldChangeListener()
		{
			
			public void fieldChanged(Field field, int context) {
				fireActioned(RecentUpdatesScreen.ACTION_ENTER);
			}
			
		});
		topManager.add(recentUpdatesButton);
		
		uploadPhotoButton = new ButtonField(LABEL_UPLOAD_PHOTO);
		uploadPhotoButton.setChangeListener(new FieldChangeListener()
		{
			
			public void fieldChanged(Field field, int context) {
				fireActioned(UploadPhotoScreen.ACTION_ENTER);
			}
			
		});
		topManager.add(uploadPhotoButton);
		
		friendListButton = new ButtonField(LABEL_FRIENDS_LIST);
		friendListButton.setChangeListener(new FieldChangeListener()
		{
			
			public void fieldChanged(Field field, int context) {
				fireActioned(FriendsListScreen.ACTION_ENTER);
			}
			
		});
		topManager.add(friendListButton);
		
		pokeButton = new ButtonField(LABEL_POKE_FRIEND);
		pokeButton.setChangeListener(new FieldChangeListener()
		{
			
			public void fieldChanged(Field field, int context) {
				fireActioned(PokeFriendScreen.ACTION_ENTER);
			}
			
		});
		topManager.add(pokeButton);
		
		wallButton = new ButtonField(LABEL_WRITE_WALL);
		wallButton.setChangeListener(new FieldChangeListener()
		{
			
			public void fieldChanged(Field field, int context) {
				fireActioned(PostWallScreen.ACTION_ENTER);
			}
			
		});
		topManager.add(wallButton);
		
		sendMessageButton = new ButtonField(LABEL_SEND_MESSAGE);
		sendMessageButton.setChangeListener(new FieldChangeListener()
		{
			
			public void fieldChanged(Field field, int context) {
				fireActioned(SendMessageScreen.ACTION_ENTER);
			}
			
		});
		topManager.add(sendMessageButton);
		
		add(new SeparatorField());
	}
	
	protected void makeMenu(Menu menu, int instance) {
        menu.deleteAll();
    }
    
    public boolean onClose() {
		if (Dialog.ask(Dialog.D_YES_NO, "Exit now?", Dialog.NO) == Dialog.YES) {
			System.exit(0);
			
			return true;
		} else {
			return false;
		}
	}
}
