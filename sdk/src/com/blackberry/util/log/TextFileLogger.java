package com.blackberry.util.log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

public class TextFileLogger extends AbstractLogger {

	protected String logFile = null;
	protected FileConnection fc = null;
	protected OutputStream os = null;

	protected TextFileLogger(String pFileName) {
		logFile = pFileName;
	}

	protected void writeLine(String line) {
		try {
			if (fc == null) {
				try {
					fc = (FileConnection) Connector.open(logFile, Connector.READ_WRITE);
					if (!fc.exists()) {
						fc.create();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
			os = fc.openOutputStream(fc.fileSize());
			line = line + "\n";
			byte[] lineArray = line.getBytes();
			os.write(lineArray, 0, lineArray.length);

		} catch (IOException ex) {
			ex.printStackTrace();

		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		}
	}

	public void clearLogFile() {
		if (fc == null) {
			try {
				fc = (FileConnection) Connector.open(logFile, Connector.READ_WRITE);
			} catch (IOException ex) {
				ex.printStackTrace();
			}

		}
		if (fc.exists()) {
			try {
				fc.delete();
				fc.create();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public void close() {
		if (os != null) {
			try {
				os.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				os = null;
			}
		}
		if (fc != null) {
			try {
				fc.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			} finally {
				fc = null;
			}
		}
	}

	public void debug(String message) {
		writeLine("[DEBUG] " + simpleDateFormat.format(new Date()) + ": " + message);
	}

	public void info(String message) {
		writeLine("[INFO] " + simpleDateFormat.format(new Date()) + ": " + message);
	}

	public void warn(String message) {
		writeLine("[WARN] " + simpleDateFormat.format(new Date()) + ": " + message);
	}

	public void error(String message) {
		writeLine("[ERROR] " + simpleDateFormat.format(new Date()) + ": " + message);
	}

	public void fatal(String message) {
		writeLine("[FATAL] " + simpleDateFormat.format(new Date()) + ": " + message);
	}

}