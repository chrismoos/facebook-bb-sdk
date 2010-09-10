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

import javax.microedition.content.Invocation;
import javax.microedition.content.Registry;

import com.blackberry.util.log.Logger;
import com.blackberry.util.log.LoggerFactory;
import com.blackberry.util.log.ScreenLogger;

import net.rim.device.api.system.EventLogger;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.SeparatorField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;

public class BasicLogTestScreen extends MainScreen {

	protected Logger consoleLogger = LoggerFactory.getLogger("CONSOLE");
	protected Logger textFileLogger = LoggerFactory.getLogger("TEXT_FILE");
	protected Logger richTextFileLogger = LoggerFactory.getLogger("RICH_TEXT_FILE");
	protected Logger screenLogger = LoggerFactory.getLogger("SCREEN");
	protected Logger eventLogger = LoggerFactory.getLogger("EVENT_LOG");
	protected Logger defaultLogger = LoggerFactory.getLogger();

	public BasicLogTestScreen() {

		setTitle("Basic Log Test");

		VerticalFieldManager topManager = new VerticalFieldManager(Manager.VERTICAL_SCROLL);

		HorizontalFieldManager hfm1 = new HorizontalFieldManager();
		HorizontalFieldManager hfm2 = new HorizontalFieldManager();
		HorizontalFieldManager hfm3 = new HorizontalFieldManager();
		HorizontalFieldManager hfm4 = new HorizontalFieldManager();
		HorizontalFieldManager hfm5 = new HorizontalFieldManager();
		HorizontalFieldManager hfm6 = new HorizontalFieldManager();

		hfm1.add(new ButtonField("Log to Console") {
			protected boolean invokeAction(int action) {
				doLog(consoleLogger);
				Dialog.inform("Written to: [Console]");
				return true;
			}
		});

		hfm2.add(new ButtonField("Log to Text File") {
			protected boolean invokeAction(int action) {
				doLog(textFileLogger);
				if (Dialog.ask("Written to: " + textFileLogger.getDestination() + "\n" + "Open Now?:", new String[] { "Yup", "Nope" }, 0) == 0) {
					openFile(textFileLogger.getDestination());
				}
				return true;
			}
		});

		hfm2.add(new ButtonField("Clear") {
			protected boolean invokeAction(int action) {
				doClear(textFileLogger);
				Dialog.inform("Cleared: " + textFileLogger.getDestination());
				return true;
			}
		});

		hfm3.add(new ButtonField("Log to RichText File") {
			protected boolean invokeAction(int action) {
				doLog(richTextFileLogger);
				if (Dialog.ask("Written to: " + richTextFileLogger.getDestination() + "\n" + "Open Now?:", new String[] { "Yup", "Nope" }, 0) == 0) {
					openFile(richTextFileLogger.getDestination());
				}
				return true;
			}
		});

		hfm3.add(new ButtonField("Clear") {
			protected boolean invokeAction(int action) {
				doClear(richTextFileLogger);
				Dialog.inform("Cleared: " + richTextFileLogger.getDestination());
				return true;
			}
		});

		hfm4.add(new ButtonField("Log to Screen") {
			protected boolean invokeAction(int action) {
				doLog(screenLogger);
				if (Dialog.ask("Written to: [Log Screen]" + "\n" + "Open Now?:", new String[] { "Yup", "Nope" }, 0) == 0) {
					UiApplication.getUiApplication().pushScreen(((ScreenLogger) screenLogger).getLogScreen());
				}
				return true;
			}
		});

		hfm4.add(new ButtonField("Clear") {
			protected boolean invokeAction(int action) {
				doClear(screenLogger);
				Dialog.inform("Cleared: [Log Screen]");
				return true;
			}
		});

		hfm5.add(new ButtonField("Log to EventLog") {
			protected boolean invokeAction(int action) {
				doLog(eventLogger);
				if (Dialog.ask("Written to: [Event Log]" + "\n" + "Open Now?:", new String[] { "Yup", "Nope" }, 0) == 0) {
					EventLogger.startEventLogViewer();
				}
				return true;
			}
		});

		hfm5.add(new ButtonField("Clear") {
			protected boolean invokeAction(int action) {
				doClear(eventLogger);
				Dialog.inform("Cleared: [Event Log]");
				return true;
			}
		});

		hfm6.add(new ButtonField("Log to Default Logger") {
			protected boolean invokeAction(int action) {
				doLog(defaultLogger);
				Dialog.inform("Written to: [Default Logger]");
				return true;
			}
		});

		hfm6.add(new ButtonField("Clear") {
			protected boolean invokeAction(int action) {
				doClear(defaultLogger);
				Dialog.inform("Cleared: [Default Logger]");
				return true;
			}
		});

		topManager.add(hfm1);
		topManager.add(new SeparatorField());

		topManager.add(hfm2);
		topManager.add(new SeparatorField());

		topManager.add(hfm3);
		topManager.add(new SeparatorField());

		topManager.add(hfm4);
		topManager.add(new SeparatorField());

		topManager.add(hfm5);
		topManager.add(new SeparatorField());

		topManager.add(hfm6);
		topManager.add(new SeparatorField());

		add(topManager);

	}

	protected void doLog(Logger log) {
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

	protected void doClear(Logger log) {
		log.clearLog();
	}

	protected void openFile(String fileName) {
		try {
			Registry.getRegistry("net.rim.device.api.content.BlackBerryContentHandler").invoke(new Invocation(fileName));
		} catch (Throwable t) {
			t.printStackTrace();
		}
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
