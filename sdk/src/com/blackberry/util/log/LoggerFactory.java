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

public class LoggerFactory {

	private static final String DEFAULT_PROPERTIES_FILENAME = "log4b.properties";
	private static final String LOGGER_PREFFIX = "log4b.logger.";

	private static Hashtable loggers = new Hashtable();
	private static Logger ROOT_LOGGER = createLogger("ROOT", "DEBUG", new Appender[] { AppenderFactory.getRootAppender() });

	static {
		load(DEFAULT_PROPERTIES_FILENAME);
	}

	public static void load(String propFile) {
		try {
			AppenderFactory.load(propFile);
			Properties prop = Properties.loadProperties("/" + propFile);
			Enumeration enum = prop.getEnumeratedNames();

			while (enum.hasMoreElements()) {
				String key = null;
				String value = null;
				String[] values = null;
				String loggerName = null;
				String loggerLevel = null;

				key = ((String) enum.nextElement());

				if ((key != null) && !key.equals("") && key.startsWith(LOGGER_PREFFIX)) {
					key = key.trim();
					loggerName = key.substring(LOGGER_PREFFIX.length());
					value = prop.getProperty(key);
					if ((value != null) && !value.equals("")) {
						values = StringUtils.split(value, ',', 0);
						if ((values != null) && (values.length > 1)) {
							loggerLevel = values[0].trim();
							Appender[] appenderList = new Appender[values.length - 1];

							for (int i = 1; i < values.length; i++) {
								appenderList[i - 1] = AppenderFactory.getAppender(values[i].trim());
							}

							loggers.put(loggerName, createLogger(loggerName, loggerLevel, appenderList));
						}
					}
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

		}
	}

	public static Logger getRootLogger() {
		return ROOT_LOGGER;
	}

	public static Logger getLogger(String name) {
		if ((name != null) && !name.equals("")) {
			if (loggers.containsKey(name)) {
				return (Logger) loggers.get(name);
			} else {
				String parentName = StringUtils.parentOf(name);
				if ((parentName != null) && !parentName.equals("")) {
					return getLogger(parentName);
				} else {
					if (!name.equals("ROOT")) {
						return getLogger("ROOT");
					} else {
						return ROOT_LOGGER;
					}
				}
			}
		} else {
			return null;
		}
	}

	public static void clear() {
		AppenderFactory.clear();
		loggers = new Hashtable();
	}

	public static void reset() {
		clear();
		load(DEFAULT_PROPERTIES_FILENAME);
	}

	protected static Logger createLogger(String pName, String pLevel, Appender[] pAppenders) {

		Logger out = null;

		if ((pName != null) && !pName.equals("") && (pLevel != null) && !pLevel.equals("") && (pAppenders != null) && (pAppenders.length > 0)) {
			if (pLevel.trim().equalsIgnoreCase("DEBUG")) {
				out = new Logger(pName.trim(), Level.DEBUG, pAppenders);
			} else if (pLevel.trim().equalsIgnoreCase("INFO")) {
				out = new Logger(pName.trim(), Level.INFO, pAppenders);
			} else if (pLevel.trim().equalsIgnoreCase("WARN")) {
				out = new Logger(pName.trim(), Level.WARN, pAppenders);
			} else if (pLevel.trim().equalsIgnoreCase("ERROR")) {
				out = new Logger(pName.trim(), Level.ERROR, pAppenders);
			} else if (pLevel.trim().equalsIgnoreCase("FATAL")) {
				out = new Logger(pName.trim(), Level.FATAL, pAppenders);
			} else {
				// do nothing
			}
		}

		return out;

	}
}
