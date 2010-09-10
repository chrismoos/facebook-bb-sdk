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
package com.blackberry.util.properties;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Hashtable;

import com.blackberry.util.string.StringUtils;

public class Properties {

	private Hashtable propTable = new Hashtable();

	public static Properties loadProperties(String filePath) throws IOException {
		Properties result = new Properties();

		InputStream stream = result.getClass().getResourceAsStream(filePath);
		InputStreamReader reader = new InputStreamReader(stream);

		StringBuffer sBuf = new StringBuffer();
		char[] buff = new char[1024];

		int pos = reader.read(buff, 0, 1024);
		while (pos != -1) {
			sBuf.append(buff, 0, pos);
			pos = reader.read(buff, 0, 1024);
		}

		String[] lines = StringUtils.split(sBuf.toString(), '\n', 0);
		for (int i = 0; i < lines.length; i++) {
			String[] kv = StringUtils.split(StringUtils.chomp(lines[i]), '=', 2);
			if (kv.length == 1) {
				result.setProperty(kv[0], "");
			}
			if (kv.length == 2) {
				result.setProperty(kv[0], kv[1]);
			}
		}

		return result;
	}

	public void setProperty(String key, String val) {
		propTable.put(key, val);
	}

	public String getProperty(String key) {
		return (String) propTable.get(key);
	}

	public int getPropertyCount() {
		return propTable.size();
	}

	public Enumeration getEnumeratedNames() {
		return propTable.keys();
	}

	public String[] getPropertyNames() {
		String[] result = new String[propTable.size()];
		int c = 0;
		for (Enumeration e = propTable.keys(); e.hasMoreElements();) {
			result[c] = (String) e.nextElement();
			c++;
		}
		return result;
	}

}