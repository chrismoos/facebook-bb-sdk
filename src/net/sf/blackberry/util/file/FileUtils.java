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
package net.sf.blackberry.util.file;

import java.util.Enumeration;
import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;
import javax.microedition.io.file.FileSystemRegistry;

public class FileUtils {

	protected static final String FILE_PREFIX = "file:///";

	public static boolean createRecursively(String fileName, int mode, boolean isDir) {

		boolean created = false;
		boolean parentCreated = false;

		if ((fileName == null) || fileName.equals("") || !fileName.trim().startsWith(FILE_PREFIX)) {
			// do nothing
		} else {

			fileName = fileName.trim();

			if (isRoot(fileName)) {
				created = true;
			} else {
				parentCreated = createRecursively(parentOf(fileName), mode, true);
				if (parentCreated) {
					created = createFileOrDir(fileName, mode, isDir);
				}
			}

		}

		return created;

	}

	protected static boolean createFileOrDir(String fileName, int mode, boolean isDir) {

		boolean created = false;

		try {
			fileName = fileName.trim();

			if (isDir && !fileName.endsWith("/")) {
				fileName += "/";
			}

			FileConnection fc = (FileConnection) Connector.open(fileName, mode);

			if (fc.exists()) {
				created = true;
			} else {
				if (isDir) {
					fc.mkdir();
				} else {
					fc.create();
				}
				created = true;
			}

		} catch (Throwable t) {
			t.printStackTrace();
		} finally {
		}

		return created;
	}

	public static String parentOf(String inStr) {
		String result = null;

		if ((inStr != null) && !inStr.trim().equals("")) {
			inStr = inStr.trim();
			int index = inStr.lastIndexOf('/');
			if (index != -1) {
				result = inStr.substring(0, index);
			}
		}

		return result;
	}

	public static boolean isRoot(String pFileName) {
		boolean output = false;
		Enumeration e = FileSystemRegistry.listRoots();
		String fileName = pFileName.trim() + "/";

		while (e.hasMoreElements()) {
			String thisRoot = (String) e.nextElement();
			output = fileName.equals(FILE_PREFIX + thisRoot);
			if (output) {
				break;
			}
		}

		return output;
	}

}