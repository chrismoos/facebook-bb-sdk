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

import java.util.Date;

import net.rim.device.api.i18n.SimpleDateFormat;
import net.rim.device.api.ui.Color;

public abstract class AbstractAppender implements Appender {

	protected static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	protected String name;
	protected String type;
	protected int threshold;
	protected String destination;

	public abstract void close();

	public abstract void reset();

	public abstract void clear();

	public abstract void show();

	public abstract void writeLine(int level, String message, final int fg, final int bg, final boolean bold);

	public AbstractAppender(String pName, String pType, int pThreshold, String pDestination) {
		name = pName;
		type = pType;
		threshold = pThreshold;
		destination = pDestination;
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public int getThreshold() {
		return threshold;
	}

	public String getDestination() {
		return destination;
	}

	public void debug(String message) {
		if (Level.isGreaterOrEqual(Level.DEBUG, threshold)) {
			writeLine(Level.DEBUG, formatDebug(message), Color.BLACK, Color.WHITE, false);
		}
	}

	public void info(String message) {
		if (Level.isGreaterOrEqual(Level.INFO, threshold)) {
			writeLine(Level.INFO, formatInfo(message), Color.GREEN, Color.WHITE, true);
		}
	}

	public void warn(String message) {
		if (Level.isGreaterOrEqual(Level.WARN, threshold)) {
			writeLine(Level.WARN, formatWarn(message), Color.ORANGE, Color.WHITE, true);
		}
	}

	public void error(String message) {
		if (Level.isGreaterOrEqual(Level.ERROR, threshold)) {
			writeLine(Level.ERROR, formatError(message), Color.RED, Color.WHITE, true);
		}
	}

	public void fatal(String message) {
		if (Level.isGreaterOrEqual(Level.FATAL, threshold)) {
			writeLine(Level.FATAL, formatFatal(message), Color.RED, Color.BLACK, true);
		}
	}

	protected String formatDebug(String message) {
		return simpleDateFormat.format(new Date()) + " [DEBUG] " + message;
	}

	protected String formatInfo(String message) {
		return simpleDateFormat.format(new Date()) + " [INFO] " + message;
	}

	protected String formatWarn(String message) {
		return simpleDateFormat.format(new Date()) + " [WARN] " + message;
	}

	protected String formatError(String message) {
		return simpleDateFormat.format(new Date()) + " [ERROR] " + message;
	}

	protected String formatFatal(String message) {
		return simpleDateFormat.format(new Date()) + " [FATAL] " + message;
	}
}
