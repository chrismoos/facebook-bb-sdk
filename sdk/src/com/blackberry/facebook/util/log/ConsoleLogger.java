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
package com.blackberry.facebook.util.log;

import java.util.Date;

import net.rim.device.api.i18n.SimpleDateFormat;

public class ConsoleLogger implements Logger {

	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss z");

	private String name;

	ConsoleLogger(String name) {
		this.name = name;
	}

	public String getName() {
		return null;
	}

	public void debug(String message) {
		System.out.println("[DEBUG] " + simpleDateFormat.format(new Date()) + " " + name + ": " + message);
	}

	public void info(String message) {
		System.out.println("[INFO] " + simpleDateFormat.format(new Date()) + " " + name + ": " + message);
	}

	public void warn(String message) {
		System.out.println("[WARN] " + simpleDateFormat.format(new Date()) + " " + name + ": " + message);
	}

	public void error(String message) {
		System.out.println("[ERROR] " + simpleDateFormat.format(new Date()) + " " + name + ": " + message);

	}

	public void fatal(String message) {
		System.out.println("[FATAL] " + simpleDateFormat.format(new Date()) + " " + name + ": " + message);
	}

}
