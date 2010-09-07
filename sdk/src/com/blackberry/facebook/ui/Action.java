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
package com.blackberry.facebook.ui;

public class Action {

	/**
	 * Event source.
	 */
	private Object source;

	/**
	 * Event action. eg. "success", "failure", "error"
	 */
	private String action;

	/**
	 * Event data.
	 */
	private Object data = null;

	/**
	 * Create an event given source, action name and data.
	 * 
	 * @param source
	 *            the source of event.
	 * @param action
	 *            the name of action.
	 * @param data
	 *            the data.
	 */
	public Action(Object source, String action, Object data) {
		this.source = source;
		this.action = action;
		this.data = data;
	}

	/**
	 * Obtain the source of event.
	 * 
	 * @return the source object.
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * Obtain the event action name.
	 * 
	 * @return the name.
	 */
	public String getAction() {
		return action;
	}

	/**
	 * Obtain the event data.
	 * 
	 * @return the data object.
	 */
	public Object getData() {
		return data;
	}

}
