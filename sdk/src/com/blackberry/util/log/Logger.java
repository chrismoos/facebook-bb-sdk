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

import java.util.Vector;

public class Logger extends AbstractLogger {

	protected Logger parent;
	protected boolean additive = false;

	public Logger(String pName, int pLevel, boolean pAdditive, Appender[] pAppenders) {
		super(pName, pLevel, pAppenders);
		additive = pAdditive;
	}

	public static Logger getLogger(String name) {
		return LoggerFactory.getLogger(name);
	}

	public static Logger getLogger(Class className) {
		return LoggerFactory.getLogger(className.getName());
	}

	public static Logger getLogger(String name, LoggerFactory factory) {
		return factory.getLogger(name);
	}

	public static Logger getRootLogger() {
		return LoggerFactory.getRootLogger();
	}

	public static Logger[] getAllLogger() {
		return LoggerFactory.getAllLogger();
	}

	public void setParent(Logger pParent) {
		parent = pParent;
	}

	public void debug(String message) {
		if (Level.isGreaterOrEqual(Level.DEBUG, level)) {
			invokeAppenders(Level.DEBUG, message);
		}
		if (additive && (parent != null)) {
			parent.debug(message);
		}
	}

	public void info(String message) {
		if (Level.isGreaterOrEqual(Level.INFO, level)) {
			invokeAppenders(Level.INFO, message);
		}
		if (additive && (parent != null)) {
			parent.info(message);
		}
	}

	public void warn(String message) {
		if (Level.isGreaterOrEqual(Level.WARN, level)) {
			invokeAppenders(Level.WARN, message);
		}
		if (additive && (parent != null)) {
			parent.warn(message);
		}
	}

	public void error(String message) {
		if (Level.isGreaterOrEqual(Level.ERROR, level)) {
			invokeAppenders(Level.ERROR, message);
		}
		if (additive && (parent != null)) {
			parent.error(message);
		}
	}

	public void fatal(String message) {
		if (Level.isGreaterOrEqual(Level.FATAL, level)) {
			invokeAppenders(Level.FATAL, message);
		}
		if (additive && (parent != null)) {
			parent.fatal(message);
		}
	}

	public boolean getAdditive() {
		return additive;
	}

	public Logger getParent() {
		return parent;
	}

	public LogScreen[] getLogScreens() {

		Vector output = new Vector();
		LogScreen[] result = null;
		Appender[] appenders = getAppenders();

		for (int i = 0; i < appenders.length; i++) {
			if ((appenders[i].getType() != null) && appenders[i].getType().trim().equals(Appender.SCREEN)) {
				output.addElement(((ScreenAppender) appenders[i]).getLogScreen());
			}
		}

		if ((output != null) && (output.size() > 0)) {
			result = new LogScreen[output.size()];
			output.copyInto(result);
		}

		return result;

	}

	public void clear() {
		Appender[] appenders = getAppenders();

		for (int i = 0; i < appenders.length; i++) {
			appenders[i].clear();
		}
	}

	public void show() {
		Appender[] appenders = getAppenders();

		for (int i = 0; i < appenders.length; i++) {
			appenders[i].show();
		}
	}

	protected void invokeAppenders(int pLevel, String pMessage) {
		Appender[] appenders = getAppenders();

		for (int i = 0; i < appenders.length; i++) {
			if (pLevel == Level.DEBUG) {
				appenders[i].debug(pMessage);
			} else if (pLevel == Level.INFO) {
				appenders[i].info(pMessage);
			} else if (pLevel == Level.WARN) {
				appenders[i].warn(pMessage);
			} else if (pLevel == Level.ERROR) {
				appenders[i].error(pMessage);
			} else if (pLevel == Level.FATAL) {
				appenders[i].fatal(pMessage);
			} else {
				// do nothing
			}
		}
	}

}
