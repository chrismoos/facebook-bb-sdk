package com.blackberry.util.log;

import java.util.Date;

public class RichTextFileLogger extends TextFileLogger {

	protected RichTextFileLogger(String pFileName) {
		super(pFileName);
	}

	public void debug(String message) {
		message = "[DEBUG] " + simpleDateFormat.format(new Date()) + ": " + message;
		writeLine("<span style=\"color:" + "#000000" + "\">" + message + "</span><br>");
	}

	public void info(String message) {
		message = "[INFO] " + simpleDateFormat.format(new Date()) + ": " + message;
		writeLine("<span style=\"font-weight:bold; color:" + "#00FF00" + "\">" + message + "</span><br>");
	}

	public void warn(String message) {
		message = "[WARN] " + simpleDateFormat.format(new Date()) + ": " + message;
		writeLine("<span style=\"font-weight:bold; color:" + "#F4A460" + "\">" + message + "</span><br>");
	}

	public void error(String message) {
		message = "[ERROR] " + simpleDateFormat.format(new Date()) + ": " + message;
		writeLine("<span style=\"font-weight:bold; color:" + "#FF0000" + "\">" + message + "</span><br>");
	}

	public void fatal(String message) {
		message = "[FATAL] " + simpleDateFormat.format(new Date()) + ": " + message;
		writeLine("<span style=\"font-weight:bold; color:" + "#FF0000" + ";" + "background-color:" + "#000000" + "\">" + message + "</span><br>");
	}

}