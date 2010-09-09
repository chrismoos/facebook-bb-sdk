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