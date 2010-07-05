package blackberry.ui;

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

import java.util.Enumeration;
import java.util.Vector;

import blackberry.action.ActionEvent;
import blackberry.action.ActionListener;
import net.rim.device.api.ui.container.MainScreen;

/**
 * AbstractScreen
 * 
 * An abstraction of screen navigated by action outcomes.
 * 
 * @author Eki Baskoro
 * @version 0.1
 *
 */
public abstract class AbstractScreen extends MainScreen
{

	/**
	 * The screen's action listener.
	 */
	protected Vector actionListeners = new Vector();
	
	/**
	 * Register an action listener.
	 * 
	 * @param actionListener the action listener object.
	 */
	public void addActionListener(ActionListener actionListener) {
		if (actionListener != null)
			actionListeners.addElement(actionListener);
	}
	
	/**
	 * Fire actioned event.
	 * 
	 * @param action the action name.
	 */
	protected void fireActioned(String action) {
		Enumeration listenersEnum = actionListeners.elements();
		
		while (listenersEnum.hasMoreElements()) {
			((ActionListener)listenersEnum.nextElement()).actioned(new ActionEvent(this, action));
		}
	}
	
	/**
	 * Fire actioned event given data.
	 * 
	 * @param action the action name.
	 * @param data the event data.
	 */
	protected void fireActioned(String action, Object data) {
		Enumeration listenersEnum = actionListeners.elements();
		
		while (listenersEnum.hasMoreElements()) {
			((ActionListener)listenersEnum.nextElement()).actioned(new ActionEvent(this, action, data));
		}
	}
	
}
