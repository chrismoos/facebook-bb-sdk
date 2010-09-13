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
package com.blackberry.util.log;

import net.rim.device.api.util.StringUtilities;

public class EventLogAppender extends AbstractAppender {

	protected long guid;

	public EventLogAppender(String pName, String pType, String pDestination) {
		super(pName, pType, pDestination);
		guid = StringUtilities.stringHashToLong(pDestination);
		net.rim.device.api.system.EventLogger.register(guid, pDestination, net.rim.device.api.system.EventLogger.VIEWER_STRING);
	}

	public void close() {
	}

	public void debug(String message) {
		if ((message != null) && !message.equals("")) {
			net.rim.device.api.system.EventLogger.logEvent(guid, message.getBytes(), net.rim.device.api.system.EventLogger.DEBUG_INFO);
		}
	}

	public void info(String message) {
		if ((message != null) && !message.equals("")) {
			net.rim.device.api.system.EventLogger.logEvent(guid, message.getBytes(), net.rim.device.api.system.EventLogger.INFORMATION);
		}
	}

	public void warn(String message) {
		if ((message != null) && !message.equals("")) {
			net.rim.device.api.system.EventLogger.logEvent(guid, message.getBytes(), net.rim.device.api.system.EventLogger.WARNING);
		}
	}

	public void error(String message) {
		if ((message != null) && !message.equals("")) {
			net.rim.device.api.system.EventLogger.logEvent(guid, message.getBytes(), net.rim.device.api.system.EventLogger.ERROR);
		}
	}

	public void fatal(String message) {
		if ((message != null) && !message.equals("")) {
			net.rim.device.api.system.EventLogger.logEvent(guid, message.getBytes(), net.rim.device.api.system.EventLogger.SEVERE_ERROR);
		}
	}

	public void clear() {
		net.rim.device.api.system.EventLogger.clearLog();
	}

}