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
package com.blackberry.util.log.test;

import com.blackberry.util.log.Level;
import com.blackberry.util.log.Logger;
import com.blackberry.util.log.LoggerFactory;

import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.FontFamily;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;

public class BasicLogTestScreen extends MainScreen {

	protected Logger[] loggers = null;

	public BasicLogTestScreen() {

		setTitle("Basic Log Test");

		loggers = Logger.getAllLogger();

		if ((loggers != null) && (loggers.length > 0)) {

			for (int i = 0; i < loggers.length; i++) {

				final String loggerName = loggers[i].getName();
				String loggerLevel = null;

				if (loggers[i].getLevel() == Level.DEBUG) {
					loggerLevel = "DEBUG";
				} else if (loggers[i].getLevel() == Level.INFO) {
					loggerLevel = "INFO";
				} else if (loggers[i].getLevel() == Level.WARN) {
					loggerLevel = "WARN";
				} else if (loggers[i].getLevel() == Level.ERROR) {
					loggerLevel = "ERROR";
				} else if (loggers[i].getLevel() == Level.FATAL) {
					loggerLevel = "FATAL";
				}

				String lft1 = "Name: " + loggers[i].getName();
				String lft2 = "Level: " + loggerLevel;
				String lft3 = "Additive: " + loggers[i].getAdditive();
				String lft4 = "Parent: " + loggers[i].getParent().getName();

				LabelField lf1 = new LabelField(lft1);
				LabelField lf2 = new LabelField(lft2);
				LabelField lf3 = new LabelField(lft3);
				LabelField lf4 = new LabelField(lft4);

				try {
					lf1.setFont(FontFamily.forName("BBAlpha Serif").getFont(Font.BOLD, 15));
					lf2.setFont(FontFamily.forName("BBAlpha Serif").getFont(Font.PLAIN, 12));
					lf3.setFont(FontFamily.forName("BBAlpha Serif").getFont(Font.PLAIN, 12));
					lf4.setFont(FontFamily.forName("BBAlpha Serif").getFont(Font.PLAIN, 12));
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}

				add(lf1);
				add(lf2);
				add(lf3);
				add(lf4);

				HorizontalFieldManager hfm = new HorizontalFieldManager();

				ButtonField bLog = new ButtonField("Log") {
					protected boolean invokeAction(int action) {
						doLog(loggerName);
						Dialog.inform("Logged to: " + loggerName);
						return true;
					}
				};

				ButtonField bShow = new ButtonField("Show") {
					protected boolean invokeAction(int action) {
						doShow(loggerName);
						return true;
					}
				};

				ButtonField bClear = new ButtonField("Clear") {
					protected boolean invokeAction(int action) {
						doClear(loggerName);
						Dialog.inform("Cleared: " + loggerName);
						return true;
					}
				};

				hfm.add(bLog);
				hfm.add(bShow);
				hfm.add(bClear);

				add(hfm);
				add(new SeparatorField());
			}

		} else {
			// do nothing
		}

	}

	protected void doLog(String loggerName) {
		Logger log = Logger.getLogger(loggerName);
		log.debug("*************************************** This is a LONG line ***************************************");
		log.debug("This is just a testing log message.");
		log.info("This is just a testing log message.");
		log.warn("This is just a testing log message.");
		log.error("This is just a testing log message.");
		log.fatal("This is just a testing log message.");
		log.debug("*************************************** This is a LONG line ***************************************");
		log.debug("This is just a testing log message.");
		log.info("This is just a testing log message.");
		log.warn("This is just a testing log message.");
		log.error("This is just a testing log message.");
		log.fatal("This is just a testing log message.");
		log.debug("*************************************** This is a LONG line ***************************************");
		log.debug("This is just a testing log message.");
		log.info("This is just a testing log message.");
		log.warn("This is just a testing log message.");
		log.error("This is just a testing log message.");
		log.fatal("This is just a testing log message.");
		log.debug("*************************************** This is a LONG line ***************************************");
		log.debug("This is just a testing log message.");
		log.info("This is just a testing log message.");
		log.warn("This is just a testing log message.");
		log.error("This is just a testing log message.");
		log.fatal("This is just a testing log message.");
		log.debug("*************************************** This is a LONG line ***************************************");
		log.debug("This is just a testing log message.");
		log.info("This is just a testing log message.");
		log.warn("This is just a testing log message.");
		log.error("This is just a testing log message.");
		log.fatal("This is just a testing log message.");
	}

	protected void doShow(String loggerName) {
		Logger log = Logger.getLogger(loggerName);
		log.show();
	}

	protected void doClear(String loggerName) {
		Logger log = Logger.getLogger(loggerName);
		log.clear();
	}

	public boolean onClose() {
		LoggerFactory.clear();
		System.exit(0);
		return true;
	}

	protected boolean onSavePrompt() {
		return true;
	}

}
