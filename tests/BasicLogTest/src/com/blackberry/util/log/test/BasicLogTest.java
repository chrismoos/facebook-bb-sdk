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

import com.blackberry.util.log.Logger;
import com.blackberry.util.log.LoggerFactory;

import net.rim.device.api.applicationcontrol.ApplicationPermissions;
import net.rim.device.api.applicationcontrol.ApplicationPermissionsManager;
import net.rim.device.api.ui.UiApplication;

public class BasicLogTest extends UiApplication {

	public static void main(String[] args) {
		BasicLogTest app = new BasicLogTest();
		app.enterEventDispatcher();
	}

	public BasicLogTest() {
		checkPermissions();
		init();
		pushScreen(new BasicLogTestScreen());
	}

	private void checkPermissions() {

		ApplicationPermissionsManager apm = ApplicationPermissionsManager.getInstance();
		ApplicationPermissions original = apm.getApplicationPermissions();

		if ((original.getPermission(ApplicationPermissions.PERMISSION_INPUT_SIMULATION) == ApplicationPermissions.VALUE_ALLOW) && (original.getPermission(ApplicationPermissions.PERMISSION_DEVICE_SETTINGS) == ApplicationPermissions.VALUE_ALLOW) && (original.getPermission(ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION) == ApplicationPermissions.VALUE_ALLOW) && (original.getPermission(ApplicationPermissions.PERMISSION_INTERNET) == ApplicationPermissions.VALUE_ALLOW) && (original.getPermission(ApplicationPermissions.PERMISSION_SERVER_NETWORK) == ApplicationPermissions.VALUE_ALLOW) && (original.getPermission(ApplicationPermissions.PERMISSION_EMAIL) == ApplicationPermissions.VALUE_ALLOW)) {
			return;
		}

		ApplicationPermissions permRequest = new ApplicationPermissions();
		permRequest.addPermission(ApplicationPermissions.PERMISSION_INPUT_SIMULATION);
		permRequest.addPermission(ApplicationPermissions.PERMISSION_DEVICE_SETTINGS);
		permRequest.addPermission(ApplicationPermissions.PERMISSION_CROSS_APPLICATION_COMMUNICATION);
		permRequest.addPermission(ApplicationPermissions.PERMISSION_INTERNET);
		permRequest.addPermission(ApplicationPermissions.PERMISSION_SERVER_NETWORK);
		permRequest.addPermission(ApplicationPermissions.PERMISSION_EMAIL);

		boolean acceptance = ApplicationPermissionsManager.getInstance().invokePermissionsRequest(permRequest);

		if (acceptance) {
			// User has accepted all of the permissions.
			return;
		} else {
		}
	}

	private void init() {
	}

	private void testLog() {

		Logger def = LoggerFactory.getLogger();
		Logger log = LoggerFactory.getLogger("TEXT_FILE");
		Logger rlog = LoggerFactory.getLogger("RICH_TEXT_FILE");

		def.debug("************************** BasicLogTest.def.xxx() **********************************");
		def.debug("This is just a testing log message.");
		def.info("This is just a testing log message.");
		def.warn("This is just a testing log message.");
		def.error("This is just a testing log message.");
		def.fatal("This is just a testing log message.");
		def.debug("************************** /BasicLogTest.def.xxx() **********************************");

		log.debug("************************** BasicLogTest.log.xxx() **********************************");
		log.debug("This is just a testing log message.");
		log.info("This is just a testing log message.");
		log.warn("This is just a testing log message.");
		log.error("This is just a testing log message.");
		log.fatal("This is just a testing log message.");
		log.debug("************************** /BasicLogTest.log.xxx() **********************************");

		rlog.debug("************************** BasicLogTest.rlog.xxx() **********************************");
		rlog.debug("This is just a testing log message.");
		rlog.info("This is just a testing log message.");
		rlog.warn("This is just a testing log message.");
		rlog.error("This is just a testing log message.");
		rlog.fatal("This is just a testing log message.");
		rlog.debug("************************** /BasicLogTest.rlog.xxx() **********************************");
	}

	private void exit() {
		LoggerFactory.clear();
		System.exit(0);
	}

}