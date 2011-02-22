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
package net.sf.blackberry.util.string;

import java.util.Vector;

public class StringUtils {

	public static String[] split(String str, char sep, int maxNum) {
		if ((str == null) || (str.length() == 0)) {
			return new String[0];
		}

		Vector results = maxNum == 0 ? new Vector() : new Vector(maxNum);

		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);

			if (c == sep) {
				if (maxNum != 0) {
					if ((results.size() + 1) == maxNum) {
						for (; i < str.length(); i++) {
							buf.append(str.charAt(i));
						}
						break;
					}
				}

				results.addElement(buf.toString());
				buf.setLength(0);
			} else {
				buf.append(c);
			}
		}

		if (buf.length() > 0) {
			results.addElement(buf.toString());
		}

		return toStringArray(results);
	}

	public static String[] toStringArray(Vector strings) {
		String[] result = new String[strings.size()];
		for (int i = 0; i < strings.size(); i++) {
			result[i] = strings.elementAt(i).toString();
		}
		return result;
	}

	public static String chomp(String inStr) {
		if ((inStr == null) || "".equals(inStr)) {
			return inStr;
		}

		char lastChar = inStr.charAt(inStr.length() - 1);
		if (lastChar == 13) {
			return inStr.substring(0, inStr.length() - 1);
		} else if (lastChar == 10) {
			String tmp = inStr.substring(0, inStr.length() - 1);
			if ("".equals(tmp)) {
				return tmp;
			}
			lastChar = tmp.charAt(tmp.length() - 1);
			if (lastChar == 13) {
				return tmp.substring(0, tmp.length() - 1);
			} else {
				return tmp;
			}
		} else {
			return inStr;
		}
	}

	public static String parentOf(String inStr) {
		String result = null;

		if ((inStr != null) && !inStr.trim().equals("")) {
			inStr = inStr.trim();
			int index = inStr.lastIndexOf('.');
			if (index != -1) {
				result = inStr.substring(0, index);
			}
		}

		return result;
	}

}