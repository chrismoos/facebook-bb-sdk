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
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

public class TextFileLogger extends AbstractLogger {

	protected String logFile = null;
	protected FileConnection fc = null;
	protected OutputStream os = null;

	public TextFileLogger(String pName, String pType, String pDestination) {
		super(pName, pType, pDestination);
		logFile = pDestination;
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

	public void clearLog() {
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
		writeLine(formatDebug(message));
	}

	public void info(String message) {
		writeLine(formatInfo(message));
	}

	public void warn(String message) {
		writeLine(formatWarn(message));
	}

	public void error(String message) {
		writeLine(formatError(message));
	}

	public void fatal(String message) {
		writeLine(formatFatal(message));
	}

}