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
package samples.StrawBerry;

import com.blackberry.facebook.FacebookContext;
import com.blackberry.facebook.ui.FacebookScreen;

import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;

final class SendMessageScreen extends FacebookScreen {

	// List of actions:
	static final String ACTION_ENTER = "sendMessage";
	static final String ACTION_SUCCESS = "messageSent";
	static final String ACTION_ERROR = "error";

	// List of labels:
	private static final String LABEL_TITLE = "Send Message";

	/**
	 * Default constructor.
	 * 
	 */
	SendMessageScreen(FacebookContext pfbc) {
		super(pfbc);
		LabelField titleLabel = new LabelField(LABEL_TITLE, LabelField.ELLIPSIS | LabelField.USE_ALL_WIDTH);
		setTitle(titleLabel);
		Dialog.alert("Not implemented yet.");
	}

}
