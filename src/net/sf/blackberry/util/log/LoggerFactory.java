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
package net.sf.blackberry.util.log;

import java.util.Enumeration;
import java.util.Hashtable;

import net.sf.blackberry.util.properties.Properties;
import net.sf.blackberry.util.string.StringUtils;


public class LoggerFactory {

	private static final String DEFAULT_PROPERTIES_FILENAME = "log4b.properties";
	private static final String LOGGER_PREFFIX = "log4b.logger.";

	private static Hashtable loggers = new Hashtable();
	private static Logger GOD_LOGGER = createLogger("GOD", "DEBUG", false, new Appender[] { AppenderFactory.getGodAppender() });

	private static String propFile;

	static {
		load(DEFAULT_PROPERTIES_FILENAME);
	}

	public static void load(String pPropFile) {
		if ((pPropFile == null) || pPropFile.trim().equals("")) {
			propFile = DEFAULT_PROPERTIES_FILENAME;
		} else {
			propFile = pPropFile;
		}
		AppenderFactory.load(propFile);
		loadLoggers(propFile);
		loadParentLoggers(propFile);
	}

	private static void loadLoggers(String pPropFile) {
		try {
			Properties prop = Properties.loadProperties("/" + pPropFile);
			Enumeration _enum = prop.getEnumeratedNames();

			while (_enum.hasMoreElements()) {
				String key = null;
				String value = null;
				String[] values = null;
				String loggerName = null;
				String loggerLevel = null;
				boolean additive = false;

				key = ((String) _enum.nextElement());

				if ((key != null) && !key.equals("") && key.startsWith(LOGGER_PREFFIX)) {
					key = key.trim();
					loggerName = key.substring(LOGGER_PREFFIX.length());
					value = prop.getProperty(key);
					if ((value != null) && !value.equals("")) {
						values = StringUtils.split(value, ',', 0);
						if ((values != null) && (values.length > 2)) {
							loggerLevel = values[0].trim();
							additive = values[1].trim().equalsIgnoreCase("true");
							Appender[] appenderList = new Appender[values.length - 2];

							for (int i = 2; i < values.length; i++) {
								appenderList[i - 2] = AppenderFactory.getAppender(values[i].trim());
							}

							loggers.put(loggerName, createLogger(loggerName, loggerLevel, additive, appenderList));
						}
					}
				}

			}

		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
		}
	}

	private static void loadParentLoggers(String pPropFile) {
		try {
			Properties prop = Properties.loadProperties("/" + pPropFile);
			Enumeration _enum = prop.getEnumeratedNames();

			while (_enum.hasMoreElements()) {
				String key = null;
				String value = null;
				String[] values = null;
				String childName = null;
				String parentName = null;

				key = ((String) _enum.nextElement());

				if ((key != null) && !key.equals("") && key.startsWith(LOGGER_PREFFIX)) {
					key = key.trim();

					childName = key.substring(LOGGER_PREFFIX.length());
					parentName = StringUtils.parentOf(childName);

					if ((parentName == null) || parentName.trim().equals("")) {
						if (childName.equals("ROOT")) {
							parentName = "GOD";
						} else {
							parentName = "ROOT";
						}
					}

					Logger childLogger = getLogger(childName);
					Logger parentLogger = null;

					if (parentName.equals("GOD")) {
						parentLogger = getGodLogger();
					} else {
						parentLogger = getLogger(parentName);
					}

					if ((childLogger != null) && (parentLogger != null)) {
						childLogger.setParent(parentLogger);
					}
				}

			}

		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
		}
	}

	private static Logger getGodLogger() {
		return GOD_LOGGER;
	}

	public static Logger getRootLogger() {
		return getLogger("ROOT");
	}

	public static Logger[] getAllLogger() {

		if ((loggers == null) || (loggers.size() <= 0)) {
			return null;
		}

		Logger[] output = new Logger[loggers.size()];
		Enumeration e = loggers.elements();
		int i = 0;
		while (e.hasMoreElements()) {
			output[i] = (Logger) e.nextElement();
			i++;
		}

		return output;
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
						return GOD_LOGGER;
					}
				}
			}
		} else {
			return null;
		}
	}

	public static void close() {
		Enumeration _enum = loggers.elements();
		if (_enum != null) {
			while (_enum.hasMoreElements()) {
				((Logger) _enum.nextElement()).close();
			}
		}
		loggers = new Hashtable();

		AppenderFactory.close();
	}

	public static void reset() {
		close();
		load(propFile);
	}

	protected static Logger createLogger(String pName, String pLevel, boolean pAdditive, Appender[] pAppenders) {

		Logger out = null;

		if ((pName != null) && !pName.equals("") && (pLevel != null) && !pLevel.equals("") && (pAppenders != null) && (pAppenders.length > 0)) {
			if (pLevel.trim().equalsIgnoreCase("DEBUG")) {
				out = new Logger(pName.trim(), Level.DEBUG, pAdditive, pAppenders);
			} else if (pLevel.trim().equalsIgnoreCase("INFO")) {
				out = new Logger(pName.trim(), Level.INFO, pAdditive, pAppenders);
			} else if (pLevel.trim().equalsIgnoreCase("WARN")) {
				out = new Logger(pName.trim(), Level.WARN, pAdditive, pAppenders);
			} else if (pLevel.trim().equalsIgnoreCase("ERROR")) {
				out = new Logger(pName.trim(), Level.ERROR, pAdditive, pAppenders);
			} else if (pLevel.trim().equalsIgnoreCase("FATAL")) {
				out = new Logger(pName.trim(), Level.FATAL, pAdditive, pAppenders);
			} else {
				// do nothing
			}
		}

		return out;

	}
}
