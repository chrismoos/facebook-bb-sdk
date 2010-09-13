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

import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import com.blackberry.util.properties.Properties;
import com.blackberry.util.string.StringUtils;

public class AppenderFactory {

	private static final String DEFAULT_TEXT_LOG_FILENAME = "file:///SDCard/log.txt";
	private static final String DEFAULT_RICHTEXT_LOG_FILENAME = "file:///SDCard/log.html";
	private static final String DEFAULT_PROPERTIES_FILENAME = "log4b.properties";
	private static final String APPENDER_PREFFIX = "log4b.appender.";

	private static Hashtable appenders = new Hashtable();
	private static Appender ROOT_APPENDER = createAppender("ROOT", Appender.CONSOLE, null);

	public static void load(String propFile) {
		try {
			Properties prop = Properties.loadProperties("/" + propFile);
			Enumeration enum = prop.getEnumeratedNames();

			while (enum.hasMoreElements()) {
				String key = null;
				String value = null;
				String[] values = null;
				String appenderName = null;
				String appenderType = null;
				String appenderDest = null;

				key = ((String) enum.nextElement());

				if ((key != null) && !key.equals("") && key.startsWith(APPENDER_PREFFIX)) {
					key = key.trim();
					appenderName = key.substring(APPENDER_PREFFIX.length());
					value = prop.getProperty(key);
					if ((value != null) && !value.equals("")) {
						values = StringUtils.split(value, ',', 0);
						if ((values != null) && (values.length > 0)) {
							for (int i = 0; i < values.length; i++) {
								if (i == 0) {
									appenderType = values[i].trim();
								} else if (i == 1) {
									appenderDest = values[i].trim();
								}
							}

							if (appenderType.equals(Appender.CONSOLE)) {
								appenders.put(appenderName, createAppender(appenderName, Appender.CONSOLE, appenderDest));

							} else if (appenderType.equals(Appender.TEXT_FILE)) {
								if ((appenderDest == null) || appenderDest.equals("")) {
									appenderDest = DEFAULT_TEXT_LOG_FILENAME;
								}
								appenders.put(appenderName, createAppender(appenderName, Appender.TEXT_FILE, appenderDest));

							} else if (appenderType.equals(Appender.RICH_TEXT_FILE)) {
								if ((appenderDest == null) || appenderDest.equals("")) {
									appenderDest = DEFAULT_RICHTEXT_LOG_FILENAME;
								}
								appenders.put(appenderName, createAppender(appenderName, Appender.RICH_TEXT_FILE, appenderDest));

							} else if (appenderType.equals(Appender.SCREEN)) {
								appenders.put(appenderName, createAppender(appenderName, Appender.SCREEN, appenderDest));

							} else if (appenderType.equals(Appender.EVENT_LOG)) {
								if ((appenderDest != null) && !appenderDest.equals("")) {
									appenders.put(appenderName, createAppender(appenderName, Appender.EVENT_LOG, appenderDest));
								}
							}
						}
					}
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
	}

	public static Appender getRootAppender() {
		return ROOT_APPENDER;
	}

	public static Appender getAppender(String name) {
		if ((name != null) && !name.equals("")) {
			if (appenders.containsKey(name)) {
				return (Appender) appenders.get(name);
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	public static void clear() {
		Enumeration enum = appenders.elements();
		if (enum != null) {
			while (enum.hasMoreElements()) {
				((Appender) enum.nextElement()).close();
			}
		}
		appenders = new Hashtable();
	}

	public static void reset() {
		clear();
		load(DEFAULT_PROPERTIES_FILENAME);
	}

	private static Appender createAppender(String name, String type, String destination) {

		Appender out = null;

		if (type.trim().equalsIgnoreCase(Appender.CONSOLE)) {
			out = new ConsoleAppender(name, type, destination);

		} else if (type.trim().equalsIgnoreCase(Appender.TEXT_FILE)) {
			out = new TextFileAppender(name, type, destination);

		} else if (type.trim().equalsIgnoreCase(Appender.RICH_TEXT_FILE)) {
			out = new RichTextFileAppender(name, type, destination);

		} else if (type.trim().equalsIgnoreCase(Appender.SCREEN)) {
			out = new ScreenAppender(name, type, destination);

		} else if (type.trim().equalsIgnoreCase(Appender.EVENT_LOG)) {
			out = new EventLogAppender(name, type, destination);
		}

		return out;

	}
}
